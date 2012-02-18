package jadeCW;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

public class FindAppointmentOwner extends Behaviour {

	private final PatientAgent agent;
	
	public FindAppointmentOwner(PatientAgent agent) {
		this.agent = agent;
	}
	
	@Override
	public void action() {
		agent.getAppointment();
		
		
		ACLMessage msg = new ACLMessage(ACLMessage.QUERY_IF);
		msg.addReceiver(agent.getProvider());
		agent.send(msg);
		ACLMessage reply = agent.blockingReceive();
		
		
		agent.updateProvider(new AID(reply.getContent(), true));
		// TODO Auto-generated method stub

	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}

}
