package com.juksoft.bbvocab;

import net.rim.blackberry.api.menuitem.ApplicationMenuItem;
import net.rim.blackberry.api.stringpattern.PatternRepository;
import net.rim.device.api.system.ApplicationDescriptor;
import net.rim.device.api.ui.UiApplication;

public class VocabApp extends UiApplication {

	private static ApplicationMenuItem[] _menuItems = new ApplicationMenuItem[1];
	static VocabApp app;
	
	public VocabApp() {
		this.pushScreen(new DictionaryScreen());
	}

	public static void main(String[] args) {		
		app = new VocabApp();
    	_menuItems[0] = DictionaryScreen.definitionMenuItem;
		ApplicationDescriptor appDesc = new ApplicationDescriptor(ApplicationDescriptor.currentApplicationDescriptor(), null);
        PatternRepository.addPattern(appDesc,"{[a-zA-z ]*}", PatternRepository.PATTERN_TYPE_REGULAR_EXPRESSION, _menuItems); 
        app.enterEventDispatcher();        		
	}	
}
