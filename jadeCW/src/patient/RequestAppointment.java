package patient;

import utils.Appointment;
import jade.core.behaviours.Behaviour;

public class RequestAppointment extends Behaviour {

	private PatientAgent agent;

	RequestAppointment(PatientAgent agent) {
		this.agent = agent;
	}

	@Override
	public void action() {
		if(agent.hasAlocationProvider() && !agent.hasAlocatedAppointment()) {
			Appointment alocatedApp = agent.requestAppointment();
			if (alocatedApp != null) {
				agent.updateAppointment(alocatedApp);
			}
		}
		
	}

	@Override
	public boolean done() {
		return false;
	}

}
