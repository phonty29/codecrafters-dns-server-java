package io;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class DnsAnswer implements BufferWrapper {

  private final ByteBuffer answerBuffer;
  private final int answerCount;
  private ByteBuffer[] answers;

  DnsAnswer(ByteBuffer answerBuffer, int answerCount) {
    this.answerBuffer = answerBuffer.duplicate().position(0);
    this.answerCount = answerCount;
    initAnswers();
  }

  @Override
  public ByteBuffer getBuffer() {
    return this.answerBuffer;
  }

  @Override
  public byte[] getBytes() {
    return this.answerBuffer.array();
  }

  @Override
  public int length() {
    return this.answerBuffer.array().length;
  }

  public ByteBuffer[] getAnswers() {
    return Arrays.stream(this.answers)
        .map(buff -> buff.duplicate().position(0).limit(buff.limit()).slice())
        .toArray(ByteBuffer[]::new);
  }

  public void initAnswers() {
    this.answers = new ByteBuffer[this.answerCount];
    ByteBuffer copyBuffer = this.answerBuffer.duplicate();
    int qIndex = 0, sPos, ePos = 0;

    while (copyBuffer.hasRemaining() && qIndex < this.answerCount) {
      byte nextByte = copyBuffer.get();
      // If terminator
      if (nextByte == 0x00) {
        sPos = ePos;
        // Get Type
        copyBuffer.getShort();
        // Get Class
        copyBuffer.getShort();
        // Get TTL
        copyBuffer.getInt();
        // Get RDLength
        short rdlen = copyBuffer.getShort();
        // Get IP address by RDLength
        copyBuffer.get(new byte[rdlen]);
        ePos = copyBuffer.position();
        this.answers[qIndex++] = copyBuffer.duplicate().position(sPos).limit(ePos).slice();
      }
    }
  }
}
