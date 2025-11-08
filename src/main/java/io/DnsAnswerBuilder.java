package io;

import java.nio.ByteBuffer;

class DnsAnswerBuilder implements Builder<DnsAnswer> {

  private final ByteBuffer answerBuffer;
  private int answerCount;

  DnsAnswerBuilder() {
    this.answerBuffer = ByteBuffer.allocate(IDnsMessage.PACKET_SIZE);
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
    return new DnsAnswer(this.answerBuffer.duplicate().position(0).limit(cursor).slice(),
        this.answerCount);
  }

  private void setResourceRecord(ByteBuffer question) {
    this.answerBuffer.put(question);
  }
}
