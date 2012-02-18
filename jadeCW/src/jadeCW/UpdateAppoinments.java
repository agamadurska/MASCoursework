package jadeCW;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class UpdateAppoinments extends CyclicBehaviour {

	private final HospitalAgent agent;
	
	public UpdateAppoinments(HospitalAgent agent) {
		this.agent = agent;
	}
	
	@Override
	public void action() {
		ACLMessage request = agent.blockingReceive();
		
		// TODO Auto-generated method stub

	}

}
