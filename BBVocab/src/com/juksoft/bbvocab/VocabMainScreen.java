package com.juksoft.bbvocab;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.MainScreen;

public class VocabMainScreen extends MainScreen implements FieldChangeListener {
	
	BasicEditField wordText;
	RichTextField definitionText;
	ButtonField getDefinitionButton;
	ButtonField addToMyWordsButton;
	SeparatorField separator;	
	
	public VocabMainScreen() {
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
		definitionText = new RichTextField(RichTextField.USE_ALL_HEIGHT);
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
			addToMyWordsMenuItem();
		}		
	}
	
	public String getWordText() {
		return wordText.getText().trim();
	}
	public void setDefinitionText(String text) {
		this.definitionText.setText(text);
	}
	
	public void addToMyWordsMenuItem() {
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
		Settings.INSTANCE.saveSettings();
		this.close();
		return true;
	}
}
