package io;

import static consts.QuestionClass.IN;
import static consts.QuestionType.A;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

class DnsQuestionBuilder implements Builder<DnsQuestion> {
  private final ByteBuffer questionBuffer;

  DnsQuestionBuilder(int size) {
    this.questionBuffer = ByteBuffer.allocate(size);
  }

  DnsQuestionBuilder questions(String[] questions) {
    for (var question : questions) {
      this.setQuestion(question);
    }
    return this;
  }

  @Override
  public DnsQuestion build() {
    int cursor = questionBuffer.position();
    return new DnsQuestion(questionBuffer.position(0).limit(cursor).slice());
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
}
