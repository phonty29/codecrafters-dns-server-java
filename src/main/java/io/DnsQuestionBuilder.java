package io;

import static consts.QuestionClass.IN;
import static consts.QuestionType.A;
import static utils.ByteUtils.getOffsetFromPointer;
import static utils.ByteUtils.isPointer;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Map;

class DnsQuestionBuilder implements Builder<DnsQuestion> {
  private final ByteBuffer questionBuffer;
  private short qdCount = 0;
  private Map<Integer, ByteBuffer> labelsMap;

  DnsQuestionBuilder(int size) {
    this.questionBuffer = ByteBuffer.allocate(size);
  }

//  DnsQuestionBuilder questions(String[] names) {
//    this.qdCount = (short) names.length;
//    for (var name : names) {
//      this.setQuestion(name);
//    }
//    return this;
//  }

  DnsQuestionBuilder questions(ByteBuffer[] labels, Map<Integer, ByteBuffer> map) {
    this.qdCount = (short) labels.length;
    this.labelsMap = map;
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

//  private void setQuestion(String name) {
//    String[] domainParts = name.split("\\.");
//    String secondLevelDomainName = domainParts[0];
//    String topLevelDomainName = domainParts[1];
//    byte terminator = 0;
//
//    // Name
//    this.questionBuffer
//        .put((byte) secondLevelDomainName.length())
//        .put(secondLevelDomainName.getBytes(StandardCharsets.UTF_8))
//        .put((byte) topLevelDomainName.length())
//        .put(topLevelDomainName.getBytes())
//        .put(terminator);
//    // Type
//    this.questionBuffer.putShort(A.value());
//    // Class
//    this.questionBuffer.putShort(IN.value());
//  }

  private void setQuestion(ByteBuffer label) {
    // Question domain
    for (int i = 0; i < label.limit(); i++) {
      byte nextByte = label.get(i);
      if (isPointer(nextByte)) {
        short offset = getOffsetFromPointer(nextByte, label.get(i+1));
        ByteBuffer mappedBuffer = this.labelsMap.get((int) offset);
        if (mappedBuffer != null) {
          System.out.println(Arrays.toString(mappedBuffer.array()));
        }
      }
    }
    this.questionBuffer.put(label);
    // Type
    this.questionBuffer.putShort(A.value());
    // Class
    this.questionBuffer.putShort(IN.value());

  }
}
