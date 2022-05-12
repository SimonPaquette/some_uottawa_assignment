/**
 * Array Heap implimentation of a double-ended priority queue
 * @author Simon Paquette
 */
public class HeapPriorityQueue <K extends Comparable, V> implements PriorityQueue <K, V> {

	private Entry [] minHeap;	//heap to store the min value of an association
	private Entry [] maxHeap;	//heap to store the max value of an association
	private Entry buffer;		//store an individual element
	private int tail;    		//Index of last element in each heap

	/**
	 * Default constructor
	 */
	public HeapPriorityQueue() {
		this(100);
	}


	/**
	 * HeapPriorityQueue constructor with max storage of size elements
	 */
	public HeapPriorityQueue( int size ) {
		minHeap = new Entry [ size/2 ];
		maxHeap = new Entry [ size/2 ];
		tail    = -1;
	}


	/****************************************************
	*
	*             Priority Queue Methods
	*
	****************************************************/

	/**
	 * Returns the number of items in the priority queue.
	 * O(1)
	 * @return number of items
	 */
	public int size() {
		int size = 2*(tail + 1);	//2 heap and maybe a buffer
		if (buffer != null) {
			size++;
		}
		return size;
	} /* size */


	/**
	 * Tests whether the priority queue is empty.
	 * O(1)
	 * @return true if the priority queue is empty, false otherwise
	 */
	public boolean isEmpty() {

		return (tail < 0 && buffer==null);
	} /* isEmpty */


	/**
	 * Inserts a key-value pair and returns the entry created.
	 * O(log(n))
	 * @param key     the key of the new entry
	 * @param value   the associated value of the new entry
	 * @param ref     a reference to the associated entry in the othe heap
	 * @return the entry storing the new key-value pair
	 * @throws IllegalArgumentException if the heap is full
	 */
	public Entry <K, V>insert( K key, V value) throws IllegalArgumentException {
		if( tail == minHeap.length - 1 )
			throw new IllegalArgumentException ( "Heap Overflow" );
		Entry <K, V>    e = new Entry <> ( key, value );
		//insert in the buffer
		if (buffer == null) {		
			buffer = e;
		//create an association with the buffer
		} else {					
			tail++;
			//set information
			e.setAssociate(buffer);
			e.setIndex(tail);
			buffer.setAssociate(e);
			buffer.setIndex(tail);
			//the new entry is smaller than the buffer
			if (e.getKey().compareTo(buffer.getKey()) < 0) {		
				minHeap[tail] = e;
				maxHeap[tail] = buffer;
			//the new entry is greater than the buffer
			} else if (e.getKey().compareTo(buffer.getKey()) > 0) {	
				minHeap[tail] = buffer;
				maxHeap[tail] = e;	
			}
			//restore the heap condition
			upMinHeap(tail);
			upMaxHeap(tail);
			buffer = null;	
		}
		return e;
	} /* insert */


	/**
	 * Returns (but does not remove) an entry with minimal key.
	 * O(1)
	 * @return entry having a minimal key (or null if empty)
	 */
	public Entry <K, V> min() {
		if( isEmpty() )
			return null;
		//no buffer
		if (buffer == null) {												
			return minHeap[0];
		//no element in the heap
		} else if (tail == -1) {
			return buffer;
		//buffer smaller than the root of minHeap
		} else if (buffer.getKey().compareTo(minHeap[0].getKey()) < 0) {	
			return buffer;
		//buffer greater than the root of minHeap
		} else {															
			return minHeap[0];
		}	
	} /* min */


	/**
	 * Returns (but does not remove) an entry with maximal key.
	 * O(1)
	 * @return entry having a maximal key (or null if empty)
	 */
	public Entry <K, V> max() {
		if( isEmpty() )
			return null;
		//no buffer
		if (buffer == null) {												
			return maxHeap[0];
		//no element in the heap
		} else if (tail == -1) {
			return buffer;
		//buffer greater than the root of minHeap
		} else if (buffer.getKey().compareTo(maxHeap[0].getKey()) > 0) {
			return buffer;
		//buffer smaller than the root of minHeap
		} else {
			return maxHeap[0];
		}
	} /* max */


	/**
	 * Removes and returns an entry with minimal key.
	 * O(log(n))
	 * @return the removed entry (or null if empty)
	 */
	public Entry <K, V> removeMin() {
		if( isEmpty() )
			return null;

		Entry <K, V> ret, toInsert;
		int index;
		
		//no buffer
		if (buffer == null) {													
			
			//information retrieval
			ret = minHeap[0];
			buffer = ret.getAssociate();
			index = buffer.getIndex();

			//separate associate entry from the heap
			ret.setAssociate(null);
			buffer.setAssociate(null);
			ret.setIndex(-1);
			buffer.setIndex(-1);

			//restore the heap condition
			minHeap[0] = minHeap[tail];
			downMinHeap(0);
			maxHeap[index] = maxHeap[tail];
			downMaxHeap(index);

			//remove last element in the heap
			minHeap[tail] = maxHeap[tail] = null;
			tail--;

		//no element in the heap
		} else if (tail == -1) {
			ret = buffer;
			buffer = null;

		//buffer smaller than the root of minHeap
		} else if (buffer.getKey().compareTo(minHeap[0].getKey()) < 0) {
			ret = buffer;
			buffer = null;

		//buffer greater than the root of minHeap
		} else {
			
			//information retrieval
			ret = minHeap[0];
			toInsert = ret.getAssociate();
			index = toInsert.getIndex();

			//separate associate entry from the heap
			ret.setAssociate(null);
			toInsert.setAssociate(null);
			ret.setIndex(-1);
			toInsert.setIndex(-1);

			//restore the heap condition
			minHeap[0] = minHeap[tail];
			downMinHeap(0);
			maxHeap[index] = maxHeap[tail];
			downMaxHeap(index);
			
			//remove last element in the heap
			minHeap[tail] = maxHeap[tail] = null;
			tail--;
			
			//create a new association with the buffer to reinsert the removed element (Entry toInsert)
			insert(toInsert.getKey(),toInsert.getValue());	
		}

		return ret;
	} /* removeMin */


	/**
	 * Removes and returns an entry with maximal key.
	 * O(log(n))
	 * @return the removed entry (or null if empty)
	 */
	public Entry <K, V> removeMax() {
		if( isEmpty() )
			return null;

		Entry <K, V> ret, toInsert;
		int index;

		//no buffer
		if (buffer == null) {
			
			//information retrieval
			ret = maxHeap[0];
			buffer = ret.getAssociate();
			index = buffer.getIndex();
			
			//separate associate entry from the heap
			ret.setAssociate(null);
			buffer.setAssociate(null);
			ret.setIndex(-1);
			buffer.setIndex(-1);

			//restore the heap condition
			maxHeap[0] = maxHeap[tail];
			downMaxHeap(0);
			minHeap[index] = minHeap[tail];
			downMinHeap(index);

			//remove last element in the heap
			minHeap[tail] = maxHeap[tail] = null;
			tail--;

		//no element in the heap
		} else if (tail == -1) {
			ret = buffer;
			buffer = null;

		//buffer greater than the root of minHeap
		} else if (buffer.getKey().compareTo(maxHeap[0].getKey()) > 0) {
			ret = buffer;
			buffer = null;

		//buffer smaller than the root of minHeap
		} else {

			//information retrieval
			ret = maxHeap[0];
			toInsert = ret.getAssociate();
			index = toInsert.getIndex();

			//separate associate entry from the heap
			ret.setAssociate(null);
			toInsert.setAssociate(null);
			ret.setIndex(-1);
			toInsert.setIndex(-1);

			//restore the heap condition
			maxHeap[0] = maxHeap[tail];
			downMaxHeap(0);
			minHeap[index] = minHeap[tail];
			downMinHeap(index);

			//remove last element in the heap
			minHeap[tail] = maxHeap[tail] = null;
			tail--;
			
			//create a new association with the buffer to reinsert the removed element (Entry toInsert)
			insert(toInsert.getKey(),toInsert.getValue());
		}

		return ret;
	} /* removeMax */


	/****************************************************
	*
	*           Methods for Heap Operations
	*
	****************************************************/

	/**
	 * Algorithm to place element after insertion at the tail. up the min-key
	 * O(log(n))
	 */
	private void upMinHeap( int location ) {
		if( location == 0 ) return;

		int    parent = parent ( location );

		if( minHeap [ parent ].key.compareTo ( minHeap [ location ].key ) > 0 ) {
			swap ( location, parent, minHeap );
			upMinHeap ( parent );
		}
	} /* upMinHeap */


	/**
	 * Algorithm to place element after insertion at the tail. up the max-key
	 * O(log(n))
	 */
	private void upMaxHeap( int location ) {
		if( location == 0 ) return;

		int    parent = parent ( location );

		if( maxHeap [ parent ].key.compareTo ( maxHeap [ location ].key ) < 0 ) {
			swap ( location, parent, maxHeap );
			upMaxHeap ( parent );
		}
	} /* upMaxHeap */


	/**
	 * Algorithm to place element after removal of root and tail element placed at root. down the min-key
	 * O(log(n))
	 */
	private void downMinHeap( int location ) {
		int    left  = (location * 2) + 1;
		int    right = (location * 2) + 2;

		//Both children null or out of bound
		if( left > tail ) return;

		//left in right out;
		if( left == tail ) {
			if( minHeap [ location ].key.compareTo ( minHeap [ left ].key ) > 0 )
				swap ( location, left, minHeap );
			return;
		}

		int    toSwap = (minHeap [ left ].key.compareTo ( minHeap [ right ].key ) < 0) ?
		                left : right;

		if( minHeap [ location ].key.compareTo ( minHeap [ toSwap ].key ) > 0 ) {
			swap ( location, toSwap, minHeap );
			downMinHeap ( toSwap );
		}
	} /* downMinHeap */


	/**
	 * Algorithm to place element after removal of root and tail element placed at root. down the max-key
	 * O(log(n))
	 */
	private void downMaxHeap( int location ) {
		int    left  = (location * 2) + 1;
		int    right = (location * 2) + 2;

		//Both children null or out of bound
		if( left > tail ) return;

		//left in right out;
		if( left == tail ) {
			if( maxHeap [ location ].key.compareTo ( maxHeap [ left ].key ) < 0 )
				swap ( location, left, maxHeap );
			return;
		}

		int    toSwap = (maxHeap [ left ].key.compareTo ( maxHeap [ right ].key ) > 0) ?
		                left : right;

		if( maxHeap [ location ].key.compareTo ( maxHeap [ toSwap ].key ) < 0 ) {
			swap ( location, toSwap, maxHeap );
			downMaxHeap ( toSwap );
		}
	} /* downMaxHeap */


	/**
	 * Find parent of a given location,
	 * Parent of the root is the root
	 * O(1)
	 */
	private int parent( int location ) {
		return (location - 1) / 2;
	} /* parent */


	/**
	 * Inplace swap of 2 elements, assumes locations are in the given heap
	 * O(1)
	 */
	private void swap( int location1, int location2, Entry[] heap ) {
		Entry <K, V>    temp = heap [ location1 ];
		heap [ location1 ] = heap [ location2 ];
		heap [ location2 ] = temp;
		
		heap[location1].index= location1;
		heap[location2].index= location2;
	} /* swap */


	/**
	 * print each value in the structure
	 */
    public void print() {
		
		System.out.println("\nprint..");
		System.out.println("buffer : ");
		if (buffer!=null) {
			System.out.println("("+ buffer.key.toString() + "," + buffer.value.toString() + ")");
		}
		System.out.println("minHeap : ");
		for (Entry<K,V> e : minHeap){
			if (e==null) {
				break;
			}
		  	System.out.println ( "(" + e.key.toString() + "," + e.value.toString() + ":" + e.index + "), " );
		}
		System.out.println("maxHeap : ");
		for (Entry<K,V> e : maxHeap){
			if (e==null) {
				break;
			}
		  	System.out.println ( "(" + e.key.toString() + "," + e.value.toString() + ":" + e.index + "), " );
		}
	} /* print */

}
