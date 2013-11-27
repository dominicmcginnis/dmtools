package dmtools.util;

import java.io.DataInputStream;
import java.io.BufferedReader;
import java.io.FilterInputStream;
import java.io.InputStreamReader;
import java.io.IOException;

public class ParserInputStream  extends FilterInputStream {
	private String substring;
	private BufferedReader reader;
	   
	public ParserInputStream(DataInputStream in, String substring) {
	   super(in);
	   this.substring = substring;
	   this.reader = new BufferedReader(new InputStreamReader(in));
	}
	    
	//	parse line will return all instances of the search string in the Input Stream
	public final String parseLine() throws IOException {
	   String line;
	   do {
	     line = reader.readLine();
	   } while ((line != null) && line.indexOf(substring) == -1);
	   		return line;
	}
	
}
