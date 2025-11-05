package io2;

import io.DnsAnswer;

public interface IDnsResponse extends IDnsMessage {
  DnsAnswer getAnswer();
}
