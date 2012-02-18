package jadeCW;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

public class FindAppointmentOwner extends Behaviour {

	private final PatientAgent agent;
	private int lastAppointmentTry;
	private final int maxAppointmentNumber;
	private boolean stopQuerying;
	
	public FindAppointmentOwner(PatientAgent agent, int maxAppointmentNumber) {
		this.agent = agent;
		this.maxAppointmentNumber = maxAppointmentNumber;
		stopQuerying = false;
		lastAppointmentTry = agent.getAppointment().getNumber();
	}
	
	@Override
	public void action() {
		++lastAppointmentTry;
		if (lastAppointmentTry > maxAppointmentNumber) {
			stopQuerying = true;
			return;
		}

		ACLMessage msg = new ACLMessage(ACLMessage.QUERY_REF);
		msg.setContent(String.valueOf(lastAppointmentTry));
		msg.addReceiver(agent.getProvider());
		agent.send(msg);
		
		ACLMessage reply = agent.blockingReceive();
		String aid = reply.getContent();
		if (!aid.equals("")) {
			
			stopQuerying = true;
		}
	}

	@Override
	public boolean done() {
		return stopQuerying;
	}

}
