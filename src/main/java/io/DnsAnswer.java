package io;

import java.nio.ByteBuffer;

class DnsAnswer implements BufferWrapper {
  private final ByteBuffer answerBuffer;

  DnsAnswer(ByteBuffer answerBuffer) {
    this.answerBuffer = answerBuffer;
  }

  @Override
  public ByteBuffer getBuffer() {
    return this.answerBuffer;
  }

  @Override
  public byte[] getBytes() {
    return this.answerBuffer.array();
  }
}
