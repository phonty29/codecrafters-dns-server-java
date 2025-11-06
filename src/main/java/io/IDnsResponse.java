package io;

public interface IDnsResponse extends IDnsMessage {
  DnsAnswer getAnswer();
}
