package io;

import static consts.QuestionClass.IN;
import static consts.QuestionType.A;

import java.net.Inet4Address;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

class DnsAnswerBuilder {
  private final ByteBuffer answerBuffer;

  private final static int TTL = 60;
  private final static short RDLENGTH = 4;

  DnsAnswerBuilder(int size) {
    this.answerBuffer = ByteBuffer.allocate(size);
  }

  protected DnsAnswerBuilder answers(String[] records) {
    for (var record : records) {
      this.setResourceRecord(record);
    }
    return this;
  }

  protected ByteBuffer build() {
    int cursor = answerBuffer.position();
    return answerBuffer.position(0).limit(cursor).slice();
  }

  private void setResourceRecord(String name) {
    String[] domainParts = name.split("\\.");
    String secondLevelDomainName = domainParts[0];
    String topLevelDomainName = domainParts[1];
    byte terminator = 0;

    // Name
    this.answerBuffer
        .put((byte) secondLevelDomainName.length())
        .put(secondLevelDomainName.getBytes(StandardCharsets.UTF_8))
        .put((byte) topLevelDomainName.length())
        .put(topLevelDomainName.getBytes())
        .put(terminator);
    // Type
    this.answerBuffer.putShort(A.value());
    // Class
    this.answerBuffer.putShort(IN.value());
    // TTL (Time-to-live)
    this.answerBuffer.putInt(TTL);
    // Length (RDLENGTH
    this.answerBuffer.putShort(RDLENGTH);
    // Data (RDATA)
    this.answerBuffer.put(Inet4Address.getLoopbackAddress().getAddress());
  }
}
