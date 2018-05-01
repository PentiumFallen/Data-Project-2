package managerClasses;

import java.util.ArrayList;

import lineClasses.ArrivalQueue;
import lineClasses.WaitingQueue;
import peopleClasses.Clerk;
import peopleClasses.Customer;

/**
 * Manager class for Multiple Line Multiple Server Balanced Waiting Time approach
 * 
 * @author Raúl Vargas
 *
 */
public class MLMSBWT_Manager {
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
//	private boolean transferCheck = true;

	/**
	 * Constructor
	 * @param servers amount of servers to assign
	 * @param arrival ArrivalQueue to use
	 */
	public MLMSBWT_Manager(int servers, ArrivalQueue arrival) {
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
//		if (servers==1)
//			transferCheck=false;
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
			//Transfer
			//This Transfer block is only here to prove the point that a correctly implemented
			//MSMLBWT will never use a transfer. It can be commented without damaging the project
//			while (transferCheck){
//				int goodLine = lines[0];
//				int badLine = lines[0];
//				int transfer = 0;
//				for (int i = 1; i < lines.length; i++) {
//					if (lines[i]<goodLine)
//						goodLine = lines[i];
//					if (lines[i]>badLine)
//						badLine = lines[i];
//				}
//				if (badLine==0)
//					break;
//				for (int i = 0; i < lines.length; i++)
//					if (lines[i]==badLine) {
//						if (clerks[i].getServiceLine().isEmpty())
//							break;
//						else {
//							transfer = clerks[i].getLastCustomer().getServiceTime();
//							break;
//						}
//					}
//				if (transfer==0)
//					break;
//				if ((goodLine+transfer)>(badLine-transfer))
//					break;
//				else {
//					int priorityArrival = currentTime;
//					int priorityLine = 0;
//					for (int i = 0; i < lines.length; i++) 
//						if (lines[i]==badLine) 
//							if (!clerks[i].getServiceLine().isEmpty() && clerks[i].getLastCustomer().getArrivalTime()<priorityArrival) {
//								priorityArrival = clerks[i].getLastCustomer().getArrivalTime();
//								priorityLine = i;
//							}
//					boolean done = false;
//					if (priorityLine==clerks.length-1)
//						for (int i = 0; i < clerks.length; i++) {
//							if (lines[i]==goodLine) {
//								Customer target = clerks[priorityLine].getServiceLine().lastDequeue();
//								clerks[i].getServiceLine().enqueue(target);
//								lines[priorityLine]-=target.getServiceTime();
//								lines[i]-=target.getServiceTime();
////								System.out.println("Transfered from Clerk"+(priorityLine+1)+" to Clerk"+(i+1));
//								done=true;
//								break;
//							}
//						}
//					else {
//						for (int i = priorityLine+1; i < clerks.length; i++)
//							if (!clerks[priorityLine].getServiceLine().isEmpty())
//								if (lines[i]==goodLine) {
//									Customer target = clerks[priorityLine].getServiceLine().lastDequeue();
//									clerks[i].getServiceLine().enqueue(target);
//									lines[priorityLine]-=target.getServiceTime();
//									lines[i]-=target.getServiceTime();
////									System.out.println("Transfered from Clerk"+(priorityLine+1)+" to Clerk"+(i+1));
//									done=true;
//									break;
//								}							
//						if (!done) 
//							for (int i = 0; i < clerks.length; i++) 
//								if (!clerks[priorityLine].getServiceLine().isEmpty())
//									if (lines[i]==goodLine) {
//										Customer target = clerks[priorityLine].getServiceLine().lastDequeue();
//										clerks[i].getServiceLine().enqueue(target);
//										lines[priorityLine]-=target.getServiceTime();
//										lines[i]-=target.getServiceTime();
////										System.out.println("Transfered from Clerk"+(priorityLine+1)+" to Clerk"+(i+1));
//										done=true;
//										break;
//									}
//					}
//				}
//			}
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
					int peopleTemp = customerTotal*12;
					int targetLine=0;
					int serv = arrival.first().getServiceTime();
					for (int i = 0; i < lines.length; i++) 
						if (lines[i]<peopleTemp) {
							peopleTemp=lines[i];
							targetLine=i;
						}
					if (arrival.first().getArrivalTime()==currentTime) {
						Customer target = arrival.dequeue();
						clerks[targetLine].getServiceLine().enqueue(target);
						clerks[targetLine].setCustomersLeft(clerks[targetLine].getCustomersLeft()+1);
						lines[targetLine]+=target.getServiceTime();
//						System.out.println("Customer arrives at "+currentTime+" for "+serv+" in Line "+(targetLine+1));
					}
				}
				else
					break;
				continue;
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
			for (int i = 0; i < lines.length; i++) 
				if (lines[i]!=0)
					lines[i]--;

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
			for (int i = 0; i < lines.length; i++) { 
				lines[i]-=(next-currentTime);
				if (lines[i]<0)
					lines[i]=0;
			}
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
		String outEntry="\nMLMSBWT "+servers+":\t"+finalTime+"\t"+String.format("%.2f", averageWait)+"  \t"+String.format("%.2f", overpassCount);
		return outEntry;
	}
}
