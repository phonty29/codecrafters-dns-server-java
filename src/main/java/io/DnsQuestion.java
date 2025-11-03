package io;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class DnsQuestion implements BufferWrapper {
  private final ByteBuffer questionBuffer;
  private final short qdCount;
  private final Map<Integer, ByteBuffer> labelsMap = new HashMap<>();

  private final static byte terminator = 0;

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
      if (nextByte == terminator) {
        sPos = ePos;
        ePos = (short) this.questionBuffer.position();
        labelsBuffer[qIndex++] = this.questionBuffer.duplicate().position(sPos).limit(ePos).slice();
        ePos += 4;
        if (ePos <= this.questionBuffer.limit()) {
          this.questionBuffer.position(ePos);
        }
      } else if (isPointer(nextByte)) {
        short offset = getOffsetFromPointer(nextByte, this.questionBuffer.get());

        int currentPosition = this.questionBuffer.position();

        this.questionBuffer.position(offset - DnsHeader.SIZE);
        byte labelLength = this.questionBuffer.get();
        var duplicate = this.questionBuffer.duplicate().position(offset-DnsHeader.SIZE).limit(offset-DnsHeader.SIZE+labelLength+1).slice();
        for (int i = 0; i < duplicate.limit(); i++) {
          var b = duplicate.get(i);
          if ((b >= 'A' && b <= 'Z') || (b >= 'a' && b <= 'z')) {
            System.out.println("Letter: " + (char) b);
          } else {
            System.out.println("Length: " + b);
          }
        }

        this.questionBuffer.position(currentPosition);
      }
    }
    return labelsBuffer;
  }

  private short getOffsetFromPointer(byte nextByte, byte restByte) {
    short pointer = (short) (((nextByte & 0xFF) << 8) | (restByte & 0xFF));
    return (short) (pointer & ~(0b11 << 14));
  }

  private boolean isPointer(byte nextByte) {
    byte pointerMask = (byte) 0b11000000;
    return (nextByte & pointerMask) == pointerMask;
  }
}
