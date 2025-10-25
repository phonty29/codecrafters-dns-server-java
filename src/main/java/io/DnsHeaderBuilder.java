package io;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.BitSet;

class DnsHeaderBuilder {
  private final ByteBuffer headerBuffer;

  private final static int HEADER_SIZE = 12;

  public DnsHeaderBuilder() {
    this.headerBuffer = ByteBuffer
        .allocate(HEADER_SIZE)
        .order(ByteOrder.BIG_ENDIAN);
  }

  protected DnsHeaderBuilder transactionId(short transactionId) {
    headerBuffer.putShort(transactionId);
    return this;
  }

  protected DnsHeaderBuilder flags(boolean isReply) {
    // QR (Query/Response) flag - bit 7
    final BitSet flags = new BitSet(7);
    flags.set(7, isReply);
    this.headerBuffer.put(flags.toByteArray()[0]);
    this.headerBuffer.put((byte) 0);
    return this;
  }

  protected DnsHeaderBuilder qdCount(short qdCount) {
    // Number of questions in the question section
    this.headerBuffer.putShort(qdCount);
    return this;
  }

  protected DnsHeaderBuilder anCount(short anCount) {
    // Number of answers in the answer section
    this.headerBuffer.putShort(anCount);
    return this;
  }

  protected DnsHeaderBuilder nsCount(short nsCount) {
    // Number of authority records in the authority section
    this.headerBuffer.putShort(nsCount);
    return this;
  }

  protected DnsHeaderBuilder arCount(short arCount) {
    // Number of additional records in the additional section
    this.headerBuffer.putShort(arCount);
    return this;
  }

  protected byte[] build() {
    return this.headerBuffer.array();
  }
}
