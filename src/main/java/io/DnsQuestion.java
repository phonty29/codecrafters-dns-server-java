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
        int offset = getOffsetFromPointer(nextByte, this.questionBuffer.get());
        int qOffset = offset - DnsHeader.SIZE;
        int currentPosition = this.questionBuffer.position();
        // Cache referred label by its offset
        this.questionBuffer.position(qOffset);
        byte labelLength = this.questionBuffer.get();
        ByteBuffer duplicate = this.questionBuffer.duplicate().position(qOffset).limit(qOffset+labelLength+1).slice();
        labelsMap.put(qOffset, duplicate);

        this.questionBuffer.position(currentPosition);
      }
    }

    System.out.println(labelsMap.keySet());
    return labelsBuffer;
  }

  private int getOffsetFromPointer(byte nextByte, byte restByte) {
    short pointer = (short) (((nextByte & 0xFF) << 8) | (restByte & 0xFF));
    return pointer & ~(0b11 << 14);
  }

  private boolean isPointer(byte nextByte) {
    byte pointerMask = (byte) 0b11000000;
    return (nextByte & pointerMask) == pointerMask;
  }
}
