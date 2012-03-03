package jadeCW;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class RequestAppointment extends Behaviour {

	private final PatientAgent agent;
	private boolean requestingApp = false;
	private boolean done;

	RequestAppointment(PatientAgent agent) {
		this.agent = agent;
		this.done = false;
	}

	@Override
	public void action() {
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		String cid = agent.getLocalName() + "reqapp";
		if(agent.hasAlocationProvider() && !requestingApp) {
			System.out.println(agent.getLocalName() + ": requesting appointment from " +
					agent.getProvider().getLocalName());
			requestingApp = true;
			msg.addReceiver(agent.getProvider());
			msg.setConversationId(cid);
			agent.send(msg);
		}

		if (requestingApp) {
			MessageTemplate template = MessageTemplate.and(MessageTemplate.or(
					MessageTemplate.MatchPerformative(ACLMessage.INFORM),
		            MessageTemplate.MatchPerformative(ACLMessage.REFUSE)),
		            MessageTemplate.MatchConversationId(cid));
			ACLMessage reply = agent.receive(template);
			if (reply != null) {
				requestingApp = false;
				if (reply.getPerformative() == ACLMessage.REFUSE) {
					System.out.println(agent.getLocalName() + ": was refused an appointment from " +
							agent.getProvider().getLocalName());
					done = true;
					return;
				}
	
				int appNumber = Integer.parseInt(reply.getContent());
				System.out.println(agent.getLocalName() + ": was granted appointment " + (appNumber+1) +
						" from " + agent.getProvider().getLocalName());
				agent.updateAppointment(
						new Appointment(appNumber, agent.getPriority(appNumber)));
			} else {
				block();
			}
		}
	}

	@Override
	public boolean done() {
		return agent.hasAlocatedAppointment() || done;
	}

}
