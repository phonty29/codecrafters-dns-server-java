package io;

import java.nio.ByteBuffer;

public class DnsResponse {
  private final ByteBuffer responseBuffer;

  public DnsResponse(ByteBuffer responseBuffer) {
    this.responseBuffer = responseBuffer;
  }

  public static DnsResponseBuilder builder() {
    return new DnsResponseBuilder();
  }

  public byte[] array() {
    return this.responseBuffer.array();
  }
}
