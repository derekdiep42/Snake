import javax.swing.*;
import java.awt.*;
import javax.swing.Timer;
import java.awt.event.*;
import java.util.Random;
import java.io.*;

public class Snake extends JFrame implements KeyListener
{

	boolean left = false, right = false, up = false, down = false, pause = true, canPress = true;
	int xPos = 158, yPos = 301, coinXPos = 338, coinYPos = 301, blockNum = 4, coinCounter = 0;

	int blockXPos[] = new int [256]; //Block x position
	int blockYPos[] = new int [256]; //Block y position

	Container frame; //Create frame for graphics

	//Key typed method
	public void keyTyped (KeyEvent e)
	{

	}

	public void keyPressed (KeyEvent e)
	{
		if (e.getKeyCode () == KeyEvent.VK_LEFT && right == false && canPress == true && pause == false) //If left arrow is clicked, make "left" true
		{
			pause = false;
			left = true;
			right = false;
			down = false;
			up = false;
			canPress = false;
		}
		else if (e.getKeyCode () == KeyEvent.VK_RIGHT && left == false && canPress == true) //If right arrow is clicked, make "right" true
		{
			pause = false;
			right = true;
			left = false;
			down = false;
			up = false;
			canPress = false;
		}
		else if (e.getKeyCode () == KeyEvent.VK_UP && down == false && canPress == true && pause == false) //If up arrow is clicked, make "up" true
		{
			pause = false;
			up = true;
			down = false;
			left = false;
			right = false;
			canPress = false;
		}
		else if (e.getKeyCode () == KeyEvent.VK_DOWN && up == false && canPress == true && pause == false) //If down arrow is clicked, make "down" true
		{
			pause = false;
			down = true;
			up = false;
			left = false;
			right = false;
			canPress = false;
		}
		/*
		else if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			if (pause == true)
			{
				pause = false;
			}
			else if (pause == false)
			{
				pause = true;
			}
		}
		 */
	}

	public void keyReleased (KeyEvent e)
	{

	}

	public Snake ()
	{
		super ("Snake");       // Set the frame's name
		setSize (495, 588);     // Set the frame's size
		frame = getContentPane ();
		addKeyListener (this);
		setResizable (false);

		blockXPos [0] = 158;
		blockXPos [1] = 128;
		blockXPos [2] = 98;
		blockXPos [3] = 68;
		blockYPos [0] = 301;
		blockYPos [1] = 301;
		blockYPos [2] = 301;
		blockYPos [3] = 301;

		Timer timer = new Timer (105, new ActionListener ()  //Declare and activate a timer (runs every 40 milliseconds)
				{
			public void actionPerformed (ActionEvent evt)
			{
				movementMethod (); //The timer runs the movement method, which runs everything else
			}
				}
				);
		timer.start ();

		show ();        // Show the frame
	} // Constructor

	public void paint (Graphics g)
	{
		// Draw background
		g.setColor(Color.BLACK);
		g.fillRect (0, 0, 495, 518);

		g.setColor (Color.GREEN);
		// Draw snake body
		for (int i = 0; i < blockNum; i++)
		{
			g.fillRect(blockXPos[i], blockYPos[i], 30, 30);
		}

		// Draw snake head
		g.fillRect (xPos, yPos, 30, 30); 

		// Draw coin
		g.setColor(Color.YELLOW);
		g.fillRect(coinXPos, coinYPos, 30, 30);

		// Draw menu bar
		g.setColor(Color.BLUE);
		g.fillRect(0, 511, 495, 8);

		g.setColor(Color.BLACK);
		g.fillRect(0, 523, 495, 65);

		Font font = new Font ("Helvetica BOLD", 1, 20);
		g.setColor(Color.WHITE);
		g.setFont(font);

		g.drawString("Coins: " + coinCounter, 40, 555);

	}

	public static void main(String[] args) 
	{
		new Snake ();
	}

	public void movementMethod ()
	{	
		if (pause == false)
		{
			if (right == true || left == true || up == true || down == true)
			{
				behindBlocks();	
			}

			if (right == true && xPos < 458) //Moves block right
			{
				xPos += 30;
				canPress = true;
			}
			else if (right == true)
			{
				pause = true;
				JOptionPane.showMessageDialog(null, "YOU LOSE. Try again! \nScore: " + coinCounter);
				System.exit(0);
			}
			if (left == true && xPos > 8) //Moves block left
			{
				xPos -= 30;
				canPress = true;
			}
			else if (left == true)
			{
				pause = true;
				JOptionPane.showMessageDialog(null, "YOU LOSE. Try again! \nScore: " + coinCounter);
				System.exit(0);
			}

			if (down == true && yPos < 481) //Moves block right
			{
				yPos += 30;
				canPress = true;
			}
			else if (down == true)
			{
				pause = true;
				JOptionPane.showMessageDialog(null, "YOU LOSE. Try again! \nScore: " + coinCounter);
				System.exit(0);
			}

			if (up == true && yPos > 31) //Moves block left
			{
				yPos -= 30;
				canPress = true;
			}
			else if (up == true)
			{
				pause = true;
				JOptionPane.showMessageDialog(null, "YOU LOSE. Try again! \nScore: " + coinCounter);
				System.exit(0);
			}
			collision();
		}


		repaint ();
	}

	public void behindBlocks ()
	{
		blockXPos[0] = xPos;
		blockYPos[0] = yPos;
		for (int i = blockNum - 1; i > 0; i--)
		{
			blockXPos[i] = blockXPos[i-1];
			blockYPos[i] = blockYPos[i-1];
		}
	}

	public void collision ()
	{
		// Self collision
		for (int i = 1; i < blockNum; i++)
		{
			if(xPos == blockXPos[i] && yPos == blockYPos[i])
			{
				pause = true;
				JOptionPane.showMessageDialog(null, "YOU LOSE. Try again! \nScore: " + coinCounter);
				System.exit(0);
			}
		}

		// Coin collision
		if (xPos == coinXPos && yPos == coinYPos)
		{
			blockNum++;
			coinCounter++;
			makeFood();
		}
	}

	public void makeFood()
	{
		Random random = new Random();
		coinXPos = 30 * random.nextInt(16) + 8;
		coinYPos = 30 * random.nextInt(16) + 31;	

		boolean diff = true;
		do
		{
			diff = true;
			for (int i = 1; i < blockNum; i++)
			{
				if(coinXPos == blockXPos[i] && coinYPos == blockYPos[i])
				{
					diff = false;
					coinXPos = 30 * random.nextInt(16) + 8;
					coinYPos = 30 * random.nextInt(16) + 31;
					break;
				}
			}
		} 
		while(diff == false);
	}


}
