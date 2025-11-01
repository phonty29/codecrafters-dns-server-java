package io;

import io.DnsHeader.Flags;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

class DnsHeaderBuilder implements Builder<DnsHeader> {

  private final ByteBuffer headerBuffer;

  private final static int HEADER_SIZE = 12;
  private final static byte NO_ERROR = 0;
  private final static byte NOT_IMPLEMENTED = 4;

  DnsHeaderBuilder() {
    this.headerBuffer = ByteBuffer
        .allocate(HEADER_SIZE)
        .order(ByteOrder.BIG_ENDIAN);
  }

  protected DnsHeaderBuilder transactionId(short transactionId) {
    this.headerBuffer.putShort(transactionId);
    return this;
  }

  /**
   * @param queryFlags flags from DNS query
   * @return this
   */
  protected DnsHeaderBuilder flags(Flags queryFlags) {
    Flags responseFlags = new Flags((short) 0);
    responseFlags.setQueryResponseIndicator(true);
    responseFlags.setOpcode(queryFlags.getOpcode());
    responseFlags.setRecursionDesired(queryFlags.isRecursionDesired());
    responseFlags.setRecursionDesired(queryFlags.isRecursionDesired());
    if (queryFlags.getOpcode() != 0) {
      responseFlags.setResponseCode(NOT_IMPLEMENTED);
    } else {
      responseFlags.setResponseCode(NO_ERROR);
    }
    this.headerBuffer.putShort(responseFlags.value());
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
    return new DnsHeader(this.headerBuffer.duplicate().position(0));
  }
}
