package managerClasses;

import java.util.ArrayList;

import lineClasses.ArrivalQueue;
import lineClasses.WaitingQueue;
import peopleClasses.Clerk;
import peopleClasses.Customer;

/**
 * Manager class for Multiple Line Multiple Server approach
 * 
 * @author Raúl Vargas
 *
 */
public class MLMS_Manager {
	private int customerTotal;
	private int servers;
	private int currentTime = 0;
	private int finalTime;
	private float averageWait = 0;
	private float overpassCount = 0;
	private Clerk[] clerks;
	private ArrivalQueue arrival;
	private ArrayList<Customer> overpass = new ArrayList<>();
	private boolean finish = false;

	/**
	 * Constructor
	 * @param servers amount of servers to assign
	 * @param arrival ArrivalQueue to use
	 */
	public MLMS_Manager(int servers, ArrivalQueue arrival) {
		this.servers = servers;
		this.arrival = arrival;	
		clerks = new Clerk[servers];
		for (int i = 0; i < servers; i++) {
			clerks[i] = new Clerk(new WaitingQueue());
		}
		customerTotal = arrival.size();
		currentTime = arrival.first().getArrivalTime();
	}

	/**
	 * Runs the approach
	 * @return a piece of the final output
	 */
	public String execute() {
		while (!finish) {
//			System.out.println("\nCurrent time "+currentTime);
			//Service completed
			for (int i = 0; i < clerks.length; i++) 
				if (clerks[i].isServing()) 
					if (clerks[i].isFinished(currentTime)) {
						Customer target = clerks[i].completeService();
//						System.out.println("Clerk "+(i+1)+" finished serving "+target.getArrivalTime()+"-"+target.getServiceTime());
					}
			//Service start
			for (int i = 0; i < clerks.length; i++) 
				if (!clerks[i].isServing() && !clerks[i].getServiceLine().isEmpty()) {
					Customer target = clerks[i].getServiceLine().first();
					clerks[i].startService(clerks[i].getServiceLine().dequeue(), currentTime);
					averageWait += clerks[i].getCustomer().getWaitingTime();
					overpass.add(target);
//					System.out.println("1Clerk "+(i+1)+" started serving "+target.getArrivalTime()+"-"+target.getServiceTime());
				}
			//Arrival...
			while (!arrival.isEmpty()) {
				if (arrival.first().getArrivalTime()==currentTime) { 
					int peopleTemp = customerTotal;
					int serv = arrival.first().getServiceTime();
					for (int i = 0; i < clerks.length; i++) 
						if (clerks[i].getCustomersLeft()<peopleTemp)
							peopleTemp=clerks[i].getCustomersLeft();
					for (int i = 0; i < clerks.length; i++) {
						if (clerks[i].getCustomersLeft()==peopleTemp && !arrival.isEmpty())
							if (arrival.first().getArrivalTime()==currentTime) {
								clerks[i].getServiceLine().enqueue(arrival.dequeue());
								clerks[i].setCustomersLeft(clerks[i].getCustomersLeft()+1);
//								System.out.println("Customer arrives at "+currentTime+" for "+serv+" in Line "+(i+1));
						}
					}
					continue;
				}
				else 
					break;
			}
			//...and immediate service (if applicable)
			for (int i = 0; i < clerks.length; i++) 
				if (!clerks[i].isServing() && !clerks[i].getServiceLine().isEmpty()) {
					Customer target = clerks[i].getServiceLine().first();
					clerks[i].startService(clerks[i].getServiceLine().dequeue(), currentTime);
					averageWait += clerks[i].getCustomer().getWaitingTime();
					overpass.add(target);
//					System.out.println("2Clerk "+(i+1)+" started serving "+target.getArrivalTime()+"-"+target.getServiceTime());
				}

			currentTime++;

			finish=true;
			if (!arrival.isEmpty())
				finish=false;
			for (int i = 0; i < clerks.length; i++) 
				if (!clerks[i].getServiceLine().isEmpty() && finish)
					finish=false;
			for (int i = 0; i < clerks.length; i++) 
				if (clerks[i].isServing() && finish)
					finish=false;			
			if (!finish)
				timeskip();
		}
		return finalCompute();
	}

	/**
	 * Skips ahead to the next event
	 */
	private void timeskip() {
		int next = 100+(customerTotal*13); 
		if (!arrival.isEmpty())
			if (arrival.first().getArrivalTime()<next)	
				next = arrival.first().getArrivalTime();
		for (int i = 0; i < clerks.length; i++) 
			if (clerks[i].isServing())
				if (clerks[i].getCustomer().getDepartureTime()<next)
					next = clerks[i].getCustomer().getDepartureTime();
		if (next>currentTime) {
			currentTime=next;
//			System.out.println("Time skip to "+currentTime);
		}
	}

	/**
	 * Performs the final computations for the approach
	 * @return a piece of the final output
	 */
	private String finalCompute() {
		finalTime = currentTime;
		averageWait = averageWait/customerTotal;
		//overpass calculation
		for (int i = 0; i < overpass.size()-1; i++) 
			for (int j = i+1; j < overpass.size(); j++) 
				if (overpass.get(i).getArrivalTime()>overpass.get(j).getArrivalTime())
					overpassCount++;
		overpassCount = overpassCount/customerTotal;
		String outEntry="\nMLMS "+servers+":\t"+finalTime+"\t"+String.format("%.2f", averageWait)+"  \t"+String.format("%.2f", overpassCount);
		return outEntry;
	}
}
