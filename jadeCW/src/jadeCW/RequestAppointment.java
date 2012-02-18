package jadeCW;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

public class RequestAppointment extends Behaviour {

	private PatientAgent agent;

	RequestAppointment(PatientAgent agent) {
		this.agent = agent;
	}

	@Override
	public void action() {
		if(agent.hasAlocationProvider() && !agent.hasAlocatedAppointment()) {
			ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
			msg.addReceiver(agent.getProvider());
			agent.send(msg);
			ACLMessage reply = agent.blockingReceive();
			if (reply.getPerformative() == ACLMessage.REFUSE) {
				return;
			}

			int appNumber = Integer.parseInt(reply.getContent());
			agent.updateAppointment(
					new Appointment(appNumber, agent.getPriority(appNumber)));
		}
	}

	@Override
	public boolean done() {
		return false;
	}

}
