package consts;

public enum QuestionType {
  A((short) 1);

  private final short type;

  QuestionType(short type) {
    this.type = type;
  }

  public short value() {
    return this.type;
  }
}
