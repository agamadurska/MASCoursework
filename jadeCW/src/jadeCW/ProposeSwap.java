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
			System.out.println(agent.getLocalName() + ": proposed swap to agent " +
					agent.getDesiredAppOwner());
			proposedSwap = true;
			ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
			if (agent.getDesiredAppOwner().equals(agent.getProvider().getLocalName())) {
				msg.setContent(agent.getDesiredAppNumber()+"");
			} else {
				msg.setContent(String.valueOf(appointment.getNumber()));
			}
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
				String desiredAppOwner = agent.getDesiredAppOwner();
				agent.clearDesiredAppOwner();
				if (reply.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
					System.out.println(agent.getLocalName() + ": swap with agent " +
							desiredAppOwner + " got accepted by " + desiredAppOwner);
					int appNumber = agent.getDesiredAppNumber();
					agent.updateAppointment(
							new Appointment(appNumber, agent.getPriority(appNumber)));
					if (!reply.getSender().equals(agent.getProvider())) {
						ACLMessage hospitalNotification = new ACLMessage(ACLMessage.INFORM);
						hospitalNotification.addReceiver(agent.getProvider());
						hospitalNotification.setContent(appNumber+"");
						agent.send(hospitalNotification);
					}
				} else {
					System.out.println(agent.getLocalName() + ": swap with agent " +
							desiredAppOwner + " got refused by " + desiredAppOwner);
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
