import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Cursor;
import java.awt.Point;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.MouseInfo;
import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import sun.audio.*;

public class Game extends JPanel implements ActionListener
{
    private int width = 1200;
    private int height = 900;
    private int delay = 50;
    private int count = 0;
    private int gaugeX = 150;
    private int gaugeY = 800;
    private int gaugeTipX = 0;
    private int gaugeTipY = 0;
    private int playerDirectionY = 0;
    private int pause = 1;
    private int temp = 0;
    private int pedal = 0;
    private int gear = 1;
    private int xLoc=20;
    private int y=450;
    private final int gearBox = 7;//number of gears in trans
    private final int horsePower = 300;//hp
    private final int mass = 1500;//kg
    private final int maxPowerRPM = 6500;//rpm where most power is made
    private double speed = .0001;
    private double accel = .0001;
    private double throttle = .0001;
    private double rpm =0;
    private double power =0;
    private double ratio = 0.0;
    private boolean start = false;
    private boolean inGame = true;


    private Timer timer;
	//InputStream in;
	//AudioStream as;


    public Game()
    {
        addKeyListener(new KeyListener());
        setBackground(Color.black);
        setFocusable(true);
        //hideMouse();

        setPreferredSize(new Dimension(width, height));
        loadImages();
        //loadSounds("sound.wav");
        initGame();
        setDoubleBuffered(true);
    }

    private void loadImages()
    {
        ImageIcon icoPlayer = new ImageIcon("player.png");
	}

    private void initGame()
    {
        timer = new Timer(delay, this);
        timer.start();
    }

    private void reinitGame()
    {
        timer.start();
    }


    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        doDrawing(g);
    }
	//Main method that collects all graphics on screen and puts them in order
    private void doDrawing(Graphics g)
    {
        if(inGame)
        {
            tachometer(g, gaugeX, gaugeY);
            g.drawRect(xLoc,y,20,10);
            stats(g);
        }
    }
    //game over screen
    private void gameOver(Graphics g)
    {
       String msg = "You Lose!";
       Font small = new Font("Verdana", Font.BOLD, 14);
       FontMetrics metr = getFontMetrics(small);

       g.setColor(Color.white);
       g.setFont(small);
    }
	//shows runtime variables
    private void stats(Graphics g)
    {
       String msg = "ratio"+String.format("%.2f", ratio)+"|||rpm"+String.format("%.2f", rpm);
       String msg2 = "power" + String.format("%.2f", power)+"|||X:"+ xLoc +"|||speed:"+String.format("%.2f", (speed)/10);
       String msg3 = "count:" + count;
       Font small = new Font("Verdana", Font.BOLD, 14);
       FontMetrics metr = getFontMetrics(small);

       g.setColor(Color.white);
       g.setFont(small);
       g.drawString(msg, 20,20);
       g.drawString(msg2, 20,40);
       g.drawString(msg3, 20,60);
    }
	//Draws Tachometer
    private void tachometer(Graphics g, int x, int y)
    {
		g.setColor(Color.white);
		g.fillArc(x-100,y-100,200,200,0,180);
		g.setColor(new Color(250,150,150));
		g.fillArc(x-100,y-100,200,200,0,45);
		g.setColor(new Color(250,75,75));
		g.fillArc(x-100,y-100,200,200,0,30);
		if(pedal==1)
		{
			if(throttle<2.0)
				throttle+=((throttle)*1.02)/(gear*gear);
			else
				throttle+=.05;
		}
		else if((throttle>.3)&&(pedal==-1))
			throttle-=.3/(Math.abs(gear)*3);
		else if(throttle<0)
			throttle=.1;
		if(throttle>=2.7)
		{
			throttle = 2.5;
		}
		if(throttle<=.3)
		{
			throttle = .37;
		}
		g.setColor(Color.black);
		g.fillRect(x-10,y-35,20,20);

		rpm = throttle*2800;
       	String msg = "" + gear;
       	if(gear==-1)
       		msg = "R";
     	Font small = new Font("Verdana", Font.BOLD, 14);
       	FontMetrics metr = getFontMetrics(small);

       	g.setColor(Color.white);
       	g.setFont(small);
       	g.drawString(msg, x-5,y-20);

		g.setColor(Color.red);
		gaugeTipX = (-(int)(Math.cos(throttle)*100))+x;
		gaugeTipY = (-(int)(Math.sin(throttle)*100))+y;
		g.drawLine(x,y,gaugeTipX,gaugeTipY);
    }
	//hides mouse
    private void hideMouse()
    {
	        ImageIcon emptyIcon = new ImageIcon(new byte[0]);
	        Cursor invisibleCursor = getToolkit().createCustomCursor(emptyIcon.getImage(), new Point(0,0), "");
	        this.setCursor(invisibleCursor);
    }

    public void actionPerformed(ActionEvent v)
    {
		power=((300*rpm)/7500)*((double)gear/(double)gearBox);
		speed =((0.00595*(rpm*11))/(ratio*3.6));
		if(start)
			xLoc+=(speed)/10;
	    //x = (MouseInfo.getPointerInfo().getLocation().x);
	    //y = (MouseInfo.getPointerInfo().getLocation().y);
        if(gear==1)
        	ratio=2.3;
        else if(gear==2)
        	ratio=1.9;
        else if(gear==3)
        	ratio=1.4;
        else if(gear==4)
        	ratio=1.2;
        else if(gear==5)
        	ratio=1.1;
        else if(gear==6)
        	ratio=0.9;
        else if(gear==7)
        	ratio=0.85;
        else if(gear==-1)
        {
        	ratio=-3.5;
		}
        count++;
        if(count>=24)
        {
			start=true;
        	count=0;
		}
        repaint();
		//loadSounds("soundLowShort.wav");
		//AudioPlayer.player.start(as);
    }

    private class KeyListener extends KeyAdapter
    {
        public void keyPressed(KeyEvent v)
        {
            int key = v.getKeyCode();
            if((key == KeyEvent.VK_W))
            {
				pedal = 1;
			}
            if((key == KeyEvent.VK_S))
            {
				pedal = -1;
			}
            if((key == KeyEvent.VK_Q))
            {
				if(gear<gearBox)
				{
					if(gear==-1)
					{
						gear=1;
						rpm=50;
						throttle = .02;
					}
					else
						gear++;
					if(rpm>3000)
					{
						if(gear==gearBox)
							throttle-=((10+gear)/10);
						else
							throttle-=(gearBox+gear)/gearBox;
					}
				}
			}
            if((key == KeyEvent.VK_SPACE))
            {
				timer.stop();
			}
            if((key == KeyEvent.VK_F))
            {
				timer.start();
			}
		}
        public void keyReleased(KeyEvent v)
        {
            int key = v.getKeyCode();
            if((key == KeyEvent.VK_W))
            {
				pedal = -1;
			}
            if((key == KeyEvent.VK_S))
            {
				pedal = -1;
			}
            if((key == KeyEvent.VK_A))
            {
				if(gear==1)
				{
					gear=-1;
					rpm=50;
					throttle = .02;
				}
				if(gear>1)
					gear--;
				if(rpm>3000)
					throttle+=(gearBox+gear)/gearBox;
			}
		}
    }
}