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
		int proposedAppNumber = Integer.parseInt(request.getContent());
		
		ACLMessage reply = request.createReply();
		if (agent.isAppAvailable(proposedAppNumber)) {
			reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
			agent.allocateAppointment(proposedAppNumber, request.getSender());
		} else {
			reply.setPerformative(ACLMessage.REJECT_PROPOSAL);	
			reply.setContent(agent.getAppAgent(proposedAppNumber).getName());
		}
		agent.send(reply);
	}

}
