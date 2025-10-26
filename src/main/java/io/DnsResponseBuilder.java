package io;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class DnsResponseBuilder implements Builder<DnsResponse> {

  private final ByteBuffer messageBuffer;
  private DnsQuery query;

  private final static int MAX_DNS_PACKET_SIZE = 512;
  private final static String[] questions = {"codecrafters.io"};
  private final static String[] resourceRecords = {"codecrafters.io"};

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
        .qdCount((short) questions.length)
        .anCount((short) resourceRecords.length)
        .nsCount((short) 0)
        .arCount((short) 0)
        .build()
        .getBuffer();
  }

  private ByteBuffer question() {
    return new DnsQuestionBuilder(this.messageBuffer.remaining())
        .questions(questions)
        .build()
        .getBuffer();
  }

  private ByteBuffer answer() {
    return new DnsAnswerBuilder(this.messageBuffer.remaining())
        .answers(resourceRecords)
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
