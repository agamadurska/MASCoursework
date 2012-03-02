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
			System.out.println(agent.getLocalName() + " received propsal");
			int proposedAppNumber = Integer.parseInt(request.getContent());
			int appNumber = agent.getAppointment().getNumber();
			
			ACLMessage reply = request.createReply();
			if (agent.getPriority(appNumber) <= agent.getPriority(proposedAppNumber)) {
				reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
				reply.setContent(appNumber+"");
				agent.updateAppointment(new Appointment(proposedAppNumber, agent.getPriority(proposedAppNumber)));
				ACLMessage hospitalNotification = new ACLMessage(ACLMessage.INFORM);
				hospitalNotification.addReceiver(agent.getProvider());
				hospitalNotification.setContent(proposedAppNumber+"");
				agent.send(hospitalNotification);
			} else {
				reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
			}
			agent.send(reply);
		} else {
			block();
		}
	}

}
