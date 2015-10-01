package UI;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.EventQueue;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import VacuumCleaner.Action;


@SuppressWarnings("serial")
public class Core extends JFrame implements Observer{

	private final Action _act = new Action();
	private final Sizes _cont = new Sizes(10, 10, 10, 100);

	private ControlPanel _control;
	private EnvironmentPanel _environement;

	public Core ()
	{
		_act.addObserver(this);
		_cont.addObserver(this);

		_environement = new EnvironmentPanel(_cont.getSize(), _cont.getObstaclesNumber(), _cont.getZombiesNumber(), _act, _cont.getWaitInterval());
		_control = new ControlPanel(_cont);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Container pane = getContentPane();
		pane.add(_control, BorderLayout.WEST);
		pane.add(new JSeparator(SwingConstants.VERTICAL), BorderLayout.CENTER);
		pane.add(_environement, BorderLayout.EAST);
		pack();
		setVisible(true);
	}


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Core();
			}
		});
	}


	@Override
	public void update(Observable observale, Object object) {
		if (observale == _cont)
		{
			switch (_cont.flag)
			{
			case -1 :
				_environement.setAgentWaitInterval(_cont.getWaitInterval());
				break;
			case 0 :
				remove(_environement);
				_environement.StopAgent();
				_environement = new EnvironmentPanel(_cont.getSize(), _cont.getObstaclesNumber(), _cont.getZombiesNumber(), _act, _cont.getWaitInterval());
				add(_environement);
				pack();
				break;
			case 1 :
				_environement.StartAgent();
				break;
			case 2 :
				_environement.InterruptAgent();
				break;
			}
		}
		else
		{
			if (_act.getCost() == 100)
				_cont.ZombieKilled();
			_control.UpdateScore(_act.getCost());
			if (_cont.getZombiesNumber() == 0 && _environement.isAgentInInitialPosition())
				_environement.InterruptAgent();
		}
	}
}
