

package com.juksoft.bbvocab;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

import javax.microedition.io.SocketConnection;

public class Dictionary {
	
	private static final String DEF_DELIMITER = "\r\n.\r\n";
	public static final String DICT_MAIN_HOST = "dict.org";
	public static final int DICT_PORT = 2628;
	public static final int TIMEOUT = 15;
	public static final int READ_BUFFER = 1024;
	public static final String DEF_END_TOKEN = "250 ok";
	public static final String WORDNET_DB = "wn";

	public static Object[] getDefinitions(String searchWord) throws IOException {
		InputStream is = null;
		OutputStreamWriter os = null;
		SocketConnection socket = null;
		Object[] returnBundle = new Object[2]; //0 is the string[] for defs, 1 is for error message
		String[] definitions = null;
		
		try
		{
			ConnectionFactory cf = new ConnectionFactory();
			socket = cf.getSocketConnection(DICT_MAIN_HOST, DICT_PORT, TIMEOUT);
			is = socket.openInputStream();	
			readFromStream(is, false); //read banner and discard
			
			os = new OutputStreamWriter(socket.openOutputStream());
			String commandString = "DEFINE " + WORDNET_DB + " " + searchWord.trim().toLowerCase() + "\r\n";
			os.write(commandString);		
			
			String definitionsResponse = readFromStream(is, true);
			/*
			550 Invalid database, use "SHOW DB" for list of databases
			552 No match
	        150 n definitions retrieved - definitions (151) follow, may be in same chunk or next chunk
		    151 word database name - text follows //start of definition
		    250 ok (optional timing information here) //finished sending definitions
			*/
			int statusCode = Integer.parseInt(definitionsResponse.substring(0, 3));
			if (statusCode != 150) {
				
				returnBundle[0] = null;
				returnBundle[1] = definitionsResponse.substring(4, definitionsResponse.indexOf('[')).toUpperCase();
				return returnBundle;
			}
			
			//int numDefinitions = Integer.parseInt(definitionsResponse.substring(4,5));
			definitions = split(definitionsResponse, DEF_DELIMITER);
			for(int i = 0; i < definitions.length; i++) {
				//remove 151 status code text for each definition
				definitions[i] = definitions[i].substring
					(definitions[i].indexOf("\r\n", definitions[i].indexOf("151 " + "\"" + searchWord.toLowerCase() + "\"") ) + 2);
			}
			returnBundle[0] = definitions;
			returnBundle[1] = null;
			
			commandString = "QUIT";
			os.write(commandString);			
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		finally
		{
			is.close();
			os.close();
			socket.close();
		}
		return returnBundle;
	}

	/*public static String getBanner() throws IOException {
		InputStream is = null;
		SocketConnection socket = null;
		String banner = "";
		try
		{
			ConnectionFactory cf = new ConnectionFactory();
			socket = cf.getSocketConnection(DICT_MAIN_HOST, DICT_PORT, TIMEOUT);
			is = socket.openInputStream();	
			banner = readFromStream(is, false);
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		finally
		{
			is.close();
			socket.close();
		}
		return banner;
	}
	*/
	
	private static String readFromStream(InputStream is, boolean isDefinition) throws IOException {
		StringBuffer stringRead = new StringBuffer();
		String firstBlock = readBytesAvailable(is);
		stringRead.append(firstBlock);
		
		while((isDefinition) && 
				(stringRead.toString().toLowerCase().indexOf(DEF_DELIMITER + DEF_END_TOKEN) == -1))  {
			String block = readBytesAvailable(is);
			stringRead.append(block);		
		}
		return stringRead.toString();
	}

	private static String readBytesAvailable(InputStream is) throws IOException {
		
		int bytesRead = 0;
		int numBytesAvailable = 0;
		StringBuffer stringBlock  = new StringBuffer();
		
		int firstByte = is.read(); //block until first byte
		if (firstByte >= 0) {
			char firstChar = (char)firstByte;
			stringBlock.append(firstChar);
			
			numBytesAvailable = is.available();
			while(numBytesAvailable  > 0) {
			    byte[] buffer = new byte[numBytesAvailable];					
				bytesRead += is.read(buffer);
				stringBlock.append(new String(buffer));
				numBytesAvailable = is.available();
			}
		}
		/*System.out.println("in:" + stringBlock.toString());
		System.out.println("in numBytesAvailable:" + numBytesAvailable);
		System.out.println("in bytesRead:" + bytesRead);
		System.out.println("in isAvailable:" + is.available());*/
		return stringBlock.toString();		
	}	

	public static String[] split(String strString, String strDelimiter) {
	    String[] strArray;
	    int iOccurrences = 0;
	    int iIndexOfInnerString = 0;
	    int iIndexOfDelimiter = 0;
	    int iCounter = 0;

	    //Check for null input strings.
	    if (strString == null) {
	        throw new IllegalArgumentException("Input string cannot be null.");
	    }
	    //Check for null or empty delimiter strings.
	    if (strDelimiter.length() <= 0 || strDelimiter == null) {
	        throw new IllegalArgumentException("Delimeter cannot be null or empty.");
	    }

	    //strString must be in this format: (without {} )
	    //"{str[0]}{delimiter}str[1]}{delimiter} ... 
	    // {str[n-1]}{delimiter}{str[n]}{delimiter}"

	    //If strString begins with delimiter then remove it in order
	    //to comply with the desired format.

	    if (strString.startsWith(strDelimiter)) {
	        strString = strString.substring(strDelimiter.length());
	    }

	    //If strString does not end with the delimiter then add it
	    //to the string in order to comply with the desired format.
	    if (!strString.endsWith(strDelimiter)) {
	        strString += strDelimiter;
	    }

	    //Count occurrences of the delimiter in the string.
	    //Occurrences should be the same amount of inner strings.
	    while((iIndexOfDelimiter = strString.indexOf(strDelimiter,
	           iIndexOfInnerString)) != -1) {
	        iOccurrences += 1;
	        iIndexOfInnerString = iIndexOfDelimiter +
	            strDelimiter.length();
	    }

	    //Declare the array with the correct size.
	    strArray = new String[iOccurrences];

	    //Reset the indices.
	    iIndexOfInnerString = 0;
	    iIndexOfDelimiter = 0;

	    //Walk across the string again and this time add the 
	    //strings to the array.
	    while((iIndexOfDelimiter = strString.indexOf(strDelimiter,
	           iIndexOfInnerString)) != -1) {

	        //Add string to array.
	        strArray[iCounter] = strString.substring(iIndexOfInnerString,iIndexOfDelimiter);

	        //Increment the index to the next character after 
	        //the next delimiter.
	        iIndexOfInnerString = iIndexOfDelimiter +
	            strDelimiter.length();

	        //Inc the counter.
	        iCounter += 1;
	    }

	    return strArray;
	}
}
