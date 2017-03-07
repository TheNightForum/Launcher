package com.TNF;

import com.TNF.screen.MainScreen;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.swing.JFrame;

/**
 * 		Game Container
 * 
 * Main class used as a manager of a currently running game.
 * 
 * Purpose, responsibilities, functions:
 *  - Entry-point for starting the game.
 *  - Creator and owner of the UI (JFrame).
 *  - Singleton interface, handles one "main" game (though the number of games
 *  	running at the same time is theoretically not limited by this).
 *  - Save and load function (+ GUI), swaps loaded game with the active one. 
 * 
 * @author Dejvino
 */
public class GameContainer
{
	private static GameContainer singleton;
	
	private JFrame jFrame;
	
	private Main game;

	private static int count = 0;

	private GameContainer()
	{
		this.game = new Main();
		this.game.initGraphics();
		this.game.setMenu(new MainScreen());
	}
	
	/**
	 * Returns a singleton instance of this class.
	 * 
	 * @return singleton
	 */
	public static GameContainer getInstance()
	{
		if (singleton == null) {
			singleton = new GameContainer();
		}
		return singleton;
	}
	
	/**
	 * Returns the currently active game.
	 * 
	 * @return game
	 */
	public Main getGame()
	{
		return this.game;
	}
	
	/**
	 * Changes the currently active game.
	 * 
	 * @param game
	 */
	public void setGame(Main game)
	{
		this.game = game;
	}
	
	/**
	 * Performs a one-time initialization of the environment.
	 * Namely it creates a UI window (JFrame) which is later used to display the game.
	 */
	public void init()
	{
		JFrame frame = new JFrame(Main.NAME);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.setResizable(false);
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
		
		jFrame = frame;
	}
	
	/**
	 * The currently active game is attached to the UI window and then started.
	 */
	public void startGame()
	{
		game.setMinimumSize(new Dimension(Main.WIDTH * Main.SCALE, Main.HEIGHT * Main.SCALE));
		game.setMaximumSize(new Dimension(Main.WIDTH * Main.SCALE, Main.HEIGHT * Main.SCALE));
		game.setPreferredSize(new Dimension(Main.WIDTH * Main.SCALE, Main.HEIGHT * Main.SCALE));
		
		jFrame.add(game, BorderLayout.CENTER);
		jFrame.pack();
		
		this.game.start();
	}


	public static void letsStart(){
		Data.main();
		if (count < 2) {
			File f = new File(Data.locationBin + "Launcher.jar");
			if (!f.exists()) {
				Download("launcher");
			} else {
				File res = new File(Data.locationRes + "icons.png");
				if (!res.exists()) {
					Download("res");
				} else {
					Launch.main();
				}
			}
		}else{
			Launch.main();
		}
	}

	private static void Download(String fileName){
		File f = new File(Data.locationBin + "Launcher.jar");
		if(f.exists()){
			f.delete();
		}
		String newURL = "";
		String newPath = "";
		if (fileName.equals("launcher")){
			newURL = "http://zectr.com/repository/Launcher/Latest/Launcher.zip";
			newPath = Data.locationTemp + "Launcher.zip";
		}else if (fileName.equals("res")){
			newURL = "http://zectr.com/repository/Launcher/res/res.zip";
			newPath = Data.locationTemp + "res.zip";
		}
		try(
				ReadableByteChannel in=Channels.newChannel(

						new URL(newURL).openStream());
				FileChannel out=new FileOutputStream(newPath).getChannel() ) {
			out.transferFrom(in, 0, Long.MAX_VALUE);
			System.err.println("Unzipping");
			if(fileName.equals("launcher")){
				UnZip(Data.locationTemp + "Launcher.zip", Data.locationBin);
			}else if (fileName.equals("res")){
				UnZip(Data.locationTemp + "res.zip", Data.locationRes);
			}
			letsStart();
		}
		catch(IOException ex){
		}
	}

	public static void UnZip(String zipFile, String outputFolder){

		byte[] buffer = new byte[1024];

		try{

			//create output directory is not exists
			File folder = new File(outputFolder);
			if(!folder.exists()){
				folder.mkdir();
			}

			//get the zip file content
			ZipInputStream zis =
					new ZipInputStream(new FileInputStream(zipFile));
			//get the zipped file list entry
			ZipEntry ze = zis.getNextEntry();

			while(ze!=null){

				String fileName = ze.getName();
				File newFile = new File(outputFolder + File.separator + fileName);

				System.out.println("file unzip : "+ newFile.getAbsoluteFile());

				//create all non exists folders
				//else you will hit FileNotFoundException for compressed folder
				new File(newFile.getParent()).mkdirs();

				FileOutputStream fos = new FileOutputStream(newFile);

				int len;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}

				fos.close();
				ze = zis.getNextEntry();
			}

			zis.closeEntry();
			zis.close();

			System.out.println("Done");

		}catch(IOException ex){
			ex.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{
		GameContainer cont = GameContainer.getInstance();
		cont.init();
		cont.startGame();
	}

}
