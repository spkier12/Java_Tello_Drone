package Automation;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class AutoFlight implements Runnable{
    private final String[] Directions;
    private final DatagramSocket Socket;
    int Tries = 0;

    public AutoFlight(String[] Directions, DatagramSocket socket) {
        this.Directions = Directions;
        this.Socket = socket;
    }

    public void run() {
        for (String direction : Directions) {
            try {
                byte[] buffer;
                if (direction.contains("wait")) {
                    Thread.sleep(2000);
                } else if(direction.contains("repeat")) {
                    Tries++;
                    if (Tries < 10) { run(); }
                } else {
                    buffer = direction.getBytes(StandardCharsets.UTF_8);
                    DatagramPacket Packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("192.168.10.1"), 8889);
                    Socket.send(Packet);
                }
            } catch (IOException | InterruptedException e) {
                System.out.println(e.getCause());
            }
        }
    }
}
