package io;

import java.nio.ByteBuffer;

public class DnsResponse implements BufferWrapper {

  private final ByteBuffer responseBuffer;

  public DnsResponse(ByteBuffer responseBuffer) {
    this.responseBuffer = responseBuffer.duplicate().position(0);
  }

  public static DnsResponseBuilder builder() {
    return new DnsResponseBuilder();
  }

  public ByteBuffer getBuffer() {
    return this.responseBuffer;
  }

  public byte[] getBytes() {
    return this.responseBuffer.array();
  }
}
