package hospital;

import java.util.LinkedList;
import java.util.List;

import utils.Appointment;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionInitiator;
import jade.util.leap.Iterator;

public class PatientAgent extends Agent {
	
	private List<Appointment> priorities = new LinkedList<Appointment>();
	private Appointment allocatedAppointment;
	private AID provider;

	protected void setup() {
		Object[] arguments = getArguments();
		if (arguments != null) {
			buildPriorities((String) arguments[0]);
			subscribeToService("allocate-appointments");
	  	} else {
	  		// Terminate if created without arguments.
	  		doDelete();
	  		return;
	  	}
	}

	private void subscribeToService(final String serviceType) {
		// Build the description used as template for the subscription
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription templateSd = new ServiceDescription();
		templateSd.setType(serviceType);
		template.addServices(templateSd);
  		
		addBehaviour(new SubscriptionInitiator(this, DFService.createSubscriptionMessage(this, getDefaultDF(), template, null)) {
			protected void handleInform(ACLMessage inform) {

  			try {
				DFAgentDescription[] results = DFService.decodeNotification(inform.getContent());
		  		if (results.length > 0) {
		  			for (int i = 0; i < results.length; ++i) {
		  				DFAgentDescription dfd = results[i];
		  				AID provider = dfd.getName();
		  				// The same agent may provide several services;
		  				Iterator it = dfd.getAllServices();
		  				while (it.hasNext()) {
		  					ServiceDescription sd = (ServiceDescription) it.next();
		  					if (sd.getType().equals(serviceType)) {
		  						storeProvider(provider);
		  					}
		  				}
		  			}
		  		}	
	  			System.out.println();
		  	}
		  	catch (FIPAException fe) {
		  		fe.printStackTrace();
		  	}
			}
		} );
	}

	protected void storeProvider(AID provider) {
		this.provider = provider;
	}

	private void buildPriorities(String preferences) {
  		String[] prioritySets = preferences.split("-");
  		String[] appNumbers;
  		int priority = prioritySets.length;
  		for (String set : prioritySets) {
  			appNumbers = set.split(" ");
  			for (String number : appNumbers) {
  				priorities.add(new Appointment(Integer.parseInt(number), priority));
  			}
  			priority--;
  		}
	}

	public boolean hasAlocatedAppointment() {
		return allocatedAppointment == null;
	}
}
