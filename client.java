import java.io.*;
import java.net.Socket;
import java.util.Scanner;

class client extends Thread{
    String clientname;
    String servermsg;
    BufferedReader serverinput;
    Socket socket;
    int bit = 0;

    client(Socket socket)
    {
        this.socket = socket;
        try{
            serverinput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }catch(Exception e)
        {

        }
    }
    public void run()
    {
            try{
            while(true)
             {
                 if(bit == 1)
                 {  
                     break;
                 }
                servermsg = serverinput.readLine();
                if(servermsg.equalsIgnoreCase("kick"))
                {
                    System.exit(0);
                }
                if(servermsg!=null)
                {
                    //serverinput.println(servermsg);
                    System.out.println(servermsg);
                    //System.out.println("Enter Message: ");
                    servermsg=null;
                }

               
             }
            }catch(Exception e)
            {

            }
       

    }
    void message()
    {
        try{
            Scanner scan = new Scanner(System.in);           
            //serverinput = new BufferedReader(new InputStreamReader(socket.getInputStream())); //receive from server
            PrintWriter cresponse = new PrintWriter(socket.getOutputStream(), true); //client sending messages
            String sresponse = serverinput.readLine();
            //System.out.println("Hello");
            System.out.print(sresponse);
            String name = scan.nextLine();
            cresponse.println(name);
            this.clientname = name;
            String msg;
            System.out.println("Enter Message: ");
            this.start();
            do{
                //System.out.println("Enter Message: ");
                msg = scan.nextLine();
                if(msg.equalsIgnoreCase("close"))
                {
                    bit = 1;
                    System.exit(0);
                    //this.join();
                    break;
                }
                cresponse.println(msg);
                
            } while(!msg.equals("close"));
            this.join();
        }catch(Exception e){
            //System.out.println(e.getMessage());
        }
    }    

    public static void main(String [] args)
    {
        try {
            Socket socket = new Socket("127.0.0.1", 5000);
            client c = new client(socket);
            c.message();
        } catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}