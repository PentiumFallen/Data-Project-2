package peopleClasses;

/**
 * This is the main "object" class
 * It comes with an arrival time and service time
 * Upon entering service it gets a waiting time and departure time
 * 
 * @author Raúl Vargas
 *
 */
public class Customer implements Comparable<Customer> {
	private int arrivalTime;
	private int serviceTime;
	private int waitingTime;
	private int departureTime;

	/**
	 * Constructor
	 * @param at arrival time
	 * @param st service time
	 */
	public Customer(int at, int st) {
		arrivalTime=at;
		serviceTime=st;
	}
	/**
	 * @return arrival time
	 */
	public int getArrivalTime() {
		return arrivalTime;
	}
	/**
	 * @return service time
	 */
	public int getServiceTime() {
		return serviceTime;
	}
	/**
	 * @return waiting time
	 */
	public int getWaitingTime() {
		return waitingTime;
	}
	/**
	 * @return departure time
	 */
	public int getDepartureTime() {
		return departureTime;
	}
	/**
	 * Upon entering service, sets waiting time
	 * @param wt waiting time given by Clerk
	 */
	public void setWaitingTime(int wt) {
		waitingTime = wt;
	}
	/**
	 * Upon entering service, sets departure time
	 * @param dt departure time given by Clerk
	 */
	public void setDepartureTime(int dt) {
		departureTime = dt;
	}
	/**
	 * Used to sort Customers in ClientMaker
	 */
	public int compareTo(Customer c) {
		return this.arrivalTime-c.arrivalTime;
	}
}