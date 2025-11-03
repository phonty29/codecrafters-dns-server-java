package utils;

public class ByteUtils {

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

  public static boolean isPointer(byte nextByte) {
    byte pointerMask = (byte) 0b11000000;
    return (nextByte & pointerMask) == pointerMask;
  }

  public static short getOffsetFromPointer(byte nextByte, byte restByte) {
    short pointer = (short) (((nextByte & 0xFF) << 8) | (restByte & 0xFF));
    return (short) (pointer & ~(0b11 << 14));
  }
}
