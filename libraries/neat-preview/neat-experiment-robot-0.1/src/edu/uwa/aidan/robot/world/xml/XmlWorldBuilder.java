package edu.uwa.aidan.robot.world.xml;

import java.io.File;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import edu.uwa.aidan.robot.world.AgentWorld;
import edu.uwa.aidan.robot.world.Goal;
import edu.uwa.aidan.robot.world.Obstacle;
import edu.uwa.aidan.robot.world.WorldBuilder;

public class XmlWorldBuilder extends WorldBuilder {
	private static final String Y_COORDINATE_ATTRIBUTE_NAME = "y";
	private static final String X_COORDINATE_ATTRIBUTE_NAME = "x";
	private static final String AGENT_WORLD_ELEMENT = "agent-world";

	private static AgentWorld loadAgentWorld(File f) {
		StreamSource ss = new StreamSource(f);
		DOMResult dr = new DOMResult();
		
		try {
			TransformerFactory.newInstance().newTransformer().transform(ss, dr);
		} catch (TransformerConfigurationException e) {
			throw new IllegalStateException("Could not load AgentWorld from " + f.getAbsolutePath() + ".", e);
		} catch (TransformerException e) {
			throw new IllegalStateException("Could not load AgentWorld from " + f.getAbsolutePath() + ".", e);
		} catch (TransformerFactoryConfigurationError e) {
			throw new IllegalStateException("Could not load AgentWorld from " + f.getAbsolutePath() + ".", e);
		}
		
		Element rootElement = ((Document)dr.getNode()).getDocumentElement();
		
		if(!rootElement.getNodeName().equals(AGENT_WORLD_ELEMENT)) {
			throw new IllegalStateException("Invalid document element found, expected " + AGENT_WORLD_ELEMENT + ", got " + rootElement.getNodeName() + ".");
		}
		
		AgentWorld agentWorld;
		try {
			double width = Double.valueOf(rootElement.getAttribute("width"));
			double height = Double.valueOf(rootElement.getAttribute("height"));
			double bestFitness = Double.valueOf(rootElement.getAttribute("best-fitness"));
			
			agentWorld = new AgentWorld(width, height);
			agentWorld.setWorstFitnessScore(bestFitness);
			
			double[] agentInitialPosition = getPosition(rootElement, "agent-position");
			agentWorld.setInitialLocation(agentInitialPosition[0], agentInitialPosition[1]);
			
			loadObstacles(agentWorld, rootElement);
			loadGoal(agentWorld, rootElement);
			
		} catch (NumberFormatException e) {
			throw new IllegalStateException("Exception thrown loading AgentWorld.", e);
		}
		
		return agentWorld;
		
	}

	private static void loadGoal(AgentWorld agentWorld, Element parent) {
		NodeList nl = parent.getElementsByTagName("goal");
		
		if(nl.getLength() > 1) {
			throw new IllegalStateException("Can only have one Goal defined.");
		}
		
		if(nl.getLength() == 1) {
			Element e = (Element)nl.item(0);
			
			double x_pos = Double.valueOf(e.getAttribute(X_COORDINATE_ATTRIBUTE_NAME));
			double y_pos = Double.valueOf(e.getAttribute(Y_COORDINATE_ATTRIBUTE_NAME));
			
			Goal g = new Goal(x_pos, y_pos);
			agentWorld.setGoal(g);
		}
	}

	private static void loadObstacles(AgentWorld agentWorld, Element parent) {
		NodeList nl = parent.getElementsByTagName("obstacle");
		
		for(int i = 0; i < nl.getLength(); i++) {
			Element e = (Element)nl.item(i);

			double[] from = getPosition(e, "start");
			double[] to = getPosition(e, "finish");
			
			Obstacle o = new Obstacle(from[0], from[1], to[0], to[1]);
			agentWorld.addObstacle(o);
		}
	}
	
	private static double[] getPosition(Element parent, String name) {
		NodeList nl = parent.getElementsByTagName(name);
		
		if(nl.getLength() != 1) {
			throw new IllegalStateException("Found " + nl.getLength() + " Elements with name " + name + ", expected 1.");
		}
		
		Element e = (Element)nl.item(0);
		
		double[] ret = new double[2];
		ret[0] = Double.valueOf(e.getAttribute(X_COORDINATE_ATTRIBUTE_NAME));
		ret[1] = Double.valueOf(e.getAttribute(Y_COORDINATE_ATTRIBUTE_NAME));

		return ret;
	}

	private AgentWorld template;
	
	public XmlWorldBuilder(File f) {
		super(f);
		
		template = loadAgentWorld(f);
	}
	
	@Override
	public AgentWorld createWorld() {
		return template.copy();
	}

	@Override
	public double getWorstFitnessScore() {
		return template.getWorstFitnessScore();
	}

}
