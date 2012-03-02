package jadeCW;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class UpdateAppoinments extends CyclicBehaviour {

	private final HospitalAgent agent;
	
	public UpdateAppoinments(HospitalAgent agent) {
		this.agent = agent;
	}
	
	@Override
	public void action() {
		MessageTemplate template = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
		ACLMessage request = agent.receive(template);
		if (request != null) {
			int appointmentNumber = Integer.parseInt(request.getContent());
			AID sender = request.getSender();
			agent.allocateAppointment(appointmentNumber, sender);
		} else {
			block();
		}
	}

}
