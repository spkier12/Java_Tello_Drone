package ReciveData;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Received implements Runnable{
    private final DatagramSocket var;

    public Received(DatagramSocket var) {
        this.var = var;
    }

    @Override
    public void run() {
        while (true) try {
            byte[] buf = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            var.receive(packet);

            String data = new String(
                    packet.getData(), 0, packet.getLength()
            );

            if (!data.isEmpty()) {
                System.out.println(data);
            }
        } catch (Exception e) {
            System.out.println("\nError in getting data " + e.getCause());
        }
    }
}
