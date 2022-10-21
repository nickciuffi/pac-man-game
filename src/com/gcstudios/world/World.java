package com.gcstudios.world;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.gcstudios.entities.Enemy;
import com.gcstudios.entities.Entity;
import com.gcstudios.entities.Player;
import com.gcstudios.main.Game;

public class World {

	public static Tile[] tiles;
	public static int WIDTH,HEIGHT;
	public static final int TILE_SIZE = 16;
	private int numEnemies = 0;
	
	public World(String path){
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			int[] pixels = new int[map.getWidth() * map.getHeight()];
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			tiles = new Tile[map.getWidth() * map.getHeight()];
			map.getRGB(0, 0, map.getWidth(), map.getHeight(),pixels, 0, map.getWidth());
			System.out.println(map.getWidth());
			for(int xx = 0; xx < map.getWidth(); xx++){
				for(int yy = 0; yy < map.getHeight(); yy++){
					int pixelAtual = pixels[xx + (yy * map.getWidth())];
					tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16,yy*16,Tile.TILE_FLOOR);
					if(pixelAtual == 0xFF00FF33) {
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16,yy*16,Tile.TILE_FLOOR);
					}
					else if(pixelAtual == 0xFF000000){
						//dotTile
						Game.entities.add(new DotTile(xx*16, yy*16, Tile.TILE_DOT));
					}
					else if(pixelAtual == 0xFFFF00E5){
						//dotTile
						Game.entities.add(new PowerTile(xx*16, yy*16, Tile.TILE_DOT_POWER));
					}
					else if(pixelAtual == 0xFFFFFFFF){
						//Parede
						
						tiles[xx + (yy * WIDTH)] = new WallTile(xx*16,yy*16,Tile.TILE_WALL);
					}
					else if(pixelAtual == 0xFF5066E5){
					//Canto esquerda cima
						tiles[xx + (yy * WIDTH)] = new WallTile(xx*16,yy*16,Tile.WALL_ESQUERDA_CIMA);
					}
					else if(pixelAtual == 0xFFE27851){
						//Canto esquerda baixo
							tiles[xx + (yy * WIDTH)] = new WallTile(xx*16,yy*16,Tile.WALL_ESQUERDA_BAIXO);
						}
					else if(pixelAtual == 0xFF7448E2){
						//Canto direita cima
							tiles[xx + (yy * WIDTH)] = new WallTile(xx*16,yy*16,Tile.WALL_DIREITA_CIMA);
						}
					else if(pixelAtual == 0xFFE24FDB){
						//Canto direita baixo
							tiles[xx + (yy * WIDTH)] = new WallTile(xx*16,yy*16,Tile.WALL_DIREITA_BAIXO);
						}
					else if(pixelAtual == 0xFF1DE2B8){
						//Canto cima
							tiles[xx + (yy * WIDTH)] = new WallTile(xx*16,yy*16,Tile.WALL_CIMA);
						}
					else if(pixelAtual == 0xFF56E24F){
						//Canto baixo
							tiles[xx + (yy * WIDTH)] = new WallTile(xx*16,yy*16,Tile.WALL_BAIXO);
						}
					else if(pixelAtual == 0xFFE02F35){
						//Canto direita
							tiles[xx + (yy * WIDTH)] = new WallTile(xx*16,yy*16,Tile.WALL_DIREITA);
						}
					else if(pixelAtual == 0xFF4EE0A6){
						//Canto baixo
							tiles[xx + (yy * WIDTH)] = new WallTile(xx*16,yy*16,Tile.WALL_ESQUERDA);
						}
					else if(pixelAtual == 0xFFB5E3FF){
						//teleport tile
							tiles[xx + (yy * WIDTH)] = new TeleportTile(xx*16,yy*16,Tile.WALL_ESQUERDA);
						}
					else if(pixelAtual == 0xFF0026FF) {
						//Player
						Game.player.setX(xx*16);
						Game.player.setY(yy*16);
						Game.player.initialPositionX = xx*16;
						Game.player.initialPositionY = yy*16;
					}
					else if(pixelAtual == 0xFFFF4530) {
						//enemy
						if(numEnemies == 0) {
							Enemy enemi = new Enemy(xx*16, yy*16,16, 16, 1, false, 1);
							Game.entities.add(enemi);
							Game.enemies.add(enemi);
						}
						else if(numEnemies == 1){
							
							Enemy enemi = new Enemy(xx*16, yy*16,16, 16, 1, true, 2);
							Game.entities.add(enemi);
							Game.enemies.add(enemi);
						}
						else if(numEnemies == 2){
							
							Enemy enemi = new Enemy(xx*16, yy*16,16, 16, 1, false, 3);
							Game.entities.add(enemi);
							Game.enemies.add(enemi);
						}
						else if(numEnemies == 3){
							
							Enemy enemi = new Enemy(xx*16, yy*16,16, 16, 1, false, 4);
							Game.entities.add(enemi);
							Game.enemies.add(enemi);
						}
						numEnemies++;
					}
					
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void restart() {
		for(int i = 0; i < Game.enemies.size(); i ++) {
			Enemy e = Game.enemies.get(i);
			e.setX(e.initialPositionX);
			e.setY(e.initialPositionY);
			e.circleWalkFrames = 0;
			e.isActive = e.isActiveStart;
			
		}
		Game.player.setX(Game.player.initialPositionX);
		Game.player.setY(Game.player.initialPositionY);
		Game.player.lastDir = "right";
		Player.powerMode = false;
	}
	public static void restartAll() {
		Game.player = new Player(0, 0, 16, 16, 1);
		Game.enemies = new ArrayList<Enemy>();
		Game.entities = new ArrayList<Entity>();
		Game.entities.add(Game.player);
		Game.world = new World("/level1.png");
		Game.gameState = "NORMAL";
		Player.score = 0;
		
	}
	
	public static boolean isFree(int xnext,int ynext){
		
		int x1 = xnext / TILE_SIZE;
		int y1 = ynext / TILE_SIZE;
		
		int x2 = (xnext+TILE_SIZE-1) / TILE_SIZE;
		int y2 = ynext / TILE_SIZE;
		
		int x3 = xnext / TILE_SIZE;
		int y3 = (ynext+TILE_SIZE-1) / TILE_SIZE;
		
		int x4 = (xnext+TILE_SIZE-1) / TILE_SIZE;
		int y4 = (ynext+TILE_SIZE-1) / TILE_SIZE;
		
		return !((tiles[x1 + (y1*World.WIDTH)] instanceof WallTile) ||
				(tiles[x2 + (y2*World.WIDTH)] instanceof WallTile) ||
				(tiles[x3 + (y3*World.WIDTH)] instanceof WallTile) ||
				(tiles[x4 + (y4*World.WIDTH)] instanceof WallTile));
	}
	
	
	public static GraphicsConfiguration getDefaultConfiguration() {
	    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    GraphicsDevice gd = ge.getDefaultScreenDevice();
	    return gd.getDefaultConfiguration();
	}
	
	public static BufferedImage rotate(BufferedImage image, double angle) {
	    int w = image.getWidth(), h = image.getHeight();
	    GraphicsConfiguration gc = getDefaultConfiguration();
	    BufferedImage result = gc.createCompatibleImage(w, h);
	    Graphics2D g = result.createGraphics();
	    g.rotate(Math.toRadians(angle), w / 2, h / 2);
	    g.drawRenderedImage(image, null);
	    g.dispose();
	    return result;
	}
	
	public void render(Graphics g){
	
		for(int xx = 0; xx <= WIDTH; xx++) {
			for(int yy = 0; yy <= HEIGHT; yy++) {
				if(xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT)
					continue;
				Tile tile = tiles[xx + (yy*WIDTH)];
				tile.render(g);
			}
		}
	}
	
}
