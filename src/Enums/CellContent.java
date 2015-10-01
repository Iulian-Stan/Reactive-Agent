package Enums;
public enum CellContent {
	Free(0), 
	Obstacle(1), 
	Zombie(2), 
	Blood(3), 
	TankNorth(4), 
	TankWest(5), 
	TankSouth(6), 
	TankEast(7);

	private final int _index; 

	private CellContent (int index)
	{
		_index = index;
	}

	public int getIndex()
	{
		return _index;
	}
};