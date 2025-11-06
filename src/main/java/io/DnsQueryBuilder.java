package io;

import java.nio.ByteBuffer;
import java.util.Objects;

public class DnsQueryBuilder implements Builder<DnsQuery> {
  private final ByteBuffer queryBuffer;
  private final DnsHeader header;
  private final DnsQuestion question;

  DnsQueryBuilder(byte[] bytes) {
    if (Objects.isNull(bytes) || bytes.length != 512) {
      throw new IllegalArgumentException("Incorrect query size");
    }
    this.queryBuffer = ByteBuffer.wrap(bytes);
    this.header = new DnsHeader(queryBuffer.duplicate().position(0).limit(12).slice());
    this.question = new DnsQuestion(queryBuffer.duplicate().position(12).slice(), this.header.getQDCount());
  }

  DnsQueryBuilder(ByteBuffer buffer) {
    this.queryBuffer = buffer.duplicate().position(0);
    this.header = new DnsHeader(queryBuffer.duplicate().position(0).limit(12).slice());
    this.question = new DnsQuestion(queryBuffer.duplicate().position(12).slice(), this.header.getQDCount());
  }

  @Override
  public DnsQuery build() {
    return new DnsQuery(this.queryBuffer, this.header, this.question);
  }
}
