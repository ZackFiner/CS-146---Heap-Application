/**
 * This class is responsible for scheduling tasks in a priority queue; it ultimately serves as a wrapper for the TaskHeap structure.
 * @author Zackary Finer
 *
 */
public class TaskScheduler {
	private TaskHeap priorityQueue = new TaskHeap();//a heap representing the priority queue of tasks
	private boolean increaseForWaitTime = false;
	public static final int TASK_INCRIMENT_WAITTIME = 100;//the maximum wait time before a task in the heap will have it's priority increased
	public static final int PRIORITY_INCREASE_AMOUNT = 10;//the amount by which a tasks' priority should be increased after it's been in queue for too long
	/**
	 * This method increases the priority of all nodes in the tree 
	 * which have been in the queue over a certain period of time
	 * @param A - Task[] of the heap to be operated upon
	 */
	private void increaseForWaitTime(Task[] A)
	{
		for (int i = 0; i < priorityQueue.getHeapSize(); i++)//for every task in the heap
		{
			if (A[i].getTimeInQueue()>TASK_INCRIMENT_WAITTIME)//if the wait time is greater than the max wait time
			{
				/*
				 * Increase the wait time by the amount specified in our constant, to the level of it's parent
				 * repeat this linearly depending on how many times over the wait time the task has been in queue
				 */
				int priorityIncreaseLevel =  A[i].getTimeInQueue()/TASK_INCRIMENT_WAITTIME;//this is how many times we need to increase priority
				int newPriorityID = A[i].getPriority();//set the prioirty to the original prioirty
				newPriorityID += priorityIncreaseLevel*PRIORITY_INCREASE_AMOUNT;//increase the tasks prioirty by a certain amount
				Task newKey = new Task(newPriorityID, A[i].getTimeInQueue(), A[i].getTitle());//create a new key with the increased priority
				priorityQueue.heapIncreaseKey(A, i, newKey);//insert update the key at the given index with the new priorityID
			}
		}
	}
	
	/**
	 * Returns a string representation of a given task scheduler
	 * @param A - Task[] representing the task scheduler 
	 * @return String of the task Scheduler
	 */
	@Deprecated
	public String toString(Task[] A)
	{
		return priorityQueue.toString(A);//return the string representation of the priorityQueue of A
	}
	public void increaseForWaitTime(boolean isTrue)
	{
		increaseForWaitTime = isTrue;
	}
	/**
	 * Builds a taskSchedular from a given array of tasks
	 * @param A - Task[] to be converted to be processed by the task scheduler
	 */
	public void buildTaskScheduler(Task[] A)
	{
		priorityQueue.buildMaxHeap(A);//build a task heap from A
		if (increaseForWaitTime)
			increaseForWaitTime(A);//increase the priority of any tasks which have been in the queue for too long
	}
	/**
	 * A modified version of buildTaskSchedular intended to operate on a buffer
	 * @param A - Task[] to be converted to be processed by the task scheduler
	 * @param bufferedSize - Integer representing the number of elements currently allocated on the buffer
	 */
	public void buildTaskScheduler(Task[] A, int bufferedSize)
	{
		priorityQueue.buildMaxHeap(A, bufferedSize);//build a task heap from A
		if (increaseForWaitTime)
			increaseForWaitTime(A);//increase the priority of any tasks which have been in the queue for too long
	}
	
	/**
	 * Adds a new task to the priority Queue
	 * @param A - Task[] representing the heap of tasks
	 * @param newTask - Task to be inserted into queue
	 */
	public void addTask(Task[] A, Task newTask)
	{
		priorityQueue.maxHeapInsert(A, newTask);//insertion is done by the heap
		if (increaseForWaitTime)
			increaseForWaitTime(A);//we compensate for wait time
	}
	
	/**
	 * Pops the top task off the heap
	 * @param A - Task[] represnting the heap
	 * @return Task that was on top of the heap
	 */
	public Task popTopTask(Task[] A)
	{
		return priorityQueue.heapExtractMax(A);//extraction is done by the heap
	}
	/**
	 * Increases the priority of a given task in the priority queue
	 * @param A - Task[] representing the task buffer
	 * @param i - Integer representing the index of the entry in the buffer we want to increase
	 * @param key - Task representing the amount we will increase the specified value by
	 */
	public void increaseTaskPriority(Task[] A, int i, Task key)
	{
		priorityQueue.heapIncreaseKey(A, i, key);
	}
}
