import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.lang.reflect.Constructor;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client implements Runnable {

    Socket mioSocket = null;
    public int codiceErrore;
    String ricevuta;

    int porta;
    String ip;

    DataInputStream in;
    DataOutputStream out;

    public Client(int porta, String ip) {
        this.porta = porta;
        this.ip = ip;
        codiceErrore = connetti();
    }

    public void Comunica(String testo){
        try {
            System.out.print("Messaggio inviato: " + testo + "\n") ;
            out.writeBytes(testo + "\n");

            if (testo.equals("quit"))
                java.lang.System.exit(0);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public int connetti(){
        try {
            Socket mioSocket = new Socket(ip, porta);
            in = new DataInputStream(mioSocket.getInputStream());
            out = new DataOutputStream(mioSocket.getOutputStream());
            return 1;
        }catch (UnknownHostException e){
            return 2;
        }
        catch (IOException e) {
            return 3;
        }
    }

    @Override
    public void run() {
        System.out.println("Sto ascoltando...");
        boolean flag = true;
        while(flag){
        try{
            ricevuta = in.readLine();
            new GUI(ricevuta);

            if(ricevuta.equals("quit"))
                flag = false;
            else {
                System.out.println("Risposta: " + ricevuta);
            }
        }catch (Exception e){ }
        }
    }
}