package io;

import java.nio.ByteBuffer;

public class DnsResponse implements BufferWrapper {

  private final ByteBuffer responseBuffer;
  private final DnsHeader header;
  private final DnsQuestion question;
  private final DnsAnswer answer;

  protected DnsResponse(ByteBuffer responseBuffer, DnsHeader header, DnsQuestion question, DnsAnswer answer) {
    this.responseBuffer = responseBuffer.duplicate().position(0);
    this.header = header;
    this.question = question;
    this.answer = answer;
  }

  public DnsHeader getHeader() {
    return this.header;
  }

  public DnsQuestion getQuestion() {
    return this.question;
  }

  public DnsAnswer getAnswer() {
    return this.answer;
  }

  public static DnsResponseBuilder builder() {
    return new DnsResponseBuilder();
  }

  public static DnsResponseBuilder builder(byte[] response) {
    return new DnsResponseBuilder(response);
  }

  public ByteBuffer getBuffer() {
    return this.responseBuffer;
  }

  public byte[] getBytes() {
    return this.responseBuffer.array();
  }

  @Override
  public int length() {
    return this.responseBuffer.array().length;
  }
}
