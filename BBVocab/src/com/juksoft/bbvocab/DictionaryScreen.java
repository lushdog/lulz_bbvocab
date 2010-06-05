package com.juksoft.bbvocab;

import net.rim.blackberry.api.menuitem.ApplicationMenuItem;
import net.rim.blackberry.api.stringpattern.PatternRepository;
import net.rim.device.api.system.ApplicationDescriptor;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ActiveRichTextField;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.MainScreen;

public class DictionaryScreen extends MainScreen implements FieldChangeListener {
	
	BasicEditField wordText;
	ActiveRichTextField definitionText;
	ButtonField getDefinitionButton;
	ButtonField addToMyWordsButton;
	SeparatorField separator;	
	
	public DictionaryScreen() {
		this.setBanner(new LabelField("My Dictionary"));
		
		wordText = new BasicEditField("Word:", "");
		this.add(wordText);
		
		getDefinitionButton = new ButtonField(ButtonField.CONSUME_CLICK);
		getDefinitionButton.setLabel("Get Definition");
		getDefinitionButton.setChangeListener(this);
		this.add(getDefinitionButton);		
		
		addToMyWordsButton = new ButtonField(ButtonField.CONSUME_CLICK);
		addToMyWordsButton.setLabel("Add To 'My Words'");
		addToMyWordsButton.setChangeListener(this);		
		this.add(addToMyWordsButton);		
		
		separator = new SeparatorField();
		this.add(separator);
		
		definitionText = new ActiveRichTextField("", Field.USE_ALL_HEIGHT);
		this.add(definitionText);
	}
	
	protected void makeMenu(Menu menu, int instance) {
		super.makeMenu(menu, instance);
		menu.add(new GoToMyWordsMenuItem());
	}
	
	public void fieldChanged(Field field, int context) {
		if (field == getDefinitionButton) {
			Thread thread = new GetDefinitionThread(this);
			thread.start();
		}		
		else if (field == addToMyWordsButton) {
			addToMyWords();
		}		
	}
	
	public String getWordText() {
		return wordText.getText().trim();
	}
	
	public void setWordText(String word) {
		wordText.setText(word.trim());
	}
	
	public void setDefinitionText(String text) {
		this.definitionText.setText(text);
	}
	
	public void addToMyWords() {
		Settings.INSTANCE.addWordToList(getWordText());
		Dialog.inform("Added To 'My Words'");
	}
	
	public class GoToMyWordsMenuItem extends MenuItem	{
		public GoToMyWordsMenuItem() {
			super("Go To 'My Words'", 1, 1);
		}

		public void run() {
			UiApplication.getUiApplication().pushScreen(new MyWordsScreen());
		}		
	}
	
	public boolean onClose() {
		if (UiApplication.getUiApplication().getScreenCount() == 1)  {
			Settings.INSTANCE.saveSettings();
			ApplicationDescriptor appDesc = new ApplicationDescriptor(ApplicationDescriptor.currentApplicationDescriptor(), null);
	        PatternRepository.removePatterns(appDesc);
	    }
        this.close();
		return true;
	}
	
	static ApplicationMenuItem definitionMenuItem = new ApplicationMenuItem(0)  {
        public Object run(Object context)  {
        	DictionaryScreen newScreen = new DictionaryScreen();
			UiApplication.getUiApplication().pushScreen(newScreen);
			String synonym = context.toString();
			synonym = Util.removeString(synonym, "{");
			synonym = Util.removeString(synonym, "}");			
			newScreen.setWordText(synonym);	
			Thread thread = new GetDefinitionThread(newScreen);
			thread.start();
        	return null;
        }
        public String toString()  {
            return "Get Definition";   
        }
    };    
}
