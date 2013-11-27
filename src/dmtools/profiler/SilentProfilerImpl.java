/* 
 * Created by Rob Simitus
 */ 
package dmtools.profiler;

import java.io.PrintWriter;

/**
*  This implementation of the <code>Profiler</code> interface ignores
*  all method calls.  This is used when profiling is disabled in the
*  system and prevents branching code such as <code>if (isProfilingEnabled) { }</code>
*  blocks in the caller class or in the main <code>Profiler</code> implementations.
*  
*/
public class SilentProfilerImpl implements Profiler {
   static final String EMPTY_STRING = "";
    
    public SilentProfilerImpl() {
        System.out.println("new SilentProfilerImpl()");
    }
    
    /**
    *  This method does nothing.
    */
    public void startTask(String name) {
    }
    
    
    /**
    *  This method does nothing.
    */
    public void stopTask(String name) {
    }


    /**
    *  This method does nothing.
    */
    public void clearTasks() {
    }

    /**
    *  This method does nothing.
    */
    public void clearTasksForDisable() {
    }
    
    /**
    *  This method always returns an empty string.
    */
    public String toString() {
        return EMPTY_STRING; // don't just create one every time; wasteful!
    }
    
    /**
    *  This method does nothing.
    */
    public String getStatisticsCSV() {
        return EMPTY_STRING;
    }
    
}
