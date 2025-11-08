import inet.DnsForwarder;
import io.DnsHeaderBuilder;
import io.DnsQuery;
import io.DnsResponse;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Main {

  public static void main(String[] args) {
    System.out.println("Logs from your program will appear here!");
    int port = 2053;
    int bufSize = 512;

    if (args.length < 2) {
      System.err.println("Usage: java Main [--resolver] [address:port]");
      System.exit(1);
    }
    String forwardServer = args[1];

    try (DatagramSocket serverSocket = new DatagramSocket(port)) {
      while (true) {
        final byte[] buf = new byte[bufSize];
        final DatagramPacket packet = new DatagramPacket(buf, buf.length);
        serverSocket.receive(packet);

        DnsQuery query = DnsQuery.builder(packet.getData()).build();
        String forwardHost = forwardServer.split(":")[0], forwardPort = forwardServer.split(":")[1];
        DnsForwarder forwarder = new DnsForwarder(forwardHost, forwardPort);
        DnsResponse forwardResponse = forwarder.forward(query);

        byte[] response = DnsResponse
            .builder()
            .header(new DnsHeaderBuilder()
                .transactionId(query.getHeader().getPacketID())
                .flags(query.getHeader().getFlags(), true)
                .qdCount(query.getHeader().getQDCount())
                .anCount(query.getHeader().getQDCount())
                .nsCount((short) 0)
                .arCount((short) 0)
                .build())
            .question(query.getQuestion().getDecompressedQuestions())
            .answer(forwardResponse.getAnswer().getAnswers())
            .build()
            .getBytes();

        final DatagramPacket packetResponse = new DatagramPacket(response, response.length,
            packet.getSocketAddress());
        serverSocket.send(packetResponse);
      }
    } catch (IOException e) {
      System.out.println("IOException: " + e.getMessage());
    }
  }
}
