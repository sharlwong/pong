package com.sutd.Network;
/**
 * This is used by the server and clients to handle incoming messages.
 * @author Swayam
 *
 */
public interface MessageHandler {
	/**
	 * Generic method that handles a message 
	 * @param id id of the sender
	 * @param type type of message
	 * @param message payload data
	 */
	void handle(int id, String type, String message);
}
