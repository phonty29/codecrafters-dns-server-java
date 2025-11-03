package io;

import static utils.ByteUtils.getOffsetFromPointer;
import static utils.ByteUtils.isPointer;

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
        short qOffset = (short) (offset - DnsHeader.SIZE);
        int currentPosition = this.questionBuffer.position();

        // Cache referred label by its offset
        this.questionBuffer.position(qOffset);
        byte limit = this.questionBuffer.get();
        while (this.questionBuffer.hasRemaining() && limit != terminator) {
            limit = this.questionBuffer.get();
        }
        ByteBuffer duplicate = this.questionBuffer.duplicate().position(qOffset).limit(limit).slice();
        for (int j = 0; j < duplicate.limit(); j++) {
          byte b = duplicate.get(j);
          if ((b >= 'A' && b <= 'Z') || (b >= 'a' && b <= 'z')) {
            System.out.println("Letter: " + (char) b);
          } else {
            System.out.println("Length: " + b);
          }
        }
        labelsMap.put((int) offset, duplicate);

        this.questionBuffer.position(currentPosition);
      }
    }

    return labelsBuffer;
  }

  public Map<Integer, ByteBuffer> getLabelsMap() {
    return this.labelsMap;
  }
}
