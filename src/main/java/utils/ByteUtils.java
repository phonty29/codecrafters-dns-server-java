package utils;

public class ByteUtils {

  public static boolean isPointer(byte nextByte) {
    byte pointerMask = (byte) 0b11000000;
    return (nextByte & pointerMask) == pointerMask;
  }

  public static short getOffsetFromPointer(byte nextByte, byte restByte) {
    short pointer = (short) (((nextByte & 0xFF) << 8) | (restByte & 0xFF));
    return (short) (pointer & ~(0b11 << 14));
  }
}
