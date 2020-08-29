package queue;


public class ArrayQueue extends AbstractQueue {
    private int left = 0;
    private Object[] items = new Object[32];
	
    private int next() {
        return (left + size) % items.length;
    }
    
	protected void enqueueImpl(Object element) {
        items[next()] = element;
        checkArraySize(size + 1);
    }

    private void checkArraySize(int capacity) {
        if (capacity < items.length - 1) {
            return;
        }
        Object[] newItems = new Object[2 * capacity];
		for(int j = 0; j <= size ; j++) {
            newItems[j] = items[left];
            left = (left + 1) % items.length;
        }
        items = newItems;
        left = 0;
    }

    protected void remove() {
        left = (left + 1) % items.length;
    }

    protected Object elementImpl() {
		return items[left];
    }
    
	protected void delete() {
		items = new Object[32];
        left = 0;
	}
	
	protected Object[] toArr(Object[] array) {
        for (int i = 0; i < size; i++) {
            array[i] = items[(left + i) % items.length];
        }
        return array;
    }
}