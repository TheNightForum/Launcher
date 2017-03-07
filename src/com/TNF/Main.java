package com.TNF;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;

import com.TNF.gfx.Color;
import com.TNF.gfx.Font;
import com.TNF.gfx.Screen;
import com.TNF.gfx.SpriteSheet;
import com.TNF.screen.Menu;

import javax.imageio.ImageIO;

public class Main extends Canvas implements Runnable
{
	private static final long serialVersionUID = 2L;
	
	public static final String NAME = "TNF Launcher";
	public static final String VERSION = "1.0";
	public static final int HEIGHT = 200;
	public static final int WIDTH = 300;
	public static final int SCALE = 3;

	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	private boolean running = false;
	public static Screen screen;
	public static Screen lightScreen;
	public static Screen fogScreen;
	private InputHandler input = new InputHandler(this);

	private int[] colors = new int[256];
	private int tickCount = 0;
	public int gameTime = 0;

	public Menu menu;
	
	public void setMenu(Menu menu) {
		this.menu = menu;
		if (menu != null) menu.init(this, input);
	}
	
	public void start() {
		running = true;
		new Thread(this).start();
	}

	public void stop() {
		running = false;
	}


	/**
	 * Performs a full initialization of the game - graphics, generated levels,
	 * etc. The result is a fresh new game ready to be played.
	 * 
	 * This method should NOT be used for loaded games. Loaded games are inited
	 * as the player starts a new game. After loading them we call initGraphics
	 * and we are done.
	 */
	public void init() {
		initGraphics();
	}
	
	/**
	 * Performs initialization of the game graphics.
	 */
	protected void initGraphics() {
		int pp = 0;
		for (int r = 0; r < 6; r++) {
			for (int g = 0; g < 6; g++) {
				for (int b = 0; b < 6; b++) {
					int rr = (r * 255 / 5);
					int gg = (g * 255 / 5);
					int bb = (b * 255 / 5);
					int mid = (rr * 30 + gg * 59 + bb * 11) / 100;

					int r1 = ((rr + mid * 1) / 2) * 230 / 255 + 10;
					int g1 = ((gg + mid * 1) / 2) * 230 / 255 + 10;
					int b1 = ((bb + mid * 1) / 2) * 230 / 255 + 10;
					colors[pp++] = r1 << 16 | g1 << 8 | b1;

				}
			}
		}
		try {
			screen = new Screen(WIDTH, HEIGHT, new SpriteSheet(ImageIO.read(Main.class.getResourceAsStream("/icons.png"))));
			lightScreen = new Screen(WIDTH, HEIGHT, new SpriteSheet(ImageIO.read(Main.class.getResourceAsStream("/icons.png"))));
			fogScreen = new Screen(WIDTH, HEIGHT, new SpriteSheet(ImageIO.read(Main.class.getResourceAsStream("/icons.png"))));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		long lastTime = System.nanoTime();
		double unprocessed = 0;
		double nsPerTick = 1000000000.0 / 60;
		long lastTimer1 = System.currentTimeMillis();

		while (running) {
			long now = System.nanoTime();
			unprocessed += (now - lastTime) / nsPerTick;
			lastTime = now;
			boolean shouldRender = true;
			while (unprocessed >= 1) {
				tick();
				unprocessed -= 1;
				shouldRender = true;
			}
			
			if (!running) {
				shouldRender = false;
			}

			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (shouldRender) {
				try {
					render();
				} catch (IllegalStateException e) {
					// this is where it gets messed up so we bail out!
					System.err.println("Game thread exiting, rendering failed:");
					e.printStackTrace();
					running = false;
					break;
				}
			}

			if (System.currentTimeMillis() - lastTimer1 > 1000) {
				lastTimer1 += 1000;
			}
		}
	}

	public void tick() {
		tickCount++;
		if (!hasFocus()) {
			input.releaseAll();
		} else {
			input.tick();
			if (menu != null) {
				menu.tick();
			}
		}
	}

	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			requestFocus();
			return;
		}

		renderGui();

		//if (!hasFocus()) renderFocusNagger();

		for (int y = 0; y < screen.h; y++) {
			for (int x = 0; x < screen.w; x++) {
				int cc = screen.pixels[x + y * screen.w];
				if (cc < 255) pixels[x + y * WIDTH] = colors[cc];
			}
		}

		Graphics g = bs.getDrawGraphics();
		g.fillRect(0, 0, getWidth(), getHeight());

		int ww = WIDTH * 3;
		int hh = HEIGHT * 3;
		int xo = (getWidth() - ww) / 2;
		int yo = (getHeight() - hh) / 2;
		g.drawImage(image, xo, yo, ww, hh, null);
		g.dispose();
		bs.show();
	}
	
	private void renderGui() {
		if (this.gameTime > 0) {
			for (int y = 0; y < 2; y++) {
				for (int x = 0; x < 20; x++) {
					screen.render(x * 8, screen.h - 16 + y * 8, 0 + 12 * 32, Color.get(000, 000, 000, 000), 0);
				}
			}
	
		}

		if (menu != null) {
			menu.render(screen);
		}
	}

	private void renderFocusNagger() {
		String msg = "Click to focus!";
		int xx = (WIDTH - msg.length() * 8) / 2;
		int yy = (HEIGHT - 8) / 2;
		int w = msg.length();
		int h = 1;

		screen.render(xx - 8, yy - 8, 0 + 13 * 32, Color.get(-1, 1, 5, 445), 0);
		screen.render(xx + w * 8, yy - 8, 0 + 13 * 32, Color.get(-1, 1, 5, 445), 1);
		screen.render(xx - 8, yy + 8, 0 + 13 * 32, Color.get(-1, 1, 5, 445), 2);
		screen.render(xx + w * 8, yy + 8, 0 + 13 * 32, Color.get(-1, 1, 5, 445), 3);
		for (int x = 0; x < w; x++) {
			screen.render(xx + x * 8, yy - 8, 1 + 13 * 32, Color.get(-1, 1, 5, 445), 0);
			screen.render(xx + x * 8, yy + 8, 1 + 13 * 32, Color.get(-1, 1, 5, 445), 2);
		}
		for (int y = 0; y < h; y++) {
			screen.render(xx - 8, yy + y * 8, 2 + 13 * 32, Color.get(-1, 1, 5, 445), 0);
			screen.render(xx + w * 8, yy + y * 8, 2 + 13 * 32, Color.get(-1, 1, 5, 445), 1);
		}

		if ((tickCount / 20) % 2 == 0) {
			Font.draw(msg, screen, xx, yy, Color.get(5, 333, 333, 333));
		} else {
			Font.draw(msg, screen, xx, yy, Color.get(5, 555, 555, 555));
		}
	}
}