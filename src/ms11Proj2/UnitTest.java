package ms11Proj2;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Vector;

public class UnitTest extends Puzzle  {

    @Test //Pair
    public void testOne() {
		Pair object1 = new Pair(0, 0);
		Pair object2 = new Pair(0, 0);
		
		assertEquals(0, object1.getX());
		assertEquals(0, object1.getY());
		
		object1.setX(12);
		object1.setY(4);
		
		assertEquals(12, object1.getX());
		assertEquals(4,  object1.getY());
		
		object2.setX(7);
		object2.setY(6);
		
		assertEquals(7, object2.getX());
		assertEquals(6, object2.getY());
		
		Pair dist = new Pair (0, 0);
		
		assertEquals(0, dist.getX());
		assertEquals(0, dist.getY());
		
		dist.setDistBetween2Points(object1, object2);
		
		assertEquals(5,  dist.getX());
		assertEquals(-2, dist.getY());
		
		return;
    }
    
    @Test //Tuple
    public void testTwo() {
    	Vector<Integer> i = new Vector<Integer>();
    	
    	i.addElement(1);
    	i.addElement(2);
    	i.addElement(3);
    	
    	Vector<Integer> j = new Vector<Integer>();

    	j.addElement(4);
    	j.addElement(5);
    	j.addElement(6);
    	
		Tuple object1 = new Tuple(i, j); 
		
		assertEquals(i, object1.getX());
		assertEquals(j, object1.getY());
		
		i = null;
		j = null;
		return;
    }
    
    @Test //even inversion count
    public void testThree() {    	
    	ButtonShuffle object1 = new ButtonShuffle();
    	bShuffle.addActionListener(object1);
    	bShuffle.doClick();
    	
    	assertEquals(0,object1.getInversionCount()%2);	
    }
    
    @Test //button press and tile movement
    public void testFour() {
    	ButtonNumsInGrid object1 = new ButtonNumsInGrid();
    
    	int i;
    	for(i=0;i<16;i++)
    		bNumsInGrid[i].addActionListener(object1);
    	
    	bNumsInGrid[0].doClick();
    	assertEquals(bNumsInGrid[0].getText(), "1");
    	
    	bNumsInGrid[15].doClick();
    	assertEquals(bNumsInGrid[15].getText(), "");
    	
    	bNumsInGrid[14].doClick();
    	assertEquals(bNumsInGrid[14].getText(), "");
    	assertEquals(bNumsInGrid[15].getText(), "15");
    }
    
    @Test //double click / everything is in correct spot
    public void testFive() {
    	ButtonNumsInGrid object1 = new ButtonNumsInGrid();
        
    	int i;
    	for(i=0;i<16;i++)
    		bNumsInGrid[i].addActionListener(object1);
    	
    	assertEquals("1", bNumsInGrid[0].getText());
    	assertEquals("2", bNumsInGrid[1].getText());
    	assertEquals("3", bNumsInGrid[2].getText());
    	assertEquals("4", bNumsInGrid[3].getText());
    	assertEquals("5", bNumsInGrid[4].getText());
    	assertEquals("6", bNumsInGrid[5].getText());
    	assertEquals("7", bNumsInGrid[6].getText());
    	assertEquals("8", bNumsInGrid[7].getText());
    	assertEquals("9", bNumsInGrid[8].getText());
    	assertEquals("10", bNumsInGrid[9].getText());
    	assertEquals("11", bNumsInGrid[10].getText());
    	assertEquals("12", bNumsInGrid[11].getText());
    	assertEquals("13", bNumsInGrid[12].getText());
    	assertEquals("14", bNumsInGrid[13].getText());
    	assertEquals("15", bNumsInGrid[14].getText());
    	assertEquals("", bNumsInGrid[15].getText());
    	
    	bNumsInGrid[14].doClick();
    	assertEquals("", bNumsInGrid[14].getText());
    	assertEquals("15", bNumsInGrid[15].getText());
    	
    	bNumsInGrid[14].doClick();
    	assertEquals("", bNumsInGrid[14].getText());
    	
    	bNumsInGrid[15].doClick();
    	assertEquals("15", bNumsInGrid[14].getText());
    	assertEquals("", bNumsInGrid[15].getText());
    	
    	bNumsInGrid[15].doClick();
    	assertEquals("", bNumsInGrid[15].getText());
    	
    	bNumsInGrid[11].doClick();
    	assertEquals("", bNumsInGrid[11].getText());
    	assertEquals("12", bNumsInGrid[15].getText());
    }
    
    @Test// double click // movestack is correct
    public void testSix() {
    	ButtonNumsInGrid object1 = new ButtonNumsInGrid();
        
    	int i;
    	for(i=0;i<16;i++)
    		bNumsInGrid[i].addActionListener(object1);
    	
    	bNumsInGrid[14].doClick();
    	bNumsInGrid[14].doClick();
    	
    	bNumsInGrid[15].doClick();
    	bNumsInGrid[15].doClick();
    	
    	bNumsInGrid[11].doClick();
    	bNumsInGrid[11].doClick();
    	
    	assertEquals(3, moveStack.size());
    }
    
    @Test // undo move stack is correct
    public void testSeven() throws InterruptedException {
        Puzzle p = new Puzzle();
        
    	p.bNumsInGrid[14].doClick();
    	p.bNumsInGrid[14].doClick();
    	
    	p.bNumsInGrid[10].doClick();
    	p.bNumsInGrid[10].doClick();
    	
    	p.bNumsInGrid[11].doClick();
    	p.bNumsInGrid[11].doClick();
    	
    	assertEquals("12", p.bNumsInGrid[10].getText());
    	assertEquals("", p.bNumsInGrid[11].getText());
    	assertEquals("11", p.bNumsInGrid[14].getText());
    	assertEquals("15", p.bNumsInGrid[15].getText());
    	
    	assertEquals(3, p.moveStack.size());
    	
    	p.bUndo.doClick();
    	
    	Thread.sleep(2000);
    	
    	assertEquals("", p.bNumsInGrid[10].getText());
    	assertEquals("12", p.bNumsInGrid[11].getText());
    	assertEquals("11", p.bNumsInGrid[14].getText());
    	assertEquals("15", p.bNumsInGrid[15].getText());
    	
    	assertEquals(2, p.moveStack.size());
    }
    
    @Test // undoAll move stack is correct
    public void testEight() throws InterruptedException {
        Puzzle p = new Puzzle();
    	
    	p.bNumsInGrid[14].doClick();
    	p.bNumsInGrid[14].doClick();
    	
    	p.bNumsInGrid[10].doClick();
    	p.bNumsInGrid[10].doClick();
    	
    	p.bNumsInGrid[11].doClick();
    	p.bNumsInGrid[11].doClick();
    	
    	assertEquals("12", p.bNumsInGrid[10].getText());
    	assertEquals("", p.bNumsInGrid[11].getText());
    	assertEquals("11", p.bNumsInGrid[14].getText());
    	assertEquals("15", p.bNumsInGrid[15].getText());
    	
    	assertEquals(3, p.moveStack.size());
    	
    	p.bUndoAll.doClick();
    	
    	Thread.sleep(5000);
    	    	
    	assertEquals(0, p.moveStack.size());
    	
    	assertEquals("11", p.bNumsInGrid[10].getText());
    	assertEquals("12", p.bNumsInGrid[11].getText());
    	assertEquals("15", p.bNumsInGrid[14].getText());
    	assertEquals("", p.bNumsInGrid[15].getText());
    	
    	assertEquals(0, p.moveStack.size());
    }
    
    @Test// auto solve + correct positions 
    public void testNine() throws InterruptedException {
        Puzzle p = new Puzzle();
        
    	p.bNumsInGrid[14].doClick();
    	p.bNumsInGrid[14].doClick();
    	p.bNumsInGrid[13].doClick();
    	p.bNumsInGrid[12].doClick();
    	p.bNumsInGrid[8].doClick();
    	
    	assertEquals("", p.bNumsInGrid[8].getText());
    	
    	p.bAutoSolve.doClick();
    	
    	Thread.sleep(5000);
    	
    	assertEquals("11", p.bNumsInGrid[10].getText());
    	assertEquals("12", p.bNumsInGrid[11].getText());
    	assertEquals("15", p.bNumsInGrid[14].getText());
    	assertEquals("", p.bNumsInGrid[15].getText());
    }
    
    @Test// checks if isCorrect is checking for correct vector
    public void testTen() {
    	ButtonAutoSolve object1 = new ButtonAutoSolve();
    	Vector<Integer> i = new Vector<Integer>();
    	
    	int j;
    	for(j=0;j<16;j++)
    		i.addElement(j+1);

    	Tuple x = new Tuple(i, null);
    	
    	assertEquals (true, object1.isCorrect(x));
    }
    
    @Test // where is the blank spot 
    public void testEleven() {
    	ButtonAutoSolve object1 = new ButtonAutoSolve();
        Puzzle p = new Puzzle();
        int b = object1.getBlank(p.bNumsInGrid);
        
    	assertEquals (15, b);
        
    	p.bNumsInGrid[14].doClick();
    	p.bNumsInGrid[14].doClick();
    	p.bNumsInGrid[13].doClick();
    	p.bNumsInGrid[12].doClick();
    	p.bNumsInGrid[8].doClick();
    	
        b = object1.getBlank(p.bNumsInGrid);
        
    	assertEquals (8, b);
    }

    @Test //what surrounds the blank spot
    public void testTwelve() {
    	ButtonAutoSolve object1 = new ButtonAutoSolve();
        Puzzle p = new Puzzle();
        int b = object1.getBlank(p.bNumsInGrid);
        
    	assertEquals (15, b);
        
    	Vector<Integer> expected = new Vector<Integer>();
    	expected.addElement(11);
    	expected.addElement(14);
    	Vector<Integer> result = new Vector<Integer>();

    	result = object1.getSurrPos(b);
    	
    	assertEquals(expected, result);
    	
    	p.bNumsInGrid[14].doClick();
    	p.bNumsInGrid[14].doClick();
    	p.bNumsInGrid[13].doClick();
    	p.bNumsInGrid[12].doClick();
    	p.bNumsInGrid[8].doClick();
    	
        b = object1.getBlank(p.bNumsInGrid);
        
        assertEquals (8, b);
        
        expected.clear();
        expected.addElement(4);
        expected.addElement(12);
        expected.addElement(9);
        
        result = object1.getSurrPos(b);
        
    	assertEquals(expected, result);
    	
    	p.bNumsInGrid[9].doClick();
    	
        b = object1.getBlank(p.bNumsInGrid);
        
        assertEquals (9, b);
        
        expected.clear();
        expected.addElement(5);
        expected.addElement(13);
        expected.addElement(10);
        expected.addElement(8);
        
        result = object1.getSurrPos(b);
        
    	assertEquals(expected, result);
    }
    
    @Test // what is returned from the possible arrangements (starting position)
    public void testThirteen() {
    	ButtonAutoSolve object1 = new ButtonAutoSolve();
       
    	Vector<Integer> bArray = new Vector<Integer>();
    	
    	int i;
    	for(i=0;i<16;i++)
    		bArray.addElement(i+1);
    	
        int b = object1.getBlankInArray(bArray);
    	assertEquals (15, b);
    	
    	Vector<Integer> expected = new Vector<Integer>();
    	expected.addElement(11);
    	expected.addElement(14);
    	Vector<Integer> result = new Vector<Integer>();
    	
    	result = object1.getSurrPos(b);
    	
    	assertEquals(expected, result);
    	
    	Tuple test = new Tuple(bArray, null);
    	Vector<Vector<Integer>> a = object1.getPossibleArrangements(test);	
    	
    	expected.clear();
        expected.addElement(1);
        expected.addElement(2);
        expected.addElement(3);
        expected.addElement(4);
        expected.addElement(5);
        expected.addElement(6);
        expected.addElement(7);
        expected.addElement(8);
        expected.addElement(9);
        expected.addElement(10);
        expected.addElement(11);
        expected.addElement(16);
        expected.addElement(13);
        expected.addElement(14);
        expected.addElement(15);
        expected.addElement(12);
        
    	assertEquals(expected, a.elementAt(0));
    	
    	
    	expected.clear();
        expected.addElement(1);
        expected.addElement(2);
        expected.addElement(3);
        expected.addElement(4);
        expected.addElement(5);
        expected.addElement(6);
        expected.addElement(7);
        expected.addElement(8);
        expected.addElement(9);
        expected.addElement(10);
        expected.addElement(11);
        expected.addElement(12);
        expected.addElement(13);
        expected.addElement(14);
        expected.addElement(16);
        expected.addElement(15);
        
    	assertEquals(expected, a.elementAt(1));
    }
    
    @Test // what is returned from the possible arrangements (random position)
    public void testFourteen() {
    	ButtonAutoSolve object1 = new ButtonAutoSolve();
        
    	Vector<Integer> bArray = new Vector<Integer>();
    	
    	bArray.addElement(1);
    	bArray.addElement(2);
    	bArray.addElement(3);
    	bArray.addElement(4);
    	bArray.addElement(5);
    	bArray.addElement(16);
    	bArray.addElement(7);
        bArray.addElement(8);
        bArray.addElement(9);
        bArray.addElement(10);
        bArray.addElement(11);
        bArray.addElement(12);
        bArray.addElement(13);
        bArray.addElement(14);
        bArray.addElement(15);
        bArray.addElement(06);
    	
        int b = object1.getBlankInArray(bArray);
    	assertEquals (5, b);
    	
    	Vector<Integer> expected = new Vector<Integer>();
    	expected.addElement(1);
    	expected.addElement(9);
    	expected.addElement(6);
    	expected.addElement(4);
    	Vector<Integer> result = new Vector<Integer>();
    	
    	result = object1.getSurrPos(b);
    	
    	assertEquals(expected, result);
    	
    	Tuple test = new Tuple(bArray, null);
    	Vector<Vector<Integer>> a = object1.getPossibleArrangements(test);	
    	
    	expected.clear();
    	expected.addElement(1);
    	expected.addElement(16);
    	expected.addElement(3);
    	expected.addElement(4);
    	expected.addElement(5);
    	expected.addElement(2);
    	expected.addElement(7);
    	expected.addElement(8);
        expected.addElement(9);
        expected.addElement(10);
        expected.addElement(11);
        expected.addElement(12);
        expected.addElement(13);
        expected.addElement(14);
        expected.addElement(15);
        expected.addElement(06);
        
    	assertEquals(expected, a.elementAt(0));
    	
    	expected.clear();
    	expected.addElement(1);
    	expected.addElement(2);
    	expected.addElement(3);
    	expected.addElement(4);
    	expected.addElement(5);
    	expected.addElement(10);
    	expected.addElement(7);
    	expected.addElement(8);
        expected.addElement(9);
        expected.addElement(16);
        expected.addElement(11);
        expected.addElement(12);
        expected.addElement(13);
        expected.addElement(14);
        expected.addElement(15);
        expected.addElement(06);
        
    	assertEquals(expected, a.elementAt(1));
    	
    	expected.clear();
    	expected.addElement(1);
    	expected.addElement(2);
    	expected.addElement(3);
    	expected.addElement(4);
    	expected.addElement(5);
    	expected.addElement(7);
    	expected.addElement(16);
    	expected.addElement(8);
        expected.addElement(9);
        expected.addElement(10);
        expected.addElement(11);
        expected.addElement(12);
        expected.addElement(13);
        expected.addElement(14);
        expected.addElement(15);
        expected.addElement(06);
        
    	assertEquals(expected, a.elementAt(2));
    	
    	expected.clear();
    	expected.addElement(1);
    	expected.addElement(2);
    	expected.addElement(3);
    	expected.addElement(4);
    	expected.addElement(16);
    	expected.addElement(5);
    	expected.addElement(7);
    	expected.addElement(8);
        expected.addElement(9);
        expected.addElement(10);
        expected.addElement(11);
        expected.addElement(12);
        expected.addElement(13);
        expected.addElement(14);
        expected.addElement(15);
        expected.addElement(06);
        
    	assertEquals(expected, a.elementAt(3));
    }
    
    @Test// array to grid and back
    public void testFifteen() {
    	ButtonNumsInGrid object1 = new ButtonNumsInGrid();
       
    	object1.setGrid();
    	
        int expected = 16;
        Pair p = object1.getBNumLocalInGrid(expected);
        int result = object1.getBNumLocalInArray(p);
        
    	assertEquals(expected, result+1);
    }
}
