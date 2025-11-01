package io;

import java.nio.ByteBuffer;

class DnsQuestion implements BufferWrapper {
  private final ByteBuffer questionBuffer;
  private final short qdCount;
  private ByteBuffer[] labels;

  DnsQuestion(ByteBuffer questionBuffer, short questionNumber) {
    this.questionBuffer = questionBuffer.duplicate();
    this.qdCount = questionNumber;
    this.setLabels();
  }

  public ByteBuffer getBuffer() {
    return this.questionBuffer;
  }

  public byte[] getBytes() {
    return this.questionBuffer.array();
  }

  private void setLabels() {
    this.labels = new ByteBuffer[this.qdCount];
    short qIndex = 0, sPos, ePos = 0;
    while (this.questionBuffer.hasRemaining() && qIndex < this.qdCount) {
      byte nextByte = this.questionBuffer.get();
      if (nextByte == (byte) 0) {
        sPos = (short) (ePos + (qIndex > 0 ? 4 : 0));
        ePos = (short) this.questionBuffer.position();
        this.labels[qIndex++] = this.questionBuffer.duplicate().position(sPos).limit(ePos).slice();
      }
    }
  }

  public ByteBuffer[] getLabels() {
    return this.labels;
  }
}
