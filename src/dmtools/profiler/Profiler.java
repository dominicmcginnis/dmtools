/**
 * Credit for this class goes to Rob Simutis
 */
package dmtools.profiler;

import java.io.PrintWriter;

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
*  Developers should never instantiate an implementation of this class directly; 
*  it is used internally by the <code>dmtools.profiler.Tracer</code> class.
*
*  @see dmtools.profiler.Tracer
*  
*/
public interface Profiler {
    
    
    /**
    *  Begin a unit of work defined by the parameter <code>name</code>.
    *  Units of work may be nested by repeatedly calling <code>startTask</code>.
    */
    public void startTask(String name);
    
    
    /**
    *  End a unit of work defined by the parameter <code>name</code>.
    *  For each call to <code>startTask(String)</code>, a corresponding
    *  <code>stopTask(String)</code> must be called with the identical
    *  <code>String</code> parameter.
    */
    public void stopTask(String name);


    /**
    *  Clear all tasks created so far.  This should be called
    *  after the <code>toString()</code> method has been called
    *  in order to reset the state of the <code>Profiler</code>.
    *  The tree of tasks should be closed off prior to calling
    *  clearTasks(); otherwise eventually more stopTask() calls
    *  will be made than startTask() calls since the previous clear.
    */
    public void clearTasks();
    
    
    /**
    *  Clear all tasks created so far.  This should be called
    *  after the <code>toString()</code> method has been called
    *  in order to reset the state of the <code>Profiler</code>.
    */
    public String toString();
    
    
    /**
    *  A variant of clearTasks() which indicates to the Profiler interface that
    *  it should be prepared to be disabled.
    *
    *  @see clearTasks()
    */
    public void clearTasksForDisable();
    
    /**
    *  Calculate and report statistics from the tasks in a CSV (comma-separated values)
    *  string.The exported report is implementation-dependent but generally includes
    *  information such as a comprehensive list of 
    *  
    *  @return String : a CSV report of the tasks.  
    */
    public String getStatisticsCSV();
    
}
