package io;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.BitSet;
import utils.ByteManipulator;

class DnsHeaderBuilder {

  private final ByteBuffer headerBuffer;

  private final static int HEADER_SIZE = 12;

  DnsHeaderBuilder() {
    this.headerBuffer = ByteBuffer
        .allocate(HEADER_SIZE)
        .order(ByteOrder.BIG_ENDIAN);
  }

  protected DnsHeaderBuilder transactionId(short transactionId) {
    this.headerBuffer.putShort(transactionId);
    return this;
  }

  protected DnsHeaderBuilder flags(boolean isReply) {
    final BitSet flags = new BitSet(16);
    // QR (Query/Response) flag - bit 15
    flags.set(15, isReply);

    this.headerBuffer.put(ByteManipulator.toBigEndian(flags.toByteArray()));
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
