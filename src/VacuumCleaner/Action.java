package VacuumCleaner;

import java.util.Observable;

public class Action extends Observable{
	private int _cost = 0;

	public int getCost()
	{
		return _cost;
	}

	public void updateCost(int cost)
	{
		_cost = cost;
		setChanged();
		notifyObservers();
	}
}
