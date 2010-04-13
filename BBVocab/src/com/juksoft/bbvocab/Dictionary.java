

package com.juksoft.bbvocab;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

import javax.microedition.io.SocketConnection;

public class Dictionary {
	
	public static final String DICT_MAIN_HOST = "dict.org";
	public static final int DICT_PORT = 2628;
	public static final int TIMEOUT = 15;
	
	public Dictionary() throws IOException 
	{		
		readFromDictServer();
	}

	private void readFromDictServer() throws IOException {
		InputStream is = null;
		OutputStreamWriter os = null;
		SocketConnection socket = null;
		StringBuffer banner = new StringBuffer();
		try
		{
			ConnectionFactory cf = new ConnectionFactory();
			socket = cf.getSocketConnection(DICT_MAIN_HOST, DICT_PORT, TIMEOUT);
			is = socket.openInputStream();	
			int firstByte = is.read(); //bad connection will timeout here 
			if (firstByte >= 0) {
				char firstChar = (char)firstByte;
				int numBytesInStream = is.available();
				banner.append(firstChar);
				for (int i = 0; i < numBytesInStream; i++) {
					banner.append((char)is.read());
				}
			}
			System.out.println(banner.toString());
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
	}

}
