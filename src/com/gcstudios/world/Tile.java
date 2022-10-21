package com.gcstudios.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.gcstudios.main.Game;

public class Tile {
	
	public static BufferedImage TILE_FLOOR = Game.spritesheet.getSprite(0,0,16,16);
	public static BufferedImage TILE_DOT = Game.spritesheet.getSprite(0,80,16,16);
	public static BufferedImage TILE_DOT_POWER = Game.spritesheet.getSprite(16,80,16,16);
	public static BufferedImage TILE_WALL = Game.spritesheet.getSprite(16,0,16,16);
	public static BufferedImage WALL_DIREITA_CIMA = Game.spritesheet.getSprite(16,16,16,16);
	public static BufferedImage WALL_DIREITA_BAIXO = Game.spritesheet.getSprite(16,32,16,16);
	public static BufferedImage WALL_ESQUERDA_CIMA = Game.spritesheet.getSprite(0,16,16,16);
	public static BufferedImage WALL_ESQUERDA_BAIXO = Game.spritesheet.getSprite(0,32,16,16);
	public static BufferedImage WALL_CIMA = Game.spritesheet.getSprite(0,48,16,16);
	public static BufferedImage WALL_BAIXO = Game.spritesheet.getSprite(0,64,16,16);
	public static BufferedImage WALL_DIREITA = Game.spritesheet.getSprite(16,48,16,16);
	public static BufferedImage WALL_ESQUERDA = Game.spritesheet.getSprite(16,64,16,16);

	private BufferedImage sprite;
	public int x,y;
	
	public Tile(int x,int y,BufferedImage sprite){
		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}
	
	public void render(Graphics g){
		
		g.drawImage(sprite, x, y, null);
	}

}
