package io;

import java.nio.ByteBuffer;

class DnsQuery implements BufferWrapper {
  private final ByteBuffer queryBuffer;
  private final DnsHeader header;

  DnsQuery(ByteBuffer buffer, DnsHeader header) {
    this.queryBuffer = buffer.duplicate();
    this.header = header;
  }

  public static DnsQueryBuilder builder(byte[] bytes) {
    return new DnsQueryBuilder(bytes);
  }

  @Override
  public ByteBuffer getBuffer() {
    return this.queryBuffer;
  }

  @Override
  public byte[] getBytes() {
    return this.queryBuffer.array();
  }

  DnsHeader getHeader() {
    return this.header;
  }
}
