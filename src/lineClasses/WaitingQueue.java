package lineClasses;

import peopleClasses.Customer;

/**
 * Service line assigned to the Clerks
 * 
 * @author Raúl Vargas
 *
 */
public class WaitingQueue implements Queue<Customer>{
	/**
	 * Doubly linked Node class
	 * 
	 * @author Raúl Vargas
	 *
	 * @param <Customer>
	 */
	private static class Turn<Customer> { 
		private Customer c; 
		private Turn<Customer> next, prev;
		public Turn(Customer e) {
			c = e;
			next = null;
		}
		public Customer getElement() {
			return c;
		}
		public Turn<Customer> getNext() {
			return next;
		}
		public void setNext(Turn<Customer> next) {
			this.next = next;
		}
		public Turn<Customer> getPrev() {
			return prev;
		}
		public void setPrev(Turn<Customer> prev) {
			this.prev = prev;
		}
	}	
	private Turn<Customer> first, last;
	private int size; 
	
	public WaitingQueue() {
		first = last = null; 
		size = 0; 
	}


	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size==0;
	}

	public Customer first() {
		return first.getElement();
	}
	
	public Customer last() {
		return last.getElement();
	}

	public void enqueue(Customer person) {
		Turn<Customer> nuevo = new Turn<Customer>(person);
		if (isEmpty()) {
			first=nuevo;
		}
		else {
			last.setNext(nuevo);
			nuevo.setPrev(last);
		}
		last=nuevo;
		size++;
	}

	public Customer dequeue() {
		if (this.isEmpty())
			return null;
		Customer target = first.getElement();
		Turn<Customer> cleanUp = first;
		if (size==1)
			last = null;
		first = first.getNext();
		if (first != null)
			first.setPrev(null);
		cleanUp.setNext(null);
		cleanUp.setPrev(null);
		size--;
		return target;
	}
	
	public Customer lastDequeue() {
		if (this.isEmpty())
			return null;
		Customer target = last.getElement();
		Turn<Customer> cleanUp = last;
		if (size==1)
			first=null;
		last = last.getPrev();
		if (last != null)
			last.setNext(null);
		cleanUp.setNext(null);
		cleanUp.setPrev(null);
		size--;
		return target;
	}
	
	public int getTime() {
		int sum=0;
		Turn<Customer> current = first;
		while (current.getElement()!=null) {
			sum+=current.getElement().getServiceTime();
			current=current.getNext();
		}
		return sum;
	}
}
