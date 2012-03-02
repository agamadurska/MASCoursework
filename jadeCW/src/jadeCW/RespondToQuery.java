package jadeCW;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class RespondToQuery extends CyclicBehaviour {

	private final HospitalAgent agent;
	
	public RespondToQuery(HospitalAgent agent) {
		this.agent = agent;
	}
	
	@Override
	public void action() {
		MessageTemplate template = MessageTemplate.MatchPerformative(ACLMessage.QUERY_REF);
		ACLMessage request = agent.receive(template);
		if (request != null) {
			int appointmentNumber = Integer.parseInt(request.getContent());
			AID aid = agent.getAppAgent(appointmentNumber);
			
			ACLMessage reply = request.createReply();
			if (aid == null) {
				reply.setContent(agent.getLocalName());
			} else {
				reply.setContent(aid.getLocalName());
			}

			if (!agent.validAppointment(appointmentNumber)) {
				reply.setContent(Appointment.OWNER_NOT_KNOWN);
			}
	
			reply.setPerformative(ACLMessage.INFORM);		
			agent.send(reply);
		} else {
			block();
		}
	}
}
