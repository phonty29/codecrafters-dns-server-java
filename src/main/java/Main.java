import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import io.DnsResponse;

public class Main {
  public static void main(String[] args){
    System.out.println("Logs from your program will appear here!");
    int port = 2053;
    int bufSize = 512;

     try(DatagramSocket serverSocket = new DatagramSocket(port)) {
       while(true) {
         // Allocate buffer
         final byte[] buf = new byte[bufSize];
         final DatagramPacket packet = new DatagramPacket(buf, buf.length);
         // Receive datagram packet
         serverSocket.receive(packet);
         System.out.println("Received data");

         final byte[] response = DnsResponse
             .builder()
             .query(packet.getData())
             .build()
             .array();
         final DatagramPacket packetResponse = new DatagramPacket(response, response.length, packet.getSocketAddress());
         // Send datagram packet
         serverSocket.send(packetResponse);
       }
     } catch (IOException e) {
         System.out.println("IOException: " + e.getMessage());
     }
  }
}
