package io;

import java.net.Inet4Address;
import java.nio.ByteBuffer;

class DnsAnswerBuilder implements Builder<DnsAnswer> {
  private final ByteBuffer answerBuffer;
  private int answerCount;

  private final static int TTL = 60;
  private final static short RDLENGTH = 4;

  DnsAnswerBuilder() {
    this.answerBuffer = ByteBuffer.allocate(512);
  }

  DnsAnswerBuilder answers(ByteBuffer[] answers) {
    this.answerCount = answers.length;
    for (var answer : answers) {
      this.setResourceRecord(answer);
    }
    return this;
  }

  @Override
  public DnsAnswer build() {
    int cursor = this.answerBuffer.position();
    return new DnsAnswer(this.answerBuffer.duplicate().position(0).limit(cursor).slice(), this.answerCount);
  }

  private void setResourceRecord(ByteBuffer question) {
    if (question != null) {
      this.answerBuffer.put(question);
    }
  }
}
