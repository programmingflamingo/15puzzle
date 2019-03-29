package puzzle;

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

public class Puzzle {
  private final JFrame window;
    
  protected final JButton[] btnNumsInGrid;
  protected final JButton   btnAbout;
  protected final JButton   btnHelp;
  protected final JButton   btnShuffle;
  protected final JButton   btnAutoSolve;
  protected final JButton   btnUndo;
  protected final JButton   btnUndoAll;
  protected final JButton   btnQuit;
  protected Stack<Pair>     moveStack;

  /**
   * hello world.
   * 
   */
  public Puzzle() {
    window = new JFrame("Michael Slomczynski - 15 Puzzle");

    btnNumsInGrid = new JButton[16];
    btnAbout      = new JButton("About");
    btnHelp       = new JButton("Help");
    btnShuffle    = new JButton("Shuffle and Play");
    btnAutoSolve  = new JButton("Auto Solve");
    btnUndo       = new JButton("Undo");
    btnUndoAll    = new JButton("Undo All");
    btnQuit       = new JButton("Quit");

    btnAbout.addActionListener(new ButtonAbout());
    btnHelp.addActionListener(new ButtonHelp());
    btnShuffle.addActionListener(new ButtonShuffle());
    btnAutoSolve.addActionListener(new ButtonAutoSolve());
    btnUndo.addActionListener(new ButtonUndo());
    btnUndoAll.addActionListener(new ButtonUndoAll());
    btnQuit.addActionListener(new ButtonQuit());
    int i;
    for (i = 0; i < 16;i++) {
      String gridNum;
      if (i < 15) {
        gridNum = Integer.toString(i + 1);
      } else {
        gridNum = "";
      }
      btnNumsInGrid[i] = new JButton(gridNum);
      btnNumsInGrid[i].addActionListener(new ButtonNumsInGrid());
    }

    moveStack = new Stack<Pair>();
  }

  /**
   * hello world.
   * 
   */
  public void createWindow() {
    Dimension winDim = new Dimension();
    int winWidth = 640;
    int winHeight = 480;
    int winWScale = 4;
    int winHscale = 12;
    winDim.setSize(winWidth + 16, winHeight + 29);

    int i;
    int btnX;
    int btnY;
    int btnWidth = (winWidth / winWScale);
    int btnHeight = (winHeight / winHscale);
    for (i = 0; i < 16; i++) {
      btnX = ((i % 4) * btnWidth + ((i % 4) + 1) * 2);
      btnY = (((i / 4) + 3) * btnHeight + ((i / 4) + 1) * 2);
      btnNumsInGrid[i].setBounds(btnX, btnY, btnWidth, btnHeight);
      window.add(btnNumsInGrid[i]);
    }
    
    btnX = (0 * btnWidth + 2);
    btnY = (0 * btnHeight + 2);
    btnAbout.setBounds(btnX, btnY, btnWidth, btnHeight);
    
    btnX = (1 * btnWidth + 4);
    btnY = (0 * btnHeight + 2);
    btnHelp.setBounds(btnX, btnY, btnWidth, btnHeight);
    
    btnX = (1 * btnWidth + 4);
    btnY = (9 * btnHeight + 2);
    btnShuffle.setBounds(btnX, btnY, 2 * btnWidth, btnHeight);

    btnX = (0 * btnWidth + 2);
    btnY = (11 * btnHeight - 2);
    btnAutoSolve.setBounds(btnX, btnY, btnWidth, btnHeight);
    
    btnX = (1 * btnWidth + 4);
    btnY = (11 * btnHeight - 2);
    btnUndo.setBounds(btnX, btnY, btnWidth, btnHeight);
    
    btnX = (2 * btnWidth + 6);
    btnY = (11 * btnHeight - 2);
    btnUndoAll.setBounds(btnX, btnY, btnWidth, btnHeight);
    
    btnX = (3 * btnWidth + 8);
    btnY = (11 * btnHeight - 2);
    btnQuit.setBounds(btnX, btnY, btnWidth, btnHeight);

    window.add(btnAbout);
    window.add(btnHelp);
    window.add(btnShuffle);
    window.add(btnAutoSolve);
    window.add(btnUndo);
    window.add(btnUndoAll);
    window.add(btnQuit);

    window.setSize(winDim);
    window.setResizable(false);
    window.setLocationRelativeTo(null);
    window.setLayout(null);
    window.setVisible(true);
  }
    
  public void refreshWindow() {
    window.revalidate();
    window.repaint();
  }
    
  protected class Pair {
    private int x0;
    private int x1;
    
    public Pair(int i, int j) {
      x0 = i;
      x1 = j;
    }

    public int getX() {
      return x0;
    }
    
    public int getY() {
      return x1;
    }
    
    public void setX(int x) {
      this.x0 = x;
    }
    
    public void setY(int y) {
      this.x1 = y;
    }
    
    public void setDistBetween2Points(Pair point1, Pair point2) {
      int a;
      int b;
      
      
      a = point1.getX() - point2.getX();
      b = point1.getY() - point2.getY();
      
      this.setX(a);
      this.setY(b);
      return;
    }
  }
    
  protected class Tuple {
    private Vector<Integer> x0; //currentArrangement
    private Vector<Integer> x1; //solution
    
    public Tuple(Vector<Integer> i, Vector<Integer> j) {
      x0 = i;
      x1 = j;
    }
    
    public Vector<Integer> getX() {
      return x0;
    }

    public Vector<Integer> getY() {
      return x1;
    }
  }
    
  protected class ButtonNumsInGrid implements ActionListener {
    private int[][] btnGrid;

    public ButtonNumsInGrid() {
      btnGrid = new int[4][4];
    }
    
    public int getButtonNumber(ActionEvent e) {
      JButton btnGridNum = (JButton)e.getSource();
      String gridNumText = "";
      
      if (btnGridNum != null) {
        gridNumText = btnGridNum.getText();
      }
      
      if (gridNumText.equals("")) {
        return 16;
      }
      
      int gridNum = Integer.parseInt(gridNumText);
      return gridNum;
    }

    public Pair getBNumLocalInGrid(int btnNum) {
      int i = 0;
      int j = 0;
      Pair coordinates = new Pair(i, j);
      for (i = 0; i < 4; i++) {
        for (j = 0; j < 4; j++) {
          if (btnGrid[i][j] == btnNum) {
            coordinates.setX(j);
            coordinates.setY(i);
            return coordinates;
          }
        }
      }
      return coordinates;
    }

    public int getBNumLocalInArray(Pair coordinates) {
      int i = coordinates.getX();
      int j = coordinates.getY();

      return j * 4 + i;
    }

    public void setGrid() {
      int i;
      int j;
      for (i = 0; i < 4; i++) {
        for (j = 0; j < 4; j++) {
          if (btnNumsInGrid[(i * 4) + (j)].getText().equals("")) {
            btnGrid[i][j] = 16;
          } else {
            btnGrid[i][j] = Integer.parseInt(btnNumsInGrid[(i * 4) + (j)].getText());
          }
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

      if (Math.abs(dist.getX()) > 1) {
        return;
      }
      
      if (Math.abs(dist.getY()) > 1) {
        return;
      }
      
      if (dist.getX() == 0 && dist.getY() == 0) {
        return;
      }
      
      if (Math.abs(dist.getX()) == Math.abs(dist.getY())) {
        return;
      }

      moveStack.push(blank);
      //moveStack.peek().printPair();

      String temp  = Integer.toString(getButtonNumber(e));
      //int    tempx = filled.getX();
      //int    tempy = filled.getY();
      
      btnNumsInGrid[getBNumLocalInArray(filled)].setText("");
      //filled.setX(blank.getX());
      //filled.setY(blank.getY());

      btnNumsInGrid[getBNumLocalInArray(blank)].setText(temp);
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
  
  protected class ButtonAbout implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      JLabel label = new JLabel(" < html > Author's Name: Michael Slomczynski < br > Program Created: 10/3/17 < br > Why: For CS 342, Program 2 < br > Extra Credit: No < /html > ");
      label.setFont(new Font("Courier New", Font.PLAIN, 12));
      JOptionPane.showMessageDialog(null, label, "About the 15 Puzzle", JOptionPane.PLAIN_MESSAGE);
    }
  }
  
  protected class ButtonHelp implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      JLabel label = new JLabel(" < html > Welcome. < br > You can find general information here about what a button does or how to play the game. < br >  < br > Button Information: < br > About = If you want to know more about who created this, why they created this, and when they created this program. < br >  < br > Help = If you need more information about what a button does or how to play the game. < br >  < br > Shuffle and Play = Once you are ready to start playing the game this button will shuffle the board. < br >  < br > Auto Solve = If the board is too difficult or you're getting bored and want to see the solution this button will provide the solution for you step-by-step. < br >  < br > Undo = If you made a mistake or a few this button will allow you to redo your previous moves to a state that you are happy with. < br >  < br > Undo All = This button will remove all of the moves you have done to the board returning the board to its original shuffled state. < br >  < br > Quit = If you want to quit the program. < br >  < br > How to Play: < br > The goal of the game is to reach an unshuffled end state of: < br > | 01 | 02 | 03 | 04 | < br > | 05 | 06 | 07 | 08 | < br > | 09 | 10 | 11 | 12 | < br > | 13 | 14 | 15 | BL | < br >  < br > There are 4 possible moves: < br > Down Into: < br > | 01 | 02 | 03 | 04 |    -- >     | 01 | BL | 03 | 04 | < br > | 05 | BL | 07 | 08 |    -- >     | 05 | 02 | 07 | 08 | < br > | 09 | 10 | 11 | 12 |    -- >     | 09 | 10 | 11 | 12 | < br > | 13 | 14 | 15 | 06 |    -- >     | 13 | 14 | 15 | 06 | < br >  < br > Left Into: < br > | 01 | 02 | 03 | 04 |    -- >     | 01 | 02 | 03 | 04 | < br > | 05 | BL | 07 | 08 |    -- >     | 05 | 07 | BL | 08 | < br > | 09 | 10 | 11 | 12 |    -- >     | 09 | 10 | 11 | 12 | < br > | 13 | 14 | 15 | 06 |    -- >     | 13 | 14 | 15 | 06 | < br >  < br > Right Into: < br > | 01 | 02 | 03 | 04 |    -- >     | 01 | 02 | 03 | 04 | < br > | 05 | BL | 07 | 08 |    -- >     | BL | 05 | 07 | 08 | < br > | 09 | 10 | 11 | 12 |    -- >     | 09 | 10 | 11 | 12 | < br > | 13 | 14 | 15 | 06 |    -- >     | 13 | 14 | 15 | 06 | < br >  < br > Up Into: < br > | 01 | 02 | 03 | 04 |    -- >     | 01 | 02 | 03 | 04 | < br > | 05 | BL | 07 | 08 |    -- >     | 05 | 10 | 07 | 08 | < br > | 09 | 10 | 11 | 12 |    -- >     | 09 | BL | 11 | 12 | < br > | 13 | 14 | 15 | 06 |    -- >     | 13 | 14 | 15 | 06 | < br >  < /html > ");
      label.setFont(new Font("Courier New", Font.PLAIN, 12));
      JOptionPane.showMessageDialog(null, label, "About the 15 Puzzle", JOptionPane.PLAIN_MESSAGE);
    }
  }
    
  protected class ButtonShuffle implements ActionListener {
    private int[] tileNumbers;
    private int[] inversionCount;

    public ButtonShuffle() {
      tileNumbers = new int[16];
      inversionCount = new int[16];
      int i;
      for (i = 0;i < 16;i++) {
        tileNumbers[i] = i + 1;
        inversionCount[i] = 0;
      }
    }

    public int getInversionCount() {
      int i;
      int j;
      int sum =  0;
      for (i = 0; i < 16; i++) {
        inversionCount[i] = 0;
        for (j = i; j < 16; j++) {
          if (tileNumbers[i] > tileNumbers[j]) {
            inversionCount[i]++;
          }
        }
        sum = sum + inversionCount[i];
      }
      return sum;
    }

    public void setTileNumbersRand() {
      int i;
      int min = 0;
      int max = 15;
      int randNum;
      //int tempNum;

      for (i = 0; i < 15; i++) {
        randNum = ThreadLocalRandom.current().nextInt(min, max);
        //tempNum = tileNumbers[randNum];
        tileNumbers[randNum] = tileNumbers[i];
      }
      return;
    }

    public void setBoard() {
      int i;
      for (i = 0; i < 16; i++) {
        String gridNum;
        if (i < 15) {
          gridNum = Integer.toString(tileNumbers[i]);
        } else {
          gridNum = "";
        }
        btnNumsInGrid[i].setText(gridNum);
      }
      return;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      do {
        setTileNumbersRand();
        if (getInversionCount() % 2 == 0) {
          setBoard();
          if (moveStack.empty()) {
            return;
          } else {
            while (!moveStack.isEmpty()) {
              moveStack.pop();
            }
            return;
          }
        }
      } while (getInversionCount() % 2 == 1);
    }
  }
    
  protected class ButtonAutoSolve extends ButtonUndoAll {
    private Queue<Tuple> myQueue;
    private Set<Vector<Integer>> visited;
    private Vector<Integer> currArrangment;
    
    @SuppressWarnings("unchecked")
    public Vector<Vector<Integer>> getPossibleArrangements(Tuple curr) {
      Vector<Vector<Integer>> ret = new Vector<Vector<Integer>>();
      int blankPos = getBlankInArray(curr.getX());
      Vector<Integer> surrPos = getSurrPos(blankPos);

      int i;
      for (i = 0; i < surrPos.size(); i++) {
        Vector<Integer> next = new Vector<Integer>();
        next = (Vector<Integer>) curr.getX().clone();

        int pos = surrPos.elementAt(i);

        next.set(blankPos, next.get(pos));
        next.set(pos, 16);

        //System.out.println("Curr");
        //System.out.println(curr.getX());

        ret.addElement(next);

      }
      return ret;
    }

    public int getBlankInArray(Vector<Integer> r) {
      int i;
      for (i = 0; i < 16; i++) {
        if (r.elementAt(i).equals(16)) {
          return i;
        }
      }
      return 16;
    }

    public Vector<Integer> getSurrPos(int blankPos) { 
      Vector<Integer> ret = new Vector<Integer>();
      int a = blankPos - 4; //if a < than 0
      int b = blankPos + 4; //if b > than 15
      int c = blankPos + 1; //if c % 4 == 0
      int d = blankPos - 1; //if blank % 4 == 0

      if (a > 0) {
        ret.add(a);
      }
      if (b < 16) {
        ret.add(b);
      }
      if (c % 4 != 0) {
        ret.add(c);
      }
      if (blankPos % 4 != 0) {
        ret.add(d);
      }
      return ret;
    }

    public Vector<Integer> setCurrArrangment() {
      int i;
      for (i = 0; i < 16; i++) {
        if (btnNumsInGrid[i].getText().equals("")) {
          currArrangment.add(16);
        } else {
          currArrangment.add(Integer.parseInt(btnNumsInGrid[i].getText()));
        }
      }
      return currArrangment;
    }

    @SuppressWarnings("unchecked")
    public Tuple breadthFirstSearch() {
      long start = System.currentTimeMillis();
      long end = start + 5 * 60 * 1000;
      myQueue = new LinkedList<Tuple>();

      visited = new HashSet<Vector<Integer>>();
      currArrangment = new Vector<Integer>();
      currArrangment = setCurrArrangment();
      
      Tuple curr = new Tuple(currArrangment, null);
      myQueue.add(curr);
      visited.add(currArrangment);


      while (!myQueue.isEmpty()) {
        curr = myQueue.poll();
        if (isCorrect(curr)) { 
          return curr;
        }

        if (!(System.currentTimeMillis() < end)) {
          myQueue = null;
          currArrangment = null;
          visited = null;

          System.gc();
          //System.out.println("Hello");
          JLabel label = new JLabel(" < html > This is a hard problem and I need more memory to solve it. < br > Try solving doing a little bit on your own to help me out. < /html > ");
          label.setFont(new Font("Courier New", Font.PLAIN, 12));
          JOptionPane.showMessageDialog(null, label, "About the 15 Puzzle", JOptionPane.WARNING_MESSAGE);
          return null;
        }

        for (Vector<Integer> possibleArrangement : getPossibleArrangements(curr)) {
          if (!visited.contains(possibleArrangement)) {
            Vector<Integer> solution;
            if (curr.getY() != null) {
              solution = (Vector<Integer>) curr.getY().clone();
            } else {
              solution = new Vector<Integer>();
            }

            solution.add(getBlankInArray(possibleArrangement));
            Tuple next = new Tuple(possibleArrangement, solution);
            myQueue.add(next);
            visited.add(possibleArrangement);
          }
        }
      }
      return null;
    }

    public boolean isCorrect(Tuple curr) {
      int i;
      int count = 0;
      for (i = 0; i < 16; i++) {
        if (curr.getX().elementAt(i) == (i + 1)) {
          count++;
        }
      }
      if (count == 16) {
        return true;
      }
      return false;
    }
    
    public int getBlank(JButton[] btnNumsInGrid) {
      int i;
      for (i = 0; i < 16; i++) {
        if (btnNumsInGrid[i].getText().equals("")) {
          return i;
        }
      }
      return 16;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      Timer myTimer = new Timer();

      Tuple solution = breadthFirstSearch();

      if (solution == null) {
        return;
      }

      if (solution.getY() == null) {
        return;
      } else {
        setBNumDisabled();
        myTimer.schedule(new TimerTask() {
          public void run() {
            if (!solution.getY().isEmpty()) {
              int start = getBlank(btnNumsInGrid);
              //System.out.println(start);
              int next = solution.getY().firstElement();
              solution.getY().removeElementAt(0);

              btnNumsInGrid[start].setText(btnNumsInGrid[next].getText());
              btnNumsInGrid[next].setText("");
            } else { 
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
    
  protected class ButtonUndo extends ButtonNumsInGrid {
    public void swapButtonNumbers(Pair old) {
      setGrid();
      Pair filled = old;
      Pair blank = getBNumLocalInGrid(16);

      Pair dist = new Pair(0, 0);
      dist.setDistBetween2Points(filled, blank);
      if (Math.abs(dist.getX()) > 1) {
        return;
      }
      if (Math.abs(dist.getY()) > 1) {
        return;
      }
      
      if (dist.getX() == 0 && dist.getY() == 0) {
        return;
      }
      
      if (Math.abs(dist.getX()) == Math.abs(dist.getY())) {
        return;
      }

      //moveStack.push(blank);
      //moveStack.peek().printPair();

      String temp = btnNumsInGrid[getBNumLocalInArray(filled)].getText();
      //int    tempx = filled.getX();
      //int    tempy = filled.getY();

      btnNumsInGrid[getBNumLocalInArray(filled)].setText("");
      //filled.setX(blank.getX());
      //filled.setY(blank.getY());

      btnNumsInGrid[getBNumLocalInArray(blank)].setText(temp);
      //blank.setX(tempx);
      //blank.setY(tempy);

      return;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
      if (!moveStack.isEmpty()) {
        Pair oldCoordinates = moveStack.pop();
        swapButtonNumbers(oldCoordinates);
      }
    }
  }
    
  protected class ButtonUndoAll extends ButtonUndo {
    public void setBNumDisabled() {
      int i;
      for (i = 0; i < 16; i++) {
        btnNumsInGrid[i].setEnabled(false);
      }
      return;
    }
    
    public void setBNumEnabled() {
      int i;
      for (i = 0; i < 16; i++) {
        btnNumsInGrid[i].setEnabled(true);
      }
      return;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      Timer myTimer = new Timer();
      
      if (moveStack.isEmpty()) {
        return;
      }

      setBNumDisabled();
      myTimer.schedule(new TimerTask() {
        public void run() {
          if (!moveStack.isEmpty()) {
            Pair oldCoordinates = moveStack.pop(); 
            swapButtonNumbers(oldCoordinates);
          } else {
            setBNumEnabled();
            myTimer.cancel();
            myTimer.purge();
            return;
          }
        }
      }, 1000, 750);
    }
  }

  protected class ButtonQuit implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      System.exit(0);
    }
  }
    
  public static void main(String[] args) {
    Puzzle p = new Puzzle();
    p.createWindow();
  }
}


