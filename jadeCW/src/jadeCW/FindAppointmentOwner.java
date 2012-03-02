package jadeCW;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class FindAppointmentOwner extends Behaviour {

	private final PatientAgent agent;
	private int lastAppointmentTry;
	private final int maxAppointmentNumber;
	private boolean stopQuerying;
	private int reqNum;
	private boolean requestedOwner;
	String cid;
	
	public FindAppointmentOwner(PatientAgent agent, int maxAppointmentNumber) {
		this.agent = agent;
		this.maxAppointmentNumber = maxAppointmentNumber;
		stopQuerying = false;
		lastAppointmentTry = 0;
		reqNum = 0;
		requestedOwner = false;
		cid = "";
	}
	
	@Override
	public void action() {
		if (agent.hasAlocatedAppointment() && agent.hasAlocationProvider() && !requestedOwner) {
			while (lastAppointmentTry <= maxAppointmentNumber && 
					agent.getPriority(lastAppointmentTry) <= agent.getCurrentPriority()) {
				++lastAppointmentTry;
			}
	
			if (lastAppointmentTry > maxAppointmentNumber) {
				stopQuerying = true;
				return;
			}
			requestedOwner = true;
			cid = agent.getLocalName() + "find-app-owner" + reqNum;
			ACLMessage msg = new ACLMessage(ACLMessage.QUERY_REF);
			msg.setContent(String.valueOf(lastAppointmentTry));
			msg.addReceiver(agent.getProvider());
			msg.setConversationId(cid);
			agent.send(msg);
			reqNum++;
			lastAppointmentTry++;
		}
		
		if (requestedOwner) {
			MessageTemplate template = MessageTemplate.and(
					MessageTemplate.MatchPerformative(ACLMessage.INFORM),
		            MessageTemplate.MatchConversationId(cid));
			ACLMessage reply = agent.receive(template);
			if (reply != null) {
				String aid = reply.getContent();
				if (!aid.equals(Appointment.OWNER_NOT_KNOWN)) {
					stopQuerying = true;
					agent.updateDesiredAppOwner(aid);
				}
				requestedOwner = false;
			} else {
				block();
			}
		}
	}

	@Override
	public boolean done() {
		return stopQuerying;
	}

}
