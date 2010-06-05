package com.juksoft.bbvocab;

import net.rim.device.api.system.Application;
import net.rim.device.api.ui.component.ActiveRichTextField;

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
						definitionText = Util.formatDefinitionForTextField(definitionText);
						ActiveRichTextField rtf = new ActiveRichTextField(definitionText);
						screen.replace(screen.definitionText, rtf);
						screen.definitionText = rtf;
					}
				});
	}
}
