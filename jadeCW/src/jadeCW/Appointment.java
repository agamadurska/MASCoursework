package jadeCW;

public class Appointment {

	public static final String OWNER_NOT_KNOWN = "Owner_not_known";
	private final int number;
	private int priority;

	public Appointment(int number, Integer priority) {
		this.number = number;
		if (priority != null) {
			this.priority = priority;
		}
	}

	public int getNumber() {
		return number;
	}

	public int getPriority() {
		return priority;
	}
}
