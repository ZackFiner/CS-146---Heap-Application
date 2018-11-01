import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;



/**
 * This class serves to create a GUI which the user can use to add/remove tasks,
 * view the layout of the task scheduler
 * and test various features for this application
 * @author Zackary Finer
 *
 */
public class SchedulerGuiMain extends JFrame{
	private JPanel optionPanel;//option panel will hold buttons for adding/removing entries, as well as displaying the heap
	private JPanel editPanel;//edit panel will hold buttons for editing entries currently in the heap
	private JPanel editPanel2;//another edit panel
	private JButton addTaskB;//add task button
	private JButton popTopB;//remove task button
	private JButton showSortedB;//show the sorted tasks button
	private JButton showHeapDiagramB;//show heap diagram button
	private JButton addMultipleB;//button to add multiple entries at once
	private JButton increaseKeyB;//button to increase a selected entries key
	private JButton clearB;//button to clear all entries from the heap
	private JButton showRawHeapB;//button to show the array representation of the heap
	
	private JRadioButton autoSiftB;//radio which allows the user to toggle between auto sift or not
	
	private JTextField taskNameEntry;//text field for entry of task names
	private JTextField taskWaitTimeEntry;//text field for entry of task delay time
	
	private JScrollPane listScroll;//a scroll pane which will contain a JList of tasks
	private DefaultListModel<Task> taskListModel;//the list model used by the JList
	private JList<Task> taskList;//the JList which will show the user all tasks currently entered
	private HeapTreeFrame treeDisplay = null;//this will be the reference to the tree display window
	public static final int BUFFER_SIZE = 512;//size of the buffer, which will hold the heap
	public static final int MAX_PRIORITY = 1500;
	private Task[] buffer;//the buffer which will hold the heap
	private int allocated = 0;//the number of items allocated on the heap, at launch always zero
	private TaskScheduler taskPriorityQueue;//the priority queue which will manage the heap in the buffer
	private Random rng = new Random(System.currentTimeMillis());//A random number generator which will be used to generate priorityIDs
	
	public SchedulerGuiMain() {
		super("Task Schedular");// call super class, since this implements JFrame
		
		buffer = new Task[BUFFER_SIZE];//initialize the buffer to be the size specified above
		taskPriorityQueue = new TaskScheduler();//create the task schedular
		taskPriorityQueue.buildTaskScheduler(buffer, allocated);//initialize the state information for the heap
		

		setLayout(new BorderLayout());//i like borderlayout, so i'll use it for this window
		
		taskNameEntry = new JTextField(10);//initialize the name entry text field to be 10 columns wide
		taskNameEntry.setToolTipText("Insert Task Name");//set the tooltip text for it
		taskWaitTimeEntry = new JTextField(5);//initialize the time entry text field to be 10 columns wide
		/*
		 * Set the tooltip text for that aswell
		 */
		taskWaitTimeEntry.setToolTipText("(Optional) Insert the amount of time this task will have been in queue");
		addTaskB = new JButton("Add Task");//initialize the add task button
		popTopB = new JButton("Pop Top Entry");//and the pop entry button
		showSortedB = new JButton("Show Sorted Task List");//and the show sorted arraylist buton
		showHeapDiagramB = new JButton("Show Heap Diagram");//and the show heap diagram button
		addMultipleB = new JButton("Add Multiple Tasks");//and add multiple entries
		increaseKeyB = new JButton("Increase Priority");
		autoSiftB = new JRadioButton("Auto Correct for Time in Queue");
		taskListModel = new DefaultListModel<>();//initialize the task list model to be a default list model
		clearB = new JButton("Clear Heap");
		showRawHeapB = new JButton("Show Heap Array");
		
		taskList = new JList<Task>(taskListModel);//initialize the taskList to show the task list model
		taskList.setLayoutOrientation(JList.VERTICAL_WRAP);//make it so all tasks are shown in one column
		taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		taskList.setVisibleRowCount(-1);//make it so all rows are visible at any given time
		/*
		 * initialize the list scroller to hold the task list
		 * and add a scroll bar as needed
		 */
		listScroll = new JScrollPane(taskList,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		listScroll.setPreferredSize(new Dimension(250,300));//set the preferred size to 250x300
		/*
		 * The panel which will hold the buttons should have all buttons come consecutivly in one
		 * row, so we'll use a flow layout for that
		 */
		optionPanel = new JPanel(new FlowLayout());		
		editPanel = new JPanel();
		editPanel.setLayout(new BoxLayout(editPanel, BoxLayout.Y_AXIS));
		editPanel2 = new JPanel(new GridLayout(3, 2));
		/*
		 * set up the action listners for each button
		 * starting with the add task button
		 */
		addTaskB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				/*
				 * if the task name entry field has nothing in it, show the user
				 * a notification telling them they need to enter a task name
				 * 
				 * otherwise proceed
				 */
				if (taskNameEntry.getText().length()>0)
				{
					int waitTime;//this will hold the wait time the user entered
					try {
						/*
						 * If the user didn't leave the wait time field blank
						 * parse what they input and assign it to wait time
						 * if what they entered was not a valid number, tell them
						 * to re-enter a valid number or leave it blank
						 */
						if (taskWaitTimeEntry.getText().length()>0)
						{
							waitTime = Integer.parseInt(taskWaitTimeEntry.getText());
						}
						else
						{
							waitTime = 0;//set wait time to zero by default
						}
						/*
						 * Make sure that there's enough space on the buffer to add an entry.
						 * if there isn't, tell the user they need to pop an entry off the heap
						 */
						if (allocated < BUFFER_SIZE)
						{
							/*
							 * Create a new task object with the specified name and wait time
							 * then give it a randomly generated priority ID
							 */
							
							Task newTask =  new Task(getAvialableID(), waitTime, taskNameEntry.getText());
							taskPriorityQueue.addTask(buffer,newTask);//add the task to our priority queue on the buffer
							allocated++;//we are allocating a new entry on the heap, so increase the size of allocated
							taskListModel.addElement(newTask);//add the task for display on our task list
							
							/*
							 * if the tree diagram display window is currently open,
							 * update the heap it's displaying so it includes the entry
							 * we just added
							 */
							updateTree();
							repaint();//repaint this window
						}
						else
						{
							/*
							 * If there was not enough space on the heap for the new entry, let the user know with a message
							 */
							JOptionPane.showMessageDialog(null, "Buffer Overflow, you have to remove an entry before you can add another");
						}

					}
					catch (NumberFormatException e2)
					{
						/*
						 * if the user entered a bad value for the wait time, let them know with a message
						 */
						JOptionPane.showMessageDialog(null, "Please enter an integer for the wait time or leave wait time blank");
						return;
					}
				}
				else
				{
					/*
					 * show the user a message telling them to enter a task name
					 */
					JOptionPane.showMessageDialog(null, "Please enter a task name");
				}
			}
		});
		
		/*
		 * add the action listener to remove the top element from the priority queue
		 */
		popTopB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				/*
				 * If there is something allocated in the queue, perform the operation
				 * otherwise, let the user know there nothing is on the queue and therefore
				 * nothing to be removed
				 */
				if (allocated > 0)
				{
					/*
					 * remove the task using the priority queue pop top task operation
					 */
					Task returned = taskPriorityQueue.popTopTask(buffer);
					allocated--;//a task has been removed, so decrease the size of allocated by one
					/*
					 * Display information on the task that was removed from the top of the heap
					 * to the user
					 */
					JOptionPane.showMessageDialog(null, "Task "+returned.toString()+" was removed from the top of the heap");
					/*
					 * if there is nothing in the heap anymore, simply clear the task list
					 * which displays the tasks
					 * 
					 * otherwise, remove the element which was returned by the pop top task operation
					 * we called earlier
					 */
					if (allocated == 0)
					{
						taskListModel.clear();//clear list
					}
					else
					{
						taskListModel.removeElement(returned);//remove returned element
					}
					/*
					 * if the tree diagram display window is open, update it so it reflects
					 * the changes we made to the heap
					 */
					updateTree();
					repaint();//repaint this window
				}
				else
				{
					/*
					 * let the user know if there was nothing to be removed
					 */
					JOptionPane.showMessageDialog(null, "There are no entries to be removed");
				}
			}
		});
		/*
		 * Add the action listener for the show sorted button
		 */
		showSortedB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				/*
				 * If the heap is empty, tell the user there is nothing to sort.
				 * otherwise, show the user the sorted list
				 */
				if (allocated > 0)
				{
					/*
					 * for the sake of simplicity, i decided simply to copy the allocated
					 * region of the buffer into a temporary array for sorting
					 * 
					 * the heap sort algorithm still works in place
					 */
					Task[] temp = Arrays.copyOf(buffer, allocated);//copy the region to be sorted
					TaskHeap sorter = new TaskHeap();//create the heap sorter
					sorter.heapSort(temp);//sort the temporary array using heap sort

					/*
					 * Now, show the user the sorted array in a dialog box
					 */
					ArrayDisplayDialog display = new ArrayDisplayDialog(temp);
					display.setVisible(true);
				}
				else
				{
					/*
					 * tell the user that the heap was empty
					 */
					JOptionPane.showMessageDialog(null, "Buffer Underflow, there are no entries to sort");
				}
				
			}
		});
		
		/*
		 * Add the action listener for the show sorted button
		 */
		showRawHeapB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				/*
				 * If the heap is empty, tell the user there is nothing to sort.
				 * otherwise, show the user the sorted list
				 */
				if (allocated > 0)
				{
					/*
					 * for the sake of simplicity, i decided simply to copy the allocated
					 * region of the buffer into a temporary array for sorting
					 * 
					 * the heap sort algorithm still works in place
					 */
					Task[] temp = Arrays.copyOf(buffer, allocated);//copy the region to be sorted
					/*
					 * Now, show the user the sorted array in a dialog box
					 */
					ArrayDisplayDialog display = new ArrayDisplayDialog(temp);
					display.setVisible(true);
				}
				else
				{
					/*
					 * tell the user that the heap was empty
					 */
					JOptionPane.showMessageDialog(null, "Buffer underflow, there is nothing in the heap");
				}
				
			}
		});
		/*
		 * Below, we assign the 'this' reference to a temporary variable for use inside the
		 * in-line implementation of actionlistner for show heap diagram. This is necessary
		 * because due to the difference in scoping the 'this' reference will point to the actionlistner
		 * in its body instead of the window.
		 * 
		 */
		SchedulerGuiMain outer = this;//assign the self pointer to a temporary variable
		showHeapDiagramB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				/*
				 * If the show heap diagram display window is already open, simply bring it to the front.
				 * otherwise, create a new instance of the window
				 */
				if (treeDisplay==null || !treeDisplay.isDisplayable())
				{
					/*
					 * create the heap tree diagram display window to show the current state of the tree,
					 * and pass it the reference to this window so it can access the size of the heap
					 * itself for updating
					 */
					treeDisplay = new HeapTreeFrame(outer, buffer, allocated);
					treeDisplay.setVisible(true);//make the diagram display window visible
					/*
					 * Set the default close operation to dispose on close
					 * because we don't want it to exit our application, we simply want it
					 * to remove itself
					 */
					treeDisplay.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				}
				else
				{
					treeDisplay.toFront();//bring the window to the front otherwise
				}
			}
		});
		/*
		 * Add the action listner to the increase key button
		 */
		increaseKeyB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Task toIncrease = null;//intialize the task to be increased to null
				if ((toIncrease=taskList.getSelectedValue())!=null)//if the user has selected an entry to increase
				{
					int index = getIndexOf(toIncrease);//get the corresponding entry in the buffer to increase
					if (index>=0)//if an entry was found
					{
						int amount = 100;//set the increase amount to 100
						/*
						 * Below we create a task to pass into the the increase key function
						 * the priority of this task will be the priority of the task to be increased plus
						 * the ammount we want to increase it by above
						 */
						Task increased = new Task(toIncrease.getPriority()+amount, toIncrease.getTimeInQueue(), toIncrease.getTitle());
						/*
						 * Below, we perform the following operations:
						 * 1. Set the value of the task selected in the list model to be that of the increased task.
						 * this is necessary because if we want to perform this same operation again, we need the entry
						 * to match what is allocated in the heap in order to find it.
						 * 2. Use the increase task priority method (a wrapper for task heap's increase key method)
						 * to increase the priority of the desired key
						 * 3. Repaint the task list to reflect the changes we made
						 * 4. Update the tree viewer to reflect the changes we made
						 */
						taskListModel.set(taskList.getSelectedIndex(), increased);// 1
						taskPriorityQueue.increaseTaskPriority(buffer, index, increased);// 2
						taskList.repaint();// 3
						updateTree();// 4
					}
					else
					{
						/*
						 * If no entry could be found, show an error
						 */
						JOptionPane.showMessageDialog(null, "ERROR: The index of the selected entry couldn't be located in the heap");
					}
				}
				else
				{
					/*
					 * If the user did not select an entry, tell them to do so
					 */
					JOptionPane.showMessageDialog(null, "Please select an entry to increase");
				}
			}
		});
		/*
		 * Below, we set up the action listener for the add multiple task button
		 */
		addMultipleB.addActionListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (allocated < BUFFER_SIZE)//if there is enough space in the buffer to add entries
					new AddMultipleDialog(outer).setVisible(true);//create an add multiple dialog to prompt the user for more information
				else
					JOptionPane.showMessageDialog(null, "There is not enough space left in the buffer");//otherwise, tell them there isn't enough space
			}
		});
		/*
		 * Below, we set up an action listner for the auto sift feature
		 * this feature will automatically sift up entries which have a high wait time
		 */
		autoSiftB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				/*
				 * If auto sift is being toggled on
				 */
				if (autoSiftB.isSelected())
				{
					/*
					 * Show the user a warning message telling them that sifting up
					 * all entries may disturb the normal order of the heap.
					 */
					int decision = JOptionPane.showConfirmDialog(null, "Are You sure you want to enable autosift? Doing so will re-arrange the elements in the heap.");
					/*
					 * If they choose to continue dispite the warrning
					 */
					if (decision==JOptionPane.YES_OPTION)
					{
						//regenerate the heap with autosift on
						taskPriorityQueue.increaseForWaitTime(true);//set increase for wait time to true
						taskPriorityQueue.buildTaskScheduler(buffer, allocated);//rebuild the heap with these new rules
						hideOptions(true);//hide the options so the user can't corrupt the information
					}
					else
					{
						autoSiftB.setSelected(false);//otherwise, un check auto sift and exit.
					}
				}
				else
				{
					/*
					 * If auto sift was being un-toggled
					 * regenerate the heap without autosift on
					 */
					buffer = new Task[BUFFER_SIZE];//re-intialize the buffer to be empty
					allocated = 0;// set allocated to zero
					taskPriorityQueue.increaseForWaitTime(false);//set increase for wait time to false
					Object[] backup = taskListModel.toArray();//load the tasks being stored in the list mode, since they weren't modified
					for (int i = 0; i < backup.length; i++)//for every entry in the back up
					{
						buffer[i] = (Task)backup[i];//cast the back up entries to task and assign them to the buffer
						allocated++;//increment allocated since we're adding entries
					}
					taskPriorityQueue.buildTaskScheduler(buffer, allocated);//rebuild the heap using the entries we copied from the list model
					
					hideOptions(false);//allow the user edit the heap once again
				}
				updateTree();//update the tree if it's visible to reflect the changes made
			}
		});
		/*
		 * create the action listner for the clear heap button
		 */
		clearB.addActionListener((e) -> {clear();//clear the heap
										if (treeDisplay.isDisplayable()) treeDisplay.dispose();});//if the tree is displayed, close it
		
		
		editPanel2.add(addMultipleB);//put the multiple entries button in the second edit panel
		editPanel2.add(addTaskB);//then put the add task button in the same panel
		editPanel2.add(new JLabel("Task Name: "));//then add a label telling the user what the text field is
		editPanel2.add(taskNameEntry);//then add the task name entry text field
		editPanel2.add(new JLabel("Time In Queue in u/s: "));//then add another label
		editPanel2.add(taskWaitTimeEntry);//then add the wait time entry text field
		editPanel.add(increaseKeyB);//put the increase key method in the first edit panel
		editPanel.add(autoSiftB);//put the auto sift button in the first edit panel
		editPanel.add(clearB);//put the clear buffer button in the first
		optionPanel.add(popTopB);//add the pop top element button to the option panel
		optionPanel.add(showSortedB);//then add the show sorted button
		optionPanel.add(showHeapDiagramB);//then add the show heap diagram button
		optionPanel.add(showRawHeapB);
		
		add(editPanel,BorderLayout.CENTER);//put the first edit panel in the center
		add(editPanel2, BorderLayout.EAST);//then put the second edit panel to the right
		add(optionPanel, BorderLayout.NORTH);//then add that option panel to the top of this window
		add(listScroll, BorderLayout.WEST);//and add the task list to the right side
		pack();//then resize the window so everything fits
		
		
	}
	/**
	 * A method used for hiding edit butotns in the window
	 * @param hide - Boolean representing whether the option should be hidden
	 */
	public void hideOptions(boolean hide)
	{
		if (hide)//if hide is true
		{
			/*
			 * disable all buttons used for editing
			 */
			addMultipleB.setEnabled(false);
			addTaskB.setEnabled(false);
			taskNameEntry.setEnabled(false);
			taskWaitTimeEntry.setEnabled(false);
			increaseKeyB.setEnabled(false);
		}
		else//otherwise
		{
			/*
			 * Enable all buttons for editing
			 */
			addMultipleB.setEnabled(true);
			addTaskB.setEnabled(true);
			taskNameEntry.setEnabled(true);
			taskWaitTimeEntry.setEnabled(true);
			increaseKeyB.setEnabled(true);
		}
	}
	/**
	 * Returns the current number of tasks allocated on the buffer
	 * @return Integer representing the number of tasks allocated on the buffer
	 */
	public int getAllocated()
	{
		return allocated;
	}
	/**
	 * A function which adds entries to the displayed task list
	 * @param entries
	 */
	public void addEntries(Task[] entries)
	{
		if (entries.length+allocated <= BUFFER_SIZE)//if there is enough space in the buffer to add all entries
		{
			for (Task c: entries)//for every entry in the passed array
			{
				taskPriorityQueue.addTask(buffer, c);//add the task to the priority queue using the heap insert key method
				allocated++;//increment allocated
				taskListModel.addElement(c);//add the task to the task list so the user can select it
			}
		}
		/*
		 * if the tree diagram display window is open, update it so it reflects
		 * the changes we made to the heap
		 */
		updateTree();
	}
	/**
	 * A method used to determine whether a priorityID is already in the queue.
	 * Runs a linear search on the set of all entries currently allocated in the
	 * queue and returns whether a match has been identified
	 * @param buffer - Task[] representing the priority queue to be searched
	 * @param size - Integer representing the number of elements allocated in the queue
	 * @param ID - Integer representing the key, or element being searched for
	 * @return boolean representing whether a match was found
	 */
	private boolean containsID(Task[] buffer, int size, int ID)
	{
		for(int i = 0; i < size; i++)//for every entry allocated in the specified buffer
		{
			if (buffer[i].getPriority()==ID)//if the priority of this entry is the same as the passed key
				return true;//return true and exit
		}
		return false;//otherwise return false, we found nothing
	}
	/**
	 * Linear Search of the heap to find the first index of the specified key
	 * @param key - Task representing the entry we want to find
	 * @return Integer representing the index of the first occurance
	 */
	private int getIndexOf(Task key)
	{
		for (int i = 0; i < allocated; i++)//for every entry allocated in the heap
		{
			if (buffer[i].equals(key))//if the entry looking at is equal to the key
				return i;//return the index of this entry
		}
		return -1;//otherwise return -1, as we've found nothing and we need to tell the caller
	}
	/**
	 * Returns a new random available ID which is unique.
	 * @return Integer representing the priority ID
	 */
	public int getAvialableID()
	{
		int _return = -1;//Initialize the return to be -1
		boolean isUnique = false;//Initialize isUnique to be false
		while (!isUnique)//if the priority id we generated isn't unique
		{
			_return = rng.nextInt(MAX_PRIORITY)+1;//generate a new priority id
			isUnique = !containsID(buffer, allocated, _return);//test whether it's unique using the contains method
		}
		return _return;//return the unique priority id
	}
	/**
	 * A method which clears all the tasks from the priority queue
	 */
	private void clear()
	{
		buffer = new Task[BUFFER_SIZE];//re-intialize the buffer to point to a new empty array
		allocated = 0;//set allocated to zero since there is no longer anything allocated
		taskListModel.clear();//clear the task model of all entries
		taskPriorityQueue.buildTaskScheduler(buffer, allocated);//rebuild the priority queue using the clean buffer
		
		repaint();//repaint every thing to reflect the changes made
	}
	/**
	 * A method which updates the Tree Display window if it is open
	 */
	private void updateTree()
	{
		/*
		 * Below we determine whether the Tree Display is currently open
		 * by seeing if the reference to it is null, or is displayable
		 */
		if (treeDisplay!=null && treeDisplay.isDisplayable())
		{
			treeDisplay.updateHeap(buffer, allocated);//if it is, update the display with the new version of the buffer
		}
	}
	public static void main(String[] args)
	{
		/*
		 * Below, we simply create an instance of the schedular gui main window
		 * and show it to the user, setting it's default close operation to exit
		 * since it will be the main window of this application
		 */
		SchedulerGuiMain master = new SchedulerGuiMain();
		master.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		master.setVisible(true);
	}
}
