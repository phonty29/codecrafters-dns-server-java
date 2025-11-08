package io;

import java.nio.ByteBuffer;
import java.util.Objects;

public class DnsQueryBuilder implements Builder<DnsQuery> {

  private final ByteBuffer queryBuffer;
  private final DnsHeader header;
  private final DnsQuestion question;

  DnsQueryBuilder(byte[] bytes) {
    if (Objects.isNull(bytes) || bytes.length != IDnsMessage.PACKET_SIZE) {
      throw new IllegalArgumentException("Incorrect query size");
    }
    this.queryBuffer = ByteBuffer.wrap(bytes);
    this.header = new DnsHeader(queryBuffer.duplicate().position(0).limit(DnsHeader.SIZE).slice());
    this.question = new DnsQuestion(queryBuffer.duplicate().position(DnsHeader.SIZE).slice(),
        this.header.getQDCount());
  }

  @Override
  public DnsQuery build() {
    return new DnsQuery(this.queryBuffer, this.header, this.question);
  }
}
