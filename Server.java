import java.net.*;
import java.io.*;

class Server{

    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    //Constructor
    public Server(){
        try {
            server = new ServerSocket(8888);
            System.out.println("server is ready to accept connection");
            System.out.println("waiting..");
            socket = server.accept();

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
            startWriting();
            startReading();
            

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startReading(){
        Runnable r1 =()->{
            
            System.out.println("reader started..");
            try{
            while (true){
               
                String msg = br.readLine();
                if(msg.equals("Exit")){
                    System.out.println("Client termination the chat");
                    
                   socket.close();

                    break;
                }
                System.out.println("Client : " + msg);
            
            }
            }catch(Exception e){
                    //e.printStackTrace();
                    System.out.println("connection close");
                }
    };
    new Thread(r1).start();

    }
    public void startWriting(){
        Runnable r2 =()->{
            System.out.println("writer started..");
            try{
            while(!socket.isClosed()){
                
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
                System.out.println("connection close");
        }
        };
        new Thread(r2).start();

    }
    public static void main(String[] args) {
        System.out.println("this is Server..");
        new Server();
    }
}