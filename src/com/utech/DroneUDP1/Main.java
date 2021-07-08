package com.utech.DroneUDP1;

import Automation.AutoFlight;
import ReciveData.Received;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;


public class Main {

    private DatagramSocket Socket;
    private InetAddress Address;
    private int Port;
    private int ConnectionAttempts;

    public static void main(String[] args) {
        Main a = new Main();
        a.Connect();
    }

    // Setup a Configuration to the UDP Server
    public void Connect() {
        ConnectionAttempts++;
        try {
            Address = InetAddress.getByName("192.168.10.1");
            Port = 8889;
            Socket = new DatagramSocket();
            Received b = new Received(Socket);
            Thread b0 = new Thread(b);
            b0.start();
            GetMSG();
        } catch (SocketException | UnknownHostException e) {
            System.out.println("\nException in Connecting: {} " + ConnectionAttempts);
            Connect();
        }
    }

    // Input commands if you write a command it will trigger a packet send
    private void GetMSG() {
        System.out.println("Emergency shutdown Key: 0 \nCommand Takeover terminal Key: 1");
        System.out.println("Takeoff Key: 2 \nLand Key: 3 \nProgram Flight Key: 4 -> Commands here");
        System.out.println("+Enter Key");

        Scanner Msg = new Scanner(System.in);
        System.out.println("Msg: ");
        String Msg1 = Msg.nextLine();
        String[] Msg2Split = Msg1.split(" ");
        switch (Msg2Split[0]) {
            case ("0"):
                Send("emergency");
            case("1"):
                Send("command");
            case("2"):
                Send("takeoff");
            case("3"):
                Send("land");
            case("4"):
                AutoFlight a = new AutoFlight(Msg2Split, Socket);
                Thread b = new Thread(a);
                b.start();
                System.out.println("Started AutoFlight");
            default:
                Send(Msg1);
        }
    }

    // Send a Message packet to the specified UDP Server
    private void Send(String msg) {
        byte[] buf;
        try {
            buf = msg.getBytes(StandardCharsets.UTF_8);
            DatagramPacket packet = new DatagramPacket(buf, buf.length, Address, Port);
            Socket.send(packet);
            GetMSG();
        } catch (Exception e) {
            System.out.println("\n\n\nCould not send message: " + e);
            GetMSG();
        }
    }
}
