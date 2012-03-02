package jadeCW;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class RespondToProposal2 extends CyclicBehaviour {

	private final HospitalAgent agent;
	
	public RespondToProposal2(HospitalAgent agent) {
		this.agent = agent;
	}
	
	@Override
	public void action() {
		MessageTemplate template = MessageTemplate.MatchPerformative(ACLMessage.PROPOSE);
		ACLMessage request = agent.receive(template);
		if (request != null) {
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
		} else {
			block();
		}
	}

}
