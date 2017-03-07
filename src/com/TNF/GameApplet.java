package com.TNF;

import com.TNF.screen.MainScreen;

import java.applet.Applet;
import java.awt.BorderLayout;

public class GameApplet extends Applet {
	private static final long serialVersionUID = 1L;

	public void init() {
		// create a new game
		Main game = new Main();
		game.initGraphics();
		game.setMenu(new MainScreen());
		GameContainer.getInstance().setGame(game);
		
		// init design
		setLayout(new BorderLayout());
		add(game, BorderLayout.CENTER);
		setSize(Main.WIDTH * Main.SCALE, Main.HEIGHT * Main.SCALE);
	}

	public void start() {
		GameContainer.getInstance().getGame().start();
	}

	public void stop() {
		GameContainer.getInstance().getGame().stop();
	}
	
	
}