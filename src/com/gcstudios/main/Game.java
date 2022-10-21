package com.gcstudios.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JFrame;

import com.gcstudios.entities.Enemy;
import com.gcstudios.entities.Entity;
import com.gcstudios.entities.Player;
import com.gcstudios.graficos.Spritesheet;
import com.gcstudios.graficos.UI;
import com.gcstudios.world.DotTile;
import com.gcstudios.world.World;

public class Game extends Canvas implements Runnable,KeyListener,MouseListener,MouseMotionListener{

	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	public static final int WIDTH = 304;
	public static final int HEIGHT = 352;
	public static final int SCALE = 2;
	public static String gameState = "NORMAL";
	
	private BufferedImage image;
	

	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static Spritesheet spritesheet;
	public static World world;
	public static Player player;
	
	public UI ui;
	
	public int timeGame = 0;
	public static int minGame = 0,secGame = 0;
	public String scoreCompleter = "000";
	public static Icon fireWorks;
	
	public Game(){
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE + (16*SCALE)));
		initFrame();
		image = new BufferedImage(WIDTH+64,HEIGHT,BufferedImage.TYPE_INT_RGB);
		
		//Inicializando objetos.
		spritesheet = new Spritesheet("/spritesheet.png");
		player = new Player(0,0,16,16,1);
		
		ui = new UI();
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		
		entities.add(player);
		world = new World("/level1.png");
	}
	
	public void initFrame(){
		frame = new JFrame("Pac-Man");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public synchronized void start(){
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	public synchronized void stop(){
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]){
		Game game = new Game();
		game.start();
	}
	
	public void tick(){
		if(gameState == "NORMAL") {
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			
			e.tick();
			
		}
		timeGame++;
		if(timeGame == 60) {
			secGame++;
			if(secGame == 60) {
				minGame++;
				secGame = 0;
			}
		}
		if(timeGame >= 500) {
			releaseEnemy();
			timeGame = 0;
		}
		verifyScoreCompleter();
			verifyVictory();
		}
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
		if(e.animationFrames >= e.animationFramesMax) {
			e.animationFrames = 0;
			if(e.curAnimation >= e.animationMax) {
				e.curAnimation = 0;
			}
			else {
				e.curAnimation++;
			}
			
		}
		e.animationFrames++;
		}
		
	}
	
	private void verifyScoreCompleter() {
		scoreCompleter = "000";
		if(player.score > 9) {
			
			if(player.score > 99) {
				if(player.score > 999) {
					scoreCompleter = "";
					return;
				}
				scoreCompleter = "0";
				return;
			}
			scoreCompleter = "00";
		}
		
	}
	
	private void verifyVictory() {
		if(gameState == "NORMAL") {
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if(e instanceof DotTile) {
				gameState = "NORMAL";
				return;
				
			}
			
		}
		gameState = "WON";
		}
	}
	
	private void releaseEnemy() {
		for(int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
		
			if(!e.isActive) {
				e.isActive = true;
				return;
			}
			}
		}
	
	
	
	
	public void render(){
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null){
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		g.setColor(new Color(0,0,0));
		g.fillRect(0, 0,WIDTH,HEIGHT);
		
		/*Renderização do jogo*/
		//Graphics2D g2 = (Graphics2D) g;
		world.render(g);
		Collections.sort(entities,Entity.nodeSorter);
		for(int i = 0; i < entities.size(); i++) {
			
			Entity e = entities.get(i);
			e.render(g);
			
		}
		
		
		/***/
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, -32*SCALE, 16*SCALE,(WIDTH+64)*SCALE,HEIGHT*SCALE,null);
		
		
		ui.render(g);
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 25));
		
		g.drawString("Score: " + scoreCompleter + player.score, WIDTH*SCALE/2 - 68, 25);
		
		bs.show();
	}
	
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		requestFocus();
		while(isRunning){
			long now = System.nanoTime();
			delta+= (now - lastTime) / ns;
			lastTime = now;
			if(delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
			}
			
			if(System.currentTimeMillis() - timer >= 1000){
				frames = 0;
				timer+=1000;
			}
			
		}
		
		stop();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT ||
				e.getKeyCode() == KeyEvent.VK_D){
			player.right = true;
			player.left = false;
			player.up = false;
			player.down = false;
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT ||
				e.getKeyCode() == KeyEvent.VK_A){
			player.left = true;
			player.right = false;
			player.up = false;
			player.down = false;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_UP ||
				e.getKeyCode() == KeyEvent.VK_W){
			player.up = true;
			player.down = false;
			player.right = false;
			player.left = false;
			
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN ||
				e.getKeyCode() == KeyEvent.VK_S) {
			player.down = true;
			player.up = false;
			player.left = false;
			player.right = false;
		}
		else if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			if(gameState == "GAME_OVER" || gameState == "WON") {
				world.restartAll();
			}
			
		}
		
	
	}

	@Override
	public void keyReleased(KeyEvent e) {
		/*if(e.getKeyCode() == KeyEvent.VK_RIGHT ||
				e.getKeyCode() == KeyEvent.VK_D){
			player.right = false;
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT ||
				e.getKeyCode() == KeyEvent.VK_A){
			player.left = false;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_UP ||
				e.getKeyCode() == KeyEvent.VK_W){
			player.up = false;
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN ||
				e.getKeyCode() == KeyEvent.VK_S) {
			player.down = false;
		}
		*/
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {	
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	
	}

	
}
