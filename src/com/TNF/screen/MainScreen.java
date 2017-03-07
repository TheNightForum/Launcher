package com.TNF.screen;

import com.TNF.GameContainer;
import com.TNF.gfx.Screen;

public class MainScreen extends Menu {

	public MainScreen(){
	}
	/**
	 * Status -1 	= Dev did not enable it.
	 * Status 1  	= Display Credits.
	 * Status 2		= Display ChangeLog.
	 */
	public void tick() {
		DialogMenu welcome = new DialogMenu();
		welcome.setTitle("");
		welcome.setText("Please wait!\n\n" +
				"Why? We are downloading everything for the launcher.\n\n" +
				"Hopefully you will never have to see this again.\n\n" +
				"The Launcher will start automatically after this.");
		game.setMenu(welcome);
		GameContainer.letsStart();
	}

	public void render(Screen screen) {
		screen.clear(0);
	}
}
