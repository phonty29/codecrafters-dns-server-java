package io;

import java.nio.ByteBuffer;

class DnsQuestion implements BufferWrapper {
  private final ByteBuffer questionBuffer;

  DnsQuestion(ByteBuffer questionBuffer) {
    this.questionBuffer = questionBuffer.duplicate();
  }

  public ByteBuffer getBuffer() {
    return this.questionBuffer;
  }

  public byte[] getBytes() {
    return this.questionBuffer.array();
  }
}
