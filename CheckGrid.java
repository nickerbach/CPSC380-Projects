import java.io.*;
import java.util.*;

public class CheckGrid {

    private static int grid[][] = new int[9][9];
    private static int missingRowNumber = -1;
    private static int missingColNumber = -1;
    private static int missingSubGridNumber = -1;
    private static int errorCount;
    private static String rowMessage = " ";
    private static String columnMessage = " ";
    private static String subGridMessage = " ";

    private static void readInGrid(String file) {

        try {
            BufferedReader readFile = new BufferedReader(new FileReader(file));
            String line;
            int row = 0;

            while ((line = readFile.readLine()) != null) {
                String[] vals = line.trim().split(",");

                for (int col = 0; col < 9; col++) {
                    grid[row][col] = Integer.parseInt(vals[col]);
                }

                row++;
            }

        } catch (IOException io) {
            io.printStackTrace();
        }

        //printGrid();

    }

    private static void printGrid() {
        for (int i =0; i < 9; ++i) {
            for (int j =0; j < 9; ++j) {
                System.out.print(grid[i][j]);
            }
            System.out.println();
        }
    }

    static private class CheckColumns extends Thread {

        private int columnError = -1;
        private HashSet<Integer> columnSet = new HashSet<>();

        public void run() {
            System.out.println("Checking columns");
            for (int i = 0; i < 9; ++i) {
                for (int j = 0; j < 9; ++j) {

                    if(!columnSet.add(grid[i][j])) {

                        columnError = i;
                        System.out.println("Column thread found failure in column " + (i+1));
                        Integer[] tempArray = columnSet.toArray(new Integer[columnSet.size()]);
                        Arrays.sort(tempArray);
                        for(int x = 1;x<10;++x)
                        {
                          if(Arrays.asList(tempArray).contains(x)== false)
                          {
                            missingColNumber = x;
                            System.out.println("Made it.");
                            System.out.println("**Error in column " + (i+1) + ", there was a duplicate " + grid[i][j] + " in the column."+ "\n" + "You can fix this error by replacing the duplicate "+grid[i][j]+" with a "+ missingColNumber);
                          }
                        }

                        //i = 9;j = 9;
                        //columnSet.clear();

                    }

                    if (j == 8) {

                        columnSet.clear();
                    }
                }

            }
        }

        private int getColumnError() {
            return columnError;
        }
    }

    static private class CheckRows extends Thread {

        private int rowError = -1;
        private HashSet<Integer> rowSet = new HashSet<>();

        public void run() {
            System.out.println("Checking rows");
            for (int i = 0; i < 9; ++i) {
                for (int j = 0; j < 9; ++j) {

                    if(!rowSet.add(grid[i][j])) {

                        rowError = i;
                        System.out.println("Row thread found error in row " + (i+1));
                        Integer[] tempArray = rowSet.toArray(new Integer[rowSet.size()]);
                        Arrays.sort(tempArray);
                        for(int x = 1;x<10;++x)
                        {
                          if(Arrays.asList(tempArray).contains(x)== false)
                          {
                            missingRowNumber = x;
                            System.out.println("**Error in row " + (i+1) + ", there was a duplicate " + grid[i][j] + " in the row."+ "\n" + "You can fix this error by replacing the duplicate "+grid[i][j]+" with a "+ missingRowNumber);
                          }
                          //System.out.println(missingRowNumber);
                        }
                        /*if(missingRowNumber != -1)
                        {
                          rowMessage = "Error in row " + (i+1) + ", there was a duplicate " + grid[i][j] + " in the row."+ "\n" + "You can fix this error by replacing the duplicate "+grid[i][j]+" with a "+ missingRowNumber;

                        }*/
                        //i = 9;j = 9;
                        rowSet.clear();

                    }

                    if (j == 8) {
                        rowSet.clear();
                    }
                }
            }
        }

        private int getRowError() {
            return rowError;
        }

    }

    private static void subGridCheck(int rowStart, int rowStop, int columnStart, int columnStop, int subGridNum) {

        HashSet<Integer> checkSet = new HashSet<>();

        System.out.println("Checking subgrid " + subGridNum);

        for (int i = rowStart; i < rowStop; ++ i) {
            for (int j = columnStart; j < columnStop; ++j) {

                if (!checkSet.add(grid[i][j]))  {
                    System.out.println("Thread checking subgrid " + subGridNum + " found an error at row: " + (i+1) + ", column: " + (j+1));
                    Integer[] tempArray = checkSet.toArray(new Integer[checkSet.size()]);
                    //Arrays.sort(tempArray);
                    for(int x = 1;x<10;++x)
                    {
                      if(Arrays.asList(tempArray).contains(x)== false)
                      {
                        missingSubGridNumber = x;
                        System.out.println("**Error in sub grid " + subGridNum + ", there was a duplicate " + grid[i][j] + " in the sub grid."+ "\n" + "You can fix this error by replacing the duplicate "+grid[i][j]+" with a "+ missingSubGridNumber);
                      }
                    }
                    /*if(missingSubGridNumber != -1)
                    {
                      subGridMessage = "Error in sub grid " + subGridNum + ", there was a duplicate " + grid[i][j] + " in the sub grid."+ "\n" + "You can fix this error by replacing the duplicate "+grid[i][j]+" with a "+ missingSubGridNumber;

                    }*/
                    i = rowStop; j = columnStop;
                    checkSet.clear();
                }
            }
        }
    }

    static private class CheckSubGrids extends Thread {

        public void run() {
            subGridCheck(0,3,0,3,1);
            subGridCheck(0,3,3,6,2);
            subGridCheck(0,3,6,9,3);
            subGridCheck(3,6,0,3,4);
            subGridCheck(3,6,3,6,5);
            subGridCheck(3,6,6,9,6);
            subGridCheck(6,9,0,3,7);
            subGridCheck(6,9,3,6,8);
            subGridCheck(6,9,6,9,9);
        }

    }

    public static void main(String args[]) {

        readInGrid("C:/Users/Nick/Desktop/CPSC380/Project1/test4.txt");

        ArrayList<Thread> threads = new ArrayList<>();

        CheckColumns columnThread = new CheckColumns();
        CheckRows rowThread = new CheckRows();
        CheckSubGrids subGridThread = new CheckSubGrids();

        threads.add(new Thread(columnThread));
      //  threads.add(new Thread(rowThread));
      //  threads.add(new Thread(subGridThread));

        for (Thread t : threads) {
            t.start();
        }

        try {
            for (Thread t : threads) {
                //System.out.println("joining threads");
                t.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int cError = columnThread.getColumnError();

        int rError = rowThread.getRowError();

        if(cError == -1 && rError == -1) {
            System.out.println("There were no errors in the sudoku grid.");
        } else {
            if(rowMessage != " ")
            {
              System.out.println(rowMessage);
            }
            if(columnMessage != " ")
            {
              System.out.println(columnMessage);
            }
            if(subGridMessage != " ")
            {
              System.out.println(subGridMessage);
            }
            //System.out.println(rowMessage);
            //System.out.println(columnMessage);
            //System.out.println(subGridMessage);
            //System.out.println("row" +missingRowNumber);
            //System.out.println("col" +missingColNumber);
            //System.out.println("Threads done. The error occured at [" + (rError+1) + ',' + (cError+1) + "].");
            //System.out.println("There was a duplicate " + grid[rError][cError] + " that caused the error." );
            //System.out.println("Because of this duplicate, there is a missing " +missingRowNumber +" in your row, column, or subgrid, which is causing your error.");
        }
    }
}
