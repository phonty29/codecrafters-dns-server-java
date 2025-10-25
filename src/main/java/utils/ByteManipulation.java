package utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ByteManipulation {
  public static byte[] toLittleEndian(byte[] arr) {
    var buff = ByteBuffer.wrap(arr);
    return buff.order(ByteOrder.LITTLE_ENDIAN).array();
  }
}
