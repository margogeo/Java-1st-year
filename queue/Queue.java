//author Margarita Shimanskaia
//queue interface and its contract
package queue;

public interface Queue {
	//Inv: queue != null
	
	//Post: ++size, a[size] = element
    void enqueue(Object element);
	
	//Pre: size > 0
	//Post: returns a[0], size = size - 1, delets a[0], a[1] -> a[0]
    Object dequeue();
	
	//Pre: size > 0
	//Post: returns a[0]
    Object element();
	
	//Post: returns size 
    int size();
	
	//Post: returns 0 if size == 0, otherwise 1
    boolean isEmpty();
	
	//Post: size = 0
    void clear();
	
	//Post: returns array of queue elements from head to the tail
	Object[] toArray();
}
