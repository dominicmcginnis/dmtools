/*
 * Created by Rob Simutis
 */
 
package dmtools.profiler;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
*  This interface defines the behavior of a profiler class.
*  A profiler can be used to track tasks as they occur in the application.
*  The tasks correspond to some unit of work, potentially divisible, and
*  are all user-defined.
*  <p>
*  The life-cycle of a profiler is generally as follows:
*  <ul>
*    <li>Obtain instance of profiler</li>
*    <li>Call <code>startTask(String)</code>, nesting further calls to 
*       <code>startTask(String)</code> to break up the unit of work.</li>
*    <li>At some point later, call a corresponding <code>stopTask(String)</code>
*        for the task name</li>
*    <li>Call <code>toString()</code> to return a string representation of the
*        task hierarchy</li>
*    <li>Call <code>clearTasks()</code> when all profiling for that block
*        of code is complete and the string version has been obtained</li>
*  </ul>
*  <p>
*  Developers should never instantiate this class directly; it is used internally
*  by the <code>dmtools.profiler.Tracer</code> class.
*  
*/
public class ProfilerImpl implements Profiler {

    List tasks = new ArrayList();
    Task currentTask = null;
    Map taskStats = new HashMap();
    Object lock = new Object();
    
    /**
    *  If this variable is ever set to false, all further starts/stops
    *  will be ignored until clear() is called.  This flag gets set
    *  if a stop() ever doesn't match a start() so that it doesn't
    *  throw an exception on every call.  clear() resets the flag
    */
    boolean isHealthy = true;
    
    /**
    *  Begin a unit of work defined by the parameter <code>name</code>.
    *  Units of work may be nested by repeatedly calling <code>startTask</code>.
    */
    public void startTask(String name) {
        if (isHealthy) {

            Task task = new Task(name, currentTask);
            if (currentTask == null) {
                tasks.add(task);
            } else {
                currentTask.add(task);
            }
            task.start();
            currentTask = task;
        }
    }
    
    
    /**
    *  End a unit of work defined by the parameter <code>name</code>.
    *  For each call to <code>startTask(String)</code>, a corresponding
    *  <code>stopTask(String)</code> must be called with the identical
    *  <code>String</code> parameter.
    * 
    *  @exception IllegalStateException : throws this exception when the name doesn't match
    *  the currently-stored task name.
    */
    public void stopTask(String name) {
        if (isHealthy) {
            if (currentTask != null) {

                if (!currentTask.getName().equals(name)) {
                    isHealthy = false;
                    throw new IllegalStateException("Calling stop on task called " + name + " when current task is called " + currentTask.getName());
                }
                currentTask.stop();
     
                currentTask = currentTask.getParent();
            } else {
                throw new IllegalStateException("Calling stop on " + name + " when current task is null");
            }
        }
    }

    /**
    *  Clear all tasks created so far.  This should be called
    *  once the <code>toString()</code> method has been called
    *  to reset the state of the <code>Profiler</code>.
    */
    public void clearTasks() {
        if (tasks != null) {
            tasks.clear();
        }
        
        currentTask = null;
        isHealthy = true;
    }

    public void clearTasksForDisable() {
        // Don't need to do anything further here; if we're in a Thread-aware 
        // profiler, then all profilers are cleared
        clearTasks();
    }
    
    public String toString() {
        try {
            long totalTime = 0L;
            for (int i = 0, size = tasks.size(); i < size; i++) { // only add up the outermost tasks.
                totalTime += ((Task)tasks.get(i)).getTime();
            }
            StringBuffer sb = new StringBuffer();
            for (int i = 0, size = tasks.size(); i < size; i++) {
                sb.append(((Task)tasks.get(i)).toString(totalTime, 0));
            }

            return sb.toString();
        } catch (Exception e) {
            System.out.println("com.finaplex.fwm.profiler.Tracer: toString(), Exception: " + e); // temporary placeholder until I put logger in
            return "";
        }
    }
    
    public String getStatisticsCSV() {
        StringBuffer sb = new StringBuffer();
        sb.append("\"Task\", \"Number of Times Called\", \"Total Time (ms)\"\n");
        
        long totalTime = 0L;
        for (int i = 0, size = tasks.size(); i < size; i++) { // only add up the outermost tasks. 
            totalTime += ((Task)tasks.get(i)).getTime();
        }
             
        Map m = new HashMap();
        // Use this list to maintain a semi-coherent ordering of task names in
        // what will ultimately land as a CSV
        List orderedHierarchy = new ArrayList();
        for (int i = 0, size = tasks.size(); i < size; i++) {
            Task t = (Task)tasks.get(i);
            processTaskForStats(m, orderedHierarchy, t, totalTime);
        }

        for (Iterator iterator = orderedHierarchy.iterator(); iterator.hasNext(); ) {
            Task taskName = (Task)iterator.next();
            sb.append(taskName.getName())
                .append(",")
                .append(((TaskStat)m.get(taskName.getName())).numberOfTimesCalled)
                .append(",")
                .append(String.valueOf(taskName.getTime()))
                .append('\n');
        }
        
        return sb.toString();
    }
    
    private void processTaskForStats(Map m, List orderedHierarchy, Task t, long totalTime) {
       String taskName = t.getName();
       TaskStat stat = (TaskStat)m.get(taskName);
       if (stat == null) {
           stat = new TaskStat();
           m.put(taskName, stat);
           stat.name = taskName;
           orderedHierarchy.add(t);
       }
       stat.numberOfTimesCalled++;
       stat.time += t.getTime();
       
       double percentage = ((double)stat.time/(double)totalTime)*100D;
       String percentageString = String.valueOf(percentage);
       int index = percentageString.indexOf(".")+3;
       if (index < percentageString.length()) {
           percentageString = percentageString.substring(0, index);
       }
       stat.percentageOfTotalTimeString = percentageString;
       
       
        List children = t.getChildren();
        if (children != null) {
            for (int i = 0, size = children.size(); i < size; i++) {
                processTaskForStats(m, orderedHierarchy, (Task)children.get(i), totalTime);
            }
        }
    }
}


class TaskStat {
    public int time = 0;
    public int numberOfTimesCalled = 0;
    public String name = null;
    public String percentageOfTotalTimeString = null; // this probably won't stay here...this is only temporary
    public double percentageOfTotalTime = 0D;
    
    public String toCSV() {
        StringBuffer sb = new StringBuffer();
        sb.append("\"")
            .append(name)
            .append("\", ")
            .append(numberOfTimesCalled)
            .append(", ")
            .append(time)
            .append(", ")
            .append(percentageOfTotalTimeString)
            .append('%');
        return sb.toString();
    }
}
