package jadeCW;

public class Appointment implements Comparable<Appointment> {

	private final int number;
	private int priority;

	public Appointment(int number, Integer priority) {
		this.number = number;
		if (priority != null) {
			this.priority = priority;
		}
	}

	@Override
	public int compareTo(Appointment appointment) {
		return this.priority - appointment.priority;
	}

	public int getNumber() {
		return number;
	}

}
