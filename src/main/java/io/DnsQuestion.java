package io;

import java.nio.ByteBuffer;

class DnsQuestion implements BufferWrapper {
  private final ByteBuffer questionBuffer;
  private final ByteBuffer[] labels;

  DnsQuestion(ByteBuffer questionBuffer, short questionNumber) {
    this.questionBuffer = questionBuffer.duplicate();
    this.labels = new ByteBuffer[questionNumber];
    short qIndex = 0, sPos, ePos = 0;
    while (this.questionBuffer.hasRemaining() && qIndex < questionNumber) {
      byte nextByte = this.questionBuffer.get();
      if (nextByte == (byte) 0) {
        sPos = (short) (ePos + (qIndex > 0 ? 4 : 0));
        ePos = (short) this.questionBuffer.position();
        this.labels[qIndex++] = this.questionBuffer.duplicate().position(sPos).limit(ePos).slice();
      }
    }
  }

  public ByteBuffer getBuffer() {
    return this.questionBuffer;
  }

  public byte[] getBytes() {
    return this.questionBuffer.array();
  }

  public ByteBuffer[] getLabels() {
    return this.labels;
  }
}
