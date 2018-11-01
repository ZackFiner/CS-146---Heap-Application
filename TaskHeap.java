/**
 * A heap of tasks, the key for each task is it's priority ID
 * @author Zackary Finer
 *
 */

public class TaskHeap {
	//TODO: add and use an exchange method
	/**
	 * The size of the heap
	 */
	private int heapSize=0;//member variable for holding a given heap's size, this will be initialized
	/**
	 * Exchange two entries in an array
	 * @param A - Task[] of the array
	 * @param i - Integer index of the first item to be exchanged
	 * @param j - Integer index of the second item to be exchanged
	 */
	public static void exchange(Task[] A, int i, int j)
	{
		Task temp = A[i];//assign the task at the first index to a temporary variable
		A[i] = A[j];//assign the first index to the second
		A[j] = temp;//assign the second to the temporary variable
	}
	/**
	 * Returns the current size of the heap
	 * @return Integer of the heap size
	 */
	public int getHeapSize()
	{
		return heapSize;//return the heap size
	}
	
	/**
	 * Returns the index of the parent of a given node
	 * @param i - Integer of the index of a node
	 * @return Integer of the index of the given node's parent, -1 if the node has no parent
	 */
	public static int parent(int i)
	{
		/*
		 * So there is some minor deviation in in index
		 * this is because i've had to compensate for the fact that java indexes from 0...n-1
		 * as opposed to 1...n
		 */
		return ((i+1)/2)-1;//returns the index of the parent
	}
	
	/**
	 * Returns the index of the left child node index of a given node index
	 * @param i - Integer of the left child node index
	 * @return Integer of the index of the left child node
	 */
	public static int left(int i)
	{
		/*
		 * So there is some minor deviation in in index
		 * this is because i've had to compensate for the fact that java indexes from 0...n-1
		 * as opposed to 1...n
		 */
		return 2*(i+1)-1;//returns the left child index of the given node index
	}
	
	/**
	 * Returns the index of the right child node index of a given node index
	 * @param i - Integer of the right child node index
	 * @return Integer of the index of the right child node
	 */
	public static int right(int i)
	{
		/*
		 * So there is some minor deviation in in index
		 * this is because i've had to compensate for the fact that java indexes from 0...n-1
		 * as opposed to 1...n
		 */
		return (2*(i+1));//returns the right child index of the given node index
	}
	
	/**
	 * Returns the maximum (highest priority) task in a given Task Heap
	 * @param A - Task[] of the Task Heap
	 * @return Task with the highest priority in the heap
	 */
	public Task heapMax(Task[] A)
	{
		return A[0];//returns the first element of A, which is the max
	}
	
	/**
	 * Max-Heapifies - or ensures the integrity of a given node's sub heap
	 * @param A - Task[] of tasks in the heap tree
	 * @param i - Integer representing the index of the parent to be max-heapified
	 */
	public void maxHeapify(Task[] A, int i)
	{

		int l = left(i);//assign the integer l to the left index
		int r = right(i);//assign the integer r to the right index
		int largest;//declare the largest index variable, which will store the index of the task with the greatest priority
		
		/*
		 * First check the left child node.
		 * If the left index is in range, and the left child is greater than this parent.
		 * If it isn't, we make the parent the largest.
		 */
		if (l < heapSize && A[l].getPriority() > A[i].getPriority())
		{
			largest = l;//then then set the largest index is the left child
		}
		else
		{
			largest = i;//otherwise, the parent appears to be the largest
		}
		
		/*
		 * Now to check the right child node.
		 * If right index is in range, and the right child is greater than the largest,
		 * then we assign the largest to the right child's index.
		 */
		if (r < heapSize && A[r].getPriority() > A[largest].getPriority())
		{
			largest = r;//assign the largest to the right child node's index
		}
		
		if (largest!=i)//if we've identified a child node which has a higher priority than this parent
		{
			//exchange the child and parent:
			exchange(A, i, largest);
			
			maxHeapify(A, largest);//then check the integrity of that child node's sub heap tree
		}
	}
	
	/**
	 * Builds a priority heap from a given array of Tasks
	 * @param A - Task[] of tasks to be organized into a heap data structure
	 */
	public void buildMaxHeap(Task[] A)
	{
		
		heapSize = A.length;//assign the heap size to be the current number of elements in the array


		/*
		 * Below, i've made a small change to the indexing to compensate for java's rules
		 * heapsize/2 has 1 subtracted from it, if you do not do this there will always be
		 * one iteration of max heapify where nothing happens because the node being looked at has no children
		 * 
		 * However, this comes with a minor price - it is now necessary to check whether the heapSize is greater than 1
		 * because if it isn't, there will be an array index out of bounds exception thrown
		 */
		if (heapSize <= 1)//if there is one element in the heap, it is already heapified, do nothiong
			return;
		
		for (int i = (heapSize/2)-1; i >= 0; i--)//iterate from the index of the first node with children to the root
		{
			maxHeapify(A, i);//max Heapify each sub-heap
		}
	}
	/**
	 * A modified version of buildMaxHeap which is intended to operate on a buffer
	 * @param A - Task[] of tasks to be organized into a heap data structure
	 * @param heapSize - Integer representing the size of the heap currently allocated on the buffer
	 */
	public void buildMaxHeap(Task[] A, int heapSize)
	{
		this.heapSize = heapSize;//assign the heap size to the passed quantity for heapSize
		
		/*
		 * Below, i've made a small change to the indexing to compensate for java's rules
		 * heapSize/2 has 1 subtracted from it, if you do not do this there will always be
		 * one iteration of max heapify where nothing happens because the node being looked at has no children
		 * 
		 * However, this comes with a minor price - it is now necessary to check whether the heapSize is greater than 1
		 * because if it isn't, there will be an array index out of bounds exception thrown
		 */
		if (heapSize <= 1)//if there is one element in the heap, it is already heapified, do nothiong
			return;
		
		for (int i = (heapSize/2)-1; i >= 0; i--)//iterate from the index of the first node with children to the root
		{
			maxHeapify(A, i);//max Heapify each sub-heap
		}
	}
	/**
	 * Returns the maximum element in the heap and removes it from the heap
	 * @param A - Task[] representing the heap to be operated upon
	 * @return Task which has the highest priority in the heap
	 */
	public Task heapExtractMax(Task[] A)
	{
		if (heapSize<1)//if there are no entries in this heap
		{
			System.err.println("Error: Heap underflow");//print an error
			return null;//do nothing
		}
		Task max = A[0];//assign our maximum task to be returned to the first entry in the heap
		
		A[0] = A[heapSize-1];//then assign the first element in our heap to be the last element in the heap
		heapSize = heapSize-1;//reduce the heap size by 1
		
		/*
		 * Max-heapify the top node, as to ensure our heap is in proper order after replacing the first element with the last.
		 */
		maxHeapify(A, 0);
		
		return max;//return the maximum value that was in the heap
	}
	
	/**
	 * Increases the given key (in this case, the priority ID) of a given task by a specified amount in the heap
	 * then sifts it up to the correct position
	 * @param A - Task[] representing the heap to be operated upon
	 * @param i - index of the node to have it's key increased
	 * @param key - Task with the increase priorityID
	 */
	public void heapIncreaseKey(Task[] A, int i, Task key)
	{
		if (key.getPriority() < A[i].getPriority())//if the new priotiyID is less than the current priorityID of that node
		{
			System.err.println("Error: specified priorityID is less than the current priorityID of Task at Index "+i);//print an error
			return;//do nothing
		}
		
		/*
		 * Create a new Task with the increased PriorityID, but with all other state information the same as before.
		 */
		A[i] = new Task(key.getPriority(), A[i].getTimeInQueue(), A[i].getTitle());
		
		/*
		 * While we aren't looking at the root of the heap, and the parent of the current node has a lesser value than this one
		 * sift the node we changed up the heap tree until it reaches the correct position
		 */
		while (i > 0 && A[parent(i)].getPriority() < A[i].getPriority())
		{
			//exchange the parent with the child:
			exchange(A, parent(i), i);
			
			i = parent(i);//repeat this operation with the parent as the child
		}
	}
	/**
	 * Inserts a new task into the heap
	 * @param A - Task[] representing the heap to be operated upon
	 * @param key - Task which will be inserted into the heap
	 */
	public void maxHeapInsert(Task[] A, Task key)
	{
		if (heapSize+1 > A.length)//if we have hit the maximum number of entries in our heap
		{
			System.err.println("Error: heap overflow");//print an error
			return;//do nothing
		}
		heapSize = heapSize+1;//increase the heap size by 1
		
		/*
		 * Below, the following happens:
			 * 1. Create a new task which will serve as a place holder for the new task, 
			 *    it has the minimum priority so it will definately be over-written.
			 * 2. Increase the priority of the place holder task to what it should be.
		 */
		A[heapSize-1] = new Task(Integer.MIN_VALUE, key.getTimeInQueue(), key.getTitle());// 1
		heapIncreaseKey(A, heapSize-1, key);// 2
	}
	/**
	 * This is the recursive function used by the testHeapProperty function
	 * @param A - Task[] representing the heap to be tested
	 * @param heapSize - Integer representing the size of the heap
	 * @param i - Index of heap root to be tested
	 * @return boolean representing whether the heap is obeying the heap property
	 */
	private static boolean testSub(Task[] A, int heapSize, int i)
	{
		int l = left(i);//set left index to the left child of this node
		int r = right(i);//set right index to the right child of this node
		
		/*
		 * Initialize return boolean to be true, since if this child has no child nodes it is obeying the max heap property.
		 */
		boolean isOkay = true;
		
		if (l < heapSize)//if this node has a left child
		{
			if (A[i].getPriority() < A[l].getPriority())//if the left child has a greater priority than this one
			{
				return false;//return false; we have a problem
			}
			isOkay = testSub(A, heapSize,l);//otherwise, return whether that node's sub heap is obeying the heap property
			if (r < heapSize && isOkay)//if this node has a right child, and we've yet to identify a problem with the heap so far
			{
				if (A[i].getPriority() < A[r].getPriority())//if the right child has a greater priority than this one
				{
					return false;//return false; we have a problem
				}
				isOkay = testSub(A,heapSize,r);//otherwise, return whether that node's sub heap is obeying the heap property
			}
		}
		return isOkay;//return the final decision
		
	}
	/**
	 * Test's a given heap to ensure it is obeying the heap property
	 * @param A - Task[] representing the heap to be tested
	 * @param heapSize - Integer representing the size of the heap
	 * @return boolean representing whether the heap obeys the heap property or not
	 */
	public static boolean testHeapProperty(Task[] A, int heapSize)
	{
		return testSub(A, heapSize, 0);//use the recursive function testSub to determine whether the heap is obeying the heap property
	}
	/**
	 * Sorts a given array of tasks using heap operations, in the case of max heap, it will sort from smallest to greatest
	 * @param A - Task[] to be sorted
	 */
	public void heapSort(Task[] A)
	{
		buildMaxHeap(A);//heapify the array A
		for (int i = A.length-1; i >=1; i--)//iterate from the last entry in the array to the 2nd
		{
			//exchange the max value of the heap with the 
			exchange(A, i, 0);
			heapSize = heapSize-1;//decrease the size of the max heap
			maxHeapify(A, 0);//max-heapify the heap to ensure it's root is the max
		}
	}
	/**
	 * This is a modified version of the heapSort method which is intended to operate on a buffer
	 * @param A - Task[] representing the buffer of tasks
	 * @param heapSize - size of the current heap allocated on the buffer
	 */
	public void heapSort(Task[] A, int heapSize)
	{
		buildMaxHeap(A, heapSize);//heapify the array A
		int initialLength = heapSize;
		for (int i = initialLength-1; i >=1; i--)//iterate from the last entry in the array to the 2nd
		{
			//exchange the max value of the heap with the 
			exchange(A, i, 0);
			heapSize = heapSize-1;//decrease the size of the max heap
			maxHeapify(A, 0);//max-heapify the heap to ensure it's root is the max
		}
	}
	/**
	 * Utility Function for the print feature, it retrieves all nodes at a level of the tree
	 * @param A - Task[] representing the heap that we will be getting the nodes from
	 * @param depth - An integer representing the depth at which we are looking for the nodes
	 * @return Task[] of all nodes at the specified depth
	 */
	private Task[] getNodesAtDepth(Task[] A, int depth)
	{
		/*
		 * Handle scenario where the depth is 0
		 */
		if (depth <= 0)//if the depth is zero
		{
			//return the root of the heap
			Task[] _return = new Task[1];//create a new task array of size one
			_return[0] = A[0];//set the one element in it to be the root
			return _return;//return the new task array
		}
		
		/*
		 * Assign the correct variables for the range
			 * 1. Find the first index of the row, this is done by raising 2 to the power of depth minus 1
			 * 2. Find the last index of the row, similar to the first but we need to ensure we don't go out of bounds, 
			 *    so it's the min of the heap size and the power of 2
		 */
		int first = (int)Math.pow(2.0,(double)(depth))-1;// 1
		int nSize = Math.min((int)Math.pow(2.0,(double)(depth+1))-1,heapSize)-first;// 2
		
		Task[] _return = new Task[nSize];//initialize a new array which will hold all elements we find at the depth
		for (int i = 0; i < nSize; i++)//Iterate through the array up to nSize
		{
			_return[i] = A[i+first];//set the elements in our return array to be the correct elements from the heap row
		}
		return _return;//return the array of elements in a row at a certain depth
	}
	
	/**
	 * Utility Function for the print function, this one just multiplies a specified string by a given number
	 * @param a - String to be multiplied (repeated)
	 * @param b - Integer representing the amount of times we should multiply the specified string
	 * @return String composed of the given String multiplied by the specified amount
	 */
	private String multString(String a,int b)
	{
		String _return = "";//initialize the return string to be blank
		for (int i = 0; i < b; i++)//iterate b times
			_return+=a;//add a to the return each iteration
		return _return;//return the return string
	}
	
	/**
	 * Utility Function for the toString function, returns the sum of all string lengths in a given Task array
	 * @param A - Task[] to find the sum of all string lengths in
	 * @return Integer representing the sum of all string lengths
	 */
	private int getStringLength(Task[] A)
	{
		int l = 0;//initialize the sum of lengths to be 0
		for (Task c: A)//iterate through every task in A
			l+=c.toString().length();//each time adding the string length of that task to the sum
		
		return l;//return the sum
	}
	
	/**
	 * Returns a string representation of a given heap - has since been deprecated by heap tree frame
	 * @param A - Task[] representing the heap we will be returning a string representation of
	 * @return String of the string representation
	 */
	//@Deprecated
	public String toString(Task[] A)
	{
		String _return = "";//Initialize the return string to be blank
		
		/*
		 * Initialize variables which store the padding, maximum depth, and largest line of the given heap
		 * for string formatting purposes
			 * 1. Get the maximum depth of the current heap we are looking at, using floor(lg(n))+1. 
			 *    Floor is done through truncation to integer
			 * 2. Set the maximum number of characters in the largest row to to be the string length of the last row of the heap
			 * 3. Set the padding between nodes in each row to be the maximum number of characters in a 
			 *    given row divide by the number of entries in that row
		 */
		int maxDepth = (int)(Math.log(heapSize)/Math.log(2.0))+1;// 1
		int maxChar = getStringLength(getNodesAtDepth(A, maxDepth-1));// 2
		int padding = (maxChar*2 / (int)Math.pow(2.0, maxDepth))+1;// 3
		
		for (int i = 0; i < maxDepth; i++)//iterate through every level of the tree down to the leaves
		{
			Task[] currentRow = getNodesAtDepth(A, i);//set the current row to be the row at this iterations depth
			_return+=multString(" ", (maxDepth-1-i)*3);//add ' ' characters for padding, so the result resembles a triangle
			
			for (Task c: currentRow)//for every task in this row
			{
				_return += c.toString() + multString(" ", (padding-c.toString().length())+2);//add it to the return string
			}
			_return +="\n";//insert a new line for the next iteration
		}
		return _return;//return the string
	}
}
