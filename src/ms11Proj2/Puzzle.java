package ms11Proj2;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Puzzle{
	private final JFrame    Window;

	protected final JButton[] bNumsInGrid;
	protected final JButton   bAbout;
	protected final JButton   bHelp;
	protected final JButton   bShuffle;
	protected final JButton   bAutoSolve;
	protected final JButton   bUndo;
	protected final JButton   bUndoAll;
	protected final JButton   bQuit;
	
	protected Stack<Pair>     moveStack;
	
	public Puzzle()
    {
		Window = new JFrame("CS 342 Michael Slomczynski Project 2");
		
		bNumsInGrid = new JButton[16];
		bAbout = new JButton       ("About");
		bHelp = new JButton        ("Help");
		bShuffle = new JButton     ("Shuffle and Play");
		bAutoSolve = new JButton   ("Auto Solve");
		bUndo = new JButton        ("Undo");
		bUndoAll = new JButton     ("Undo All");
		bQuit = new JButton        ("Quit");

	    bAbout.addActionListener     (new ButtonAbout());
	    bHelp.addActionListener      (new ButtonHelp());
	    bShuffle.addActionListener   (new ButtonShuffle());
	    bAutoSolve.addActionListener (new ButtonAutoSolve());
	    bUndo.addActionListener      (new ButtonUndo());
	    bUndoAll.addActionListener   (new ButtonUndoAll());
	    bQuit.addActionListener      (new ButtonQuit());
		int i;
		for(i=0;i<16;i++)
		{
			String gridNum;
			if(i<15)
				gridNum = Integer.toString(i+1);
			else
				gridNum = "";
			bNumsInGrid[i] = new JButton(gridNum);
			bNumsInGrid[i].addActionListener(new ButtonNumsInGrid());
		}
		
		moveStack = new Stack<Pair>();
    }
	
    public void createWindow() {
        Dimension winDim = new Dimension();
        int width, height, wscale, hscale;
        width = 640;
        height = 480;
        wscale = 4;
        hscale = 12;
        winDim.setSize(width + 16, height + 29);
    	
        int i, x, y;
        x = (width/wscale);
        y = (height/hscale);
        for(i=0;i<16;i++)
        {
        	bNumsInGrid[i].setBounds(((i%4)*x + ((i%4)+1)*2), (((i/4)+3)*y + ((i/4)+1)*2), x, y);
        	Window.add(bNumsInGrid[i]);
        }
        
    	bAbout.setBounds     ((0*x + 2), (0*y + 2), x, y);
    	bHelp.setBounds      ((1*x + 4), (0*y + 2), x, y);
    	
    	bShuffle.setBounds   ((1*x + 4), (9*y + 2), 2*x, y);

    	bAutoSolve.setBounds ((0*x + 2), (11*y - 2), x, y);
    	bUndo.setBounds      ((1*x + 4), (11*y - 2), x, y);
    	bUndoAll.setBounds   ((2*x + 6), (11*y - 2), x, y);
    	bQuit.setBounds      ((3*x + 8), (11*y - 2), x, y);

    	Window.add(bAbout);
    	Window.add(bHelp);
    	Window.add(bShuffle);
    	Window.add(bAutoSolve);
    	Window.add(bUndo);
    	Window.add(bUndoAll);
    	Window.add(bQuit);
    	    	
        Window.setSize(winDim);
        Window.setResizable(false);
        Window.setLocationRelativeTo(null);
        Window.setLayout(null);
        Window.setVisible(true);
    }
    
    public void refreshWindow() {
    	Window.revalidate();
    	Window.repaint();
    }
    
    protected class Pair {
		private int x;
    	private int y;

    	public Pair(int i, int j) {
    		x = i;
    		y = j;
		}
    	
		public int getX() {
			return x;
		}
		
		public int getY() {
			return y;
		}

		public void setX(int x) {
			this.x = x;
		}
		
		public void setY(int y) {
			this.y = y;
		}
    
    	public void setDistBetween2Points(Pair point1, Pair point2) {
    		int a, b;
  		
    		a = point1.getX() - point2.getX();
    		b = point1.getY() - point2.getY();

            this.setX(a);
            this.setY(b);
			return;
    	}
    }
    
    protected class Tuple {
		private Vector<Integer> x;
    	private Vector<Integer> y;

    	public Tuple(Vector<Integer> i, Vector<Integer> j) {
    		x = i;
    		y = j;
		}
    	
		public Vector<Integer> getX() {
			return x;
		}
		
		public Vector<Integer> getY() {
			return y;
		}
    }
    
    protected class ButtonNumsInGrid       implements ActionListener{
    	private int[][] Grid;
    	
    	public ButtonNumsInGrid() {
    		Grid = new int[4][4];
    	}
    	
    	public int getButtonNumber(ActionEvent e) {
     		JButton bGridNum = (JButton)e.getSource();
     		String gridNumText = "";

     		if(bGridNum != null)
     			gridNumText = bGridNum.getText();

     		if(gridNumText.equals(""))
     			return 16;
  		   
     		int gridNum = Integer.parseInt(gridNumText);
    		return gridNum;
    	}
    	
    	public Pair getBNumLocalInGrid(int bNum) {
    		int i = 0, j = 0;
    		Pair coordinates = new Pair(i, j);
    		for(i=0;i<4;i++)
        		for(j=0;j<4;j++)
        			if(Grid[i][j]==bNum)
        			{
        	    		coordinates.setX(j);
        	    		coordinates.setY(i);
        				return coordinates;
        			}
			return coordinates;
    	}
    	
    	public int getBNumLocalInArray(Pair coordinates) {
    		int i, j;
    		i = coordinates.getX();
    		j = coordinates.getY();

			return j*4+i;
    	}
    	
    	public void setGrid() {
    		int i, j;
    		for(i=0;i<4;i++)
    		{
        		for(j=0;j<4;j++)
        		{
        			if(bNumsInGrid[(i*4)+(j)].getText().equals(""))
        				Grid[i][j] = 16;
        			else
        				Grid[i][j] = Integer.parseInt(bNumsInGrid[(i*4)+(j)].getText());
        		}
    		}
    		return;
    	}
    
    	public void swapButtonNumbers(ActionEvent e) {
    		setGrid();
    		Pair filled = getBNumLocalInGrid(getButtonNumber(e));
    		Pair blank = getBNumLocalInGrid(16);

    		Pair dist = new Pair(0, 0);
    		dist.setDistBetween2Points(filled, blank);
    		
    		if(Math.abs(dist.getX())>1)
    			return;
    		if(Math.abs(dist.getY())>1)
    			return;
    		if(dist.getX()==0 && dist.getY()==0)
    			return;
    		if(Math.abs(dist.getX())==Math.abs(dist.getY()))
    			return;
    		
    		moveStack.push(blank);
    		//moveStack.peek().printPair();
    		
    		String temp  = Integer.toString(getButtonNumber(e));
    		//int    tempx = filled.getX();
    		//int    tempy = filled.getY();
    	
    		bNumsInGrid[getBNumLocalInArray(filled)].setText("");
    		//filled.setX(blank.getX());
    		//filled.setY(blank.getY());

    		bNumsInGrid[getBNumLocalInArray(blank)].setText(temp);		
    		//blank.setX(tempx);
    		//blank.setY(tempy);

    		return;
    	}
    	
    	@Override
        public void actionPerformed(ActionEvent e) {
    		swapButtonNumbers(e);   
    		refreshWindow();
        }
    }
    
    protected class ButtonAbout            implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
        	JLabel label = new JLabel("<html>Author's Name: Michael Slomczynski<br>Program Created: 10/3/17<br>Why: For CS 342, Program 2<br>Extra Credit: No</html>");
            label.setFont(new Font("Courier New", Font.PLAIN, 12));
            JOptionPane.showMessageDialog(null, label, "About the 15 Puzzle", JOptionPane.PLAIN_MESSAGE);
        }
    }
    
    protected class ButtonHelp             implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
        	JLabel label = new JLabel("<html>Welcome.<br>You can find general information here about what a button does or how to play the game.<br><br>Button Information:<br>About = If you want to know more about who created this, why they created this, and when they created this program.<br><br>Help = If you need more information about what a button does or how to play the game.<br><br>Shuffle and Play = Once you are ready to start playing the game this button will shuffle the board.<br><br>Auto Solve = If the board is too difficult or you're getting bored and want to see the solution this button will provide the solution for you step-by-step.<br><br>Undo = If you made a mistake or a few this button will allow you to redo your previous moves to a state that you are happy with.<br><br>Undo All = This button will remove all of the moves you have done to the board returning the board to its original shuffled state.<br><br>Quit = If you want to quit the program.<br><br>How to Play:<br>The goal of the game is to reach an unshuffled end state of:<br>| 01 | 02 | 03 | 04 |<br>| 05 | 06 | 07 | 08 |<br>| 09 | 10 | 11 | 12 |<br>| 13 | 14 | 15 | BL |<br><br>There are 4 possible moves:<br>Down Into:<br>| 01 | 02 | 03 | 04 |    -->    | 01 | BL | 03 | 04 |<br>| 05 | BL | 07 | 08 |    -->    | 05 | 02 | 07 | 08 |<br>| 09 | 10 | 11 | 12 |    -->    | 09 | 10 | 11 | 12 |<br>| 13 | 14 | 15 | 06 |    -->    | 13 | 14 | 15 | 06 |<br><br>Left Into:<br>| 01 | 02 | 03 | 04 |    -->    | 01 | 02 | 03 | 04 |<br>| 05 | BL | 07 | 08 |    -->    | 05 | 07 | BL | 08 |<br>| 09 | 10 | 11 | 12 |    -->    | 09 | 10 | 11 | 12 |<br>| 13 | 14 | 15 | 06 |    -->    | 13 | 14 | 15 | 06 |<br><br>Right Into:<br>| 01 | 02 | 03 | 04 |    -->    | 01 | 02 | 03 | 04 |<br>| 05 | BL | 07 | 08 |    -->    | BL | 05 | 07 | 08 |<br>| 09 | 10 | 11 | 12 |    -->    | 09 | 10 | 11 | 12 |<br>| 13 | 14 | 15 | 06 |    -->    | 13 | 14 | 15 | 06 |<br><br>Up Into:<br>| 01 | 02 | 03 | 04 |    -->    | 01 | 02 | 03 | 04 |<br>| 05 | BL | 07 | 08 |    -->    | 05 | 10 | 07 | 08 |<br>| 09 | 10 | 11 | 12 |    -->    | 09 | BL | 11 | 12 |<br>| 13 | 14 | 15 | 06 |    -->    | 13 | 14 | 15 | 06 |<br></html>");
            label.setFont(new Font("Courier New", Font.PLAIN, 12));
        	JOptionPane.showMessageDialog(null, label, "About the 15 Puzzle", JOptionPane.PLAIN_MESSAGE);
        }
    }
    
    protected class ButtonShuffle          implements ActionListener{
    	private int[] TileNumbers;
    	private int[] InversionCount;
    	
    	public ButtonShuffle()
    	{
    		TileNumbers = new int[16];
    		InversionCount = new int[16];
    		int i;
    		for(i=0;i<16;i++)
    		{
    			TileNumbers[i]=i+1;
    			InversionCount[i]=0;
    		}
    	}
    	
    	public int getInversionCount() {
    		int i, j, sum;
			sum = 0;
			for(i=0;i<16;i++)
			{
				InversionCount[i] = 0;		
				for(j=i;j<16;j++)
					if(TileNumbers[i]>TileNumbers[j])
						InversionCount[i]++;		
				sum = sum + InversionCount[i];
			}
			return sum;
    	}
    	
    	public void setTileNumbersRand() {
    		int i;
    		int min = 0;
    		int max	= 15;
    		int randNum, tempNum;
    		
    		for(i=0;i<15;i++)
    		{
    			randNum = ThreadLocalRandom.current().nextInt(min, max);
    			
    			tempNum = TileNumbers[randNum];
    			TileNumbers[randNum] = TileNumbers[i];
    			TileNumbers[i] = tempNum;
    		}
			return;
    	}
    	
    	public void setBoard() {
    		int i;
			for(i=0; i<16; i++)
			{
				String gridNum;
				if(i<15)
					gridNum = Integer.toString(TileNumbers[i]);
				else
					gridNum = "";
				bNumsInGrid[i].setText(gridNum);
			}
			return;
    	}

        @Override
        public void actionPerformed(ActionEvent e) {
        	do
        	{
        		setTileNumbersRand();
            	if(getInversionCount()%2 == 0)
            	{
            	  setBoard();
            	  if(moveStack.empty())
            		  return;
            	  else
            	  {
            		  while(!moveStack.isEmpty())
            			  moveStack.pop();
            		  return;
            	  }
            	}
        	}while(getInversionCount()%2 == 1);
        }
    }
    
    protected class ButtonAutoSolve        extends ButtonUndoAll{
    	private Queue<Tuple>             q;
    	private Set<Vector<Integer>>     visited;
    	private Vector<Integer>          p;
    	    	
        @SuppressWarnings("unchecked")
		public Vector<Vector<Integer>>  getPossibleArrangements(Tuple curr) {
        	Vector<Vector<Integer>> ret = new Vector<Vector<Integer>>();
        	int blankPos = getBlankInArray(curr.getX());
        	Vector<Integer> surrPos = getSurrPos(blankPos);
        	
        	int i;
        	for(i=0; i<surrPos.size(); i++)
        	{
        		Vector<Integer> nVec = new Vector<Integer>();
        		nVec = (Vector<Integer>) curr.getX().clone();

        		int pos = surrPos.elementAt(i);
        		
        		nVec.set(blankPos, nVec.get(pos));
        		nVec.set(pos, 16);

        		//System.out.println("Curr");
				//printCurrArrangment(nVec);

        		ret.addElement(nVec);        		

        	}
        	return ret;
		}
    	
    	public int getBlankInArray(Vector<Integer> r) {
    		int i;
    		for(i=0;i<16;i++)
        		if(r.elementAt(i).equals(16))
        			return i;
			return 16;
		}
    	
    	public Vector<Integer> getSurrPos(int blankPos) {
           
    		Vector<Integer> ret = new Vector<Integer>();
            
    		int a, b, c, d;
    		
    		a = blankPos-4; //if a < than 0
    		b = blankPos+4; //if b > than 15
    		c = blankPos+1; //if c % 4==0
    		d = blankPos-1; //if blank % 4==0
    		
    		if(a>0)
    			ret.add(a);
    		if(b<16)
    			ret.add(b);
    		if(c%4!=0)
    			ret.add(c);
    		if(blankPos%4!=0)
    			ret.add(d);
    		
    		return ret;
		}

		public Vector<Integer> setCurrArrangment() {
    		int i;
    		for(i=0;i<16;i++)
    		{
    			if(bNumsInGrid[i].getText().equals(""))
    				p.add(16);
    			else
    				p.add(Integer.parseInt(bNumsInGrid[i].getText()));			
    		}
    		return p;
    	}
    	    	
        @SuppressWarnings("unchecked")
		public Tuple BFS() {
        	long start = System.currentTimeMillis();
        	long end = start + 5*60*1000;
        	q = new LinkedList<Tuple>();

        	visited = new HashSet<Vector<Integer>>();
            p = new Vector<Integer>();
        	p = setCurrArrangment();
        	
        	Tuple curr = new Tuple(p, null);
        	q.add(curr);
        	visited.add(p);
        	

        	while(!q.isEmpty())
        	{
        		curr = q.poll();
        		if(isCorrect(curr))
        			return curr;  		

        		 if(!(System.currentTimeMillis() < end))
        		 {
        	         q = null;
        	         p = null;
        	         visited = null;
        	         System.gc();
        			 //System.out.println("Hello");
        			 JLabel label = new JLabel("<html>This is a hard problem and I need more memory to solve it.<br>Try solving doing a little bit on your own to help me out.</html>");
        	         label.setFont(new Font("Courier New", Font.PLAIN, 12));
        	         JOptionPane.showMessageDialog(null, label, "About the 15 Puzzle", JOptionPane.WARNING_MESSAGE);
        	         
        	         return null;
        		 }
        			 
        		for (Vector<Integer> R : getPossibleArrangements(curr))
        		{       			
        			if(!visited.contains(R))
        			{
        				Vector<Integer> M;
        				if(curr.getY()!=null)
        					M = (Vector<Integer>) curr.getY().clone();
        				else
        					M = new Vector<Integer>();

        				M.add(getBlankInArray(R));
        				Tuple next = new Tuple(R, M);
        				q.add(next);
        				visited.add(R);
        			}
        		}
        	}
			return null;     	
        }
    	
        public boolean isCorrect(Tuple curr) {
			int i, count = 0;
			for(i=0;i<16;i++)
				if(curr.getX().elementAt(i)==(i+1))
					count++;
			if(count==16)
				return true;
			return false;
		}
		
		public int getBlank(JButton[] bNumsInGrid) {
			int i;
			for(i=0;i<16;i++)
				if(bNumsInGrid[i].getText().equals(""))
					return i;
			return 16;
		}	
		
		@Override
        public void actionPerformed(ActionEvent e) {
        	Timer myTimer = new Timer();

        	Tuple solution = BFS();
        	
       		if(solution == null)
       			return;
        	
       		if(solution.getY() == null)
       			return;
        		
        	else
        	{
       			setBNumDisabled();
           		myTimer.schedule(new TimerTask() {
               		public void run() {
                       	if(!solution.getY().isEmpty())
                       	{
                       		int start = getBlank(bNumsInGrid);
                       		//System.out.println(start);
                       		int next = solution.getY().firstElement();
                       		solution.getY().removeElementAt(0);
                       				
                    		bNumsInGrid[start].setText(bNumsInGrid[next].getText());
                    		bNumsInGrid[next].setText("");
                   		}
                       	else 
                       	{
                       		setBNumEnabled();
                       		myTimer.cancel();
                       		myTimer.purge();
                       		return;
                       	}
               		}
               	}, 1000, 750);
        	}
        	return;
		}
    }
    
    protected class ButtonUndo             extends ButtonNumsInGrid{
        
    	public void swapButtonNumbers(Pair old) {
    		setGrid();
    		Pair filled = old;
    		Pair blank = getBNumLocalInGrid(16);

    		Pair dist = new Pair(0, 0);
    		dist.setDistBetween2Points(filled, blank);
    		
    		if(Math.abs(dist.getX())>1)
    			return;
    		if(Math.abs(dist.getY())>1)
    			return;
    		if(dist.getX()==0 && dist.getY()==0)
    			return;
    		if(Math.abs(dist.getX())==Math.abs(dist.getY()))
    			return;

    		//moveStack.push(blank);
    		//moveStack.peek().printPair();
    		
    		String temp  = bNumsInGrid[getBNumLocalInArray(filled)].getText();
    		//int    tempx = filled.getX();
    		//int    tempy = filled.getY();
    	
    		bNumsInGrid[getBNumLocalInArray(filled)].setText("");
    		//filled.setX(blank.getX());
    		//filled.setY(blank.getY());

    		bNumsInGrid[getBNumLocalInArray(blank)].setText(temp);		
    		//blank.setX(tempx);
    		//blank.setY(tempy);

    		return;
    	}
    
        @Override
        public void actionPerformed(ActionEvent e) {
        	if(!moveStack.isEmpty())
        	{
        		Pair oldCoordinates = moveStack.pop();
        		swapButtonNumbers(oldCoordinates);
        	}
        }
    }
    
    protected class ButtonUndoAll          extends ButtonUndo {
    
    	public void setBNumDisabled() {
    		int i;
    		for(i=0;i<16;i++)
    			bNumsInGrid[i].setEnabled(false);
    		return;
    	}
    	
    	public void setBNumEnabled() {
    		int i;
    		for(i=0;i<16;i++)
    			bNumsInGrid[i].setEnabled(true);
    		return;
    	}
    		
        @Override
        public void actionPerformed(ActionEvent e) {
        	Timer myTimer = new Timer();
        	
        	if(moveStack.isEmpty())
        		return;
        	
   			setBNumDisabled();
       		myTimer.schedule(new TimerTask() {
           		public void run() {
                   	if(!moveStack.isEmpty())
                   	{
                   		Pair oldCoordinates = moveStack.pop();
               		    swapButtonNumbers(oldCoordinates);
               		}
                   	else 
                   	{
                   		setBNumEnabled();
                   		myTimer.cancel();
                   		myTimer.purge();
                   		return;
                   	}
           		}	
           	}, 1000, 750);
        }
    }
    
    protected class ButtonQuit             implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
        	System.exit(0);
        }
    }
}


