package io2;

import io.BufferWrapper;
import io.DnsHeader;
import io.DnsQuestion;
import java.nio.ByteBuffer;

public class DnsQuery implements BufferWrapper, IDnsMessage {
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

  public int length() {
    return this.queryBuffer.array().length;
  }

  public DnsHeader getHeader() {
    return this.header;
  }

  public DnsQuestion getQuestion() {
    return this.question;
  }
}
