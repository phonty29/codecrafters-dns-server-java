package io;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class DnsResponseBuilder {

  private final ByteBuffer messageBuffer;
  private ByteBuffer queryBuffer;

  private final static int MAX_DNS_PACKET_SIZE = 512;
  private final static int RANDOM_TRANSACTION_ID = 1234;
  private final static String[] questions = {"codecrafters.io"};
  private final static String[] resourceRecords = {"codecrafters.io"};

  public DnsResponseBuilder() {
    this.messageBuffer = ByteBuffer.allocate(MAX_DNS_PACKET_SIZE).order(ByteOrder.BIG_ENDIAN);
  }

  public DnsResponseBuilder query(byte[] query) {
    this.queryBuffer = ByteBuffer.wrap(query);
    return this;
  }

  private void buildHeader() {
    this.messageBuffer.put(
        new DnsHeaderBuilder().transactionId((short) RANDOM_TRANSACTION_ID).flags(true)
            .qdCount((short) questions.length).anCount((short) resourceRecords.length)
            .nsCount((short) 0).arCount((short) 0).build());
  }

  private void buildQuestion() {
    this.messageBuffer.put(
        new DnsQuestionBuilder(this.messageBuffer.remaining()).questions(questions).build());
  }

  private void buildAnswers() {
    this.messageBuffer.put(
        new DnsAnswerBuilder(this.messageBuffer.remaining()).answers(resourceRecords).build());
  }

  public DnsResponse build() {
    buildHeader();
    buildQuestion();
    return new DnsResponse(this.messageBuffer);
  }
}
