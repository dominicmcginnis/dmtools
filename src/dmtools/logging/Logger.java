/*
 * Created on Jun 13, 2005
 *
 * Writen by Robert Dominic McGinnis.
 * 
 */
package dmtools.logging;

import java.sql.Timestamp;

/**
 * @author Dominic
 *
 * This class is a standard System.out.println wrapper that contains functions
 * for typical logging needs, but that will give the detailed information: 
 * ClassName doing the logging, Timestamp logged, type of log (INFO, WARN, ERROR)
 *
 */
public class Logger {
	private String className = null;
	public Logger(String className) {
		this.className = className;
	}
	
    /**
     * @author Dominic
     * 
     * Creates a log message of type INFO
     *
     * @param s the log output message.
     */
	public void log(String s) {
		Timestamp t = new Timestamp(System.currentTimeMillis());
		System.out.println(className + ":[" + t + "] INFO:" + s);
	}
	
    /**
     * @author Dominic
     * 
     * Creates a log message of type DEBUG
     *
     * @param s the log output message.
     */
	public void debug(String s) {
		Timestamp t = new Timestamp(System.currentTimeMillis());
		System.out.println(className + ":[" + t + "] DEBUG:" + s);
	}

	/**
	 * @author Dominic
	 * 
     * Creates a log message of type WARN
     *
     * @param s the log output message.
     */	
	public void warn(String s) {
		Timestamp t = new Timestamp(System.currentTimeMillis());
		System.out.println(className + ":[" + t + "] WARN:" + s);		
	}
	
    /**
     * @author Dominic
     * 
     * Creates a log message of type ERROR
     *
     * @param s the log output message.
     */	
	public void error(String s) {
		Timestamp t = new Timestamp(System.currentTimeMillis());
		System.out.println(className + ":[" + t + "] ERROR:" + s);
	}
}
