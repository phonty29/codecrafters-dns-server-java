package io2;

import io.DnsAnswer;
import io.DnsHeader;
import io.DnsQuestion;

public class DnsResponse implements IDnsResponse{
  private final DnsHeader header;
  private final DnsQuestion question;
  private final DnsAnswer answer;

  public DnsResponse(DnsHeader header, DnsQuestion question, DnsAnswer answer) {
    this.header = header;
    this.question = question;
    this.answer = answer;
  }

  @Override
  public DnsAnswer getAnswer() {
    return this.answer;
  }

  @Override
  public DnsHeader getHeader() {
    return this.header;
  }

  @Override
  public DnsQuestion getQuestion() {
    return this.question;
  }
}
