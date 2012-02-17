package utils;

public class Appointment implements Comparable<Appointment> {
	private int number;
	private int priority;

	public Appointment(int number, int priority) {
		this.number = number;
		this.priority = priority;
	}

	@Override
	public int compareTo(Appointment appointment) {
		return this.priority - appointment.priority;
	}

	public int getNumber() {
		return number;
	}
}
