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

public class GUI extends JFrame {
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
    private Client cliente;
    private int codiceErrore;

    public GUI(String testo){
        areaMessaggi.setText(areaMessaggi.getText() + testo + "\n");
    }

    public GUI() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setContentPane(pannello_main);
        this.pack();
        this.setVisible(true);
        this.inviaButton.setEnabled(false);

        this.areaMessaggi.setFont(new Font("helevtica",Font.PLAIN, 17));
        this.areaMessaggi.setEditable(false);

        procediButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Integer.parseInt(porta_client.getValue().toString()) > 0 && !ip_clientTextField.getText().isEmpty() && porta_server.getValue().equals(0)) {
                    // il pc diventa client

                    if(nickname.getText().isEmpty()){
                        errori.setForeground(Color.red);
                        errori.setText("Errore, inserisci il nickname!");
                    }
                    else{
                        errori.setForeground(Color.black);
                        errori.setText("Provo a connettermi...\n");
                        cliente = new Client((Integer) porta_client.getValue(), ip_clientTextField.getText());
                        if (cliente.codiceErrore == 1){
                            errori.setForeground(Color.green);
                            errori.append("---CONNESSIONE STABILITA---\n");

                            Thread t1 = new Thread(cliente);
                            t1.start();


                            System.out.println("GUI: " + cliente.ricevuta);

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
                                cliente.Comunica(contenutoMessaggio.getText());
                                StyleConstants.setAlignment(obj, StyleConstants.ALIGN_LEFT);
                                doc.setParagraphAttributes(0, doc.getLength(), obj, false);
                                areaMessaggi.setText(areaMessaggi.getText() + "\n" + nickname.getText() + ": " + contenutoMessaggio.getText());
                                contenutoMessaggio.setText("");
                            });


                        }else if (cliente.codiceErrore == 2){
                            errori.setForeground(Color.red);
                            errori.append("Errore, host sconosciuto!\n");
                        }else if (cliente.codiceErrore == 3){
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
        });

        try {
            ip_serverTextField.setText(Inet4Address.getLocalHost().getHostAddress());       // setto ip locale in versione host non modificabile
            ip_serverTextField.setEditable(false);
            errori.setEditable(false);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        contenutoMessaggio.addKeyListener(new KeyAdapter() {
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
                        cliente.Comunica(nickname.getText() + ": " +contenutoMessaggio.getText());
                        contenutoMessaggio.setText("");
                    }
                }
            }
        });


    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    public static void main(String[] args) {

        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }

            JFrame frame = new GUI();
            frame.setSize(600,450);
            ImageIcon foto = new ImageIcon("fake_putty.png");
            frame.setIconImage(foto.getImage());
            frame.setTitle("Fake_Putty");
            frame.setAlwaysOnTop(true);

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}