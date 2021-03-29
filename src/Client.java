import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.lang.reflect.Constructor;
import java.net.Socket;
import java.net.UnknownHostException;

/** Client class **/
public class Client implements Runnable {

    /** Attribute Client class**/
    public int error_code;
    public String data_recive;

    public int porta;
    public String ip;

    public DataInputStream in;
    public DataOutputStream out;

    /*** public Constructor ***/
    public Client(int porta, String ip) {
        this.porta = porta;
        this.ip = ip;
        error_code = connect();
    }

    /*** method Send data ***/
    public void send_data(String testo){
        try {
            System.out.print("Messaggio inviato: " + testo + "\n") ;
            out.writeBytes(testo + "\n");

            if (testo.equals("quit"))
                java.lang.System.exit(0);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*** method Connect to server ***/
    public int connect(){
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

    /*** method Receive data ***/
    @Override
    public void run() {

        System.out.println("Sto ascoltando...");
        boolean flag = true;

        while(flag){
            try{
                data_recive = in.readLine();

                if(data_recive.equals("quit"))
                    flag = false;
                else {
                    System.out.println("Data recive str: " + data_recive);
                }

            }catch (Exception e){
                System.out.println("Error recive");
                e.printStackTrace();
            }
        }
    }
}