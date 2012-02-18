package jadeCW;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

public class ProposeSwap extends Behaviour {

	private final PatientAgent agent;
	
	public ProposeSwap(PatientAgent agent) {
		this.agent = agent;
	}
	
	@Override
	public void action() {
		
		// TODO Auto-generated method stub
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setContent("");
		msg.addReceiver(agent.getProvider());
		agent.send(msg);
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}

}
