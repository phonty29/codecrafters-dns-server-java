package io;

import java.nio.ByteBuffer;

public class DnsQuery implements BufferWrapper, IDnsMessage {
  private final ByteBuffer queryBuffer;
  private final DnsHeader header;
  private final DnsQuestion question;

  DnsQuery(ByteBuffer buffer, DnsHeader header, DnsQuestion question) {
    this.queryBuffer = buffer.duplicate();
    this.header = header;
    this.question = question;
  }

  public static DnsQueryBuilder builder(byte[] bytes) {
    return new DnsQueryBuilder(bytes);
  }

  public static DnsQueryBuilder builder(ByteBuffer buffer) {
    return new DnsQueryBuilder(buffer);
  }

  @Override
  public ByteBuffer getBuffer() {
    return this.queryBuffer;
  }

  @Override
  public byte[] getBytes() {
    return this.queryBuffer.array();
  }

  public int length() {
    return this.queryBuffer.array().length;
  }

  public DnsHeader getHeader() {
    return this.header;
  }

  public DnsQuestion getQuestion() {
    return this.question;
  }

  public ByteBuffer[] split() {
    ByteBuffer[] queries = new ByteBuffer[this.header.getQDCount()];
    int i = 0;
    for (ByteBuffer question : this.question.getDecompressedQuestions()) {
      ByteBuffer queryBuffer = ByteBuffer.allocate(512);
      DnsHeader header = new DnsHeaderBuilder()
          .transactionId(this.header.getPacketID())
          .flags(this.header.getFlags(), false)
          .qdCount((short) 1)
          .anCount((short) 0)
          .arCount((short) 0)
          .nsCount((short) 0)
          .build();
      queryBuffer.put(header.getBuffer());
      queryBuffer.put(question);
      queries[i++] = queryBuffer.duplicate().position(0).slice();
    }
    return queries;
  }
}
