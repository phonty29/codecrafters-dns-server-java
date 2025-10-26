package io;

import io.DnsHeader.Flags;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

class DnsHeaderBuilder implements Builder<DnsHeader> {

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

  protected DnsHeaderBuilder flags(Flags queryFlags) {
    short flags = 0;
    flags |= 1 << 15; // QR bit
    flags |= queryFlags.getOpcode() << 11;
    flags |= queryFlags.isRD() ? 1 << 8 : 0;
    if (queryFlags.getOpcode() != 0) {
      flags |= 4;
    }

    this.headerBuffer.putShort(flags);
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

  @Override
  public DnsHeader build() {
    return new DnsHeader(this.headerBuffer.position(0).duplicate());
  }
}
