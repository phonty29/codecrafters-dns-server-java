package io;

import static consts.QuestionClass.IN;
import static consts.QuestionType.A;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

class DnsQuestionBuilder implements Builder<DnsQuestion> {
  private final ByteBuffer questionBuffer;
  private short qdCount = 0;

  DnsQuestionBuilder(int size) {
    this.questionBuffer = ByteBuffer.allocate(size);
  }

  DnsQuestionBuilder questions(String[] names) {
    this.qdCount = (short) names.length;
    for (var name : names) {
      this.setQuestion(name);
    }
    return this;
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

  private void setQuestion(String name) {
    String[] domainParts = name.split("\\.");
    String secondLevelDomainName = domainParts[0];
    String topLevelDomainName = domainParts[1];
    byte terminator = 0;

    // Name
    this.questionBuffer
        .put((byte) secondLevelDomainName.length())
        .put(secondLevelDomainName.getBytes(StandardCharsets.UTF_8))
        .put((byte) topLevelDomainName.length())
        .put(topLevelDomainName.getBytes())
        .put(terminator);
    // Type
    this.questionBuffer.putShort(A.value());
    // Class
    this.questionBuffer.putShort(IN.value());
  }

  private void setQuestion(ByteBuffer label) {
    // Question domain
    this.questionBuffer.put(label);
    // Type
    this.questionBuffer.putShort(A.value());
    // Class
    this.questionBuffer.putShort(IN.value());

  }
}
