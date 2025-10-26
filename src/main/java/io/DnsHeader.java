package io;

import java.nio.ByteBuffer;

class DnsHeader implements BufferWrapper {
  private final ByteBuffer headerBuffer;

  DnsHeader(ByteBuffer headerBuffer) {
    this.headerBuffer = headerBuffer;
  }

  @Override
  public ByteBuffer getBuffer() {
    return this.headerBuffer;
  }

  @Override
  public byte[] getBytes() {
    return this.headerBuffer.array();
  }

  public short getPacketIdentifier() {

  }
}
