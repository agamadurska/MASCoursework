package hospital;

import jade.core.Agent;

/**
 * Simple hospital agent, storing appointments and interacting
 * with patient agents.
 *
 * @author Aga Madurska (amm208)
 *
 */
public class HospitalAgent extends Agent{

	protected void setup() {
	  	// Make this agent terminate
	  	doDelete();
	  } 
}
