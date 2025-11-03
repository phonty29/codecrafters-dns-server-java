package io;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class DnsResponseBuilder implements Builder<DnsResponse> {

  private final ByteBuffer messageBuffer;
  private DnsQuery query;

  private final static int MAX_DNS_PACKET_SIZE = 512;

  public DnsResponseBuilder() {
    this.messageBuffer = ByteBuffer.allocate(MAX_DNS_PACKET_SIZE).order(ByteOrder.BIG_ENDIAN);
  }

  public DnsResponseBuilder query(byte[] query) {
    this.query = DnsQuery
        .builder(query)
        .build();
    return this;
  }

  private ByteBuffer header() {
    return new DnsHeaderBuilder()
        .transactionId(this.query.getHeader().getPacketID())
        .flags(this.query.getHeader().getFlags())
        .qdCount(this.query.getHeader().getQDCount())
        .anCount(this.query.getHeader().getQDCount())
        .nsCount((short) 0)
        .arCount((short) 0)
        .build()
        .getBuffer();
  }

  private ByteBuffer question() {
    return new DnsQuestionBuilder(this.messageBuffer.remaining())
        .questions(this.query.getQuestion().getLabels(), this.query.getQuestion().getLabelsMap())
        .build()
        .getBuffer();
  }

  private ByteBuffer answer() {
    return new DnsAnswerBuilder(this.messageBuffer.remaining())
        .answers(this.query.getQuestion().getLabels(), this.query.getQuestion().getLabelsMap())
        .build()
        .getBuffer();
  }

  @Override
  public DnsResponse build() {
    return new DnsResponse(this.messageBuffer
        .put(header())
        .put(question())
        .put(answer())
        .duplicate()
        .position(0)
    );
  }
}
