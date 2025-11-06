package io;

import java.nio.ByteBuffer;

class DnsQuestionBuilder implements Builder<DnsQuestion> {
  private final ByteBuffer questionBuffer;
  private short qdCount = 0;

  DnsQuestionBuilder() {
    this.questionBuffer = ByteBuffer.allocate(512);
  }

  DnsQuestionBuilder questions(ByteBuffer[] labels) {
    this.qdCount = (short) labels.length;
    for (var label : labels) {
      this.setQuestion(label);
    }
    return this;
  }

  @Override
  public DnsQuestion build() {
    int cursor = questionBuffer.position();
    return new DnsQuestion(this.questionBuffer.duplicate().position(0).limit(cursor).slice(),
        this.qdCount);
  }

  private void setQuestion(ByteBuffer label) {
    this.questionBuffer.put(label);
  }
}
