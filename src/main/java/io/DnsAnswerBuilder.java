package io;

import static consts.QuestionClass.IN;
import static consts.QuestionType.A;
import static utils.ByteUtils.getOffsetFromPointer;
import static utils.ByteUtils.isPointer;

import java.net.Inet4Address;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Objects;

class DnsAnswerBuilder implements Builder<DnsAnswer> {
  private final ByteBuffer answerBuffer;
  private Map<Integer, ByteBuffer> labelsMap;

  private final static int TTL = 60;
  private final static short RDLENGTH = 4;

  DnsAnswerBuilder(int size) {
    this.answerBuffer = ByteBuffer.allocate(size);
  }

//  DnsAnswerBuilder answers(String[] records) {
//    for (var record : records) {
//      this.setResourceRecord(record);
//    }
//    return this;
//  }

  DnsAnswerBuilder answers(ByteBuffer[] records, Map<Integer, ByteBuffer> labelsMap) {
    this.labelsMap = labelsMap;
    for (var record : records) {
      this.setResourceRecord(record);
    }
    return this;
  }

  @Override
  public DnsAnswer build() {
    int cursor = this.answerBuffer.position();
    return new DnsAnswer(this.answerBuffer.duplicate().position(0).limit(cursor).slice());
  }

//  private void setResourceRecord(String name) {
//    String[] domainParts = name.split("\\.");
//    String secondLevelDomainName = domainParts[0];
//    String topLevelDomainName = domainParts[1];
//    byte terminator = 0;
//
//    // Name
//    this.answerBuffer
//        .put((byte) secondLevelDomainName.length())
//        .put(secondLevelDomainName.getBytes(StandardCharsets.UTF_8))
//        .put((byte) topLevelDomainName.length())
//        .put(topLevelDomainName.getBytes())
//        .put(terminator);
//    // Type
//    this.answerBuffer.putShort(A.value());
//    // Class
//    this.answerBuffer.putShort(IN.value());
//    // TTL (Time-to-live)
//    this.answerBuffer.putInt(TTL);
//    // Length (RDLENGTH
//    this.answerBuffer.putShort(RDLENGTH);
//    // Data (RDATA)
//    this.answerBuffer.put(Inet4Address.getLoopbackAddress().getAddress());
//  }

  private void setResourceRecord(ByteBuffer record) {
    ByteBuffer copyBuffer = ByteBuffer.allocate(512);
    for (int i = 0; i < record.limit(); i++) {
      byte nextByte = record.get(i);
      if (isPointer(nextByte)) {
        short offset = getOffsetFromPointer(nextByte, record.get(i+1));
        ByteBuffer mappedBuffer = this.labelsMap.get((int) offset);
        if (Objects.nonNull(mappedBuffer)) {
          copyBuffer.put(mappedBuffer);
        }
        i += 2;
      } else {
        copyBuffer.put(nextByte);
      }
    }
    int limit = copyBuffer.position();

    // Name
    this.answerBuffer.put(copyBuffer.duplicate().position(0).limit(limit).slice());
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
