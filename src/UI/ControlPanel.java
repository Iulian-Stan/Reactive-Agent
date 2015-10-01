package UI;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


@SuppressWarnings("serial")
public class ControlPanel extends JPanel
{
	private JPanel _controls;
	private JLabel _waitLabel;
	private SpinnerNumberModel _waitSpin;
	private JSpinner _waitSpinner;
	private JLabel _sizeLabel;
	private SpinnerNumberModel _sizeSpin;
	private JSpinner _sizeSpinner;
	private JLabel _zombiesLabel;
	private SpinnerNumberModel _zombieSpin;
	private JSpinner _zombieSpinner;
	private JLabel _obstaclesLabel;
	private SpinnerNumberModel _obstaclesSpin;
	private JSpinner _obstaclesSpinner;
	private JButton _generate;
	private JButton _start;
	private JButton _stop;
	private JLabel _scoreLabel;
	private JLabel _scoreValueLabel;

	public ControlPanel(final Sizes cont)
	{		
		int n = cont.getSize();

		_controls = new JPanel();	
		_controls.setLayout(new GridLayout(13, 1));

		_sizeLabel = new JLabel("Size :");

		_sizeSpin = new SpinnerNumberModel(n, 5, 15, 1);
		_sizeSpinner = new JSpinner(_sizeSpin);
		_sizeSpinner.addChangeListener(new ChangeListener() {			
			@Override
			public void stateChanged(ChangeEvent arg) {
				_zombieSpinner.setValue(1);
				_obstaclesSpin.setValue(1);
			}
		});
		
		_waitLabel = new JLabel("Waint interval :");

		_waitSpin = new SpinnerNumberModel(cont.getWaitInterval(), 0, 1000, 10);
		_waitSpinner = new JSpinner(_waitSpin);
		_waitSpinner.addChangeListener(new ChangeListener() {			
			@Override
			public void stateChanged(ChangeEvent arg0) {
				cont.updateInterval(((Integer)_waitSpinner.getValue()).intValue());
			}
		});

		_zombiesLabel = new JLabel("Number of zombies :");

		_zombieSpin = new SpinnerNumberModel(cont.getZombiesNumber(), 1, n * n / 2, 1);
		_zombieSpinner = new JSpinner(_zombieSpin);

		_obstaclesLabel = new JLabel("Number of obstacles :");

		_obstaclesSpin = new SpinnerNumberModel(cont.getObstaclesNumber(), 1, 2 * n, 1);
		_obstaclesSpinner = new JSpinner(_obstaclesSpin);

		_generate = new JButton("Generate Map");
		_generate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cont.updateValues(((Integer)_sizeSpinner.getValue()).intValue(),
						((Integer)_zombieSpinner.getValue()).intValue(), 
						((Integer)_obstaclesSpinner.getValue()).intValue());
				_scoreValueLabel.setText("0");
			}
		});

		_start = new JButton("Start");
		_start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cont.updateFlag(1);
			}
		});

		_stop = new JButton("Stop");
		_stop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cont.updateFlag(2);
			}
		});

		_scoreLabel = new JLabel("SCORE : ");
		_scoreValueLabel = new JLabel("0");

		_controls.add(_waitLabel);
		_controls.add(_waitSpinner);
		_controls.add(_sizeLabel);
		_controls.add(_sizeSpinner);
		_controls.add(_zombiesLabel);
		_controls.add(_zombieSpinner);
		_controls.add(_obstaclesLabel);
		_controls.add(_obstaclesSpinner);
		_controls.add(_generate);
		_controls.add(_start);
		_controls.add(_stop);
		_controls.add(_scoreLabel);
		_controls.add(_scoreValueLabel);

		add(_controls, BorderLayout.NORTH);
	}
	
	public void UpdateScore(int value)
	{
		_scoreValueLabel.setText("" + (Integer.parseInt(_scoreValueLabel.getText()) + value));
	}

}
