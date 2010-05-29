package com.juksoft.bbvocab;

import java.util.Vector;

import net.rim.device.api.system.Application;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.component.RichTextField;

public class GetDefinitionThread extends Thread {

	DictionaryScreen screen;
	public GetDefinitionThread(DictionaryScreen screen) {
		this.screen = screen;
	}

	public void run() {
		Object[] returnBundle = null;			
		try {
			returnBundle = Dictionary.getDefinitions(screen.getWordText());
			String[] definitions = (String[])returnBundle[0];
			String errorMessage = (String)returnBundle[1];
			
			if (definitions == null || definitions.length == 0) {
				if (errorMessage != null) {
				    updateDefinitionText(errorMessage);				    
				}
				else  {
					updateDefinitionText("Request returned with no definitions or error messages.");
				}
			}
			else {
				updateDefinitionText(definitions[0]);
			}			
		} catch (Exception e) {
			String msg = e.getMessage();
			System.out.println(msg);
			updateDefinitionText(msg);
		}				 	
	}
	
	protected void updateDefinitionText(final String definition) {
		Application.getApplication().invokeLater(
				new Runnable() 
				{ 
					public void run() 
					{ 
						String definitionText = definition;
						
						//TODO: here is where we format output from DICT for screen
						definitionText = Util.removeString(definitionText, "[");
						definitionText = Util.removeString(definitionText, "]");
						//definitionText = Util.removeString(definitionText, "\r\n");
						
						Font[] defFonts = new Font[2];
						defFonts[0] = Font.getDefault();
						defFonts[1] = Font.getDefault().derive(Font.DOTTED_UNDERLINED | Font.BOLD);
						
						//synonyms from WORDNET are wrapped in ('{' and '}')
						//if we need to support different dictionaries then add logic to Dictionary.formatDefinition()
						//go through string and mark start, end and each synonym wrapper chars
						char[] chars = definitionText.toCharArray();
						Vector offsetsVec = new Vector();
						offsetsVec.addElement(Integer.toString(0));
						for(int i = 0; i < chars.length; i++) {
							if (chars[i] == '{'|| chars[i] == '}') {
								int indexToAdd = i;
								if (chars[i] == '{')  {
									indexToAdd++;
								}									
								offsetsVec.addElement(Integer.toString(indexToAdd));
							}
						}
					    offsetsVec.addElement(Integer.toString(definitionText.length()));
					    
					    //convert to int[]
					    int[] offsets = new int[offsetsVec.size()];
					    for(int i = 0; i < offsets.length; i++) {
					    	offsets[i] = Integer.parseInt((String)offsetsVec.elementAt(i));
					    }
					    
					    //start to first offset is normal font, first to second offset is styled, etc. etc.
					    byte[] attributes = new byte[offsets.length - 1]; //size one less than offset[] size
						for (int i = 0; i < attributes.length; i ++) {
								attributes[i] = (byte) (i % 2);
						}
						RichTextField rtf = new RichTextField(definitionText, offsets, attributes, defFonts, RichTextField.USE_ALL_HEIGHT);
						screen.replace(screen.definitionText, rtf);
						screen.definitionText = rtf;
					}  
				});
	}
}
