/*
 * Credited to Rob Simutis
 */ 
package dmtools.profiler;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

public class ThreadAwareProfilerProxy implements Profiler {

    Map threadsToProfilersMap = Collections.synchronizedMap(new HashMap());
    
    public ThreadAwareProfilerProxy() {
        System.out.println("dmtools.profiler.ThreadAwareProfilerProxy Tracer:  Profiling is active.");
    }
    
    public void startTask(String name) {

        String threadName = Thread.currentThread().getName();
        Profiler profiler = (Profiler)threadsToProfilersMap.get(threadName);
        if (profiler == null) {
            profiler = new ProfilerImpl();
            threadsToProfilersMap.put(threadName, profiler);
        }
        profiler.startTask(name);
    }
    
    
    /**
    *  @exception IllegalStateException - thrown if stopTask is called when
    *     there is no current task being performed.
    */
    public void stopTask(String name) {
        String threadName = Thread.currentThread().getName();
        Profiler profiler = (Profiler)threadsToProfilersMap.get(threadName);
        if (profiler == null) {
            throw new IllegalStateException("Called stopTask when no current task available for task named: " + name);
        }
        profiler.stopTask(name);
    }


    /**
    *  Clear all tasks created so far.  This should be called
    *  once the <code>toString()</code> method has been called
    *  to reset the state of the <code>Profiler</code>.
    */
    public void clearTasks() {
        String threadName = Thread.currentThread().getName();
        Profiler profiler = (Profiler)threadsToProfilersMap.get(threadName);
        if (profiler != null) {
            profiler.clearTasks();
        }
    }


    public void clearTasksForDisable() {
        // Does a reasonable-attempt at clearing out the data already traced.
        // This is not guaranteed (in the initial implementation) to keep the list of Tasks cleared
        // or prevent new ProfilerImpls from being created.  My goal is to avoid
        // putting in a ton of if statements everywhere, but this comes at a cost
        // of inability to guarantee clean-up...
        // Future versions may be more accurate; all this method does is clear everything out.
        for (Iterator iterator = threadsToProfilersMap.values().iterator(); iterator.hasNext(); ) {
            Profiler profiler = (Profiler)iterator.next();
            if (profiler != null) {
                profiler.clearTasksForDisable();
            }
        }
        
        threadsToProfilersMap.clear();
    }
    
    /**
    *  Return a String output of the <code>Profiler</code> for the current 
    *  <code>Thread</code>.
    */
    public String toString() {
        String threadName = Thread.currentThread().getName();
        Profiler profiler = (Profiler)threadsToProfilersMap.get(threadName);

        StringBuffer sb = new StringBuffer();

        sb.append("Profiler Output:\n");
        sb.append("Profile for thread \"")
          .append(threadName)
          .append("\"\n");

        if (profiler != null) {
            sb.append(profiler)
              .append("\n\n");
        }

        return sb.toString();
    }
    
    public String getStatisticsCSV() {
        String threadName = Thread.currentThread().getName();
        Profiler profiler = (Profiler)threadsToProfilersMap.get(threadName);
        StringBuffer sb = new StringBuffer();
        sb.append("Profiler stats for thread \"")
            .append(threadName)
            .append("\"\n");
        
        if (profiler != null) {
            sb.append(profiler.getStatisticsCSV());
        } else {
            sb.append("{No active profiler for this thread}");
        }
        
        return sb.toString();
    }

    
}
