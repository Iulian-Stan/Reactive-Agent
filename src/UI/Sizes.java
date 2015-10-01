package UI;

import java.util.Observable;

public class Sizes extends Observable {
	public int flag = 0;

	private int _size;
	private int _zombiesNumber;
	private int _obstaclesNumber;
	private int _waitInterval;

	public Sizes(int size, int zombiesNumber, int obstaclesNumber, int waitInterval)
	{
		super();
		_size = size;
		_zombiesNumber = zombiesNumber;
		_obstaclesNumber = obstaclesNumber;
		_waitInterval = waitInterval;
	}

	public int getSize()
	{
		return _size;
	}

	public void ZombieKilled()
	{
		--_zombiesNumber;
	}

	public int getZombiesNumber()
	{
		return _zombiesNumber;
	}

	public int getObstaclesNumber()
	{
		return _obstaclesNumber;
	}

	public int getWaitInterval()
	{
		return _waitInterval;
	}

	public void updateValues(int size, int zombiesNumber, int obstaclesNumber)
	{
		flag = 0;
		_size = size;
		_zombiesNumber = zombiesNumber;
		_obstaclesNumber = obstaclesNumber;
		setChanged();
		notifyObservers();
	}

	public void updateInterval(int waitInterval)
	{
		flag = -1;
		_waitInterval = waitInterval;
		setChanged();
		notifyObservers();
	}


	public void updateFlag(int newFlag)
	{
		flag = newFlag;
		setChanged();
		notifyObservers();
	}
}
