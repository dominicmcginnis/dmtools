/*
 * Created on Jun 13, 2005
 *
 * Writen by Robert Dominic McGinnis.
 * 
 */
package dmtools.dataaccess.exception;

/**
 * @author Dominic
 *
 * This is an Exception wrapper to let the user
 * know that they are missing key connection information
 * and that without it a database connection cannot
 * be created.  
 */
public class MissingConnectionInfoException extends java.lang.SecurityException {

    /**
     * Constructs a MissingConnectionInfoException with no detail message.
     *
     */
	public MissingConnectionInfoException() {
		super();
	}

    /**
     * Constructs a MissingConnectionInfoException with a detail message.
     *
     * @param details the detail message.
     */
	public MissingConnectionInfoException(String details) {
		super("Missing connection info exception occurred: " + details);
	}
}

