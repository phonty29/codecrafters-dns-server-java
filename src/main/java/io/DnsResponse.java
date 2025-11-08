package io;

import java.nio.ByteBuffer;

public class DnsResponse implements BufferWrapper, IDnsResponse {

  private final ByteBuffer responseBuffer;
  private final DnsHeader header;
  private final DnsQuestion question;
  private final DnsAnswer answer;

  protected DnsResponse(ByteBuffer responseBuffer, DnsHeader header, DnsQuestion question,
      DnsAnswer answer) {
    this.responseBuffer = responseBuffer.duplicate().position(0);
    this.header = header;
    this.question = question;
    this.answer = answer;
  }

  public static DnsResponseBuilder builder() {
    return new DnsResponseBuilder();
  }

  public static DnsResponseBuilder builder(byte[] response) {
    return new DnsResponseBuilder(response);
  }

  @Override
  public DnsHeader getHeader() {
    return this.header;
  }

  @Override
  public DnsQuestion getQuestion() {
    return this.question;
  }

  @Override
  public DnsAnswer getAnswer() {
    return this.answer;
  }

  @Override
  public ByteBuffer getBuffer() {
    return this.responseBuffer;
  }

  @Override
  public byte[] getBytes() {
    return this.responseBuffer.array();
  }

  @Override
  public int length() {
    return this.responseBuffer.array().length;
  }
}
