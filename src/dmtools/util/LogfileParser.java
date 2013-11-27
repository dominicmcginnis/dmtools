package dmtools.util;

import java.util.Vector;
import java.util.List;

import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class LogfileParser {

	public LogfileParser() {
		
	}

	//Returns a List of strings for each occurrance of the search string in the data input stream
	public List search(DataInputStream d, String searchString) {	
		List results = new Vector();
		try {
			ParserInputStream g = new ParserInputStream(d, searchString);
			            
		    String line = null;
		    for(;;) {
		       //parse line will return all instances of the search string in the Input Stream
		       line = g.parseLine();
		       if(line != null) {
		    	   results.add(line);
		       } else {
		          break;
		       }
		    }
		    g.close();
		 } catch (IOException e) {
		    System.err.println(e);
		 }
		 return results;
	}
		
	public static void main(String args[]) {
	   if ((args.length == 0) || (args.length > 2)) {
	      System.out.println("Usage: java LogfileParser <substring> [<filename>]");
	      System.exit(0);
	   }
	        
	   try {
	      DataInputStream d;
	      if (args.length == 2) {
	        d = new DataInputStream(new FileInputStream(args[1]));
	      } else {
	        d = new DataInputStream(System.in);
	      }
	      
	      LogfileParser lp = new LogfileParser();
	      List results = lp.search(d, args[0]);
	      if(results != null) {
	    	  for(int i =0; i < results.size(); i++) {
	    		  System.out.println((String)results.get(i));
	    	  }
	      }
	   
	   } catch (IOException e) {
		   System.err.println(e);
	   }
	}
	
}
