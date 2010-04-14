

package com.juksoft.bbvocab;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

import javax.microedition.io.SocketConnection;

public class Dictionary {
	
	public static final String DICT_MAIN_HOST = "dict.org";
	public static final int DICT_PORT = 2628;
	public static final int TIMEOUT = 15;
	public static final int READ_BUFFER = 1024;
	
	public static String[] getDefinition(String searchWord, String[] errorMessage) throws IOException {
		InputStream is = null;
		OutputStreamWriter os = null;
		SocketConnection socket = null;
		String[] definitions = null;
		
		try
		{
			ConnectionFactory cf = new ConnectionFactory();
			socket = cf.getSocketConnection(DICT_MAIN_HOST, DICT_PORT, TIMEOUT);
			is = socket.openInputStream();	//read banner and discard
			readFromStream(is);
			
			os = new OutputStreamWriter(socket.openOutputStream());
			String commandString = "DEFINE ! " + searchWord.trim() + "\r\n";
			os.write(commandString);		
			
			String statusResponse = readFromStream(is);
			/*
			550 Invalid database, use "SHOW DB" for list of databases
			552 No match
	        150 n definitions retrieved - definitions follow
		    151 word database name - text follows
		    250 ok (optional timing information here)
			*/
			int statusCode = Integer.parseInt(statusResponse.substring(0, 3));
			if (statusCode != 150) {
				errorMessage[0] = statusResponse.substring(5);
				return null;
			}
			
			int numDefinitions = Integer.parseInt(statusResponse.substring(4,5));
			definitions = new String[numDefinitions];
			
			String definitionsResponse = readFromStream(is);
			for(int i = 0; i < numDefinitions; i++) {
				//split on def delimiter, drop 250 OK status code text after defs
				String definition = split(definitionsResponse, "\r\n.\r\n")[i];
				//remove 151 status code text for each def
				definition = definition.substring(definition.indexOf("\r\n") + 2);  				
				definitions[i] = definition;	
			}
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
		return definitions;
	}

	public static String getBanner() throws IOException {
		InputStream is = null;
		SocketConnection socket = null;
		String banner = "";
		try
		{
			ConnectionFactory cf = new ConnectionFactory();
			socket = cf.getSocketConnection(DICT_MAIN_HOST, DICT_PORT, TIMEOUT);
			is = socket.openInputStream();	
			banner = readFromStream(is);
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
	
	private static String readFromStream(InputStream is) throws IOException {
		StringBuffer stringRead = new StringBuffer();
		
		int firstByte = is.read(); //block until first byte
		if (firstByte >= 0) {
			char firstChar = (char)firstByte;
			stringRead.append(firstChar);
			
			int numBytesInStream = is.available();
			int bytesRead = 0;
			while (bytesRead < numBytesInStream) {
				int bufferSize = READ_BUFFER;
				if (numBytesInStream - bytesRead < READ_BUFFER) {
					bufferSize = numBytesInStream - bytesRead;
				}
				byte[] buffer = new byte[bufferSize];
				
				bytesRead += is.read(buffer);
				stringRead.append(new String(buffer));
			}
		}
		System.out.println("in:" + stringRead.toString());
		return stringRead.toString();
	}	

	//Identifies the substrings in a given string that are delimited
	//by one or more characters specified in an array, and then
	//places the substrings into a String array.
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
