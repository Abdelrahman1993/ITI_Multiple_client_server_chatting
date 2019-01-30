package client_server_multiple;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class SampleUI extends JFrame 
{
	Socket s;
	DataInputStream dis;
	PrintStream ps; 
	public SampleUI() throws UnknownHostException, IOException{
		///////////////////////////////////////
		this.setLayout(new FlowLayout());
		JTextArea ta=new JTextArea(5,150);
		JScrollPane scroll=new JScrollPane(ta);
		scroll.setViewportView(ta);
		JTextField tf=new JTextField(30);
		JButton okButton=new JButton("Send");
		///////////////////////////////////////
		
		s=new Socket("localhost", 5004);
		dis = new DataInputStream(s.getInputStream());
		ps = new PrintStream(s.getOutputStream());
		
		//////////////////////////////////////////////
		okButton.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent ae){
				//ta.append(tf.getText()+"\n");
				String msg=tf.getText();
				ps.println(msg);
				tf.setText("");
			}
		});
		////////////////////////////////////////////////
		new Thread(new Runnable() {

			@Override
			public void run() {
				while(true)
				{
					//thread will always be run listening from the inputstream
					//block operation
					try {
						ta.append(dis.readLine()+"\n");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
			
		}).start();
		
		////////////////////////////////////////////////

		add(scroll);
		add(tf);
		add(okButton);
}

public static void main(String args[]) throws UnknownHostException, IOException
	{
		SampleUI ui=new SampleUI();
		ui.setSize(400, 500);
		ui.setVisible(true);
	}
}