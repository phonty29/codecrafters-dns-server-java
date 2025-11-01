package io;

import java.nio.ByteBuffer;

public class DnsQuery implements BufferWrapper {
  private final ByteBuffer queryBuffer;
  private final DnsHeader header;
  private final DnsQuestion question;

  DnsQuery(ByteBuffer buffer, DnsHeader header, DnsQuestion question) {
    this.queryBuffer = buffer.duplicate();
    this.header = header;
    this.question = question;
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

  DnsQuestion getQuestion() {
    return this.question;
  }
}
