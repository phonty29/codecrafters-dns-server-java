import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DnsResponse {
  private final static int MAX_DNS_PACKET_SIZE = 512;
  private final static int HEADER_SIZE = 12;
  private final static int PACKET_IDENTIFIER_SIZE = 2;
  private final static short CLASS_IN = 1;
  private final ByteBuffer buffer = ByteBuffer.allocate(MAX_DNS_PACKET_SIZE);

  public byte[] response(byte[] query) {
    return buffer
        .put(getDnsHeader())
        .put(getQuestion())
        .array();
  }

  private ByteBuffer getQuestion() {
    return ByteBuffer
        .allocate(buffer.remaining())
        .put(getName())
        .put(getType())
        .put(getClassCode());
  }

  private ByteBuffer getName() {
    String topLevelDomainName = "com";
    String secondLevelDomainName = "codecrafters";
    byte terminator = 0;
    byte lengthOfTld = (byte) topLevelDomainName.length();
    byte lengthOfSld = (byte) secondLevelDomainName.length();
    // 3 = bytes that represents: length of tld + length of sld + terminator
    int size = lengthOfSld + lengthOfTld + 3;

    return ByteBuffer
        .allocate(size)
        .put(lengthOfSld)
        .put(secondLevelDomainName.getBytes(StandardCharsets.UTF_8))
        .put(lengthOfTld)
        .put(topLevelDomainName.getBytes(StandardCharsets.UTF_8))
        .put(terminator);
  }

  private ByteBuffer getType() {
    return ByteBuffer
        .allocate(2)
        .putChar('A');
  }

  private ByteBuffer getClassCode() {
    return ByteBuffer
        .allocate(2)
        .putShort(CLASS_IN);
  }

  private ByteBuffer getDnsHeader() {
    return ByteBuffer
        .allocate(HEADER_SIZE)
        .put(getPacketIdentifier())
        .put(getQueryIndicator());
  }

  private ByteBuffer getPacketIdentifier() {
    return ByteBuffer
        .allocate(PACKET_IDENTIFIER_SIZE)
        .putShort((short) 1234);
  }

  private ByteBuffer getQueryIndicator() {
    byte queryIndicator = (byte) (1<<7);
    return ByteBuffer
        .allocate(1)
        .put(queryIndicator);
  }
}
