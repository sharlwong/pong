package com.sutd.Server;

import java.io.PrintWriter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.Net.Protocol;

/**
 * The authoritative Server.
 * Responsible for handling requests
 * updating game state and responding.
 * @author Swayam
 *
 */
public class GameServer extends Thread {
	private final int port = 5000;
	public ServerSocket serverSocket;
	
	/**
	 * Start the server socket
	 */
	public void run() {
		System.out.println("Sever Starting...");
		ServerSocket serverSocket =  Gdx.net.newServerSocket(Protocol.TCP,port, null);
		Socket client_socket = serverSocket.accept(null);
		System.out.println("Client Connected!");
//		Socket client_socket_2 = serverSocket.accept(null);
		PrintWriter pw = new PrintWriter(client_socket.getOutputStream());
//		PrintWriter pw2 = new PrintWriter(client_socket_2.getOutputStream());
		pw.println("Hello");
		pw.flush();
//		pw2.println("Hello\n");
	}
	
	/** Server updates  
	 * */
}
