import java.nio.ByteBuffer;

public class DnsResponse {
  private final int MAX_DNS_PACKET_SIZE = 512;
  private final int PACKET_IDENTIFIER_SIZE = 2;
  private final ByteBuffer buffer = ByteBuffer.allocate(MAX_DNS_PACKET_SIZE);

  public byte[] compile(byte[] query) {
    byte[] packetIdentifier = ByteBuffer
        .allocate(PACKET_IDENTIFIER_SIZE)
        .putShort((short) 1234)
        .array();
    byte queryIndicator = (byte) (1<<7);

    buffer.put(packetIdentifier);
    buffer.put(queryIndicator);
    return buffer.array();
  }
}
