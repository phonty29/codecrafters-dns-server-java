package io;

public interface IDnsMessage {
  DnsHeader getHeader();

  DnsQuestion getQuestion();
}
