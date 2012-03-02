package jadeCW;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class AllocateAppointment extends CyclicBehaviour {

	private final HospitalAgent agent;

	public AllocateAppointment(HospitalAgent agent) {
		this.agent = agent;
	}

	@Override
	public void action() {
		MessageTemplate template = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
		ACLMessage request = agent.receive(template);
		if (request != null) {
			ACLMessage reply = request.createReply();
			reply.setPerformative(ACLMessage.REFUSE);
			Integer appNumber = agent.allocateAppointment(request.getSender());
			if (appNumber != null) {
				reply.setPerformative(ACLMessage.INFORM);
				reply.setContent(appNumber.toString());
			}
			agent.send(reply);
		} else {
			block();
		}
	}
}
