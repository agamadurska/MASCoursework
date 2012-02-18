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
		int proposedAppNumber = Integer.parseInt(request.getContent());
		int appNumber = agent.getAppointment().getNumber();
		
		ACLMessage reply = request.createReply();
		if (agent.getPriority(appNumber) <= agent.getPriority(proposedAppNumber)) {
			reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
		} else {
			reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
		}
		agent.send(reply);
	}

}
