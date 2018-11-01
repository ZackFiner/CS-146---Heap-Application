import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * A class which extends JFrame, intended for use displaying a heap tree
 * structure
 * 
 * @author Zackary Finer
 *
 */
public class HeapTreeFrame extends JFrame {
	private HeapTreeDrawer canvas;//canvas used to draw the heap
	private JScrollPane scroller;//a scroller to hold the canvas
	private Task[] heap;//the heap of tasks we will be drawing
	private int allocated;//the number of tasks allocated in said heap
	private JButton updateB;// a button which the user can press to update the heap
	SchedulerGuiMain p = null;//the parent object, which will be used by the main window

	/**
	 * A private class which extends JPanel and contains a drawing of a Binary
	 * Heap tree
	 * 
	 * @author Zackary Finer
	 *
	 */
	public class HeapTreeDrawer extends JPanel {
		public int screenWidth;// holds screen width
		public int screenHeight;// holds screen height
		/**
		 * A private class which extends JPanel and is used to represent a node
		 * in binary heap tree, it extends JPanel and is really just a grey rectangle
		 * with a label with the string representation of the Task it holds
		 * 
		 * @author Zackary Finer
		 *
		 */
		class HeapTreeNode extends JPanel {
			int x;// x position
			int y;// y position
			int width;// width of the node
			int height;// height of the node
			Task a;// the task which the node represents
			public HeapTreeNode parent = null;// parent of this node
			public HeapTreeNode leftChild = null;// left child of this node
			public HeapTreeNode rightChild = null;// right child of this node

			/**
			 * Constructor for heap tree node
			 * 
			 * @param a - Task which this node will represent
			 * @param x - Integer representing the nodes x position on the
			 *            tree canvas
			 * @param y - Integer representing the nodes y position on the
			 *            tree canvas
			 * @param width - Integer representing the width of the tree node
			 * @param height - Integer representing the height of the tree node
			 */
			HeapTreeNode(Task a, int x, int y, int width, int height) {
				this.add(new JLabel(a.toString()));//add a label containing the node's information

				setBackground(Color.GRAY);//set this to grey so it stands out on the canvas
				setLocation(x, y);//set it's location to the desired coordinates
				setPreferredSize(new Dimension(width, height));//and set the dimensions
				/*
				 * Below, we set all the state information
				 */
				this.a = a;
				this.x = x;
				this.y = y;
				this.width = width;
				this.height = height;
			}
		}

		HeapTreeNode[] nodes;//an array which will hold a heap of nodes
		
		/**
		 * Returns the height of a given heap tree of a certain size
		 * @param size - Integer representing the number of elements in the heap tree
		 * @return Integer of the height
		 */
		private int getHeapHeight(int size) {
			/*
			 * The floor of lg(size) (lg meaning log base 2) is the height of a binary tree
			 * so we simply return this.
			 */
			return (int) (Math.log((int) size) / Math.log(2.0));
		}
		
		/**
		 * Returns the width of a given heap's base, or last row of the heap, of a given size
		 * @param size - Integer representing the number of elements in the heap tree
		 * @return Integer of the width of the base element
		 */
		private int getHeapWidthOfBase(int size) {
			/*
			 * The width of the base of any given binary heap tree can be expressed as:
			 * 2^(height-1), 
				 * 1. since we already have a function for getting the height
				 * we can use that to retrieve the height, and 
				 * 2. then math.pow to retrieve the correct power of 2 for the solution
				 * 
			 */
			int height = getHeapHeight(size);// 1
			return (int) Math.pow(2.0, height - 1);// 2
		}
		/**
		 * Returns the offset of a given node in a heap from the center of the heap tree,
		 * returns 0 for the first element in the tree
		 * @param index - Integer representing the index of the node
		 * @return Integer represeting the offset of the given node from the center of the tree
		 */
		private int getOffSetFromCenter(int index) {
			int offset = 0;//initialized to zero
			if (index != 0) {
				int i = index + 1;// correct for java's indexing rules
				/*
				 * Use the function for getting the row index defined above for calculation
				 */
				int row = getHeapIndexRow(index);
				
				/*
				 * I derived the switchpoint from drawing a heap tree, if you
				 * draw a line down the center of the tree, you can calculate
				 * the index number next to that line on each row by 
					 * 1. raising 2 to the power of (row-2) and multiplying the result by 3
					 * 2. you can then subtract the index from this switchpoint to get the 
					 *    offset from the center
				 */
				int switchPoint = (int) (3 * Math.pow(2.0, (double) (row - 2)));// 1
				offset = i - switchPoint;// 2
				/*
				 * If this index is on the other side of the swith point,
				 * then add 1 to it because otherwise everything will be
				 * slightly to the left
				 */
				if (i >= switchPoint) {
					offset++;//increasing by 1
				}
			}
			return offset;//return the result
		}
		/**
		 * Returns the row which a given node in a binary heap should fall on.
		 * The term 'row' is referring to the level, depth, and distance from root in the tree
		 * @param index - Integer representing the node's index in the tree
		 * @return Integer representing the row
		 */
		private int getHeapIndexRow(int index) {
			/*
			 * Using algebra, we can express an element's position in terms of
			 * it's row as:
			 * i = 2^(r-1)
			 * 
			 * where i is the index of the first element
			 * in that row, and r is the row 
			 * 
			 * now taking the lg of both sides we get:
			 * lg(i) = r-1
			 * 
			 * adding 1 to both sides:
			 * lg(i)+1=r 
			 * 
			 * then, by flooring the result we can get a function for the row any index
			 * should fall on floor(lg(i)+1)=r
			 * 
			 * In the implementation below, 1 is added to the index to correct for java's indexing rules
			 */
			return (int) ((Math.log((double) (index + 1)) / Math.log(2.0)) + 1);//use the formula derived above
		}


		/**
		 * Builds a graphical representation of binary tree from a given heap of tasks
		 * @param a - Task[] representing the heap of tasks
		 * @param size - Integer representing the number of elements in the heap of tasks
		 */
		public void buildFromHeap(Task[] a, int size) {

			/*
			 * First we need  to clear all elements from this canvas, otherwise we may be
			 * leaving un-used nodes in the canvas which will be confusing to the viewer
			 */
			removeAll();
			
			/*
			 * Below, we catch the corner case where a size argument equal to or less than
			 * zero has been passed. In these cases, we simply repaint an empty canvas and
			 * end execution.
			 */
			if (size <= 0) {
				repaint();
				return;
			}
			
			int nodeDimensions = 100;//used to represent the distance between nodes
			
			/*
			 * We use the getHeapWidthOfBase function to get the appropriate width for
			 * this canvas in order to allow all nodes to fit on it, and then multiply it by
			 * a factor of our node dimensions times 2. I multiply it again by 2 because i've found
			 * this offers the best padding for the tree
			 */
			screenWidth = (getHeapWidthOfBase(size + 1) * 2) * (nodeDimensions * 2);
			
			/*
			 * We use the getHeapHeight function to get the appropriate height for
			 * this canvas in order to fit all nodes in the tree on it. This is also multiplied
			 * by a factor of our node dimensions times 2. It is also multiplied by 2 because
			 * this is what i found offers the best padding for the tree.
			 */
			screenHeight = (getHeapHeight(size + 1) * (nodeDimensions * 2)) * 2;
			
			/*
			 * Set the preferred size of this canvas to the correct dimensions
			 */
			setPreferredSize(new Dimension(screenWidth, screenHeight));
			/*
			 * Then set the bounds, since this element isn't using a layout manager
			 */
			setBounds(0, 0, screenWidth, screenHeight);
			/*
			 * We have to set this canvas to not use a layout manger, because we will
			 * be position elements within it manually
			 */
			setLayout(null);
			int screenCenter = screenWidth / 2;//use the screen width defined earlier to calculate the center

			nodes = new HeapTreeNode[size];//Initialize our nodes array to be the correct size
			
			/*
			 * Below, we will set the correct position for each node in the tree
			 */
			for (int i = 0; i < size; i++) {
				/*
				 * Calculate the correct x position using getOffSetFromCenter function
				 * and multiplication by nodeDimensions*2 and then add this number to
				 * screenCenter to attain the correct position
				 */
				int nodeX = (getOffSetFromCenter(i) * nodeDimensions * 2) + screenCenter;
				/*
				 * Calculate the correct y position for each node using the getHeapIndexRow function
				 * and multiplication by nodeDimensions*2
				 */
				int nodeY = getHeapIndexRow(i) * nodeDimensions * 2;
				
				/*
				 * We set teh width of the node using the length of the tasks
				 * string representation, multiplied by the constant 6
				 * I selected 6 because it seemed to work the best
				 */
				int nodeWidth = a[i].toString().length() * 6;
				
				/*
				 * the node's height can be constant, as there will not be any word-wrapping
				 */
				int nodeHeight = 40;
				/*
				 * Created the new node with the state information we calculated above
				 * and add it to our heap of nodes
				 */
				nodes[i] = new HeapTreeNode(a[i], nodeX, nodeY, nodeWidth, nodeHeight);
				/*
				 * Since we aren't using a layout manager, this is necessary to correctly
				 * position the nodes
				 */
				nodes[i].setBounds(nodeX, nodeY, nodeWidth, nodeHeight);
				add(nodes[i]);//add the node to the canvas
			}
			/*
			 * Below, we set up the relationships between the nodes in the heap tree
			 */
			for (int i = 0; i < size; i++) {
				/*
				 * if the node we're looking at has a left child
				 */
				if (TaskHeap.left(i) < size) {
					/*
					 * 1. Set this node's left child to that node
					 * 2. And that node's parent to this node
					 */
					nodes[i].leftChild = nodes[TaskHeap.left(i)];// 1
					nodes[TaskHeap.left(i)].parent = nodes[i];// 2
					/*
					 * if this node also has a right child
					 */
					if (TaskHeap.right(i) < size) {
						/*
						 * 1. Set this node's right child to that node
						 * 2. And that node's parent to this node
						 */
						nodes[i].rightChild = nodes[TaskHeap.right(i)];// 1
						nodes[TaskHeap.right(i)].parent = nodes[i];// 2
					}
				}

			}
			repaint();//redraw everything in this canvas to show these changes to the user
		}
		/**
		 * Updates the heap currently being displayed on the canvas
		 * @param a - Task[] representing the new heap of tasks
		 * @param size - Integer representing the size of the new heap of tasks
		 */
		public void upadateHeap(Task[] a, int size) {
			buildFromHeap(a, size);//just use the buildFromHeap method
		}
		
		@Override
		public void paint(Graphics g) {
			super.paint(g);
			/*
			 * The reason why we have overridden the function is because we're
			 * going to use it to draw the lines connecting each node
			 */
			Graphics2D g2 = (Graphics2D) g;//Graphics2D supports drawing multiple lines
			/*
			 * Ensure that nodes has been intialized, otherwise don't do anything
			 */
			
			if (nodes != null) {
				 /*
				  * Now, for every node in the array with a parent, draw a line to it's parent
				  */
				for (HeapTreeNode c : nodes) {
					/*
					 * Make sure this node has a parent
					 */
					if (c.parent != null) {
						/*
						 * Draw a line from this node to it's parent
						 * I do a few additions to the x and y values to make the connections
						 * look nice (top center of the child to the bottom center of parent) 
						 */
						g2.draw(new Line2D.Double(c.x + c.width / 2, c.y, c.parent.x + c.width / 2,
								c.parent.y + c.height));
					}
				}
			}
		}
	}
	


	/**
	 * Constructor for HeapTreeFrame which creates a new window to display a heap tree in
	 * @param parent - SchedularGuiMain of the parent object
	 * @param a - Task[] of the heap which this window will dispay initially
	 * @param size - size of the heap to be displayed
	 */
	public HeapTreeFrame(SchedulerGuiMain parent, Task[] a, int size) {
		super("Heap Tree Viewer");//call the superclass constructor to set the title of the window
		
		/*
		 * We'll use a borderLayout for this window, because I like it.
		 */
		setLayout(new BorderLayout());
		/*
		 * 1. Initialize the canvas
		 * 2. Then build the heap tree from the data given
		 */
		canvas = new HeapTreeDrawer();// 1
		canvas.buildFromHeap(a, size);// 2
		
		setSize(new Dimension(600, 600));//set the initial dimensions of the window to be 600x600
		
		/*
		 * 1. Create a new JScrollPane which contains the canvas for the tree, allows the user to scroll as needed
		 * 2. Then set the initial position of the view to be at the top of the tree
		 */
		scroller = new JScrollPane(canvas,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);// 1
		scroller.getViewport().setViewPosition(new Point((canvas.screenWidth / 2) - 280, 0));// 2
		
		updateB = new JButton("Refresh");//initialize the button to refresh the display
		this.p = parent;//set the parent of this window to be the value passed to this constructor
		/*
		 * Add an actionlistner to refresh the display
		 */
		updateB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				/*
				 * If the parent is null, 
				 * 1. redraw the number of nodes allocated in the parents heap
				 * otherwise,
				 * 2. redraw all elements in the passed task heap
				 */
				if (p != null) {
					updateHeap(heap, p.getAllocated());// 1
				}

				else {
					updateHeap(heap, heap.length);// 2
				}
				/*
				 * 1. Set the initial position of the view port to be over the root of the heap
				 * 2. Validate this jFrame, to update any changes we made to the nodes
				 * 3. Repaint everything in this object
				 */
				scroller.getViewport().setViewPosition(new Point((canvas.screenWidth / 2) - 280, 0));// 1
				validate();// 2
				repaint();// 3
			}
		});
		add(updateB, BorderLayout.NORTH);// add the update button to this jframe
		add(scroller, BorderLayout.CENTER);// add the scroller containing the canvas to this jframe
		heap = a;// set the heap to the passed array
	}
	
	/**
	 * Update this HeapTreeFrame's displayed heap tree
	 * @param a - Task[] representing the new heap to be displayed
	 * @param size - Integer representing the size of the new heap to be displayed
	 */
	public void updateHeap(Task[] a, int size) {
		Point original = scroller.getViewport().getViewPosition();
		canvas.upadateHeap(a, size);//use the canvas update function to update the heap tree
		scroller.getViewport().setViewPosition(original);
	}
	
	private static Task[] genRandArray(int size) {
		Random rng = new Random(System.currentTimeMillis());
		Task[] a = new Task[size];

		for (int i = 0; i < size; i++) {
			int priorityID = rng.nextInt(100) + 1;
			a[i] = new Task(priorityID, 0, "Task" + i);
		}
		return a;
	}

	public static void main(String[] args) {
		int nSize = 8;
		Random rng = new Random(System.currentTimeMillis());
		Task[] a = new Task[nSize];

		for (int i = 0; i < nSize; i++) {
			int priorityID = rng.nextInt(100) + 1;
			a[i] = new Task(priorityID, 0, "Task" + i);
		}

		HeapTreeFrame frame = new HeapTreeFrame(null, a, a.length);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}