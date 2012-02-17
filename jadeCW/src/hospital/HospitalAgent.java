package hospital;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.Property;
import jade.domain.FIPAAgentManagement.ServiceDescription;

/**
 * Simple hospital agent, storing appointments and interacting
 * with patient agents.
 *
 * @author Aga Madurska (amm208)
 *
 */
public class HospitalAgent extends Agent{

	/**
	 * Register the maximum number of appointments.
	 */
	protected void setup() {
		String serviceName = "allocate-appointments";
	  	Object[] arguments =  getArguments();
	  	int maxAppointments;

	  	if (arguments != null) {
	  		maxAppointments = (Integer) arguments[0];
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
	  		serviceDescription.setType("appointment-allocation");
	  		serviceDescription.addOntologies("appointment-allocation-ontology");

	  		// Agents that want to use this service need to "speak" the FIPA-SL language.
	  		serviceDescription.addLanguages(FIPANames.ContentLanguage.FIPA_SL);
	  		serviceDescription.addProperties(new Property("max-appointments", maxAppointments));

	  		agentDecription.addServices(serviceDescription);
	  		
	  		DFService.register(this, agentDecription);
	  	}
	  	catch (FIPAException fe) {
	  		fe.printStackTrace();
	  	}
	  }
}
