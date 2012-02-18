package jadeCW;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class RespondToQuery extends CyclicBehaviour {

	private final HospitalAgent agent;
	
	public RespondToQuery(HospitalAgent agent) {
		this.agent = agent;
	}
	
	@Override
	public void action() {
		ACLMessage request = agent.blockingReceive();
		int appointmentNumber = Integer.parseInt(request.getContent());
		AID aid = agent.getAppAgent(appointmentNumber);
		
		ACLMessage reply = request.createReply();
		if (aid == null) {
			reply.setContent("");
		} else {
			reply.setContent(aid.getName());
		}
		reply.setPerformative(ACLMessage.INFORM);		
		agent.send(reply);
	}

}
