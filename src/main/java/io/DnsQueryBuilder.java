package io;

import java.nio.ByteBuffer;
import java.util.Objects;

public class DnsQueryBuilder implements Builder<DnsQuery> {
  private final ByteBuffer queryBuffer;
  private DnsHeader header;

  DnsQueryBuilder(byte[] bytes) {
    if (Objects.isNull(bytes) || bytes.length != 512) {
      throw new IllegalArgumentException("Incorrect query size");
    }
    this.queryBuffer = ByteBuffer.wrap(bytes);
    this.header = new DnsHeader(queryBuffer.duplicate().position(0).limit(12).slice());
  }

  DnsQueryBuilder header() {
    return this;
  }

  @Override
  public DnsQuery build() {
    return new DnsQuery(this.queryBuffer, this.header);
  }
}
