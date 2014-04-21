package com.sutd.PongHelpers;

import com.badlogic.gdx.Input.TextInputListener;
import com.sutd.Client.GameClient;

public class PasswordListener implements TextInputListener {
	GameClient client;
	public PasswordListener(GameClient client) {
		this.client = client;
	}
	@Override
	public void input(String text) {
		client.setPassword(text);
	}

	@Override
	public void canceled() {
		// TODO Auto-generated method stub
	}
}
