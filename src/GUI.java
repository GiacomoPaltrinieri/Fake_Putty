import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.Inet4Address;
import java.net.UnknownHostException;

public class GUI {

    /** private Attribute **/
    private JPanel pannello_main;
    private JTabbedPane selezione;
    private JButton inviaButton;
    private JButton procediButton;
    private JTextField nickname;
    private JTextField ip_serverTextField;
    private JSpinner porta_server;
    private JSpinner porta_client;

    private JTextField ip_clientTextField;
    private JTextArea errori;
    private JTextField contenutoMessaggio;
    private JTextPane areaMessaggi;
    private Client client;
    private int codiceErrore;

    /** method to set gui style **/
    public void set_Gui_style(){

        this.inviaButton.setEnabled(false);
        this.areaMessaggi.setFont(new Font("helevtica",Font.PLAIN, 17));
        this.areaMessaggi.setEditable(false);
        this.errori.setEditable(false);
        this.ip_serverTextField.setEditable(false);
        this.ip_serverTextField.setText(get_ip_host());

    }

    /** method to set listener **/
    public void set_Listener(){

        /** Set start message button **/
        this.procediButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                start_message(); //Start message method
            }
        });

        /** Set send message with Enter key**/
        this.contenutoMessaggio.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int evento = e.getKeyCode();
                if (evento == KeyEvent.VK_ENTER) {
                    System.out.println("enter: " + evento);
                    if (!contenutoMessaggio.getText().isEmpty()) {
                        StyledDocument doc = areaMessaggi.getStyledDocument();
                        SimpleAttributeSet obj = new SimpleAttributeSet();
                        StyleConstants.setAlignment(obj, StyleConstants.ALIGN_LEFT);
                        doc.setParagraphAttributes(0, doc.getLength(), obj, false);
                        areaMessaggi.setText(areaMessaggi.getText() + "\n" + nickname.getText() + ": " + contenutoMessaggio.getText());
                        client.send_data(nickname.getText() + ": " +contenutoMessaggio.getText());
                        contenutoMessaggio.setText("");
                    }
                }
            }
        });

    }

    /** get ip host method**/
    public String get_ip_host(){
        //get host ip
        try {
            return Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
    }

    /** set error method **/
    public void set_error(String error){
        this.errori.setForeground(Color.RED);
        this.errori.setText(error);
    }
    public void set_information(String information){
        this.errori.setForeground(Color.GREEN);
        this.errori.setText(information);
    }
    public void add_message(String message){

    }

    /** onClick procediButton method
     *  Start_message
     * **/
    public void start_message(){

        if (Integer.parseInt(porta_client.getValue().toString()) > 0 && !ip_clientTextField.getText().isEmpty() && porta_server.getValue().equals(0)) {

            if(nickname.getText().isEmpty()){
                set_error("Errore, inserisci il nickname!");
            }
            else{
                set_information("Provo a connettermi...\n");

                client = new Client((Integer) porta_client.getValue(), ip_clientTextField.getText());

                if (client.error_code == 1){

                    set_information("---CONNESSIONE STABILITA---\n");

                    Thread t1 = new Thread(client);
                    t1.start();

                    System.out.println("GUI: " + client.data_recive);

                    areaMessaggi.setForeground(Color.BLACK);
                    StyledDocument doc = areaMessaggi.getStyledDocument();
                    SimpleAttributeSet obj = new SimpleAttributeSet();
                    StyleConstants.setAlignment(obj, StyleConstants.ALIGN_LEFT);
                    doc.setParagraphAttributes(0, doc.getLength(), obj, false);
                    areaMessaggi.setText( areaMessaggi.getText() + "\n" + nickname.getText() + " partecipa\n");


                    contenutoMessaggio.getDocument().addDocumentListener(new DocumentListener() {
                        public void changedUpdate(DocumentEvent e) {
                            changed();
                        }
                        public void removeUpdate(DocumentEvent e) {
                            changed();
                        }
                        public void insertUpdate(DocumentEvent e) {
                            changed();
                        }

                        public void changed() {
                            if (contenutoMessaggio.getText().equals("")){
                                inviaButton.setEnabled(false);
                            }
                            else {
                                inviaButton.setEnabled(true);
                            }
                        }
                    });

                    inviaButton.addActionListener(e1 -> {
                        client.send_data(contenutoMessaggio.getText());
                        StyleConstants.setAlignment(obj, StyleConstants.ALIGN_LEFT);
                        doc.setParagraphAttributes(0, doc.getLength(), obj, false);
                        areaMessaggi.setText(areaMessaggi.getText() + "\n" + nickname.getText() + ": " + contenutoMessaggio.getText());
                        contenutoMessaggio.setText("");
                    });


                }else if (client.error_code == 2){
                    errori.setForeground(Color.red);
                    errori.append("Errore, host sconosciuto!\n");
                }else if (client.error_code == 3){
                    errori.setForeground(Color.red);
                    errori.append("Errore, impossibile stabilire la connessione!\n");
                }

                System.out.println("client");
            }

        } else if (Integer.parseInt(porta_server.getValue().toString()) > 0 && !ip_serverTextField.getText().isEmpty() && porta_client.getValue().equals(0) && ip_clientTextField.getText().isEmpty()) {
            //il pc diventa server
            System.out.println("server");
            errori.setText("");
        } else {
            errori.setForeground(Color.red);
            errori.setFont(new Font("Helvetica", Font.BOLD, 13));
            errori.setText("Errore, inserisci i parametri correttemente!");
        }

    }

    /** public Constructor number 2 **/
    public GUI(String testo){
        areaMessaggi.setText(areaMessaggi.getText() + testo + "\n");
    }

    /** public Constructor **/
    public GUI() {

        set_Gui_style();

        set_Listener();

    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    /** getter methods **/
    public JPanel getPannello_main() {
        return pannello_main;
    }

}