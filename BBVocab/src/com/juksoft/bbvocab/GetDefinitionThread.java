package com.juksoft.bbvocab;

import java.io.IOException;

import net.rim.device.api.system.Application;
import net.rim.device.api.ui.component.Dialog;

public class GetDefinitionThread extends Thread {

	VocabMainScreen screen;
	public GetDefinitionThread(VocabMainScreen screen) {
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
					Dialog.alert("Request returned with no definitions or error messages.");
				}
			}
			else {
				updateDefinitionText(definitions[0]);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			Dialog.alert(e.getMessage());
		}		
	}
	protected void updateDefinitionText(final String string) {
		Application.getApplication().invokeLater(
				new Runnable() 
				{ 
					public void run() { screen.setDefinitionText(string);}  
				});
	}
}
