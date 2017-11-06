import java.io.*;
import java.util.*;
public class SeeSaw
{
  private static double fredHeight = 1.0;
  private static double wilmaHeight = 7.0;

  public static class MutEx
  {
    public int whoGoes, howMany;

    public MutEx(int who, int how) {
        whoGoes = who;
        howMany = how;
    }

    public synchronized void switchTurns(){
        if(whoGoes == 0)
        {
          whoGoes++;
        }
        else if(whoGoes == 1)
        {
          whoGoes--;
        }
        notifyAll();
    }

    public synchronized void waitForTurn(int id) throws InterruptedException{
        while(whoGoes != id)
        {
            wait();
        }
    }
}
  static private class RunFred extends Thread implements Runnable {
    private static int MY_ID;
    private static MutEx MUT_EX;

    public RunFred(int id, MutEx MUT_EXTwo) {
        MY_ID = id;
        MUT_EX = MUT_EXTwo;
    }
    public void delayProcess(int value)
    {
      try{
        sleep(value);
      }
      catch(InterruptedException c)
      {
        c.printStackTrace();
      }
    }
    public void fredSee()
    {
      for(int x = 0; x < 10; ++x)
      {
        try
          {
            MUT_EX.waitForTurn(MY_ID);
            System.out.println("Fred is going up!!");
            while(fredHeight < 7.0)
            {
              System.out.println("Fred's current height: " + fredHeight);
              System.out.println("Wilma's current height: " + wilmaHeight);
              fredHeight++;
              wilmaHeight--;
            }
            System.out.println("Fred's current height: " + fredHeight);
            System.out.println("Wilma's current height: " + wilmaHeight);
            MUT_EX.switchTurns();
          }
        catch(InterruptedException ex)
        {
          ex.printStackTrace();
        }
      }
    }

      public void run() {
          fredSee();
          try{
            Thread.sleep(1000);
          }
          catch(InterruptedException c)
          {
            c.printStackTrace();
          }
      }

  }
  static private class RunWilma extends Thread implements Runnable {
    private static int MY_ID;
    private static MutEx MUT_EX;

    public RunWilma(int num, MutEx m) {
        MY_ID = num;
        MUT_EX = m;
    }
    public void delayProcessTwo(int value)
    {
      try{
        sleep(value);
      }
      catch(InterruptedException c)
      {
        c.printStackTrace();
      }
    }
    static private void wilmaSaw()
    {
      for(int y = 0; y < 10; ++y)
      {
        try
          {
            MUT_EX.waitForTurn(MY_ID);
            System.out.println("Wilma is going up!!");
            while(wilmaHeight < 7.0)
            {
              System.out.println("Fred's current height: " + fredHeight);
              System.out.println("Wilma's current height: " + wilmaHeight);
              fredHeight -= 1.5;
              wilmaHeight += 1.5;
            }
            System.out.println("Fred's current height: " + fredHeight);
            System.out.println("Wilma's current height: " + wilmaHeight);
            MUT_EX.switchTurns();
          }
          catch(InterruptedException ex)
          {
            ex.printStackTrace();
          }
        }
    }

      public void run() {
        try{
          Thread.sleep(1000);
        }
        catch(InterruptedException c)
        {
          c.printStackTrace();
        }
          wilmaSaw();
          try{
            Thread.sleep(1000);
          }
          catch(InterruptedException c)
          {
            c.printStackTrace();
          }
      }
  }
  public static void main(String args[])
  {
      MutEx myLock = new MutEx(0, 2);
      Thread t1 = new Thread(new RunFred(0, myLock));
      Thread t2 = new Thread(new RunWilma(1, myLock));
      ArrayList<Thread> threads = new ArrayList<>();
      threads.add(new Thread(t1));
      threads.add(new Thread(t2));
      for (Thread t : threads)
      {
          t.start();
      }
      try {
          for (Thread t : threads)
          {
              t.join();
          }
      } catch (InterruptedException e)
      {
          e.printStackTrace();
      }
  }
}
