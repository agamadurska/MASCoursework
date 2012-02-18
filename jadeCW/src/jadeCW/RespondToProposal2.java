package jadeCW;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class RespondToProposal2 extends CyclicBehaviour {

	private final HospitalAgent agent;
	
	public RespondToProposal2(HospitalAgent agent) {
		this.agent = agent;
	}
	
	@Override
	public void action() {
		ACLMessage request = agent.blockingReceive();
		ACLMessage reply = request.createReply();
		
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setContent("");
		
		// TODO Auto-generated method stub

	}

}
