package com.juksoft.bbvocab;

import net.rim.device.api.system.Application;

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
	
	protected void updateDefinitionText(final String string) {
		Application.getApplication().invokeLater(
				new Runnable() 
				{ 
					public void run() { screen.setDefinitionText(string);}  
				});
	}
}
