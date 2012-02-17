package hospital;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class AllocateAppointment extends CyclicBehaviour {

	HospitalAgent agent;

	public AllocateAppointment(HospitalAgent agent) {
		this.agent = agent;
	}

	@Override
	public void action() {
		ACLMessage request = agent.blockingReceive();		
		ACLMessage reply = request.createReply();
		
		reply.setPerformative(ACLMessage.REFUSE);
		Integer appNumber = agent.allocateAppointment(request.getSender());

		if (appNumber != null) {
			reply.setPerformative(ACLMessage.INFORM);
			reply.setContent(appNumber.toString());
		}

		agent.send(reply);
	}

}
