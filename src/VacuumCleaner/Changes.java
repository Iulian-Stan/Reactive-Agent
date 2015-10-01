package VacuumCleaner;

import Enums.Direction;
import Enums.CellContent;

public class Changes {
	public int previousX;
	public int previousY;
	public int currentX;
	public int currentY;
	public CellContent cellContent;
	public Direction direction;

	public Changes()
	{
		previousX = 0;
		previousY = 0;
		currentX = 0;
		currentY = 0;
		cellContent = CellContent.Free;
		direction = Direction.East;
	}
	
	public void UpdatePrevious (CellContent content)
	{
		cellContent = content;
		previousX = currentX;
		previousY = currentY;
	}
}
