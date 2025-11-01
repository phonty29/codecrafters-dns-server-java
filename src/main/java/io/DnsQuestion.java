package io;

import java.nio.ByteBuffer;

class DnsQuestion implements BufferWrapper {
  private final ByteBuffer questionBuffer;
  private final short qdCount;

  DnsQuestion(ByteBuffer questionBuffer, short questionNumber) {
    this.questionBuffer = questionBuffer.duplicate();
    this.qdCount = questionNumber;
  }

  public ByteBuffer getBuffer() {
    return this.questionBuffer;
  }

  public byte[] getBytes() {
    return this.questionBuffer.array();
  }

  public ByteBuffer[] getLabels() {
    this.questionBuffer.position(0);
    ByteBuffer[] labelsBuffer = new ByteBuffer[this.qdCount];
    short qIndex = 0, sPos = 0, ePos = 0;
    while (this.questionBuffer.hasRemaining() && qIndex < this.qdCount) {
      byte nextByte = this.questionBuffer.get();
      if (nextByte == (byte) 0) {
          sPos = (short) (ePos + 4);
          ePos = (short) this.questionBuffer.position();
          System.out.println(sPos + " " + ePos);
          labelsBuffer[qIndex++] = this.questionBuffer.duplicate().position(sPos).limit(ePos).slice();
      }
    }
    return labelsBuffer;
  }
}
