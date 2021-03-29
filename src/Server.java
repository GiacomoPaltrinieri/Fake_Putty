import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
    ServerSocket server = null;
    Socket client = null;

    int porta = 5000;

    DataInputStream in;
    DataOutputStream out;

    public void Comunica(String testo){

        try {

            System.out.print("Messaggio inviato: " + testo + "\n") ;
            out.writeBytes(testo + "\n");

            if (testo.equals("quit")){
                client.close();
                java.lang.System.exit(0);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void ascolta(){
        Test_Frame frame = new Test_Frame();
        JButton button = new JButton("clicca");
        JTextField textField = new JTextField();

        button.addActionListener(e -> {
            Comunica(textField.getText());
            textField.setText("");
        });

        frame.add(button);
        frame.add(textField);
        frame.setVisible(true);
        System.out.println("Sto ascoltando...");
        boolean flag = true;
        while(flag){
            try{
                String letto = in.readLine();
                System.out.println("Messaggio ricevuto: " + letto);
                if(letto.equals("quit")){
                    flag = false;
                    client.close();
                }
            }catch (Exception e){ }
        }

    }


    public void attendi(){

        try {
            server = new ServerSocket(porta); //inizializziamo il servizio
            System.out.println("in attesa....");
            client = server.accept();
            server.close(); // impediamo ulteriori connessioni
            System.out.println("client connesso");
            in = new DataInputStream(client.getInputStream());
            out = new DataOutputStream(client.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        Server s = new Server();
        s.attendi();
        s.ascolta();


    }
}

//git config --global user.email "giaco.paltri@gmail.com"