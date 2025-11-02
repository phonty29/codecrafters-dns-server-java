package io;

import java.nio.ByteBuffer;

class DnsQuestion implements BufferWrapper {
  private final ByteBuffer questionBuffer;
  private final short qdCount;

  private final static byte terminator = 0;
  private final static byte pointer = (byte) 0xC0;

  DnsQuestion(ByteBuffer questionBuffer, short questionNumber) {
    this.questionBuffer = questionBuffer.duplicate();
    this.qdCount = questionNumber;
  }

  public ByteBuffer getBuffer() {
    return this.questionBuffer;
  }

  public byte[] getBytes() {
    return this.questionBuffer.array();
  }

  public ByteBuffer[] getLabels() {
    this.questionBuffer.clear();
    ByteBuffer[] labelsBuffer = new ByteBuffer[this.qdCount];
    short qIndex = 0, sPos, ePos = 0;
    while (this.questionBuffer.hasRemaining() && qIndex < this.qdCount) {
      byte nextByte = this.questionBuffer.get();
      System.out.println("Label byte");
      if (nextByte == pointer) {
        byte offset = this.questionBuffer.get();
        this.questionBuffer.position(offset - DnsHeader.SIZE);
        continue;
      }
      if (nextByte == terminator) {
        System.out.println("Terminator");
        sPos = (short) (ePos + (qIndex > 0 ? 4 : 0));
        ePos = (short) this.questionBuffer.position();
        labelsBuffer[qIndex++] = this.questionBuffer.duplicate().position(sPos).limit(ePos).slice();
        this.questionBuffer.position(ePos + 4);
      }
    }
    return labelsBuffer;
  }
}
