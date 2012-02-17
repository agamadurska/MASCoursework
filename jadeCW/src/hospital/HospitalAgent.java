package hospital;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

/**
 * Simple hospital agent, storing appointments and interacting
 * with patient agents.
 *
 * @author Aga Madurska (amm208)
 *
 */
public class HospitalAgent extends Agent {
	int maxAppointments;
	private int nextAvailableAppointment = 0;
	private Map<AID, Integer> appointmentAllocation = new HashMap<AID, Integer>();
	/**
	 * Register the maximum number of appointments.
	 */
	protected void setup() {
		String serviceName = "allocate-appointments";
	  	Object[] arguments =  getArguments();

	  	if (arguments != null) {
	  		maxAppointments = Integer.parseInt((String)arguments[0]);
	  		addBehaviour(new AllocateAppointment(this));
	  	} else {
	  		// Terminate if created without arguments.
	  		doDelete();
	  		return;
	  	}
	  	
	  	// Register the service
	  	try {
	  		DFAgentDescription agentDecription = new DFAgentDescription();
	  		agentDecription.setName(getAID());
	  		ServiceDescription serviceDescription = new ServiceDescription();
	  		serviceDescription.setName(serviceName);

	  		// Not sure if this is needed but was in example so keeping for now.
	  		serviceDescription.setType("allocate-appointments");
	  		serviceDescription.addOntologies("allocate-appointments-ontology");

	  		// Agents that want to use this service need to "speak" the FIPA-SL language.
	  		serviceDescription.addLanguages(FIPANames.ContentLanguage.FIPA_SL);

	  		agentDecription.addServices(serviceDescription);
	  		
	  		DFService.register(this, agentDecription);
	  	}
	  	catch (FIPAException fe) {
	  		fe.printStackTrace();
	  	}
	  }

	public Integer allocateAppointment(AID sender) {
		if (appointmentAvailable()) {
			Integer app = nextAvailableAppointment();
			appointmentAllocation.put(sender, app);
			nextAvailableAppointment++;
			return app;
		}
		return null;
	}

	private Integer nextAvailableAppointment() {
		return nextAvailableAppointment;
	}

	private boolean appointmentAvailable() {
		return nextAvailableAppointment <= maxAppointments;
	}

	protected void takeDown() {
	    for (Entry<AID, Integer> e : appointmentAllocation.entrySet()) {
			System.out.println(getLocalName() + ":" + e.getKey().getLocalName() +
					": Appointment " + e.getValue());
	    }
	}
}
