/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net;

import hangman.Game;
import hangman.Hangman;
import hangman.Player;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Claudio Cusano <claudio.cusano@unipv.it>
 */
public class HangmanServer extends Player{

    String riga;
    BufferedReader console;
    BufferedReader console1;
    ServerSocket serverSocket;
    Socket clientsocket;
    int porta = 6789;



    DataInputStream in;
    DataOutputStream out = null;
    BufferedReader tastiera = null;


    /**
     * Constructor.
     */
    public HangmanServer() throws IOException {

        console = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("1.server inizializzato");
        serverSocket = new ServerSocket(porta);
        System.out.println("2. server in ascolto sulla porta "+ porta);
        clientsocket = serverSocket.accept();
        System.out.println("3.Connessione stabilita");

        in = new DataInputStream(clientsocket.getInputStream());
        out = new DataOutputStream(clientsocket.getOutputStream());



    }

    public String invia() throws  IOException {
        System.out.println("4. aspetto un messaggio");
        Character  console = in.readChar();//////////////////////////////////////
        String temp= console.toString();

        System.out.println("5. messsaggio ricevuto " + console);
        String risposta = temp.toUpperCase();

        return risposta;

    }

    @Override
    public void update(Game game) {
        switch(game.getResult()) {
            case FAILED:
                printBanner("Hai perso!  La parola da indovinare era '" +
                        game.getSecretWord() + "'");
                break;
            case SOLVED:
                printBanner("Hai indovinato!   (" + game.getSecretWord() + ")");
                break;
            case OPEN:
                int rem = Game.MAX_FAILED_ATTEMPTS - game.countFailedAttempts();
                System.out.print("\n" + rem + " tentativi rimasti\n");
                System.out.println(this.gameRepresentation(game));
                System.out.println(game.getKnownLetters());
                break;
        }
    }

    private String gameRepresentation(Game game) {
        int a = game.countFailedAttempts();

        String s = "   ___________\n  /       |   \n  |       ";
        s += (a == 0 ? "\n" : "O\n");
        s += "  |     " + (a == 0 ? "\n" : (a < 5
                ? "  +\n"
                : (a == 5 ? "--+\n" : "--+--\n")));
        s += "  |       " + (a < 2 ? "\n" : "|\n");
        s += "  |      " + (a < 3 ? "\n" : (a == 3 ? "/\n" : "/ \\\n"));
        s += "  |\n================\n";
        return s;
    }

    private void printBanner(String message) {
        System.out.println("");
        for (int i = 0; i < 80; i++)
            System.out.print("*");
        System.out.println("\n***  " + message);
        for (int i = 0; i < 80; i++)
            System.out.print("*");
        System.out.println("\n");
    }

    /**
     * Ask the user to guess a letter.
     *
     * @param game
     * @return
     */
    @Override
    public char chooseLetter(Game game) throws IOException {
        for (;;) {


            String line = null;
            try {
                line = invia();
            } catch (IOException e) {
                line = "";
            }
            if (line.length() == 1 && Character.isLetter(line.charAt(0))) {
                return line.charAt(0);
            } else {
                System.out.println("Lettera non valida.");
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Hangman game = new Hangman();
        Player player = new HangmanServer();
        // Player player = new ArtificialPlayer();
        game.playGame(player);
    }
}
