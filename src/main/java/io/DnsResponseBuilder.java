package io;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

public class DnsResponseBuilder {
  private final ByteBuffer messageBuffer;
  private final DnsHeaderBuilder dnsHeaderBuilder = new DnsHeaderBuilder();
  private final DnsQuestionBuilder dnsQuestionBuilder = new DnsQuestionBuilder();
  private ByteBuffer queryBuffer;

  private final static int MAX_DNS_PACKET_SIZE = 512;
  private final static int RANDOM_TRANSACTION_ID = 1234;
  private final static String[] questions = {"codecrafters.io"};

  public DnsResponseBuilder() {
    this.messageBuffer = ByteBuffer
        .allocate(MAX_DNS_PACKET_SIZE)
        .order(ByteOrder.BIG_ENDIAN);
  }

  public DnsResponseBuilder query(byte[] query) {
    this.queryBuffer = ByteBuffer.wrap(query);
    return this;
  }

  public void buildHeader() {
    this.messageBuffer.put(
        this.dnsHeaderBuilder
            .transactionId((short) RANDOM_TRANSACTION_ID)
            .flags(true)
            .qdCount((short) questions.length)
            .anCount((short) 0)
            .nsCount((short) 0)
            .arCount((short) 0)
            .build()
    );
  }

  public void buildQuestion() {
    this.messageBuffer.put(
        this.dnsQuestionBuilder
            .questions(questions)
            .build()
    );
  }

  public DnsResponse build() {
    buildHeader();
    buildQuestion();
    return new DnsResponse(this.messageBuffer);
  }
}
