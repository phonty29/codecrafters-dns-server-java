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

  private final static int TTL = 60;
  private final static short RDLENGTH = 4;

  DnsAnswerBuilder(int size) {
    this.answerBuffer = ByteBuffer.allocate(size);
  }

  DnsAnswerBuilder answers(ByteBuffer[] records) {
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

  private void setResourceRecord(ByteBuffer record) {
    // Name
    this.answerBuffer.put(record);
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
