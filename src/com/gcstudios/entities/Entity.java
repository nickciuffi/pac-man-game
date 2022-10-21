package com.gcstudios.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import com.gcstudios.main.Game;
import com.gcstudios.world.Node;
import com.gcstudios.world.TeleportTile;
import com.gcstudios.world.Tile;
import com.gcstudios.world.Vector2i;
import com.gcstudios.world.World;

public class Entity {
	

	protected double x;
	protected double y;
	protected int width;
	protected int height;
	protected double speed;
	
	public int depth;

	protected List<Node> path;
	
	public boolean debug = false;
	
	private BufferedImage sprite;
	
	public static Random rand = new Random();
	
	public int animationFrames = 0, animationFramesMax = 5, curAnimation = 0, animationMax = 2;
	
	
	public Entity(double x,double y,int width,int height,double speed,BufferedImage sprite){
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
	}
	
	public static Comparator<Entity> nodeSorter = new Comparator<Entity>() {
		
		@Override
		public int compare(Entity n0,Entity n1) {
			if(n1.depth < n0.depth)
				return +1;
			if(n1.depth > n0.depth)
				return -1;
			return 0;
		}
		
	};
	
	
	
	public void setX(int newX) {
		this.x = newX;
	}
	
	public void setY(int newY) {
		this.y = newY;
	}
	
	public int getX() {
		return (int)this.x;
	}
	
	public int getY() {
		return (int)this.y;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public void tick(){}
	
	public double calculateDistance(int x1,int y1,int x2,int y2) {
		return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}
	
	
	public void followPath(List<Node> path) {
		if(path != null) {
			if(path.size() > 0) {
				Vector2i target = path.get(path.size() - 1).tile;
				//xprev = x;
				//yprev = y;
				if(x < target.x * 16) {
					x += speed;
				}else if(x > target.x * 16) {
					x -= speed;
				}
				
				if(y < target.y * 16) {
					y+=speed;
				}else if(y > target.y * 16) {
					y-=speed;
				}
				
				if(x == target.x * 16 && y == target.y * 16) {
					path.remove(path.size() - 1);
				}
				
			}
		}
	}
	
	public static boolean isColliding(Entity e1,Entity e2){
		Rectangle e1Mask = new Rectangle(e1.getX(),e1.getY(),e1.getWidth(),e1.getHeight());
		Rectangle e2Mask = new Rectangle(e2.getX(),e2.getY(),e2.getWidth(),e2.getHeight());
		
		return e1Mask.intersects(e2Mask);
	}
	
	public void detectTeleport() {
		for(int i = 0; i < World.tiles.length; i++) {
			Tile atual = World.tiles[i];
			
				if(isCollisionTileEntity(atual, this)){
					if(atual instanceof TeleportTile) {
						Teleport(atual.x);
					}
				}
			}
		
	}

	public boolean isCollisionTileEntity(Tile t, Entity e) {
		Rectangle tile = new Rectangle(t.x, t.y, 16, 16);
		Rectangle entitie = new Rectangle((int)e.x, (int)e.y, 16, 16);
		if(tile.intersects(entitie)) {
			return true;
		}
		return false;
	}
	
	protected void Teleport(int x) {
		System.out.println(x);
		
		if(x==0) {
			this.setX((World.WIDTH*16)-32);
		}
		else {
			
			this.setX(16);
		}
	}
	
	public void render(Graphics g) {
		g.drawImage(sprite,this.getX(),this.getY(),null);
		//g.setColor(Color.red);
		//g.fillRect(this.getX() + maskx - Camera.x,this.getY() + masky - Camera.y,mwidth,mheight);
	}
	
}
