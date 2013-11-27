/*
 * Credit to Rob Simitus
 */
package dmtools.profiler;

import java.beans.PropertyChangeListener;
import java.io.PrintWriter;


/**
*  This class is the primary public interface for profiling code.  Profiling is 
*  accomplished by a programmer importing this class into one or more of his/her
*  class files, and making calls to the static methods in this class as "tasks"
*  are started and stopped.  A "task" may include any measured unit of work that 
*  may be broken into sub-units of other "tasks", such as methods, methods that 
*  call other methods, or a chunk of work performed inside of a method (such as 
*  extracting data from a ResultSet object and populating objects with the data).
*  <code>Tracer</code> delegates all calls to an implementation of the 
*  <code>Profiler</code> class.
*  <p>
*  Typical use of this class is as follows:
*  <p>
*  <pre>
*  import dmtools.profiler.Tracer;
*  ...
*  Tracer.start("Task");
*  // ... do some work
*  Tracer.start("Nested Task");
*  // do some more work that's a subset of Task
*  Tracer.stop("NestedTask");
*  Tracer.stop("Task");
*  
*  System.out.println(Tracer.getInstance()); // Note the call to getInstance()
*  </pre>
*  <p>
*  Calls to tracer may be nested as well as laddered; a laddered Tracer call
*  simply allows for multiple root tasks such as:
*  <p>
*  <pre>
*  Tracer.start("Task 1");
*  Tracer.stop("Task 1");
*  Tracer.start("Task 2");
*  Tracer.stop("Task 2");
*  
*  System.out.println(Tracer.getInstance()); // Note the call to getInstance()
*  </pre>
*  <p>
*  The methods <code>start(String)</code>, <code>stop(String)</code>, <code>toString()</code>, and <code>printStats(PrintWriter)</code>,
*  all delegate to corresponding methods in a <code>Profiler</code> interface.  Please see that interface definition for more information.
*  <p>
*  Note: This class is maintained as the front interface (even though it sits
*  in the <code>dmtools.profiler</code> package) due to historical reasons
*  and legacy code. 
*
*  @see dmtools.profiler.Profiler
*
*/
public class Tracer {
    static Tracer tracer = null; // singleton
    
    /**
    *  Reference to a class that implements the {@link dmtools.profiler.Profiler}
    *  interface.  All calls to the Tracer are delegated to a Profiler implementation.
    */
    Profiler profiler = null;
    
    /**
    *  Mutex object used for synchronization.
    */
    Object lock = new Object();
    
    static {
        getInstance();
    }
    
    private Tracer() {
        profiler = ProfilerFactory.getInstance().createProfiler();
        
    }
    
    /**
    *  Get an instance of the Tracer singleton.  Although
    *  this method is public, it is not required to be called
    *  by any client classes <b>EXCEPT</b> when the results
    *  of the <code>Tracer</code> are being written out as a
    *  <code>String</code>.  The primary methods in this class
    *  are static and will automatically create an instance
    *  of <code>Tracer</code> as necessary.
    */
    public static Tracer getInstance() {
        if (tracer == null) {
            synchronized(Tracer.class) { // maybe sync on the lock object now that it's available?
                if (tracer == null) {   
                    tracer = new Tracer();
                }
            }
        }
        return tracer;
    }
    
    public static void clear() {
        try {
            tracer.clearTasks();
        } catch (Exception e) {
            System.out.println("dmtools.profiler.Tracer: clear(), Exception: " + e); 
        }
    }
    
    public void clearTasks() {
        if (profiler != null) {
            profiler.clearTasks();
        }
    }

    public static void start(String name) {
        try {
            if (tracer == null) {
                tracer = getInstance();
            }
            
            tracer.startTask(name);    
        } catch (Exception e) {
            System.out.println("dmtools.profiler.Tracer: start(" + name + "), Exception: " + e);
        }       
    }
    
    public void startTask(String name) {
        if (profiler != null) {
            profiler.startTask(name);
        }
    }
    
    /**
    *  Stop the current task.  If the name passed in doesn't match the stored current task
    *  in the profiler, an IllegalStateException is trapped and the stack trace is dumped.
    */
    public static void stop(String name) {
        if (tracer != null) {
            try {
                tracer.stopTask(name);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (Exception e) {
                System.out.println("dmtools.profiler.Tracer: stop(" + name + "), Exception: " + e); 
            }
        } 
    }
    
    public void stopTask(String name) {
        if (profiler != null) {
            profiler.stopTask(name);
        }
    }

    
    public String toString() {
        try {
            return profiler.toString();
        } catch (Exception e) {
            System.out.println("dmtools.profiler.Tracer: toString(), Exception: " + e); 
            return "";
        }
    }
    
    public static String getStatisticsCSV() {
        if (tracer != null) {
            return tracer.getTaskStatisticsCSV();
        }
        return null;
    }
    
    protected String getTaskStatisticsCSV() {
        if (profiler != null) {
            return profiler.getStatisticsCSV();
        }
        return null;
    }
    
    public static void main(String[] args) throws Exception {
        Tracer.start("main");
            Tracer.start("replicate");
                Tracer.start("replicateBroker");
                    Tracer.start("Replicate Accounts for Broker");
                        Thread.sleep(1050);
                        Tracer.start("getBrokerSalesIDs");
                        Tracer.stop("getBrokerSalesIDs");
                        Thread.sleep(50);
                        Tracer.start("getBrokerAccounts");
                        Tracer.stop("getBrokerAccounts");
                        Thread.sleep(20);
                    Tracer.stop("Replicate Accounts for Broker");
                    Tracer.start("Create Relationships to Broker");
                        Thread.sleep(2304);
                        Tracer.start("getBrokerClients for SalesID " );
                        Tracer.stop("getBrokerClients for SalesID " );
                   Tracer.stop("Create Relationships to Broker");
                Tracer.stop("replicateBroker");
    
            Tracer.stop("replicate");
        Tracer.stop("main");
       
        System.out.println(Tracer.getInstance());
        System.out.println(Tracer.getStatisticsCSV());
    }
}
