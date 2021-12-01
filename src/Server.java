import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(8083);
        ArrayList<ServerThread> clients = new ArrayList<>() ;
        while(true){
            Socket s = server.accept();
            clients.add(new ServerThread(s));
            clients.get(clients.size()-1).start();

        }
    }
}

class ServerThread extends Thread
{

    Socket s;

    public ServerThread(Socket s) {
        this.s = s;
    }

    @Override
    public void run() {
        super.run();

        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            s.getInputStream()
                    )
            );

            while(!s.isClosed()){
                String input, msg = "";
                while (!(input = br.readLine()).equals("")) {
                    System.out.println(input);
                    if (input.startsWith("msg:"))
                        sendMessage(input.substring("msg:".length()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void sendMessage(String msg) throws IOException {
        PrintWriter pw = new PrintWriter(
                s.getOutputStream(),
                true
        );

        pw.println("msg:"+msg.toUpperCase());
        pw.println("");
        pw.close();
    }
}
