package io;

import java.nio.ByteBuffer;

public interface BufferWrapper {
  ByteBuffer getBuffer();

  byte[] getBytes();
}
