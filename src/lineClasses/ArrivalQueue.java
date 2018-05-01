package lineClasses;

import peopleClasses.Customer;

/**
 * The list of people before arriving in order of arrival
 * 
 * @author Raúl Vargas
 *
 */
public class ArrivalQueue implements Queue<Customer>{
	/**
	 * Singly linked Node class
	 * 
	 * @author Raúl Vargas
	 *
	 * @param <Customer>
	 */
	private static class Turn<Customer> { 
		private Customer c; 
		private Turn<Customer> next;
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
	}	
	private Turn<Customer> first, last;
	private int size; 
	
	public ArrivalQueue() {
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

	public void enqueue(Customer person) {
		Turn<Customer> nuevo = new Turn<Customer>(person);
		if (isEmpty()) {
			first=nuevo;
		}
		else {
			last.setNext(nuevo);
		}
		last=nuevo;
		size++;
	}

	public Customer dequeue() {
		if (this.isEmpty())
			return null;
		Customer target = first.getElement();
		Turn<Customer> cleanup = first;
		if (size==1)
			last = null;
		first = first.getNext();
		cleanup.setNext(null);
		size--;
		return target;
	}

	public ArrivalQueue copy() {
		int originalSize = size;
		ArrivalQueue copy = new ArrivalQueue();
		for (int i=0; i<originalSize; i++) {
			Customer customerTemp = this.dequeue();
			this.enqueue(customerTemp);
			copy.enqueue(customerTemp);
		}
		return copy;
	}
}