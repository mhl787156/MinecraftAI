package edu.uwa.aidan.robot.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.neuroph.core.NeuralNetwork;

import edu.uwa.aidan.robot.nnet.RobotFitnessFunction;
import edu.uwa.aidan.robot.world.AgentWorld;

/**
 * A simple <code>JPanel</code> which allows the user to view a <code>AgentWorld</code>
 * and provides a run button to watch the <code>Robot</code>s performance. 
 * 
 * @author Aidan Morgan
 */
public class ReplayRobotPanel extends JPanel {
	public static final long MILISECONDS_PER_REPAINT = 100;

	private AgentWorld world;
	private NeuralNetwork network;
	private WorldPanel worldPanel;
	private JButton runButton;

	public ReplayRobotPanel(AgentWorld createWorld,
			NeuralNetwork createNeuralNetwork) {
		this.world = createWorld;
		this.network = createNeuralNetwork;
		
		initComponents();
		addComponents();
		addListeners();
	}

	private void initComponents() {
		worldPanel = new WorldPanel(world, network);
		runButton = new JButton("Run");
	}

	private void addComponents() {
		setLayout(new BorderLayout());
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(runButton);
		
		add(buttonPanel, BorderLayout.NORTH);
		add(worldPanel, BorderLayout.CENTER);
	}

	private void addListeners() {
		runButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				timer = new Timer();
				world.reset();
				
				timer.scheduleAtFixedRate(
						new RepaintTimerTask(), 
						MILISECONDS_PER_REPAINT, 
						MILISECONDS_PER_REPAINT);
			}
		});
	}

	private Timer timer;
	private long stepNum = 0;
	
	private void step() {
		runButton.setEnabled(false);
		world.step(stepNum);
		stepNum++;
		worldPanel.repaint();
	}

	private void stop() {
		runButton.setEnabled(true);
		worldPanel.repaint();
		timer.cancel();
		stepNum = 0;
	}

	/**
	 * Simple <code>TimerTask</code> that updates the step cound and repaints the
	 * canvas when run.
	 * 
	 * @author Aidan Morgan
	 */
	private class RepaintTimerTask extends TimerTask {
		public RepaintTimerTask() {
		}

		public void run() {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					if (stepNum < RobotFitnessFunction.MAX_STEPS) {
						step();
					} else {
						cancel();
						stop();
					}
				}

			});

		}

	}
	
	
}
