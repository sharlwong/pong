package com.sutd.Network;
/**
 * This is used by the server and clients to handle incoming messages.
 * @author Swayam
 *
 */
public interface MessageHandler {
	void handle(String Message);
}
