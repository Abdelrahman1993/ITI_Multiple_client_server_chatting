package client_server_multiple;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server 
{
	ServerSocket  serverSocket;
	public Server() throws IOException
	{
		//only one server socket for the server
		serverSocket=new ServerSocket(5004);
		
		//as long as he is able to accept a request, create internal socket to it, send it to the chat handler
		while(true)
		{
			Socket s=serverSocket.accept();
			new ChatHandler(s);
		}
	}
	
	public static void main(String[] args) throws IOException 
	{
		//one object for one server the constructor will start the server 
		new Server();
	}

}

class ChatHandler extends Thread
{
	//every chat handler represent the in/out stream of the internal thread
	//static vector for the chat handler have all in/out streams of all objects
	DataInputStream dis;
	PrintStream ps;
	static Vector<ChatHandler> clientsVector = new Vector<ChatHandler>();
	
	//create in/out stream for each object and add the object in the vector
	//start thread for each object
	public ChatHandler(Socket s) throws IOException 
	{
		dis=new DataInputStream(s.getInputStream());
		ps=new PrintStream(s.getOutputStream());
		clientsVector.add(this);
		start();
	}
	
	//each object will have thread to able to read from in stream all the time
	public void run()
	{
		while(true)
		{
			try {
				//block operation thread will always wait for the input stream 
				//if he find something read it into string and send the message to all output streams
				String msg = dis.readLine();
				sendMessageToAll(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//loop through object and for each one print data to its output stream
	void sendMessageToAll(String msg)
	{
		for(ChatHandler ch: clientsVector)
		{
			ch.ps.println(msg);
		}
	}
	
}












