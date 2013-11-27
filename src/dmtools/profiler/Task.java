/*
 * Credit to Rob Simutis
 */
package dmtools.profiler;

import java.util.List;
import java.util.ArrayList;


/**
*  This class represents a node in a tree of tasks.  A <code>Task</code>
*  is any named operation representing a set of actions within a 
*  noted time period.  Instances of this class are generally kept in a tree
*  hierarchy inside of a <code>Profiler</code>; most implementations will keep
*  a <code>List</code> of <code>Tasks</code>, since there may not necessarily
*  be a single root <code>Task</code>.
*
*/
class Task {
    List children = null;
    
    long startTimeMillis = 0L;
    long stopTimeMillis = 0L;
    String name = null;
    Task parent = null;
    
    public Task(String name, Task parent) {
        this.name = name;
        this.parent = parent;
    }
    
    public Task getParent() {
        return parent;
    }
    
    public String getName() {
        return name;
    }
    
    public void start() {
        startTimeMillis = System.currentTimeMillis();
    }
    
    public List getChildren() {
        return children;
    }
    
    public void stop() {
        stopTimeMillis = System.currentTimeMillis();

    }
    
    public long getTime() {
        return (stopTimeMillis - startTimeMillis);
    }
    
    public long getStartTime() {
        return startTimeMillis;
    }
    
    public long getStopTime() {
        return stopTimeMillis;
    }
    
    public void add(Task child) {
        if (children == null) {
            children = new ArrayList();
        }
        children.add(child);
    }
        
    public String toString(long totalTime, int indent) {
        StringBuffer sb = new StringBuffer();
        
        indent(sb, indent);
        
        boolean isError = (stopTimeMillis == 0L);
        if (isError) {
            stop();
        }
        
        long time = getTime();
        
        String percentage = String.valueOf(((double)time/(double)totalTime)*100D);
        int index = percentage.indexOf(".")+3;
        if (index < percentage.length()) {
            percentage = percentage.substring(0, index);
        }
        
        sb.append(name)
          .append(" : ")
          .append(getTime())
          .append(" ms (")
          .append(percentage)
          .append("%)");
 
        if ((children != null) && (children.size() > 0)) {
            long childrenTime = 0L;
            sb.append(" {").append('\n');
            
            Task lastTask = null;
            long lastEndTime = -1;
            for (int i = 0; i < children.size(); i++) {
                Task child = (Task)children.get(i);
                if (i == 0) {
                    long timea = child.startTimeMillis - startTimeMillis; 
                    if (timea > 0) {
                
                        indent(sb, indent+1);

                        sb.append("Unallocated time : ")
                          .append(timea)
                          .append(" ms\n");
                    }
                } else {
                    if (child.startTimeMillis - lastEndTime > 0) {
                        indent(sb, indent+1);

                        sb.append("Unallocated time : ")
                          .append(child.startTimeMillis - lastEndTime)
                          .append(" ms\n");
                    }
                }
                //childrenTime += child.getTime();
                
                sb.append(child.toString(totalTime, indent+1))
                  .append((!isError) ? "" : " *** ERROR *** ")
                  .append('\n');
                  
                lastTask = child;
                lastEndTime = lastTask.stopTimeMillis;                    
                
                if (i == children.size()-1) {
                    long timea = stopTimeMillis - child.stopTimeMillis;
                    if (timea > 0) {
                        indent(sb, indent+1);

                        sb.append("Unallocated time : ")
                          .append(timea)
                          .append(" ms\n");
                    }
                } 
                lastEndTime = child.getStopTime();
            }
            
        
            indent(sb, indent);
            sb.append('}');
        }
        return sb.toString();
    }

    private StringBuffer indent(StringBuffer sb, int numberOfIndents) {
	for (int i = 0; i < numberOfIndents; i++) {
            sb.append(' ');
        }
	return sb;
    }
}
