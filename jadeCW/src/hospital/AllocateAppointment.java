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
		ACLMessage reply = new ACLMessage(ACLMessage.REFUSE);

		ACLMessage request = agent.blockingReceive();
		AID sender = request.getSender();
		Integer appNumber = agent.allocateAppointment(sender);

		if (appNumber != null) {
			reply = new ACLMessage(ACLMessage.INFORM);
			reply.addReceiver(sender);
			reply.setContent(appNumber.toString());
		}

		agent.send(reply);
	}

}
