package io;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

class DnsQuestionBuilder {
  private final ByteBuffer questionBuffer = ByteBuffer.allocate(500);

  public DnsQuestionBuilder questions(String[] questions) {
    for (var question : questions) {
      this.setQuestion(question);
    }
    return this;
  }

  public ByteBuffer build() {
    questionBuffer.flip();
    int filledBytes = questionBuffer.remaining();
    return questionBuffer.get(new byte[filledBytes]);
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
    this.questionBuffer.putShort((short) 1);
    // Class
    this.questionBuffer.putShort((short) 1);
  }
}
