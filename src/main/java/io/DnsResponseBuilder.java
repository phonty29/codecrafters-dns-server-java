package io;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class DnsResponseBuilder implements Builder<DnsResponse> {

  private final ByteBuffer messageBuffer;
  private DnsHeader header;
  private DnsQuestion question;
  private DnsAnswer answer;

  public DnsResponseBuilder() {
    this.messageBuffer = ByteBuffer.allocate(IDnsMessage.PACKET_SIZE).order(ByteOrder.BIG_ENDIAN);
  }

  public DnsResponseBuilder(byte[] message) {
    if (message.length != IDnsMessage.PACKET_SIZE) {
      throw new IllegalArgumentException("Invalid message length: " + message.length);
    }
    this.messageBuffer = ByteBuffer.wrap(message);
    this.header = new DnsHeader(
        this.messageBuffer.duplicate().position(0).limit(DnsHeader.SIZE).slice());
    this.question = new DnsQuestion(this.messageBuffer.duplicate().position(DnsHeader.SIZE).slice(),
        this.header.getQDCount());
    this.answer = new DnsAnswer(
        this.messageBuffer.duplicate().position(DnsHeader.SIZE + this.question.limit()).slice(),
        this.header.getANCount());
  }

  public DnsResponseBuilder header(DnsHeader header) {
    this.header = header;
    this.messageBuffer.put(this.header.getBuffer());
    return this;
  }

  public DnsResponseBuilder question(ByteBuffer[] questions) {
    this.question = new DnsQuestionBuilder()
        .questions(questions)
        .build();
    this.messageBuffer.put(this.question.getBuffer());
    return this;
  }

  public DnsResponseBuilder answer(ByteBuffer[] answers) {
    this.answer = new DnsAnswerBuilder()
        .answers(answers)
        .build();
    this.messageBuffer.put(this.answer.getBuffer());
    return this;
  }

  @Override
  public DnsResponse build() {
    return new DnsResponse(this.messageBuffer.duplicate().position(0), this.header, this.question,
        this.answer);
  }
}
