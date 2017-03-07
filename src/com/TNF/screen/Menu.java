package com.TNF.screen;

import java.util.List;

import com.TNF.InputHandler;
import com.TNF.Main;
import com.TNF.gfx.Color;
import com.TNF.gfx.Font;
import com.TNF.gfx.Screen;

public class Menu {
	protected Main game;
	protected InputHandler input;

	public void init(Main game, InputHandler input) {
		this.input = input;
		this.game = game;
	}

	public void tick() {
	}

	public void render(Screen screen) {
	}
	 public int centertext(final String name) {
		 return (288 - name.length() * 8) / 2;
	 }

}
