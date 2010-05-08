

package com.juksoft.bbvocab;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

import javax.microedition.io.SocketConnection;

public class Dictionary {
	
	private static final String SYNTAX_ERROR_TOKEN = "500 Syntax error, command not recognized";
	private static final String BAD_PARAM_TOKEN = "501 Syntax error, illegal parameters";
	private static final String BAD_CMD_TOKEN = "502 Command not implemented";
	private static final String NOT_IMPL_TOKEN = "503 Command parameter not implemented";
	private static final String SERVER_DOWN_TOKEN = "420 Server temporarily unavailable";
	private static final String SERVER_SHUTTING_DOWN_TOKEN = "421 Server shutting down at operator request";
	private static final String ACCESS_DENIED_TOKEN = "530 Access denied";
	
	private static final String[] GENERAL_ERROR_RESPONSES = {
			SYNTAX_ERROR_TOKEN, 
			BAD_PARAM_TOKEN, 
			BAD_CMD_TOKEN, 
			NOT_IMPL_TOKEN, 
			SERVER_DOWN_TOKEN, 
			SERVER_SHUTTING_DOWN_TOKEN,
			ACCESS_DENIED_TOKEN
	};
	
	private static final String INVALID_DB_TOKEN = "550 Invalid database, use \"SHOW DB\" for list of databases";
	private static final String NO_MATCH_TOKEN = "552 No match";
	private static final String DEF_END_TOKEN = "250 ok";
	
	private static final String[] DEFINE_RESPONSES = { 
		INVALID_DB_TOKEN,
		NO_MATCH_TOKEN,
		DEF_END_TOKEN
	};
	
	private static final String DEF_DELIMITER = "\r\n.\r\n";
	private static final String STATUS_END_TOKEN = "\r\n";
	private static final String DICT_MAIN_HOST = "dict.org";
	private static final int DICT_PORT = 2628;
	private static final int TIMEOUT = 15;
	private static final String WORDNET_DB = "wn";

	public static Object[] getDefinitions(String searchWord) throws Exception {
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
			//String banner = 
			readFromStream(is, false); //read banner and discard
			//System.out.println(banner);
			
			os = new OutputStreamWriter(socket.openOutputStream());
			String commandString = "DEFINE " + WORDNET_DB + " " + searchWord.trim().toLowerCase() + "\r\n";
			os.write(commandString);		
			
			String definitionsResponse = readFromStream(is, true);
			int statusCode = Integer.parseInt(definitionsResponse.substring(0, 3));
			if (statusCode != 150) {				
				returnBundle[0] = null;
				returnBundle[1] = definitionsResponse.substring(4).toUpperCase();
				return returnBundle;
			}
			
			String[] splitResponse = split(definitionsResponse, DEF_DELIMITER);
			definitions = new String[splitResponse.length - 1]; //chop off 250 ok status msg
			for(int i = 0; i < definitions.length; i++) {
				definitions[i] = formatDefinition(splitResponse[i]);
			}
			returnBundle[0] = definitions;
			returnBundle[1] = null;
			
			commandString = "QUIT";
			os.write(commandString);			
		}
		catch(Exception ex)
		{	
			System.out.println(ex.getMessage());
			throw new Exception(ex.getMessage());
		}
		finally
		{
			if (is != null)
				is.close();
			if (os != null)				
				os.close();
			if (socket != null)
				socket.close();			
		}
		return returnBundle;		
	}

	private static String formatDefinition(String definition) {
		//strip def 151 header
		String defHeader = "151 ";
		String defHeaderEndToken = "\r\n";
		String defLessHeader = definition.substring(definition.indexOf(defHeaderEndToken, definition.indexOf(defHeader)) + 2);
		return defLessHeader;
	}	
	
	private static String readFromStream(InputStream is, boolean isDefinition) throws IOException {
		StringBuffer stringRead = new StringBuffer();
		boolean isComplete = false;
		String firstBlock = readBytesAvailable(is);
		stringRead.append(firstBlock);	
		
		//a 'response' terminator from the DICT protocol would end this shit below
		while(!isComplete)  {				
				if (isDefinition) {
					for (int i = 0; i < GENERAL_ERROR_RESPONSES.length; i++) {
						if (stringRead.toString().toLowerCase().indexOf(GENERAL_ERROR_RESPONSES[i].toLowerCase()) != -1) {
							isComplete = true;
						}
					}
					for (int i = 0; i < DEFINE_RESPONSES.length; i++) {
						if (stringRead.toString().toLowerCase().indexOf(DEFINE_RESPONSES[i].toLowerCase()) != -1) {
							isComplete = true;
						}
					}
				}
				else {
					if (stringRead.toString().indexOf(STATUS_END_TOKEN) != -1) {
						isComplete = true;
					}
				}
			
			if (!isComplete) {
				String block = readBytesAvailable(is);
				stringRead.append(block);	
			}
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
