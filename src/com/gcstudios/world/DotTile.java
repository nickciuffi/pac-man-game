package com.gcstudios.world;

import java.awt.image.BufferedImage;

import com.gcstudios.entities.Entity;

public class DotTile extends Entity{

	public DotTile(int x, int y, BufferedImage sprite) {
		
		super(x, y,16, 16, 0, sprite);
		depth = 0;
		// TODO Auto-generated constructor stub
	}

}
