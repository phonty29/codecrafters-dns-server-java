package inet;

import io.DnsQuery;
import io.DnsResponse;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

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
      ByteBuffer[] splitQueries = query.split();
      ByteBuffer[] answers = new ByteBuffer[splitQueries.length];
      int i = 0;
      for (ByteBuffer splitQuery : splitQueries) {
        final DatagramPacket forwardPacket = new DatagramPacket(
            splitQuery.array(),
            splitQuery.array().length,
            this.forwardHost,
            this.forwardPort
        );
        forwardSock.send(forwardPacket);

        byte[] forwardResponse = new byte[512];
        DatagramPacket replyPacket = new DatagramPacket(forwardResponse, forwardResponse.length);
        forwardSock.receive(replyPacket);
        DnsResponse response = DnsResponse.builder(replyPacket.getData()).build();
        System.out.println("Response answers: " + response.getAnswer().getAnswers().length);
        response.getAnswer().getAnswers();
        for (var answer : response.getAnswer().getAnswers()) {
          answers[i++] = answer;
        }

      }
      return DnsResponse
          .builder()
          .header(query.getHeader())
          .question(query.getQuestion().getDecompressedQuestions())
          .answer(answers)
          .build();
    } catch (IOException e) {
      System.err.println(e.getMessage());
      throw new RuntimeException(e);
    }
  }
}
