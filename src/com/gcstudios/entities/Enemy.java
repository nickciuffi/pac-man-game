package com.gcstudios.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.gcstudios.main.Game;
import com.gcstudios.world.AStar;
import com.gcstudios.world.Vector2i;



public class Enemy extends Entity{
	
	private BufferedImage[] enemyImg = new BufferedImage[2];
	private BufferedImage[] enemyImgPowerMode = new BufferedImage[2];
	
	public boolean isActive = false;
	private boolean isSeeingPlayer = false;
	public int atuationArea;
	private int ax = 5, ay = 5;
	private boolean restTime = true;
	private int restFrames = 0, restFramesMax = 200;
	private int stalkFrames = 0, stalkFramesMax = 600;
	public int circleWalkFrames = 0, circleWalkFramesMax = 16;
	public boolean inCircles = false;
	private int routePosition = 0;
	public int initialPositionX = 0, initialPositionY = 0;
	public boolean isActiveStart = false;
	
	private int[] route1 = new int[21];
	private int[] route2 = new int[23];
	private int[] route3 = new int[23];
	
	public Enemy(int x, int y, int width, int height,int speed, boolean isActiveStart, int atuation) {
		super(x, y, width, height,speed,null);
		this.atuationArea = atuation;
		animationMax = 1;
		depth = 1;
		initialPositionX = x;
		initialPositionY = y;
		this.isActive = isActiveStart;
		this.isActiveStart = isActiveStart;
		for(int i = 0; i < 2; i++) {
			enemyImg[i] = Game.spritesheet.getSprite(32+(i*16), 32,16,16);
		}
		for(int i = 0; i < 2; i++) {
			enemyImgPowerMode[i] = Game.spritesheet.getSprite(64+(i*16), 32,16,16);
		}

		getAtuationArea();
		fillCircles();
	}

	public void tick(){
		if(x/16 == ax && y/16 == ay && !isSeeingPlayer) {
			
			inCircles = true;
		}
		if(isSeeingPlayer) {
			inCircles = false;
			routePosition = 0;
			circleWalkFrames = 0;
		}
		
		if(inCircles) {
			walkInCircles();
			}
		else {
		if(isActive) {
		if(path == null || path.size() == 0) {
			Vector2i end;
				Vector2i start = new Vector2i(((int)(x/16)),((int)(y/16)));
				if(isSeeingPlayer && !restTime) {
					end = new Vector2i(((int)(Game.player.x/16)),((int)(Game.player.y/16)));
					}
				else {
					
					end = new Vector2i(ax, ay);
					
				}
				
				path = AStar.findPath(Game.world, start, end);
				
			}
		
		
				followPath(path);
	
			
			if(x % 16 == 0 && y % 16 == 0) {
			
				if(new Random().nextInt(100) > 20) {
					Vector2i end;
					Vector2i start = new Vector2i(((int)(x/16)),((int)(y/16)));
					if(isSeeingPlayer && !restTime) {
						if(rand.nextInt(100)<80) {
							end = new Vector2i(((int)(Game.player.x/16)),((int)(Game.player.y/16)));
					}
					else {
							end = new Vector2i(10, 10);
					}
						if(!inCircles) {
						path = AStar.findPath(Game.world, start, end);
						}
					}
					else {
							
								end = new Vector2i(ax, ay);
								path = AStar.findPath(Game.world, start, end);
							
					}
					
				}
			}
		}
		}
			
		verifyActive();
		
		if(restTime) {
		
		restFrames++;	
		if(restFrames >= restFramesMax) {
			restFrames = 0;
			restTime = false;
		}
		
		}
		else {
			
			stalkFrames++;
			if(stalkFrames >= stalkFramesMax) {
				stalkFrames = 0;
				restTime = true;
				
			}
		}
	}
	
	private void walkInCircles() {
		
		if(atuationArea == 1 || atuationArea == 2) {
				if(route1[routePosition] == 1) {
					circleWalkFrames++;
					x+=1;
					if(circleWalkFrames >= circleWalkFramesMax) {
						circleWalkFrames = 0;
						routePosition++;
					}
				}
				if(route1[routePosition] ==2) {
					circleWalkFrames++;
					y+=1;
					if(circleWalkFrames >= circleWalkFramesMax) {
						circleWalkFrames = 0;
						routePosition++;
					}
				}
				if(route1[routePosition] == 3) {
					circleWalkFrames++;
					x-=1;
					if(circleWalkFrames >= circleWalkFramesMax) {
						circleWalkFrames = 0;
						routePosition++;
					}
				}
				if(route1[routePosition] == 4) {
					circleWalkFrames++;
					y-=1;
					if(circleWalkFrames >= circleWalkFramesMax) {
						circleWalkFrames = 0;
						routePosition++;
					}
				}
			
					
					
				if(routePosition == route1.length-1) {
					routePosition = 0;
				}
				}
			
		else if(atuationArea == 3) {
			if(route2[routePosition] == 1) {
				
				circleWalkFrames++;
				x+=1;
				if(circleWalkFrames >= circleWalkFramesMax) {
					circleWalkFrames = 0;
					routePosition++;
				}
				
				
			}
			if(route2[routePosition] ==2) {
				circleWalkFrames++;
				y+=1;
				if(circleWalkFrames >= circleWalkFramesMax) {
					circleWalkFrames = 0;
					routePosition++;
				}
				
			}
			if(route2[routePosition] == 3) {
				circleWalkFrames++;
				x-=1;
				if(circleWalkFrames >= circleWalkFramesMax) {
					circleWalkFrames = 0;
					routePosition++;
				}
			}
			if(route2[routePosition] == 4) {
				circleWalkFrames++;
				y-=1;
				if(circleWalkFrames >= circleWalkFramesMax) {
					circleWalkFrames = 0;
					routePosition++;
				}
			}
			if(routePosition == route2.length-1) {
				routePosition = 0;
			}
			}
		else if(atuationArea == 4) {
			if(route3[routePosition] == 1) {
				
				circleWalkFrames++;
				x+=1;
				if(circleWalkFrames >= circleWalkFramesMax) {
					circleWalkFrames = 0;
					routePosition++;
				}
				
				
			}
			if(route3[routePosition] ==2) {
				circleWalkFrames++;
				y+=1;
				if(circleWalkFrames >= circleWalkFramesMax) {
					circleWalkFrames = 0;
					routePosition++;
				}
				
			}
			if(route3[routePosition] == 3) {
				circleWalkFrames++;
				x-=1;
				if(circleWalkFrames >= circleWalkFramesMax) {
					circleWalkFrames = 0;
					routePosition++;
				}
			}
			if(route3[routePosition] == 4) {
				circleWalkFrames++;
				y-=1;
				if(circleWalkFrames >= circleWalkFramesMax) {
					circleWalkFrames = 0;
					routePosition++;
				}
			}
			if(routePosition == route3.length-1) {
				routePosition = 0;
			}
			}
		}
	
	private void fillCircles() {
		for(int i = 0; i < 7; i++) {
			route1[i] = 1;
		}
		for(int i = 7; i < 7+3; i++) {
			route1[i] = 2;
		}
		for(int i = 10; i < 17; i++) {
			route1[i] = 3;
		}
		for(int i = 17; i < 20; i++) {
			route1[i] = 4;
		}
		
		for(int i = 0; i < 7; i++) {
			route2[i] = 1;
		}
		for(int i = 7; i < 9; i++) {
			route2[i] = 2;
		}
		for(int i = 9; i < 13; i++) {
			route2[i] = 3;
		}
		for(int i = 13; i < 15; i++) {
			route2[i] = 2;
		}
		for(int i = 15; i < 17; i++) {
			route2[i] = 3;
		}
		for(int i = 17; i < 19; i++) {
			route2[i] = 4;
		}
		for(int i = 19; i < 20; i++) {
			route2[i] = 3;
		}
		for(int i = 20; i < 22; i++) {
			route2[i] = 4;
		}
		for(int i = 0; i < 7; i++) {
			route3[i] = 1;
		}
		for(int i = 7; i < 9; i++) {
			route3[i] = 2;
		}
		for(int i = 9; i < 10; i++) {
			route3[i] = 3;
		}
		for(int i = 10; i < 12; i++) {
			route3[i] = 2;
		}
		for(int i = 12; i < 14; i++) {
			route3[i] = 3;
		}
		for(int i = 14; i < 16; i++) {
			route3[i] = 4;
		}
		for(int i = 16; i < 20; i++) {
			route3[i] = 3;
		}
		for(int i = 20; i < 22; i++) {
			route3[i] = 4;
		}
	}
	
	private void getAtuationArea() {
		switch(atuationArea) {
		case 1:
			ax = 3;
			ay = 1;
			break;
		case 2:
			ax = 12;
			ay = 1;
			break;
		case 3:
			ax = 3;
			ay = 14;
			break;
		case 4:
			ax = 12;
			ay = 14;
			break;
		}
		
	}
	
	private void verifyActive() {
		if(getDistancy((int)this.x, (int)this.y, Game.player.getX(), Game.player.getY()) < 150) {
			this.isSeeingPlayer = true;
			
		}
		else if(getDistancy((int)this.x, (int)this.y, Game.player.getX(), Game.player.getY()) > 200){
			this.isSeeingPlayer = false;
			this.restTime = true;
			restFrames = 0;
		}
	}
	
	public int getDistancy(int x1, int y1, int x2, int y2) {
		int dx = x1 - x2;
		int dy = y1 - y2;
		int distancy = (int)Math.sqrt(dy*dy + dx*dx);
		return distancy;
	}
	
	public void render(Graphics g) {
		if(Player.powerMode) {
			g.drawImage(enemyImgPowerMode[curAnimation],this.getX(),this.getY(),null);
			return;
		}
		g.drawImage(enemyImg[curAnimation],this.getX(),this.getY(),null);
		
	}
	
	
}
