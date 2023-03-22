import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.*;
import java.io.*;


public class Client extends JFrame{
    Socket socket;

    BufferedReader br;
    PrintWriter out;

    //Delcare components
    private JLabel heading = new JLabel("Client Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto",Font.PLAIN,20);
    //Constructor
    public Client(){
        try {
            System.out.println("sending to server..");
            socket = new Socket("127.0.0.1",8888);
            System.out.println("connection done");


            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

              createGUI();
              handleEvents();
              startReading();
//            startWriting();

        } catch (Exception e) {
//            e.printStackTrace();
        }
    }
    private void handleEvents(){
        messageInput.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
//                System.out.println("key released" + e.getKeyCode());
                if (e.getKeyCode() == 10){
                    System.out.println("you have pressed enter button");
                    String contentToSend = messageInput.getText();

                    messageArea.append("Me : "+ contentToSend + "\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }
            }
        });
    }

    private void createGUI(){
        this.setTitle("Client Message[END]");
        this.setSize(600,700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //coding for component
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);

        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messageArea.setEditable(false);

        messageInput.setHorizontalAlignment(SwingConstants.CENTER);

        this.setLayout(new BorderLayout());
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messageArea);
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);


        this.setVisible(true);

    }
    //Start reading
    public void startReading(){
        Runnable r1 =()->{
            
            System.out.println("reader started..");

            try{
            while (true){
                
                String msg = br.readLine();
                if(msg.equals("Exit")){
                    System.out.println("Server termination the chat");
                    JOptionPane.showMessageDialog(this,"Server Terminated " +
                            "the chat");
                    messageInput.setEnabled(false);
                    socket.close();
                    break;
                }
//                System.out.println("Server : " + msg);
                messageArea.append("Server : " + msg+ "\n");
            
        }
    }catch(Exception e){
        // e.printStackTrace();
        System.out.println("connection close");
    }
    };
    new Thread(r1).start();

    }
    //start writing
    public void startWriting(){
        Runnable r2 = () -> {
            System.out.println("writer started..");
            try{
            while(true){
                
                
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();
                    if(content.equals("Exit")){
                        socket.close();
                        break;
                    }
                
                
            }


        }catch(Exception e){
//            e.printStackTrace();
                System.out.println("connection is close");
        }
        };
        new Thread(r2).start();

    }
    public static void main(String[] args) {
        System.out.println("this is Client..");
        new Client();
    }    
}
