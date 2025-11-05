package io;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class DnsResponseBuilder implements Builder<DnsResponse> {

  private final ByteBuffer messageBuffer;
  private DnsHeader header;
  private DnsQuestion question;
  private DnsAnswer answer;

  private final static int MAX_DNS_PACKET_SIZE = 512;

  public DnsResponseBuilder() {
    this.messageBuffer = ByteBuffer.allocate(MAX_DNS_PACKET_SIZE).order(ByteOrder.BIG_ENDIAN);
  }

  public DnsResponseBuilder(byte[] message) {
    this.messageBuffer = ByteBuffer.wrap(message);
    this.header = new DnsHeader(this.messageBuffer.duplicate().position(0).limit(12).slice());
    this.question = new DnsQuestion(this.messageBuffer.duplicate().position(12).slice(), this.header.getQDCount());
  }

  public DnsResponseBuilder header(DnsHeader header) {
    this.header = header;
    return this;
  }

  public DnsResponseBuilder question(ByteBuffer[] questions) {
    this.question = new DnsQuestionBuilder(this.messageBuffer.remaining())
        .questions(questions)
        .build();
    return this;
  }

  public DnsResponseBuilder answer(ByteBuffer[] answers) {
    this.answer = new DnsAnswerBuilder(this.messageBuffer.remaining())
        .questions(answers)
        .build();
    return this;
  }

  @Override
  public DnsResponse build() {
    return new DnsResponse(this.messageBuffer
        .put(this.header.getBuffer())
        .put(this.question.getBuffer())
        .put(this.answer.getBuffer())
        .duplicate()
        .position(0)
    );
  }
}
