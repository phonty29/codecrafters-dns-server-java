package io;

import java.nio.ByteBuffer;
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
      if (nextByte == (byte) 0) {
        sPos = ePos;
        ePos = (short) this.questionBuffer.position();
        labelsBuffer[qIndex++] = this.questionBuffer.position(sPos).limit(ePos).slice();
        ePos += 4;
        System.out.println("Limit: " + this.questionBuffer.limit());
        System.out.println("Capacity: " + this.questionBuffer.capacity());
        if (ePos <= this.questionBuffer.limit()) {
          this.questionBuffer.position(ePos);
        }
      }
    }
    return labelsBuffer;
  }

//  public ByteBuffer[] getLabels() {
//    this.questionBuffer.clear();
//    ByteBuffer[] labelsBuffer = new ByteBuffer[this.qdCount];
//    short qIndex = 0, sPos, ePos = 0;
//    int offset = DnsHeader.SIZE;
//
//    while (this.questionBuffer.hasRemaining() && qIndex < this.qdCount) {
//      int currentPosition = this.questionBuffer.position();
//      byte nextByte = this.questionBuffer.get();
//      if (nextByte == terminator) {
//        sPos = ePos;
//        ePos = (short) this.questionBuffer.position(); // current position
//        labelsBuffer[qIndex++] = this.questionBuffer.position(sPos).limit(ePos).slice();
//        ePos += 4;  // skip 4 bytes to the next question
//        this.questionBuffer.position(ePos);
//      }
//      else if (isPointer(nextByte)) {
//        byte restByte = this.questionBuffer.get();
//        int pointerOffset = getOffsetFromPointer(nextByte, restByte);
//      } else {
//        // if nextByte = label's length
//        // map each label with its offset
//        labelsMap.put(offset, this.questionBuffer.position(currentPosition).limit(nextByte).slice());
//        this.questionBuffer.position(currentPosition + 1 + nextByte);
//        offset += nextByte + 1;
//      }
//    }
//    return labelsBuffer;
//  }

  private int getOffsetFromPointer(byte nextByte, byte restByte) {
    short pointer = (short) (((nextByte & 0xFF) << 8) | (restByte & 0xFF));
    return 0;
  }

  private boolean isPointer(byte nextByte) {
    byte pointerMask = (byte) 0b11000000;
    return (nextByte & pointerMask) == pointerMask;
  }
}
