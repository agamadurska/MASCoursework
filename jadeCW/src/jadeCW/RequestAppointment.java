package jadeCW;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class RequestAppointment extends Behaviour {

	private final PatientAgent agent;
	private boolean requestingApp = false;

	RequestAppointment(PatientAgent agent) {
		this.agent = agent;
	}

	@Override
	public void action() {
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		String cid = agent.getLocalName() + "reqapp";
		if(agent.hasAlocationProvider() && !requestingApp) {
			requestingApp = true;
			msg.addReceiver(agent.getProvider());
			msg.setConversationId(cid);
			agent.send(msg);
		}
		if (requestingApp) {
			MessageTemplate template = MessageTemplate.and( 
		            MessageTemplate.MatchPerformative(ACLMessage.INFORM),
		            MessageTemplate.MatchConversationId(cid));
			ACLMessage reply = agent.receive(template);
			if (reply != null) {
				requestingApp = false;
				if (reply.getPerformative() == ACLMessage.REFUSE) {
					return;
				}
	
				int appNumber = Integer.parseInt(reply.getContent());
				agent.updateAppointment(
						new Appointment(appNumber, agent.getPriority(appNumber)));
			} else {
				block();
			}
		}
	}

	@Override
	public boolean done() {
		return agent.hasAlocatedAppointment();
	}

}
