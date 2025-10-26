package io;

import java.nio.ByteBuffer;

class DnsHeader implements BufferWrapper {
  private final ByteBuffer headerBuffer;
  private final static int HEADER_SIZE = 12;

  DnsHeader(ByteBuffer headerBuffer) {
    this.headerBuffer = headerBuffer.duplicate();
    if (this.headerBuffer.remaining() != HEADER_SIZE) {
      throw new IllegalStateException("Invalid header size");
    }
  }

  @Override
  public ByteBuffer getBuffer() {
    return this.headerBuffer;
  }

  @Override
  public byte[] getBytes() {
    return this.headerBuffer.array();
  }

  public short getPacketID() {
    return this.headerBuffer.getShort(0);
  }

  public Flags getFlags() {
    return new Flags(this.headerBuffer.getShort(1));
  }

  public static class Flags {
    private final short flags;

    private Flags(short flags) {
      this.flags = flags;
    }

    public boolean isRD() {
      return (this.flags & (1 << 8)) == (1 << 8);
    }

    public byte getOpcode() {
      return (byte) ((this.flags >> 11) & 0b1111);
    }
  }
}
