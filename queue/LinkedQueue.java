package queue;

public class LinkedQueue extends AbstractQueue {
    private Node head;
	private Node tail;
	static int ms = 100;
    
	protected void enqueueImpl(Object element) {
        tail = new Node(element, tail);
		if (head == null) {
			head = tail;
		}
    }

    protected void remove() {
		if (head.last != null) {
			head.last.next = null;
		}
        head = head.last;
		if (head == null) {
			tail = null;
		}
    }

    protected Object elementImpl() {
        return head.value;
    }
 
    protected void delete() {
		head = null;
		tail = null;
	}	
	
	protected Object[] toArr(Object[] array) {
		Node nod = head;
        for (int i = 0; i < size; i++) {
            array[i] = nod.value;
			nod = nod.last;
        }
        return array;
    }

    private class Node {
        private Object value;
        private Node next;
		private Node last;
        public Node(Object value, Node next) {
            assert value != null;
            this.value = value;
			if (next != null) {
				next.last = this;
			}
            this.next = next;
			this.last = null;
        }
    }
}
