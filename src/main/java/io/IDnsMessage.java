package io;

public interface IDnsMessage {

  int PACKET_SIZE = 512;

  DnsHeader getHeader();

  DnsQuestion getQuestion();
}
