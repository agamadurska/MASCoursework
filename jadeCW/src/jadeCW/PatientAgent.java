package jadeCW;

import java.util.HashMap;
import java.util.Map;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.Property;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionInitiator;
import jade.util.leap.Iterator;

public class PatientAgent extends Agent {
	
	private static final int DEFAULT_LOWEST_PRIORITY = -1;
	private static int maxAppointments;

	private final Map<Integer, Integer> priorityMap =
			new HashMap<Integer, Integer>();
	private Appointment allocatedAppointment;
	private AID provider;
	protected static int cidCnt = 0;
	String cidBase;

	private String desiredAppOwner;
	private int desiredAppNumber;
	private boolean currentlySwapping;

	public boolean isCurrentlySwapping() {
		return currentlySwapping;
	}

	public void setCurrentlySwapping(boolean currentlySwapping) {
		this.currentlySwapping = currentlySwapping;
	}

	protected void setup() {
		Object[] arguments = getArguments();
		if (arguments != null) {
			buildPriorities((String) arguments[0]);
			subscribeToService("allocate-appointments");
			addBehaviour(new RequestAppointment(this));
			addBehaviour(new ProposeSwap(this));
			addBehaviour(new RespondToProposal1(this));
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
  		
		addBehaviour(new SubscriptionInitiator(
				this, DFService.createSubscriptionMessage(
						this, getDefaultDF(), template, null)) {
			
					protected void handleInform(ACLMessage inform) {	
						try {
							DFAgentDescription[] results =
									DFService.decodeNotification(inform.getContent());
	
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
					  						Iterator propertyIt = sd.getAllProperties();
					  						while (propertyIt.hasNext()){
					  							Property p = (Property) propertyIt.next();
					  							if (p.getName().equals("max-app")) {
					  								storeMaxApp(Integer.parseInt((String)p.getValue())); 
					  							}
					  						}
					  					}
					  				}
					  			}
					  		 }
							System.out.println(getLocalName() + ": registered a list of hospitals");
						}catch (FIPAException fe) {
							fe.printStackTrace();
						}
					}
		});
	}

	protected void storeMaxApp(int maxAppointments) {
		this.maxAppointments = maxAppointments;
		addBehaviour(new FindAppointmentOwner(this, maxAppointments-1));
	}

	protected void storeProvider(AID provider) {
		this.provider = provider;
	}

	private void buildPriorities(String preferences) {
		String[] prioritySets = preferences.split("-");
		int priority = prioritySets.length;
		
		for (String set : prioritySets) {
			String[] appNumbers = set.split(" ");
			for (String number : appNumbers) {
				if (!number.equals("")) {
					priorityMap.put(Integer.parseInt(number)-1, priority);
				}
			}
			priority--;
		}
	}

	public boolean hasAlocatedAppointment() {
		return allocatedAppointment != null;
	}

	public boolean hasAlocationProvider() {
		return provider != null;
	}

	public void updateAppointment(Appointment alocatedApp) {
		this.allocatedAppointment = alocatedApp;		
	}

	public Appointment getAppointment() {
		return allocatedAppointment;
	}

	public AID getProvider() {
		return provider;
	}
	
	public void updateProvider(AID provider) {
		this.provider = provider;
	}

	public Integer getPriority(int appNumber) {
		return priorityMap.get(appNumber) ==  null ?
				DEFAULT_LOWEST_PRIORITY : priorityMap.get(appNumber);
	}

	public Integer getCurrentPriority() {
		return allocatedAppointment.getPriority();
	}

	public void updateDesiredAppOwner(String aid) {
		this.desiredAppOwner = aid;
	}

	protected void takeDown() {
		System.out.println(getLocalName() + ": Appointment " +
				(allocatedAppointment == null ? "null" :
						allocatedAppointment.getNumber() + 1));
    }

	public boolean knowsDesiredAppOwner() {
		return desiredAppOwner != null;
	}

	public String getDesiredAppOwner() {
		return desiredAppOwner;
	}

	public void clearDesiredAppOwner() {
		desiredAppOwner = null;
	}

	public void updateDesiredAppNumber(int desiredAppNumber) {
		this.desiredAppNumber = desiredAppNumber;
	}

	public int getDesiredAppNumber() {
		return desiredAppNumber;
	}
}
