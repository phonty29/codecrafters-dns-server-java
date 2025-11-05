package inet;

import io2.DnsQuery;
import io.DnsResponse;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class DnsForwarder {
  private final InetAddress forwardHost;
  private final int forwardPort;

  public DnsForwarder(InetAddress forwardHost, int forwardPort) {
    this.forwardHost = forwardHost;
    this.forwardPort = forwardPort;
  }

  public DnsForwarder(String forwardHost, String forwardPort) throws UnknownHostException {
    try {
      this.forwardHost = InetAddress.getByName(forwardHost);
      this.forwardPort = Integer.parseInt(forwardPort);
    } catch (UnknownHostException ex) {
      System.err.println("Unknown host: " + forwardHost);
      throw ex;
    }
  }

  public DnsResponse forward(DnsQuery query) {
    try (DatagramSocket forwardSock = new DatagramSocket()) {
      final DatagramPacket forwardPacket = new DatagramPacket(query.getBytes(), query.length(), this.forwardHost, this.forwardPort);
      forwardSock.send(forwardPacket);

      byte[] response = new byte[512];
      DatagramPacket replyPacket = new DatagramPacket(response, response.length);
      forwardSock.receive(replyPacket);
//      return DnsResponse.builder().query(replyPacket.getData()).build();
      return null;

    } catch (IOException e) {
      System.err.println(e.getMessage());
      throw new RuntimeException(e);
    }
  }
}
