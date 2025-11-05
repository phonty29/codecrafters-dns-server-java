package io;

import java.nio.ByteBuffer;

public class DnsAnswer implements BufferWrapper {
  private final ByteBuffer answerBuffer;

  DnsAnswer(ByteBuffer answerBuffer) {
    this.answerBuffer = answerBuffer.duplicate().position(0);
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
}
