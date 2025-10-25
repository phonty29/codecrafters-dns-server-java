package utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ByteManipulation {
  public static byte[] toBigEndian(byte[] littleEndianBytes) {
    if (littleEndianBytes.length == 0) {
      return new byte[0];
    }

    // Reverse the byte array to get big-endian order
    byte[] bigEndianBytes = new byte[littleEndianBytes.length];
    for (int i = 0; i < littleEndianBytes.length; i++) {
      bigEndianBytes[i] = littleEndianBytes[littleEndianBytes.length - 1 - i];
    }
    return bigEndianBytes;
  }
}
