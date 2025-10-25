package utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ByteManipulation {
  public static byte[] toBigEndian(byte[] arr) {
    var buff = ByteBuffer.wrap(arr);
    return buff.array();
  }
}
