package io;

import static utils.ByteUtils.getOffsetFromPointer;
import static utils.ByteUtils.isPointer;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DnsQuestion implements BufferWrapper {
  private final ByteBuffer questionBuffer;
  private final short qdCount;
  private final Map<Integer, ByteBuffer> labelsMap = new HashMap<>();
  private ByteBuffer[] questions;
  private ByteBuffer[] decompressedQuestions;

  private final static byte terminator = 0;

  public DnsQuestion(ByteBuffer questionBuffer, short questionNumber) {
    this.questionBuffer = questionBuffer.duplicate().position(0);
    this.qdCount = questionNumber;
    initQuestions();
    initDecompressedQuestions();
  }

  public ByteBuffer getBuffer() {
    return this.questionBuffer;
  }

  public byte[] getBytes() {
    return this.questionBuffer.array();
  }

  @Override
  public int length() {
    return this.questionBuffer.array().length;
  }

  private void initQuestions() {
    this.questions = new ByteBuffer[this.qdCount];
    ByteBuffer copyBuffer = this.questionBuffer.duplicate();
    short qIndex = 0, sPos, ePos = 0;
    while (copyBuffer.hasRemaining() && qIndex < this.qdCount) {
      byte nextByte = copyBuffer.get();
      if (nextByte == terminator) {
        sPos = ePos;
        ePos = (short) copyBuffer.position();
        ePos += 4;
        this.questions[qIndex++] = copyBuffer.duplicate().position(sPos).limit(ePos).slice();
        if (ePos <= copyBuffer.limit()) {
          copyBuffer.position(ePos);
        }
      } else if (isPointer(nextByte)) {
        short offset = getOffsetFromPointer(nextByte, copyBuffer.get());
        short qOffset = (short) (offset - DnsHeader.SIZE);
        int currentPosition = copyBuffer.position();

        copyBuffer.position(qOffset);
        int limit = qOffset;
        while (copyBuffer.hasRemaining() && copyBuffer.get() != terminator) {
            limit = copyBuffer.position();
        }
        ByteBuffer duplicate = copyBuffer.duplicate().position(qOffset).limit(limit+1).slice();
        this.labelsMap.put((int) offset, duplicate);

        copyBuffer.position(currentPosition);
      }
    }
  }

  public int limit() {
    return this.questionBuffer.limit();
  }

  private void initDecompressedQuestions() {
    this.decompressedQuestions = new ByteBuffer[this.qdCount];
    int qIndex = 0;
    for (ByteBuffer question : this.questions) {
      ByteBuffer copiedQuestion = question.duplicate().position(0);
      ByteBuffer decompressedQuestion = ByteBuffer.allocate(512);
      for (int i = 0; i < copiedQuestion.limit(); i++) {
        byte nextByte = copiedQuestion.get(i);
        if (isPointer(nextByte)) {
          short offset = getOffsetFromPointer(nextByte, question.get(i+1));
          ByteBuffer mappedBuffer = this.labelsMap.get((int) offset);
          if (Objects.nonNull(mappedBuffer)) {
            mappedBuffer.position(0);
            decompressedQuestion.put(mappedBuffer);
          }
          i += 2;
        } else {
          decompressedQuestion.put(nextByte);
        }
      }
      int limit = decompressedQuestion.position();
      this.decompressedQuestions[qIndex++] = decompressedQuestion.duplicate().position(0).limit(limit).slice();
    }
  }

  public ByteBuffer[] getQuestions() {
    return Arrays.stream(this.questions).map(buff -> buff.duplicate().position(0).limit(buff.limit()).slice()).toArray(ByteBuffer[]::new);
  }

  public ByteBuffer[] getDecompressedQuestions() {
    return Arrays.stream(this.decompressedQuestions).map(buff -> buff.duplicate().position(0).limit(buff.limit()).slice()).toArray(ByteBuffer[]::new);
  }
}
