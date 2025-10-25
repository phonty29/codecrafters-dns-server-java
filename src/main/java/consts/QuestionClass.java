package consts;

public enum QuestionClass {
  IN((short) 1);

  private final short qclass;

  QuestionClass(short qclass) {
    this.qclass = qclass;
  }

  public short value() {
    return this.qclass;
  }
}
