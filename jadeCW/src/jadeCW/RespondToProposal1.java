package jadeCW;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class RespondToProposal1 extends CyclicBehaviour {

	private final PatientAgent agent;
	
	public RespondToProposal1(PatientAgent agent) {
		this.agent = agent;
	}
	
	@Override
	public void action() {
		MessageTemplate template = MessageTemplate.MatchPerformative(ACLMessage.PROPOSE);
		ACLMessage request = agent.receive(template);
		if (request != null) {
			if (agent.isCurrentlySwapping()) {
				agent.putBack(request);
				return;
			}
			agent.setCurrentlySwapping(true);

			System.out.println(agent.getLocalName() + ": received swap proposal from agent " +
					request.getSender().getLocalName());
			int proposedAppNumber = Integer.parseInt(request.getContent());
			
			
			ACLMessage reply = request.createReply();
			if (agent.getAppointment() != null && 
					agent.getPriority(agent.getAppointment().getNumber()) <=
					agent.getPriority(proposedAppNumber)) {

				System.out.println(agent.getLocalName() + ": accepted swap proposal from agent " +
						request.getSender().getLocalName());
				int appNumber = agent.getAppointment().getNumber();
				reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
				reply.setContent(appNumber+"");
				agent.updateAppointment(new Appointment(proposedAppNumber,
						agent.getPriority(proposedAppNumber)));

				ACLMessage hospitalNotification = new ACLMessage(ACLMessage.INFORM);
				hospitalNotification.addReceiver(agent.getProvider());
				hospitalNotification.setContent(proposedAppNumber+"");
				agent.send(hospitalNotification);

				System.out.println(agent.getLocalName() + ": notified " +
						agent.getProvider().getLocalName() + " about swap with " +
						request.getSender().getLocalName());
			} else {
				System.out.println(agent.getLocalName() + ": refused swap proposal from agent " +
						request.getSender().getLocalName());
				reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
			}
			agent.send(reply);
			agent.setCurrentlySwapping(false);
		} else {
			block();
		}
	}

}
