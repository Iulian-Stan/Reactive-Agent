package UI;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Enums.CellContent;
import VacuumCleaner.Action;
import VacuumCleaner.Agent;
import VacuumCleaner.Changes;

@SuppressWarnings("serial")
public class EnvironmentPanel extends JPanel implements ActionListener {

	private int N;

	private CellContent[][] _map;
	private List<JLabel> _list = new ArrayList<JLabel>();

	private ImageIcon _zombieIcon = new ImageIcon("resources/Zombie64.jpg");
	private ImageIcon _bloodIcon = new ImageIcon("resources/Blood64.jpg");
	private ImageIcon _freeIcon = new ImageIcon("resources/Free64.jpg");
	private ImageIcon _tankNorthIcon = new ImageIcon("resources/TankN64.jpg");
	private ImageIcon _tankWestIcon = new ImageIcon("resources/TankW64.jpg");
	private ImageIcon _tankSouthIcon = new ImageIcon("resources/TankS64.jpg");
	private ImageIcon _tankEastIcon = new ImageIcon("resources/TankE64.jpg");

	private Agent _agent = new Agent(this);
	private Action _act;

	private boolean hasNoNeighbors(int row, int column)
	{
		return (row - 1 < 0 || 
				(column - 1 < 0 || _map[row-1][column-1] != CellContent.Obstacle) &&
				_map[row-1][column] != CellContent.Obstacle &&
				(column + 1 >= N || _map[row-1][column+1] != CellContent.Obstacle)) &&
				(column - 1 < 0 || _map[row][column-1] != CellContent.Obstacle) &&
				(column + 1 >= N || _map[row][column+1] != CellContent.Obstacle) &&
				(row + 1 >= N ||
				(column - 1 < 0 || _map[row+1][column-1] != CellContent.Obstacle) &&
				_map[row+1][column] != CellContent.Obstacle &&
				(column + 1 >= N || _map[row+1][column+1] != CellContent.Obstacle));
	}

	private void GenerateMap(int obstacles, int zombies)
	{		
		Random generator = new Random();
		int poz = 0, row, column, cellNumber = N * N - 2;

		_map = new CellContent[N][N];

		for (row = 0; row < N; ++row)
			for (column = 0; column < N; ++column)
				_map[row][column] = CellContent.Free;

		while (obstacles > 0)
		{
			poz = (poz + generator.nextInt(cellNumber)) % cellNumber + 1;
			row = poz / N;
			column = poz % N;
			if (_map[row][column] == CellContent.Free && hasNoNeighbors(row, column))
			{
				_map[row][column] = CellContent.Obstacle;
				--obstacles;
			}
		}

		while (zombies > 0)
		{
			poz = (poz + generator.nextInt(cellNumber)) % cellNumber + 1;
			row = poz / N;
			column = poz % N;
			if (_map[row][column] == CellContent.Free)
			{
				_map[row][column] = CellContent.Zombie;
				--zombies;
			}
		}

		_map[0][0] = CellContent.TankEast;
	}

	public EnvironmentPanel(int size, int obstacles, int zombies, Action act, int wait) {
		int row, column;

		N = size;
		_act = act;

		this.setLayout(new GridLayout(N, N));

		GenerateMap(obstacles, zombies);

		for (row = 0; row < N; ++row) {
			for (column = 0; column < N; column++) {
				switch (_map[row][column])
				{
				case Free:
					_list.add(new JLabel(_freeIcon));
					break;
				case Zombie:
					_list.add(new JLabel(_zombieIcon));
					break;
				default :
					_list.add(new JLabel());
					break;
				}
			}
		}

		_list.get(0).setIcon(_tankEastIcon);
		for (JLabel label : _list) 
			add(label);
		validate();
		setAgentWaitInterval(wait);
		(new Thread(_agent)).start();
	}

	public CellContent getCellContent(int row, int column)
	{
		if (row < 0 || row >= N || column < 0 || column >= N)
			return CellContent.Obstacle;
		return _map[row][column];
	}

	public void StartAgent()
	{
		_agent.Run();
	}

	public void InterruptAgent()
	{		
		_agent.Stop();
	}

	public void StopAgent()
	{
		_agent.Finish();
	}

	public void setAgentWaitInterval(int interval)
	{
		_agent.setWaitInterval(interval);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		createPane(e.getID(), (Changes) e.getSource());
	}

	private void createPane(int cost, Changes changes) {
		_map[changes.previousX][changes.previousY] = changes.cellContent;
		JLabel label = (JLabel)getComponent(changes.previousX * N + changes.previousY);
		switch (changes.cellContent)
		{
		case Blood:
			label.setIcon(_bloodIcon);
			break;
		case Zombie:
			label.setIcon(_zombieIcon);
			break;
		default:
			label.setIcon(_freeIcon);
		}
		changes.UpdatePrevious(_map[changes.currentX][changes.currentY]);
		label = (JLabel)getComponent(changes.currentX * N + changes.currentY);
		switch (changes.direction)
		{
		case North:
			label.setIcon(_tankNorthIcon);
			break;
		case West:
			label.setIcon(_tankWestIcon);
			break;
		case South:
			label.setIcon(_tankSouthIcon);
			break;
		default:
			label.setIcon(_tankEastIcon);
		}
		_act.updateCost(cost);
	}

	public boolean isAgentInInitialPosition()
	{
		return _agent.isInInitialPosition();
	}
}