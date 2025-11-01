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
    return new Flags(this.headerBuffer.getShort(2));
  }

  public short getQDCount() {
    return this.headerBuffer.getShort(4);
  }

  public static class Flags {
    private short flags;

    Flags(short flags) {
      this.flags = flags;
    }

    public short value() {
      return this.flags;
    }

    public boolean isRecursionDesired() {
      return (this.flags & (1 << 8)) == (1 << 8);
    }

    public void setRecursionDesired(boolean isRecursionDesired) {
      if (isRecursionDesired) {
        this.flags |= (short) (1 << 8);
      } else {
        this.flags &= ~(1 << 8);
      }
    }

    public byte getOpcode() {
      return (byte) ((this.flags >> 11) & 0b1111);
    }

    public void setOpcode(byte opcode) {
      this.flags |= (short) ((opcode & 0b1111) << 11);
    }

    public void setQueryResponseIndicator(boolean isResponse) {
      if (isResponse) {
        this.flags |= (short) (1 << 15);
      } else {
        this.flags &= (short) ~(1 << 15);
      }
    }

    public void setResponseCode(byte code) {
      this.flags |= (short) (code & 0b1111);
    }
  }
}
