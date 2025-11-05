package io2;

import io.DnsHeader;
import io.DnsQuestion;

public interface IDnsMessage {
  DnsHeader getHeader();

  DnsQuestion getQuestion();
}
