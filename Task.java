
/**
 * This class is intended to represent a task to be executed by our task scheduler.
 * It contains the state information on the amount of time it has been in queue and it's priority.
 * 
 * TimeInQueue: the amount of time this task has supposedly been in queue, this will be entered by the user
 * Priority ID: an integer representing the tasks priority, the higher the number the greater the priority
 * 
 * @author Zackary Finer
 * 
 */
public class Task {
	private int priorityID;//create the member variable for the priority ID, which will be used for comparison of tasks
	private int timeInQueue;//create another member variable to hold the amount of time this task has been in queue in nano seconds (us)
	private String title;//name of the task
	
	/**
	 * Constructor for Task, creates a task with the given priorityID which has been in queue for specified amount of time in nano seconds
	 * @param priorityID - Integer representing the priority of this task
	 * @param timeInQueue - Integer representing the number of seconds this task has been in queue
	 * @param title - String of the title for this task
	 */
	public Task(int priorityID, int timeInQueue, String title)
	{
		this.priorityID = priorityID;//assign the given priorityID argument to the member variable for priorityID
		this.timeInQueue = timeInQueue;//assign the given timeInQueue argument to the member variable for timeInQueue
		this.title = title;
	}
	
	/**
	 * Returns the priority of this task as an integer
	 * @return Integer of the priority ID
	 */
	public int getPriority()
	{
		return priorityID;//return the priority ID of this task
	}
	
	/**
	 * Returns the time this task has been in queue in nano seconds
	 * @return Integer of the number of nano seconds this task has been in queue
	 */
	public int getTimeInQueue()
	{
		return timeInQueue;//return the amount of time this task has been in Queue in nano seconds
	}
	
	/**
	 * Returns the title of this task
	 * @return String of the title of this task
	 */
	public String getTitle()
	{
		return title;
	}
	
	/**
	 * Returns whether this task has the same state data as another
	 */
	public boolean equals(Object o)
	{
		Task t = (Task)o;
		return t.priorityID==priorityID && t.timeInQueue==timeInQueue && t.title.equals(title);
	}
	
	/**
	 * Returns the string representation of a task
	 */
	public String toString()
	{
		return "["+title+", ID="+priorityID+", "+timeInQueue+" u/s]";//just return the title
	}
}
