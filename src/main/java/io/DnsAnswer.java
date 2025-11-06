package io;

import static utils.ByteUtils.isPointer;

import java.nio.ByteBuffer;

public class DnsAnswer implements BufferWrapper {
  private final ByteBuffer answerBuffer;
  private final int answerCount;
  private ByteBuffer[] answers;
  private final static int terminator = 0;
  private int limit;

  DnsAnswer(ByteBuffer answerBuffer, int answerCount) {
    this.answerBuffer = answerBuffer.duplicate().position(0);
    this.answerCount = answerCount;
    initAnswers();
  }

  @Override
  public ByteBuffer getBuffer() {
    return this.answerBuffer;
  }

  @Override
  public byte[] getBytes() {
    return this.answerBuffer.array();
  }

  @Override
  public int length() {
    return this.answerBuffer.array().length;
  }

  public ByteBuffer[] getAnswers() {
    return this.answers;
  }

  public void initAnswers() {
    this.answers = new ByteBuffer[this.answerCount];
    ByteBuffer copyBuffer = this.answerBuffer.duplicate();
    int qIndex = 0, sPos, ePos = 0;

    while (copyBuffer.hasRemaining() && qIndex < this.answerCount) {
      byte nextByte = copyBuffer.get();
      if (nextByte == terminator) {
        sPos = ePos;

        short type = copyBuffer.getShort();
        short classCode = copyBuffer.getShort();
        int ttl = copyBuffer.getInt();
        short rdlen = copyBuffer.getShort();
        var buf = copyBuffer.get(new byte[rdlen]);
        ePos = copyBuffer.position();
        this.answers[qIndex++] = copyBuffer.duplicate().position(sPos).limit(ePos).slice();
      }
    }
    this.limit = ePos;
  }

  public int limit() {
    return this.limit;
  }
}
