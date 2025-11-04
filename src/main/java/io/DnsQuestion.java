package io;

import static utils.ByteUtils.getOffsetFromPointer;
import static utils.ByteUtils.isPointer;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class DnsQuestion implements BufferWrapper {
  private final ByteBuffer questionBuffer;
  private final short qdCount;
  private final Map<Integer, ByteBuffer> labelsMap = new HashMap<>();
  private ByteBuffer[] questions;

  private final static byte terminator = 0;

  DnsQuestion(ByteBuffer questionBuffer, short questionNumber) {
    this.questionBuffer = questionBuffer.duplicate();
    this.qdCount = questionNumber;
    initQuestions();
  }

  public ByteBuffer getBuffer() {
    return this.questionBuffer;
  }

  public byte[] getBytes() {
    return this.questionBuffer.array();
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
        this.questions[qIndex++] = copyBuffer.duplicate().position(sPos).limit(ePos).slice();
        ePos += 4;
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

  public ByteBuffer[] getQuestions() {
    return Arrays.stream(this.questions).map(buff -> buff.duplicate().position(0).limit(buff.limit()).slice()).toArray(ByteBuffer[]::new);
  }

  public Map<Integer, ByteBuffer> getLabelsMap() {
    return this.labelsMap;
  }
}
