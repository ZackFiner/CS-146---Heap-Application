import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import org.junit.Test;
/**
 * This class is dedicated solely to testing the functionality of all classes used in this application
 * @author Zackary Finer
 *
 */
public class HeapAndApplicationUnitTests {

	private static final int TEST_ARRAY_SIZE = 10;
	private static final int PRIORITY_MAX = TEST_ARRAY_SIZE*50;
	
	class RandomPriorityIDGen {
		private ArrayList<Integer> IDS = new ArrayList<>();
		
		public RandomPriorityIDGen(int max)
		{
			for (int i = 0; i < max; i++)
				IDS.add(i+1);
		}
		public int nextID()
		{
			Collections.shuffle(IDS);
			return IDS.remove(0);
		}
		public void addID(int ID)
		{
			IDS.add(ID);
		}
	}
	@Test
	public void testGetNodeIndex()
	{
		System.out.println();
		System.out.println("TESTING GET LEFT, RIGHT, AND PARENT METHODS");
		
		int l = TaskHeap.left(0);
		int r = TaskHeap.right(0);
		int p = TaskHeap.parent(0);
		assertEquals(l,1);
		assertEquals(r,2);
		assertEquals(p, -1);
		
		l = TaskHeap.left(1);
		r = TaskHeap.right(1);
		p = TaskHeap.parent(1);
		assertEquals(l,3);
		assertEquals(r,4);
		assertEquals(p, 0);
		
		p = TaskHeap.parent(2);
		assertEquals(p, 0);
		System.out.println("RESULT: SUCCESS - ALL METHODS WORKED AS INTENDED");
	}
	
	@Test
	public void testMaxHeapify()
	{
		System.out.println();
		System.out.println("TESTING FUNCTIONALITY OF HEAP MAXIFY");
		
		RandomPriorityIDGen rng = new RandomPriorityIDGen(PRIORITY_MAX);
		int nTestSize = TEST_ARRAY_SIZE;
		Task[] testArr1 = new Task[nTestSize];
		
		for (int i = 0; i < nTestSize; i++)
		{
			int priority = rng.nextID();
			testArr1[i] = new Task(priority,0,"Task"+priority);
		}
		TaskHeap taskHeap = new TaskHeap();
		taskHeap.buildMaxHeap(testArr1);
		System.out.println(Arrays.toString(testArr1));
		
		testArr1[0] = new Task(0, 0, "SIFT ME DOWN");
		System.out.println("INSERTING LOW PRIORITY ELEMENT AT HEAD");
		System.out.println(Arrays.toString(testArr1));
		
		taskHeap.maxHeapify(testArr1, 0);
		System.out.println("PERFOMRING MAX HEAPIFY OPERATION");
		System.out.println(Arrays.toString(testArr1));
		boolean isValid = TaskHeap.testHeapProperty(testArr1, testArr1.length);
		
		System.out.print("RESULT: ");
		
		if (isValid)
		{
			System.out.print("SUCCESS - THE ENTRY WAS SIFTED DOWN TO THE CORRECT POSITION TO MAINTAIN THE HEAP PROPERTY\n");
		}
		else
		{
			System.out.print("FAILURE - MAX HEAPIFY DID NOT PERFORM AS INTENDED\n");
		}
		assertEquals(true,isValid);

	}
	/*
	 *	TODO: this test assumes buildHeap works, when in actuallity it will be used to test build heap
	 *	later, you should change it so it tests the integrity of a smaller pre-defined heap
	 *	to make sure it doesn't give false positives
	 */
	@Test
	public void testCheckIntegrity()
	{

		System.out.println();
		System.out.println("TESTING FUNCTIONALITY OF HEAP INTEGRITY CHECKER");
		TaskScheduler taskScheudlar = new TaskScheduler();

		RandomPriorityIDGen rng = new RandomPriorityIDGen(PRIORITY_MAX);
		int nTestSize = TEST_ARRAY_SIZE;
		Task[] testArr1 = new Task[nTestSize];
		
		for (int i = 0; i < nTestSize; i++)
		{
			int priority = rng.nextID();
			testArr1[i] = new Task(priority,0,"Task"+priority);
		}

		taskScheudlar.buildTaskScheduler(testArr1);
		System.out.println("BUILDING A TASK HEAP OF SIZE: "+nTestSize);
		System.out.println(Arrays.toString(testArr1));
		boolean goodIntegrity = TaskHeap.testHeapProperty(testArr1, testArr1.length);
		
		
		assertEquals(true, goodIntegrity);
		if (!goodIntegrity)
		{
			System.out.println("RESULT: ERROR - THE HEAP FAILED THE TEST BEFORE A BAD NODE WAS ENTERED");
			return;
		}
		int badNodeLocation;
		
		if (nTestSize>1)
		{
			badNodeLocation = (new Random(System.currentTimeMillis())).nextInt(nTestSize-1)+1;

			testArr1[badNodeLocation] = new Task(PRIORITY_MAX+1,0,"BAD NODE");
			System.out.println("A BAD ENTRY HAS BEEN INSERTED AT POSITION: "+(badNodeLocation+1));
			System.out.println(Arrays.toString(testArr1));
			//System.out.println(taskScheudlar.toString(testArr1));
			goodIntegrity = TaskHeap.testHeapProperty(testArr1, testArr1.length);
			System.out.print("RESULT: ");
			if (!goodIntegrity)
			{
				System.out.print("SUCCESS - THE ERROR WAS IDENTIFIED\n");
			}
			else
			{
				System.out.print("FAILURE - THE ERROR WAS NOT IDENTIFIED\n");
			}
			assertEquals(false,goodIntegrity);
		}
	}
	
	@Test
	public void testBuildHeap()
	{
		System.out.println();
		System.out.println("TESTING FUNCTIONALITY OF BUILD HEAP");

		RandomPriorityIDGen rng = new RandomPriorityIDGen(PRIORITY_MAX);
		int nTestSize = TEST_ARRAY_SIZE;
		Task[] testArr1 = new Task[nTestSize];
		
		for (int i = 0; i < nTestSize; i++)
		{
			int priority = rng.nextID();
			testArr1[i] = new Task(priority,0,"Task"+priority);
		}
		
		System.out.println("BUILDING A HEAP OF SIZE: "+nTestSize);
		
		TaskScheduler taskScheudlar = new TaskScheduler();
		taskScheudlar.buildTaskScheduler(testArr1);
		System.out.println(Arrays.toString(testArr1));
		
		boolean isValid = TaskHeap.testHeapProperty(testArr1, testArr1.length);
		System.out.println("TESTING INTEGRITY OF HEAP");
		
		System.out.print("RESULT: ");
		if (isValid)
		{
			System.out.print("SUCCESS - HEAP WAS SUCCESSFULLY BUILT AND OBEYS HEAP PROPERTY\n");
		}
		else
		{
			System.out.print("FAILURE - DOES NOT OBEY HEAP PROPERTY\n");
		}
		assertEquals(true, isValid);

	}
	public boolean isSorted(Task[] a)
	{
		for (int i = 0; i< a.length; i++)
		{
			if (i!=a.length-1)
			{
				if (a[i].getPriority() > a[i+1].getPriority())
					return false;
			}
		}
		return true;
	}
	
	@Test
	public void testHeapSort()
	{
		System.out.println();
		System.out.println("TESTING FUNCTIONALITY OF HEAPSORT");

		RandomPriorityIDGen rng = new RandomPriorityIDGen(PRIORITY_MAX);
		int nTestSize = TEST_ARRAY_SIZE;
		Task[] testArr1 = new Task[nTestSize];
		
		for (int i = 0; i < nTestSize; i++)
		{
			int priority = rng.nextID();
			testArr1[i] = new Task(priority,0,"Task"+priority);
		}
		System.out.println("ARRAY BEFORE SORTING: ");
		System.out.println(Arrays.toString(testArr1));
		
		TaskHeap sorter = new TaskHeap();
		sorter.heapSort(testArr1);
		System.out.println("ARRAY AFTER SORTING: ");
		System.out.println(Arrays.toString(testArr1));
		
		boolean isValid = isSorted(testArr1);
		System.out.print("RESULT: ");
		if (isValid)
		{
			System.out.print("SUCCESS - THE ARRAY WAS SORTED FROM LEAST TO GREATEST\n");
		}
		else
		{
			System.out.print("FAILURE - THE ARRAY IS NOT SORTED ORDER\n");
		}
		assertEquals(true, isValid);

	}
	
	@Test
	public void testInsertKey()
	{
		System.out.println();
		System.out.println("TESTING FUNCTIONALITY OF HEAP INSERT KEY METHOD");
		
		RandomPriorityIDGen rng = new RandomPriorityIDGen(PRIORITY_MAX+5);
		int nTestSize = TEST_ARRAY_SIZE;
		Task[] testArr1 = new Task[nTestSize+1];//add one for the new entry
		
		for (int i = 0; i < nTestSize; i++)
		{
			int priority = rng.nextID();
			testArr1[i] = new Task(priority,0,"Task"+priority);
		}
		TaskHeap heap = new TaskHeap();
		heap.buildMaxHeap(testArr1,nTestSize);
		System.out.println("HEAP BEFORE INSERTION: ");
		System.out.println(Arrays.toString(testArr1));
		
		Task newTask = new Task(rng.nextID(), 0, "INSERTED NODE");
		
		System.out.println("INSERTING TASK: "+newTask);
		
		System.out.println("HEAP AFTER INSERTION: ");
		heap.maxHeapInsert(testArr1, newTask);
		System.out.println(Arrays.toString(testArr1));
		
		boolean isValid = (heap.getHeapSize()==testArr1.length) && TaskHeap.testHeapProperty(testArr1, heap.getHeapSize());
		
		System.out.print("RESULT: ");
		if (isValid)
		{
			System.out.print("SUCCESS - A NEW ENTRY WAS ADDED TO THE HEAP\n");
		}
		else
		{
			System.out.print("FAILURE - THE ENTRY WAS NOT SUCCESSFULLY ADDED\n");
		}	
		assertEquals(true, isValid);
	}
	
	@Test
	public void testIncreaseKey()
	{
		System.out.println();
		System.out.println("TESTING FUNCTIONALITY OF INCREASE KEY METHOD");
		
		RandomPriorityIDGen rng = new RandomPriorityIDGen(PRIORITY_MAX);
		int nTestSize = TEST_ARRAY_SIZE;
		Task[] testArr1 = new Task[nTestSize];//add one for the new entry

		for (int i = 0; i < nTestSize; i++)
		{
			int priority = rng.nextID();
			
			if (i==nTestSize-1)
			{
				testArr1[i] = new Task(0, 0,"INCREASE MY PRIORITY");
			}
			else
			{
				testArr1[i] = new Task(priority,0,"Task"+priority);
			}
		}
		
		TaskHeap heap = new TaskHeap();
		heap.buildMaxHeap(testArr1);
		
		System.out.println("HEAP BEFORE LAST KEY IS INCREASED: ");
		System.out.println(Arrays.toString(testArr1));
		
		Task top = new Task(PRIORITY_MAX+1, 0,"INCREASE MY PRIORITY");
		heap.heapIncreaseKey(testArr1, testArr1.length-1, top);
		System.out.println("HEAP AFTER LAST KEY WAS INCREASED: ");
		System.out.println(Arrays.toString(testArr1));
		
		boolean isValid = TaskHeap.testHeapProperty(testArr1, heap.getHeapSize());
		isValid = isValid && testArr1[0].equals(top);
		
		System.out.print("RESULT: ");
		if (isValid)
		{
			System.out.print("SUCCESS - THE LAST ENTRIES PRIORITY WAS INCREASED TO THE TOP\n");
		}
		else
		{
			System.out.print("FAILURE - NO PRIOIRTY INCREASE TOOK PLACE\n");
		}	
		assertEquals(true,isValid);
	}
	
	@Test
	public void testExtractMax()
	{
		System.out.println();
		System.out.println("TESTING FUNCTIONALITY OF HEAP EXTRACT MAX METHOD");
		
		RandomPriorityIDGen rng = new RandomPriorityIDGen(PRIORITY_MAX);
		int nTestSize = TEST_ARRAY_SIZE;
		Task[] testArr1 = new Task[nTestSize];//add one for the new entry
		
		for (int i = 0; i < nTestSize-1; i++)
		{
			int priority = rng.nextID();
			testArr1[i] = new Task(priority,0,"Task"+priority);
		}
		Task ExpectedMax =  new Task(PRIORITY_MAX+1, 0, "MAXIMUM VALUE");
		testArr1[nTestSize-1] = ExpectedMax;
		
		TaskHeap heap = new TaskHeap();
		
		heap.buildMaxHeap(testArr1);
		
		System.out.println("TASK HEAP BEFORE MAX IS EXTRACTED: ");
		System.out.println(Arrays.toString(testArr1));
		
		Task returnedTask = heap.heapExtractMax(testArr1);
		
		System.out.println("HEAP AFTER EXTRACTION: ");
		System.out.println(Arrays.toString(testArr1));
		
		System.out.println("RETURNED MAXIMUM TASK: "+returnedTask);
		
		boolean isValid = heap.getHeapSize()==(testArr1.length-1);
		isValid = isValid && TaskHeap.testHeapProperty(testArr1, heap.getHeapSize());
		isValid = isValid && ExpectedMax.equals(returnedTask);
		System.out.print("RESULT: ");
		
		if (isValid)
		{
			System.out.print("SUCCESS - THE MAXIMUM ENTRY WAS REMOVED FROM THE HEAP AND RETURNED\n");
		}
		else
		{
			System.out.print("FAILURE - THE MAXIMUM ELEMENT WAS NOT REMOVED CORRECTLY\n");
		}	
		assertEquals(true, isValid);
	}
	//TODO: test task schedular
}
