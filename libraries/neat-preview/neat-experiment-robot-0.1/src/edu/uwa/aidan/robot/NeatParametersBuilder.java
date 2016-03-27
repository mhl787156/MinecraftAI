package edu.uwa.aidan.robot;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.neuroph.contrib.neat.gen.NeatParameters;
import org.neuroph.contrib.neat.gen.NeuronGene;
import org.neuroph.contrib.neat.gen.NeuronType;
import org.neuroph.contrib.neat.gen.impl.SimpleNeatParameters;
import org.neuroph.contrib.neat.gen.operations.MutationOperation;
import org.neuroph.contrib.neat.gen.operations.ReproductionOperation;
import org.neuroph.contrib.neat.gen.operations.mutation.AddConnectionMutationOperation;
import org.neuroph.contrib.neat.gen.operations.mutation.AddNeuronMutationOperation;
import org.neuroph.contrib.neat.gen.operations.mutation.WeightMutationOperation;
import org.neuroph.contrib.neat.gen.operations.reproduction.AbstractReproductionOperation;
import org.neuroph.contrib.neat.gen.operations.selector.NaturalSelectionOrganismSelector;
import org.neuroph.contrib.neat.gen.operations.speciator.DynamicThresholdSpeciator;
import org.neuroph.contrib.neat.gen.persistence.impl.DirectoryOutputPersistence;
import org.neuroph.contrib.neat.gen.persistence.impl.serialize.JavaSerializationDelegate;

import edu.uwa.aidan.robot.nnet.RobotFitnessFunction;
import edu.uwa.aidan.robot.world.AgentWorld;
import edu.uwa.aidan.robot.world.WorldBuilder;
import edu.uwa.aidan.robot.world.xml.XmlWorldBuilder;

public class NeatParametersBuilder extends JPanel {
	public static final String BASE_DIRECTORY = "robot-output";

	/*
	 * I KNOW THIS IS HIDEOUS CODE.
	 * 
	 * THE POINT OF THIS IS TO GET THE USER TO BE ABLE TO TRAIN OR REPLAY AN ORGANISM
	 * AS QUICKLY AS POSSIBLE.
	 * 
	 * IDEALLY THIS FUNCTIONALITY WILL BE MADE AVAILABLE TO EASYNEURONS VERY SOON, SO THIS
	 * CODE CAN DISAPPEAR WITHOUT MUCH CONCERN
	 * 
	 * THIS IS A TEMPORARY MEASURE UNTIL A BETTER APPROACH IS IMPLEMENTED.
	 */
	public static NeatParameters neatParameters;
	public static long organismId;
	
	public static void getReplayParams() {
		NeatParametersBuilder paramsPan = new NeatParametersBuilder(true);
		show(paramsPan);

		neatParameters = paramsPan.createNeatParameters();
		organismId = paramsPan.getOrganismId();
	}
	
	public static void getTrainParams() {
		NeatParametersBuilder paramsPan = new NeatParametersBuilder(false);
		show(paramsPan);
		neatParameters = paramsPan.createNeatParameters();
	}

	private static void show(NeatParametersBuilder b) {
		JOptionPane.showMessageDialog(null, b);
	}

	private JTextField worldFile;
	private JButton worldChooserButton;
	private JTextField organismIdField;
	private boolean useOrganismId;

	private NeatParametersBuilder(boolean useOrganismId) {
		this.useOrganismId = useOrganismId;
		
		initComponents();
		addComponents();
		addListeners();
	}


	private void initComponents() {
		worldFile = new JTextField(30);
		worldFile.setEditable(false);
		
		worldChooserButton = new JButton("...");
		organismIdField = new JTextField(3);
	}

	private void addComponents() {
		JPanel worldChooserPan = new JPanel(new FlowLayout(FlowLayout.LEFT));
		worldChooserPan.add(new JLabel("World DefinitionFile:"));
		worldChooserPan.add(worldFile);
		worldChooserPan.add(worldChooserButton);
		
		setLayout(new GridLayout(3, 1));
		add(worldChooserPan);
		
		if(useOrganismId) {
			JPanel organismIdPan = new JPanel(new FlowLayout(FlowLayout.LEFT));
			organismIdPan.add(new JLabel("Replay Organism Id:"));
			organismIdPan.add(organismIdField);
			
			add(organismIdPan);
		}
	}
	
	private void addListeners() {
		worldChooserButton.addActionListener(new ActionListener()  {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(new File("."));
				chooser.showOpenDialog(worldChooserButton);
				
				worldFile.setText(chooser.getSelectedFile().getAbsolutePath());
			}
		});
	}

	private long getOrganismId() {
		return Long.valueOf(organismIdField.getText());
	}
	
	
	public NeatParameters createNeatParameters() {
		WorldBuilder builder = new XmlWorldBuilder(new File(worldFile.getText()));
		SimpleNeatParameters params = new SimpleNeatParameters();
		params.setMaximumGenerations(750);
		params.setPopulationSize(250);
		params.setMaximumFitness(builder.getWorstFitnessScore());
		params.setFitnessFunction(new RobotFitnessFunction(builder));

		DynamicThresholdSpeciator speciator = new DynamicThresholdSpeciator();
		speciator.setMaxSpecies(45);
		params.setSpeciator(speciator);

		NaturalSelectionOrganismSelector selector = (NaturalSelectionOrganismSelector) params
				.getOrganismSelector();
		selector.setKillUnproductiveSpecies(true);

		List<MutationOperation> ops = new ArrayList<MutationOperation>();

		AddNeuronMutationOperation addNeuron = new AddNeuronMutationOperation(
				0.5);
		AddConnectionMutationOperation addConnection = new AddConnectionMutationOperation(
				0.10);
		WeightMutationOperation weightMutation = new WeightMutationOperation(
				0.8);

		ops.add(addNeuron);
		ops.add(addConnection);
		ops.add(weightMutation);
		
		params.setMutationOperators(ops);

		for (ReproductionOperation op : params.getReproductionOperators()) {
			((AbstractReproductionOperation) op).setUseFitnessBias(false);
		}

		// if you change this to a different persistence mechanism, make sure to change the value
		// in the replay as well.
		params.setPersistence(new DirectoryOutputPersistence(BASE_DIRECTORY, new JavaSerializationDelegate(false)));

		return params;
	}
}
