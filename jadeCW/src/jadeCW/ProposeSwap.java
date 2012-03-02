package jadeCW;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

public class ProposeSwap extends Behaviour {

	private final PatientAgent agent;
	
	public ProposeSwap(PatientAgent agent) {
		this.agent = agent;
	}
	
	@Override
	public void action() {
		Appointment appointment = agent.getAppointment();
		
		if (agent.knowsDesiredAppOwner()) {
			ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
			msg.setContent(String.valueOf(appointment.getNumber()));
			msg.addReceiver(new AID(agent.getDesiredAppOwner(), AID.ISLOCALNAME));
			agent.send(msg);
		}
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}

}
