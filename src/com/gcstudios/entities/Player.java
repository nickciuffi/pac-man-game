package com.gcstudios.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.gcstudios.main.Game;
import com.gcstudios.world.DotTile;
import com.gcstudios.world.FloorTile;
import com.gcstudios.world.PowerTile;
import com.gcstudios.world.Tile;
import com.gcstudios.world.World;

public class Player extends Entity{
	
	public boolean right,up,left,down;
	public String lastDir = "right";
	public static int score = 0;
	public int life = 3;
	public int initialPositionX = 0, initialPositionY = 0;
	public static boolean powerMode = false;
	private int powerFrames = 0, powerFramesMax = 300;
	
	private BufferedImage[] player_right = new BufferedImage[3];
	private BufferedImage[] player_left = new BufferedImage[3];
	private BufferedImage[] player_up = new BufferedImage[3];
	private BufferedImage[] player_down = new BufferedImage[3];
	
	
	public Player(int x, int y, int width, int height,double speed) {
		
		super(x, y, width, height,speed,null);
		depth = 2;
		initialPositionX = x;
		initialPositionY = y;
		for(int i = 0; i < 3; i++) {
			player_right[i] = Game.spritesheet.getSprite(32+(i*16), 0,16,16);
		}
		for(int i = 0; i < 3; i++) {
			player_left[i] = Game.spritesheet.getSprite(32+(i*16), 16,16,16);
		}
		for(int i = 0; i < 3; i++) {
			player_down[i] = Game.spritesheet.getSprite(80+(i*16), 16,16,16);
		}
		for(int i = 0; i < 3; i++) {
			player_up[i] = Game.spritesheet.getSprite(80+(i*16), 0,16,16);
		}
	}
	
	public void tick(){
		if(right && World.isFree((int)(x+speed),this.getY())) {
			
			lastDir = "right";
		}
		else if(left && World.isFree((int)(x-speed),this.getY())) {
			lastDir = "left";
		}
		if(up && World.isFree(this.getX(),(int)(y-speed))) {
			lastDir = "up";
		}
		if(down && World.isFree(this.getX(),(int)(y+speed))) {
			lastDir = "down";
		}
		
		if(lastDir == "right" && World.isFree((int)(x+speed),this.getY())) {
			x+=speed;
			}
		else if(lastDir == "left" && World.isFree((int)(x-speed),this.getY())) {
			x-=speed;
			}
		if(lastDir == "up" && World.isFree(this.getX(),(int)(y-speed))){
			y-=speed;
		}
		else if(lastDir == "down" && World.isFree(this.getX(),(int)(y+speed))){
			y+=speed;
		}
		
		
		detectTeleport();
		checkCollisionDot();
		checkCollisionEnemy();
		checkCollisionPower();
		checkPowerTime();
	}
	
	private void checkCollisionDot(){
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			if(e instanceof DotTile && isColliding(e, this)) {
				Game.entities.remove(e);
				score+=10;
			}
		}
		
	}
	
	private void checkCollisionPower(){
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			if(e instanceof PowerTile && isColliding(e, this)) {
				Game.entities.remove(e);
				powerMode = true;
			}
		}
		
	}
	
	private void checkCollisionEnemy(){
		for(int i = 0; i < Game.enemies.size(); i++) {
			Enemy e = Game.enemies.get(i);
			if(isColliding(e, this)) {
				
				if(powerMode) {
					if(e.isActive) {
					resetEnemy(e);
					score+=500;
					}
					return;
				}
				life--;
				if(life>0) {
					World.restart();
				}
				else {
					
					World.restart();
					Game.gameState = "GAME_OVER";
					
				}
				
			}
		}
		
	}
	
	public void resetEnemy(Enemy e) {
		e.setX(e.initialPositionX);
		e.setY(e.initialPositionY);
		e.isActive = false;
	}
	
	private void checkPowerTime() {
		if(powerMode) {
		powerFrames++;
		if(powerFrames >= powerFramesMax) {
			powerMode = false;
			powerFrames = 0;
		}
		}
		else {
			powerFrames = 0;
		}
	}
	
	
	public void render(Graphics g) {
		switch(lastDir){
		case "right":
			g.drawImage(player_right[curAnimation],this.getX(),this.getY(),null);
			break;
			
		case "down":
			g.drawImage(player_down[curAnimation],this.getX(),this.getY(),null);
			break;
		
		case "left":
			g.drawImage(player_left[curAnimation],this.getX(),this.getY(),null);
			break;
			
		case "up":
			g.drawImage(player_up[curAnimation],this.getX(),this.getY(),null);
			break;
			
		}
		
	}
	
	
	


	


}
