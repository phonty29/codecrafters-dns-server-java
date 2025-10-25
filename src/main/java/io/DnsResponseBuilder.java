package io;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class DnsResponseBuilder {
  private final ByteBuffer messageBuffer;
  private final DnsHeaderBuilder dnsHeaderBuilder;
  private ByteBuffer queryBuffer;

  private final static int MAX_DNS_PACKET_SIZE = 512;
  private final static int RANDOM_TRANSACTION_ID = 1234;

  public DnsResponseBuilder() {
    this.messageBuffer = ByteBuffer
        .allocate(MAX_DNS_PACKET_SIZE)
        .order(ByteOrder.BIG_ENDIAN);

    this.dnsHeaderBuilder = new DnsHeaderBuilder();
  }

  public DnsResponseBuilder query(byte[] query) {
    this.queryBuffer = ByteBuffer.wrap(query);
    return this;
  }

  public DnsResponseBuilder buildHeader() {
    this.messageBuffer.put(
        this.dnsHeaderBuilder
        .transactionId((short) RANDOM_TRANSACTION_ID)
        .flags(true)
        .qdCount((short) 1)
        .anCount((short) 0)
        .nsCount((short) 0)
        .arCount((short) 0)
        .build()
    );
    return this;
  }

  public DnsResponseBuilder buildQuestion(String name) {
    String[] domainParts = name.split("\\.");
    String secondLevelDomainName = domainParts[0];
    String topLevelDomainName = domainParts[1];
    byte terminator = 0;
    byte lengthOfSld = (byte) secondLevelDomainName.length();
    byte lengthOfTld = (byte) topLevelDomainName.length();

    // Name
    this.messageBuffer
        .put(lengthOfSld)
        .put(secondLevelDomainName.getBytes())
        .put(lengthOfTld)
        .put(topLevelDomainName.getBytes())
        .put(terminator);
    // Type
    this.messageBuffer.putShort((short) 1);
    // Class
    this.messageBuffer.putShort((short) 1);

    return this;
  }

  public DnsResponse build() {
    return new DnsResponse(this.messageBuffer);
  }
}
