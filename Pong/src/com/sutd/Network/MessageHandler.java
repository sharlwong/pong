package com.sutd.Network;
/**
 * This is used by consumers to handle incoming messages.
 * @author Swayam
 *
 */
public interface MessageHandler {
	void handle(String Message);
}
