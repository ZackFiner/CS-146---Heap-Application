import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Arrays;

import javax.swing.*;

import com.sun.corba.se.impl.protocol.BootstrapServerRequestDispatcher;
/**
 * A simple dialog designed to display an array of tasks as a string.
 * It uses a text field and a scroller to do this so that the string does not
 * extend past the bounderies of the screen.
 * @author Zackary Finer
 *
 */
public class ArrayDisplayDialog extends JDialog{
	private JScrollPane disp;//scroller which will contain the text field object
	private JTextField container;//the text field which will contain the string representation of the Task array
	/**
	 * The default constructor for the ArrayDisplayDialog.
	 * @param array - Task[] representing the task array to be displayed
	 */
	public ArrayDisplayDialog(Task[] array)
	{
		
		container =  new JTextField(Arrays.toString(array));//intialize the text field which will hold the string of the array
		disp = new JScrollPane(container);//intialize the scroll pane to hold the text field
		setLayout(new BorderLayout());//set the layout to border because it's nice looking
		
		add(disp, BorderLayout.CENTER);//put the scroll pane with the text in it at the center of the dialog
		JButton closeB = new JButton("Close");//create a close button
		closeB.addActionListener((e)->dispose());//add an action listener to it which disposes/closes the dialog when pressed
		
		setSize(300,150);//set the default size of the dialog to 300x150
		add(closeB, BorderLayout.SOUTH);//put the close button at the bottom
	}
}
