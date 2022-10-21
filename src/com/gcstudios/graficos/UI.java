package com.gcstudios.graficos;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.gcstudios.entities.Player;
import com.gcstudios.main.Game;

public class UI {
	private int framesGameOver = 0, framesVictory = 0;
	private boolean showMessageGameOver = true, showMessageVictory = true;
	
	public static BufferedImage HEART = Game.spritesheet.getSprite(32, 48, 16, 16);
	private boolean firstRenderWin = false;
	
	public void render(Graphics g){
		g.setColor(new Color(8, 8, 40));
		g.fillRect(0, 0, Game.WIDTH*Game.SCALE, 16*Game.SCALE);
		for(int i = 0; i < Game.player.life; i++) {
		g.drawImage(HEART, 67 + (16*Game.SCALE)*i, 1, 32, 32, null);
		}
		if(Game.gameState != "NORMAL") {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(0, 0, 80, 150));
			g.fillRect(0, 16*Game.SCALE, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
		
		if(Game.gameState == "GAME_OVER") {
		
		g.setFont(new Font("arial", Font.BOLD, 30));
		g.setColor(Color.white);
		framesGameOver++;
		if(framesGameOver == 30) {
			framesGameOver = 0;
			if(showMessageGameOver) {
				showMessageGameOver = false;
			}else {
				showMessageGameOver = true;
			}
		}
		if(showMessageGameOver) {
		g.drawString(">Pressione ENTER para reiniciar<", 61, (Game.HEIGHT*(Game.SCALE/2))+20);
		}
		}
		else if(Game.gameState == "WON") {
			if(firstRenderWin) {
				Player.score = (int)(5000/((Game.minGame*60) + Game.secGame)) + Player.score;
				firstRenderWin = false;
				}
			
			Image icon = null;
			icon = new ImageIcon(getClass().getResource("/gif.gif")).getImage();
			g.drawImage(icon, 63, 150, null);
			
			g.setFont(new Font("arial", Font.BOLD, 50));
			g.setColor(Color.white);
			g.drawString("Você venceu!", 140, 330);
			g.drawString("Pontuação: " + Player.score, 105, 400);
			g.setFont(new Font("arial", Font.BOLD, 30));
			framesVictory++;
			if(framesVictory == 30) {
				framesVictory = 0;
				if(showMessageVictory) {
					showMessageVictory = false;
				}else {
					showMessageVictory = true;
				}
			}
			if(showMessageVictory) {
			g.drawString(">Pressione ENTER para reiniciar<", 61, (Game.HEIGHT*(Game.SCALE/2))+120);
			}
			
		}
		}
	}
	
}
