package jadeCW;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ProposeSwap extends Behaviour {

	private final PatientAgent agent;
	private boolean proposedSwap;
	
	public ProposeSwap(PatientAgent agent) {
		this.agent = agent;
		proposedSwap = false;
	}
	
	@Override
	public void action() {
		Appointment appointment = agent.getAppointment();
		String cid = agent.getLocalName() + "proposed_swap";
		if (agent.knowsDesiredAppOwner() && !proposedSwap) {
			proposedSwap = true;
			ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
			msg.setContent(String.valueOf(appointment.getNumber()));
			msg.addReceiver(new AID(agent.getDesiredAppOwner(), AID.ISLOCALNAME));
			msg.setConversationId(cid);
			agent.send(msg);
		}

		if (proposedSwap) {
			MessageTemplate template = MessageTemplate.and(MessageTemplate.or(
					MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL),
		            MessageTemplate.MatchPerformative(ACLMessage.REJECT_PROPOSAL)),
		            MessageTemplate.MatchConversationId(cid));
			ACLMessage reply = agent.receive(template);
			if (reply != null) {
				proposedSwap = false;
				agent.clearDesiredAppOwner();
				if (reply.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
					int appNumber = Integer.parseInt(reply.getContent());
					agent.updateAppointment(
							new Appointment(appNumber, agent.getPriority(appNumber)));
					if (!reply.getSender().equals(agent.getProvider())) {
						ACLMessage hospitalNotification = new ACLMessage(ACLMessage.INFORM);
						hospitalNotification.addReceiver(agent.getProvider());
						hospitalNotification.setContent(appNumber+"");
						agent.send(hospitalNotification);
					}
				} else {
					String owner = reply.getContent();
					agent.updateDesiredAppOwner(owner);
				}
			} else {
				block();
			}
		}
	}

	@Override
	public boolean done() {
		return false;
	}

}
