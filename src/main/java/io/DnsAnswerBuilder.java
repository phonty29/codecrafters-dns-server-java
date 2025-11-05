package io;

import java.net.Inet4Address;
import java.nio.ByteBuffer;

class DnsAnswerBuilder implements Builder<DnsAnswer> {
  private final ByteBuffer answerBuffer;

  private final static int TTL = 60;
  private final static short RDLENGTH = 4;

  DnsAnswerBuilder(int size) {
    this.answerBuffer = ByteBuffer.allocate(size);
  }

  DnsAnswerBuilder questions(ByteBuffer[] questions) {
    for (var question : questions) {
      this.setResourceRecord(question);
    }
    return this;
  }

  @Override
  public DnsAnswer build() {
    int cursor = this.answerBuffer.position();
    return new DnsAnswer(this.answerBuffer.duplicate().position(0).limit(cursor).slice());
  }

  private void setResourceRecord(ByteBuffer question) {
    // Question
    this.answerBuffer.put(question);
    // TTL (Time-to-live)
    this.answerBuffer.putInt(TTL);
    // Length (RDLENGTH)
    this.answerBuffer.putShort(RDLENGTH);
    // Data (RDATA)
    this.answerBuffer.put(Inet4Address.getLoopbackAddress().getAddress());
  }
}
