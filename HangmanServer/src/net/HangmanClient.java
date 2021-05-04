package net;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class HangmanClient {

    int porta = 6789;
    DataInputStream in;
    DataOutputStream out = null;
    BufferedReader tastiera = null;
    String riga;

    public void connetti(){

        try {
            System.out.println("1. provo a connettermi al server");
            Socket mySocket = new Socket(InetAddress.getLocalHost().getLocalHost(), porta);
            System.out.println("2. mi sono connesso");

            in = new DataInputStream(mySocket.getInputStream());
            out = new DataOutputStream(mySocket.getOutputStream());


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void invia(){

        try {
            System.out.println("3. messaggio da inviare al sever");

            tastiera = new BufferedReader(new InputStreamReader(System.in));
            riga = tastiera.readLine();

            System.out.println("4. invio il messaggio " + riga);

            out.writeBytes(riga + "\n");


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        HangmanClient c = new HangmanClient();
        c.connetti();
        while (true){
            c.invia();
        }

    }

}
