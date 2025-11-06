package io;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class DnsResponseBuilder implements Builder<DnsResponse> {

  private final ByteBuffer messageBuffer;
  private DnsHeader header;
  private DnsQuestion question;
  private DnsAnswer answer;

  private final static int MAX_DNS_PACKET_SIZE = 512;
  private boolean IS_RAW = false;

  public DnsResponseBuilder() {
    this.messageBuffer = ByteBuffer.allocate(MAX_DNS_PACKET_SIZE).order(ByteOrder.BIG_ENDIAN);
  }

  public DnsResponseBuilder(byte[] message) {
    this.IS_RAW = true;
    this.messageBuffer = ByteBuffer.wrap(message);
    this.header = new DnsHeader(this.messageBuffer.duplicate().position(0).limit(12).slice());
    this.question = new DnsQuestion(this.messageBuffer.duplicate().position(12).slice(), this.header.getQDCount());
    this.answer = new DnsAnswer(this.messageBuffer.duplicate().position(12+this.question.limit()).slice(), this.header.getANCount());
  }

  public DnsResponseBuilder header(DnsHeader header) {
    this.header = header;
    return this;
  }

  public DnsResponseBuilder question(ByteBuffer[] questions) {
    this.question = new DnsQuestionBuilder()
        .questions(questions)
        .build();
    return this;
  }

  public DnsResponseBuilder answer(ByteBuffer[] answers) {
    this.answer = new DnsAnswerBuilder()
        .answers(answers)
        .build();
    return this;
  }

  @Override
  public DnsResponse build() {
    if (this.IS_RAW) {
      return new DnsResponse(this.messageBuffer, this.header, this.question, this.answer);
    }
    return new DnsResponse(this.messageBuffer
        .put(this.header.getBuffer())
        .put(this.question.getBuffer())
        .put(this.answer.getBuffer())
        .duplicate()
        .position(0),
        this.header,
        this.question,
        this.answer
    );
  }
}
