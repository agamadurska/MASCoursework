package jadeCW;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Simple hospital agent, storing appointments and interacting
 * with patient agents.
 *
 * @author Aga Madurska (amm208)
 *
 */
public class HospitalAgent extends Agent {
	
	private int maxAppointments;
	private int nextAvailableAppointment = 0;
	private final Map<AID, Integer> agentAllocation =
			new HashMap<AID, Integer>();
	private final Map<Integer, AID> appAllocation =
			new HashMap<Integer, AID>();
	
	/**
	 * Register the maximum number of appointments.
	 */
	protected void setup() {
	  String serviceName = "allocate-appointments";
	  Object[] arguments =  getArguments();

	  if (arguments != null) {
	  	maxAppointments = Integer.parseInt((String)arguments[0]);
	  	addBehaviour(new AllocateAppointment(this));
	  	//addBehaviour(new RespondToQuery(this));
	  } else {
	  	// Terminate if created without arguments.
	  	doDelete();
	  	return;
	  }
	  	
	  // Register the service
	  try {
	  	DFAgentDescription agentDescription = new DFAgentDescription();
	  	agentDescription.setName(getAID());
	  	ServiceDescription serviceDescription = new ServiceDescription();
	  	serviceDescription.setName(serviceName);

	  	// Not sure if this is needed but was in example so keeping for now.
	  	serviceDescription.setType("allocate-appointments");
	  	serviceDescription.addOntologies("allocate-appointments-ontology");

	  	// Agents that want to use this service need to "speak" the FIPA-SL
	  	// language.
	  	serviceDescription.addLanguages(FIPANames.ContentLanguage.FIPA_SL);

	  	agentDescription.addServices(serviceDescription);
	  		
	  	DFService.register(this, agentDescription);
	  }
	  catch (FIPAException fe) {
	  	fe.printStackTrace();
	  }
	}

	public Integer allocateAppointment(AID sender) {
		if (appointmentAvailable()) {
			Integer app = nextAvailableAppointment();
			agentAllocation.put(sender, app);
			appAllocation.put(app, sender);
			while (appAllocation.get(nextAvailableAppointment) != null) {
				++nextAvailableAppointment;
			}
			return app;
		}
		return null;
	}

	public void allocateAppointment(int appointmentNumber, AID sender) {
		agentAllocation.put(sender, appointmentNumber);
		appAllocation.put(appointmentNumber, sender);
		while (appAllocation.get(nextAvailableAppointment) != null) {
			++nextAvailableAppointment;
		}
	}
	
	public AID getAppAgent(int appointmentNumber) {
		return appAllocation.get(appointmentNumber);
	}
	
	public boolean isAppAvailable(int appointmentNumber) {
		return appointmentNumber < maxAppointments &&
				appAllocation.get(appointmentNumber) == null;
	}
	
	private Integer nextAvailableAppointment() {
		return nextAvailableAppointment;
	}

	private boolean appointmentAvailable() {
		return nextAvailableAppointment <= maxAppointments;
	}

	protected void takeDown() {
		for (Entry<AID, Integer> e : agentAllocation.entrySet()) {
			System.out.println(getLocalName() + ":" + e.getKey().getLocalName() +
					": Appointment " + (e.getValue() + 1));
		}
	}

}
