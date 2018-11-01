import java.awt.CardLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Random;

import javax.swing.*;
/**
 * Window which is opened when the user wants to add multiple entries to the list
 * @author Zackary Finer
 *
 */
public class AddMultipleDialog extends JDialog{
	private SchedulerGuiMain parent;//parent object
	
	private JTextField taskNamer;//field where the user can enter the name they want for all the tasks
	private JButton confirmB;//button to confirm the addition
	private JButton cancelB;//button to cancel the operation
	private JSpinner amountS;//spinner where the user can specify the amount of entries they want to add
	private JSpinner delayS;//spinner where the user can enter the delay in u/s
	private JComboBox<String> delayTypeCB;//combo box with random, or pre-set delay time, or none
	/**
	 * A constructor which takes one argument for the parent SchedulerGuiMain object which created this dialog
	 * @param parent - SchedulerGuiMain object which created this dialog
	 */
	public AddMultipleDialog(SchedulerGuiMain parent)
	{
		/*
		 * call the super constructor for dialog and pass it a reference to the parent window
		 * this is necessary for proper modularity settings
		 */
		super((JFrame)parent);
		
		setTitle("Add Multiple Entries");//set the title of the dialog
		
		/*
		 * Below, we set the modularity type for this dialog to be application modal.
		 * What this does is force the user to either complete or cancel the add multiple task
		 * operation before they can perform any other edit operations on the heap.
		 */
		setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		
		this.parent = parent;//assign the parent instance variable to the argument
		
		taskNamer = new JTextField("Task");//initialize the taskName text field to contain Task
		
		int maxAdded = parent.BUFFER_SIZE - parent.getAllocated();//set the upper limit of the amount spinner to the correct amount
		
		String[] comboOptions = {"Random", "Custom", "None"};//create an array of strings which will be passed to the combo box
		
		delayTypeCB = new JComboBox<>(comboOptions);//create the combo box for delay type using the array defined above
		delayTypeCB.setSelectedIndex(2);//set the default selected index to none, so the user doesn't have to enter anything
		/*
		 * Below, we add an action listener to the combo box to grey out the delay spinner
		 * when necessary.
		 */
		delayTypeCB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (delayTypeCB.getSelectedIndex()==1)//if the setting "Custom" is selected
				{
					delayS.setEnabled(true);//allow the user to enter the amount of delay they'd like
				}
				else
				{
					delayS.setEnabled(false);//otherwise, disable/gray out the spinner
				}
				
			}
		});
		confirmB =  new JButton("Ok");//initialize the confirm button to have the text Ok in it
		/*
		 * Add an action listener to the confirm button which adds all the desired entries to the priority queue
		 */
		confirmB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				/*
				 * First, we retrieve the name for the task
				 */
				Random rng = new Random(System.currentTimeMillis());
				String seriesName;
				if (taskNamer.getText().length() > 0)
				{
					seriesName = taskNamer.getText();
					int numToAdd;
					
					if ((numToAdd=(int)amountS.getValue())>0)
					{
						Task[] entriesToAdd = new Task[numToAdd];
						for (int i = 0; i < numToAdd; i++)
						{
							int getID = parent.getAvialableID();
							int delayT = 0;
							switch (delayTypeCB.getSelectedIndex())
							{
							case 0:
								delayT = rng.nextInt(1000);
								break;
							case 1:
								delayT = (int)delayS.getValue();
								break;
							default:
								delayT = 0;
								break;
							}
							entriesToAdd[i] = new Task(getID, delayT, seriesName+i);
						}
						parent.addEntries(entriesToAdd);
						dispose();
					}
					else
					{
						JOptionPane.showMessageDialog(null, "Please specify a valid number of entries to add");
					}
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Please Enter a valid task name");
				}
			}
		});
		cancelB = new JButton("Cancel");
		cancelB.addActionListener((e) -> dispose());
		
		amountS = new JSpinner(new SpinnerNumberModel(1, 1, maxAdded, 1));
		delayS = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
		delayS.setEnabled(false);
		setLayout(new GridLayout(5,2));
		
		add(new JLabel("Name: "));
		add(taskNamer);
		
		add(new JLabel("Time In Queue: "));
		add(delayTypeCB);
		
		add(new JLabel("# of Entries"));
		add(amountS);
		
		add(new JLabel("u/s In Queue: "));
		add(delayS);
		
		add(confirmB);
		add(cancelB);
		pack();
	}
}
