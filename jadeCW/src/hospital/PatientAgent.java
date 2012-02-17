package hospital;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import utils.Appointment;

import jade.core.Agent;

public class PatientAgent extends Agent {
	
	private List<Appointment> priorities = new LinkedList<Appointment>();
	private Appointment allocatedAppointment;

	protected void setup() {
		Object[] arguments = getArguments();
		if (arguments != null) {
			buildPriorities((String) arguments[0]);
			
	  	} else {
	  		// Terminate if created without arguments.
	  		doDelete();
	  		return;
	  	}
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
