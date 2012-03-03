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
			System.out.println(agent.getLocalName() + ": received swap proposal from agent " +
					request.getSender().getLocalName());
			
			int proposedAppNumber = Integer.parseInt(request.getContent());
			ACLMessage reply = request.createReply();

			if (agent.isAppAvailable(proposedAppNumber)) {
				System.out.println(agent.getLocalName() + ": accepted swap proposal from " +
						request.getSender().getLocalName());

				reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
				agent.updateAlocation(proposedAppNumber, request.getSender());
			} else {
				System.out.println(agent.getLocalName() + ": rejected swap proposal from " +
						request.getSender().getLocalName() + " and notified " +
						request.getSender().getLocalName() +
						" that the owner of the desired appointment is " +
						agent.getAppAgent(proposedAppNumber).getLocalName());

				reply.setPerformative(ACLMessage.REJECT_PROPOSAL);	
				reply.setContent(agent.getAppAgent(proposedAppNumber).getLocalName());
			}
			agent.send(reply);
		} else {
			block();
		}
	}

}
