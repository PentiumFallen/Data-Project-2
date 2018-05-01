package peopleClasses;

import lineClasses.WaitingQueue;

/**
 * Clerk class functions to aid the manager classes
 * 
 * @author Raúl Vargas
 *
 */
public class Clerk {
	private Customer customerServing;
	private WaitingQueue serviceLine;
	private int customersLeft;

	/**
	 * Constructor
	 * @param line that the Clerk takes for serving
	 */
	public Clerk(WaitingQueue line) {
		setServiceLine(line);
	}
	/**
	 * @return the Customer being served
	 */
	public Customer getCustomer() {
		return customerServing;
	}
	/**
	 * @return the WaitingQueue this Clerk belongs to
	 */
	public WaitingQueue getServiceLine() {
		return serviceLine;
	}
	/**
	 * Assigns a WaitingQueue to Clerk
	 * @param serviceLine
	 */
	public void setServiceLine(WaitingQueue serviceLine) {
		this.serviceLine = serviceLine;
	}
	/**
	 * @return the amount of Customers the Clerk has in the service line + the one being served
	 */
	public int getCustomersLeft() {
		return customersLeft;
	}
	/**
	 * Used to update the amount of Customers
	 * @param customersLeft new amount 
	 */
	public void setCustomersLeft(int customersLeft) {
		this.customersLeft = customersLeft;
	}
	/**
	 * @return the last Customer this Clerk will be serving
	 */
	public Customer getLastCustomer() {
		if (!serviceLine.isEmpty())
			return serviceLine.last();
		else
			return customerServing;
	}
	/**
	 * Begins service for a Customer
	 * @param client Customer to serve
	 * @param time current time
	 */
	public void startService(Customer client, int time) {
		customerServing = client;
		customerServing.setWaitingTime(time-customerServing.getArrivalTime());
		customerServing.setDepartureTime(time+customerServing.getServiceTime());
	}
	/**
	 * Finishes service for a Customer
	 * @return Customer served
	 */
	public Customer completeService() {
		Customer goodbye = customerServing;
		customerServing = null;
		customersLeft--;
		return goodbye;
	}
	/**
	 * @return whether the Clerk is serving someone
	 */
	public boolean isServing() {
		return (customerServing!=null);
	}
	/**
	 * @param time current time
	 * @return whether the Clerk is done serving someone
	 */
	public boolean isFinished(int time) {
		return customerServing.getDepartureTime()==time;
	}
}
