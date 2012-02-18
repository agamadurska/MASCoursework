package jadeCW;

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
		ACLMessage reply = request.createReply();
		
		reply.setPerformative(ACLMessage.INFORM);
		String content = "";
		reply.setContent(content);
		
		agent.send(reply);

	}

}
