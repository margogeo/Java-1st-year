//author Margarita Shimanskaia
//Queue based on abstract class
package queue;

public abstract class AbstractQueue implements Queue {
	protected int size;

    public void enqueue(Object element) {
        assert element != null;

        enqueueImpl(element);
        size++;
    }

    protected abstract void enqueueImpl(Object element);

    public Object element() {
        assert size > 0;
        return elementImpl();
    }

    protected abstract Object elementImpl();

    public Object dequeue() {
        assert size > 0;
        Object result = element();
		remove();
        size--;
        return result;
    }

    protected abstract void remove();

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }
     
	 public void clear() {
        size = 0;
		delete();
    }
	
	protected abstract void delete();
    
	public Object[] toArray() {
		Object array[] = new Object[size];
		return toArr(array);
	}
	
	protected abstract Object[] toArr(Object[] array);
}