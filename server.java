import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

class server extends Thread{
    BufferedReader clientinput;
    FileOutputStream write1;
    PrintWriter sresponse;
    static ArrayList <server> clientlist = new ArrayList<server>();
    static ArrayList <String> clientnames = new ArrayList<String>();
    Socket socket;
    boolean Admin = false;
    String name;
    server(Socket socket)
    {
        this.socket = socket;
    }
    void sendMsg(String clientName, String clientmsg)
    {
        
        for (int i = 0; i < clientlist.size(); i++)
        {

            clientlist.get(i).sresponse.println(clientName + " : " + clientmsg);

        }
        try{
           
            byte [] a = clientName.getBytes();
            byte [] b = clientmsg.getBytes();
            write1.write(a);
            write1.write(':');
            write1.write(b);
            write1.write(';');
        }catch(Exception e)
        {
            System.out.println("Send message error");
        }
    }
    public void run(){
        try{
           
            Scanner scan = new Scanner(System.in);
            clientinput = new BufferedReader(new InputStreamReader(socket.getInputStream())); //receive form client
            write1 = new FileOutputStream("data.txt", true); //store in data.txt
            sresponse = new PrintWriter(socket.getOutputStream(), true); //server sending messages
            System.out.println("Client Connected!");
            sresponse.println("Enter Your Name: ");
            String clientName = clientinput.readLine();
            clientlist.add(this);
            clientnames.add(clientName);
            this.name = clientName;
            if (clientnames.size()==1)
            {
                Admin = true;
            }
            String clientmsg = "";
            while(true)
            {
                int indiv_check = 0;
                clientmsg = clientinput.readLine();
                String [] split_msg = clientmsg.split(" ");
                if(clientmsg.equalsIgnoreCase("close"))
                {
                    break;
                }
                if(clientmsg.equals("$"))
                {
                    FileInputStream fr = new FileInputStream("data.txt");
                    int k = 0;
                    String res="";
                    while((k=fr.read())!=-1)
                    {
                        res+= (char)k;   
                    }
                    String ressplit [] = res.split(";");
                    for(int j = 0 ; j < ressplit.length - 1; j++)
                    {
                        this.sresponse.println(ressplit[j]);
                    }
                    indiv_check = 1;
                    
                }
                else if(split_msg.length == 2)
                {
                    
                    for(int i = 0; i < clientnames.size(); i++)
                    {
                        String temp = clientnames.get(i);
                        if(split_msg[0].equalsIgnoreCase("$"))
                        {
                            
                                FileInputStream fr = new FileInputStream("data.txt");
                                int k = 0;
                                String res="";
                                while((k=fr.read())!=-1)
                                {
                                    res+= (char)k;   
                                }
                                String ressplit [] = res.split(";");
                                for(int j = 0 ; j < ressplit.length; j++)
                                {
                                    String f [] = ressplit[j].split(":");
                                    System.out.println(f[0]);
                                    if(f[0].equals(split_msg[1]))
                                    {
                                        this.sresponse.println(ressplit[j]);
                                    }
                                    //System.out.println(ressplit[j]);
                                }
                                indiv_check = 1;
                                break;
                            
                        }
                    }        
                }
                
                if(Admin)
                {
                    for(int i = 0; i < clientnames.size(); i++)
                    {
                        String temp = clientnames.get(i);
                        if(split_msg[0].equalsIgnoreCase("kick"))
                        {
                            if(split_msg[1].equalsIgnoreCase(temp) && !temp.equalsIgnoreCase(this.name))
                            {
                                clientnames.remove(temp);
                                clientlist.get(i).sresponse.println("kick");
                                clientlist.get(i).socket.close();
                                clientlist.remove(i);
                                indiv_check = 1;
                                break;
                            }
                        }
                        if(clientmsg.equalsIgnoreCase("delete file"))
                        {
                            File file = new File("./data.txt");
                            if(file.delete())
                            {
                                System.out.println("File deleted successfully");
                                indiv_check = 1;
                                break;
                            }
                            else
                            {
                                System.out.println("Failed to delete the file");
                                indiv_check = 1;
                                break;
                            }
                        }
                    }
                }
                for(int i = 0; i < clientnames.size(); i++)
                {
                    String temp = clientnames.get(i);
                    if(split_msg[0].equalsIgnoreCase(temp))
                    {
                        if(!temp.equalsIgnoreCase(clientName))
                        {
                            clientlist.get(i).sresponse.println(clientName + " : " + clientmsg);
                            this.sresponse.println(clientName + " : " + clientmsg);
                            indiv_check = 1;
                            break;
                        }
                    }
                }
                if(!clientmsg.equals(null) && indiv_check == 0)
                {
                    System.out.println(clientName + " : " + clientmsg);
                    //sresponse.println(clientName + " : " + clientmsg);
                    sendMsg(clientName, clientmsg);
                }
                indiv_check = 0;
                clientmsg = null;
            }

        }catch(Exception e){
            //System.out.println(e.getMessage());
        }

    }

    public static void main(String [] args)
    {
        ServerSocket serversocket = null;
        Socket socket = null;
        try {
            serversocket = new ServerSocket(5000);

        } catch(Exception e)
        {
            System.out.println("Exception in Main Server"); 
        }
        
        while(true)
        {
            try{
                socket = serversocket.accept();
                server client = new server(socket);
                client.start();

            }catch(Exception e)
            {
                System.out.println("Exception in Main Server Thread"); 
            }
        }
    }
}