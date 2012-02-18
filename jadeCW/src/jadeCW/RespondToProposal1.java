package jadeCW;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class RespondToProposal1 extends CyclicBehaviour {

	private final PatientAgent agent;
	
	public RespondToProposal1(PatientAgent agent) {
		this.agent = agent;
	}
	
	@Override
	public void action() {
		ACLMessage request = agent.blockingReceive();
		ACLMessage reply = request.createReply();
		
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setContent("");
		msg.addReceiver(agent.getProvider());
		agent.send(msg);
	}

}
