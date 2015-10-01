package VacuumCleaner;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import Enums.Direction;
import Enums.CellContent;
import UI.EnvironmentPanel;

public class Agent implements Runnable 
{
	public boolean life = true;
	public boolean running = false;

	private int _waitInterval;
	private Changes _chang;
	private EnvironmentPanel _listener;

	private int MoveForward()
	{
		switch (_chang.direction)
		{
		case North:
			--_chang.currentX;
			break;
		case West:
			--_chang.currentY;
			break;
		case South:
			++_chang.currentX;
			break;
		case East:
			++_chang.currentY;
			break;
		}
		return -1;
	}

	private CellContent LookForward()
	{
		switch (_chang.direction)
		{
		case North:
			return _listener.getCellContent(_chang.currentX - 1, _chang.currentY);
		case West:
			return _listener.getCellContent(_chang.currentX, _chang.currentY - 1);
		case South:
			return _listener.getCellContent(_chang.currentX + 1, _chang.currentY);
		default:
			return _listener.getCellContent(_chang.currentX, _chang.currentY + 1);
		}
	}

	private CellContent LookLeft()
	{
		switch (_chang.direction)
		{
		case North:
			return _listener.getCellContent(_chang.currentX, _chang.currentY - 1);
		case West:
			return _listener.getCellContent(_chang.currentX + 1, _chang.currentY);
		case South:
			return _listener.getCellContent(_chang.currentX , _chang.currentY + 1);
		default:
			return _listener.getCellContent(_chang.currentX - 1, _chang.currentY);
		}
	}

	@SuppressWarnings("unused")
	private CellContent LookRight()
	{
		switch (_chang.direction)
		{
		case North:
			return _listener.getCellContent(_chang.currentX, _chang.currentY + 1);
		case West:
			return _listener.getCellContent(_chang.currentX - 1, _chang.currentY);
		case South:
			return _listener.getCellContent(_chang.currentX , _chang.currentY - 1);
		default:
			return _listener.getCellContent(_chang.currentX + 1, _chang.currentY);
		}
	}

	private int TurnLeft()
	{
		_chang.direction = Direction.values()[(_chang.direction.getIndex() + 3) % 4];
		return -1;
	}

	private int TurnRight()
	{
		_chang.direction = Direction.values()[(_chang.direction.getIndex() + 1) % 4];
		return -1;
	}

	private int Kill()
	{
		playSquash();
		_chang.cellContent = CellContent.Blood;
		return 100;
	}

	public Agent (EnvironmentPanel listener)
	{
		_chang = new Changes();
		_listener = listener;
	}

	public void setWaitInterval(int delay)
	{
		_waitInterval = delay;
	}

	public void PerformAction()
	{
		int price;
		if (_listener.getCellContent(_chang.currentX, _chang.currentY) == CellContent.Zombie)
			price = Kill();
		else
		{
			Random rand = new Random();
			switch (rand.nextInt(3))
			{
			case 0:
				if (LookForward() != CellContent.Obstacle)
				{
					price = MoveForward();
					break;
				}
			case 1:
				if (LookLeft() != CellContent.Obstacle)
				{
					price = TurnLeft();
					break;
				}
			default:
				price = TurnRight();
				break;
			}
		}
		_listener.actionPerformed(new ActionEvent(_chang, price, "action"));
	}

	public void Stop()
	{
		running = false;
	}

	synchronized public void Run()
	{ 
		running = true;
		notify();
	}

	public void Finish()
	{ 
		life = false;
	}

	@Override
	public void run() {
		while(life) 
		{ 
			try 
			{ 
				synchronized(this) 
				{ 
					if(!running) 
						wait() ;
				} 
				Thread.sleep(_waitInterval);
				PerformAction();
			} 
			catch(InterruptedException e){} 
		}
	}

	public boolean isInInitialPosition()
	{
		return _chang.currentX == 0 && _chang.currentY == 0;
	}

	public static synchronized void playSquash() {
		new Thread(new Runnable() {
			public void run() {
				try {
					Clip clip = AudioSystem.getClip();
					AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File("resources/squash.wav"));
					clip.open(inputStream);
					clip.start(); 
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}).start();
	}
}