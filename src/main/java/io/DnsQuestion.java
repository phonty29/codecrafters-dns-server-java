package io;

import java.nio.ByteBuffer;

class DnsQuestion implements BufferWrapper {
  private final ByteBuffer questionBuffer;
  private final short qdCount;
  private final ByteBuffer[] labels;

  DnsQuestion(ByteBuffer questionBuffer, short questionNumber) {
    this.questionBuffer = questionBuffer.duplicate();
    this.qdCount = questionNumber;
    this.labels = retrieveLabels();
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

  private ByteBuffer[] retrieveLabels() {
    ByteBuffer[] labelsBuffer = new ByteBuffer[this.qdCount];
    short qIndex = 0, sPos, ePos = 0;
    while (this.questionBuffer.hasRemaining() && qIndex < this.qdCount) {
      byte nextByte = this.questionBuffer.get();
      if (nextByte == (byte) 0) {
          sPos = (short) (ePos + (qIndex > 0 ? 4 : 0));
          ePos = (short) this.questionBuffer.position();
          labelsBuffer[qIndex++] = this.questionBuffer.duplicate().position(sPos).limit(ePos).slice();
      }
    }
    return labelsBuffer;
  }
}
