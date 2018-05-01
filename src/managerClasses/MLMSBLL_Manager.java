package managerClasses;

import java.util.ArrayList;
import lineClasses.ArrivalQueue;
import lineClasses.WaitingQueue;
import peopleClasses.Clerk;
import peopleClasses.Customer;

/**
 * Manager class for Multiple Line Multiple Server Balanced Line Length approach
 * 
 * @author Raúl Vargas
 *
 */
public class MLMSBLL_Manager {
	private int customerTotal;
	private int servers;
	private int currentTime = 0;
	private int finalTime;
	private float averageWait = 0;
	private float overpassCount = 0;
	private Clerk[] clerks;
	private int[] lines;
	private ArrivalQueue arrival;
	private ArrayList<Customer> overpass = new ArrayList<>();
	private boolean finish = false;
	private boolean transferCheck = true;

	/**
	 * Constructor
	 * @param servers amount of servers to assign
	 * @param arrival ArrivalQueue to use
	 */
	public MLMSBLL_Manager(int servers, ArrivalQueue arrival) {
		this.servers = servers;
		this.arrival = arrival;	
		clerks = new Clerk[servers];
		lines = new int[servers];
		for (int i = 0; i < servers; i++) {
			clerks[i] = new Clerk(new WaitingQueue());
			lines[i] = 0;
		}
		customerTotal = arrival.size();
		currentTime = arrival.first().getArrivalTime();
		if (servers==1)
			transferCheck=false;
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
						lines[i]--;
//						System.out.println("Clerk "+(i+1)+" finished serving "+target.getArrivalTime()+"-"+target.getServiceTime());
					}
			//Transfer
			while (transferCheck){
				int goodLine = lines[0];
				int badLine = lines[0];
				for (int i = 0; i < lines.length; i++) {
					if (lines[i]<goodLine)
						goodLine = lines[i];
					if (lines[i]>badLine)
						badLine = lines[i];
				}
				if (goodLine+1>badLine-1)
					break;
				else {
					int priorityArrival = currentTime;
					int priorityLine = 0;
					for (int i = 0; i < lines.length; i++) 
						if (lines[i]==badLine) 
							if (!clerks[i].getServiceLine().isEmpty() && clerks[i].getLastCustomer().getArrivalTime()<priorityArrival) {
								priorityArrival = clerks[i].getLastCustomer().getArrivalTime();
								priorityLine = i;
							}
					boolean done = false;
					if (priorityLine==clerks.length-1)
						for (int i = 0; i < clerks.length; i++) {
							if (lines[i]==goodLine) {
								clerks[i].getServiceLine().enqueue(clerks[priorityLine].getServiceLine().lastDequeue());
								lines[priorityLine]--;
								lines[i]++;
//								System.out.println("Transfered from Clerk"+(priorityLine+1)+" to Clerk"+(i+1));
								done=true;
								break;
							}
						}
					else {
						for (int i = priorityLine+1; i < clerks.length; i++)
							if (lines[i]==goodLine) {
								clerks[i].getServiceLine().enqueue(clerks[priorityLine].getServiceLine().lastDequeue());
								lines[priorityLine]--;
								lines[i]++;
//								System.out.println("Transfered from Clerk"+(priorityLine+1)+" to Clerk"+(i+1));
								done=true;
								break;
							}							
						if (!done) 
							for (int i = 0; i < clerks.length; i++) 
								if (lines[i]==goodLine) {
									clerks[i].getServiceLine().enqueue(clerks[priorityLine].getServiceLine().lastDequeue());
									lines[priorityLine]--;
									lines[i]++;
//									System.out.println("Transfered from Clerk"+(priorityLine+1)+" to Clerk"+(i+1));
									done=true;
									break;
								}
					}
				}
			}
//			Service start
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
								lines[i]++;
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
		//Overpass calculation
		for (int i = 0; i < overpass.size()-1; i++) 
			for (int j = i+1; j < overpass.size(); j++) 
				if (overpass.get(i).getArrivalTime()>overpass.get(j).getArrivalTime())
					overpassCount++;
		overpassCount = overpassCount/customerTotal;
		String outEntry="\nMLMSBLL "+servers+":\t"+finalTime+"\t"+String.format("%.2f", averageWait)+"  \t"+String.format("%.2f", overpassCount);
		return outEntry;
	}
}
