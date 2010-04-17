package com.juksoft.bbvocab;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.container.MainScreen;

public class VocabMainScreen extends MainScreen implements FieldChangeListener {
	
	BasicEditField wordText;
	RichTextField definitionText;
	ButtonField getDefinitionButton;
	
	public String getWordText() {
		return wordText.getText();
	}
	public void setDefinitionText(String text) {
		this.definitionText.setText(text);
	}
	
	public VocabMainScreen() {
		wordText = new BasicEditField("Word:", "");
		this.add(wordText);
		getDefinitionButton = new ButtonField(ButtonField.CONSUME_CLICK);
		getDefinitionButton.setLabel("Get Definition");
		getDefinitionButton.setChangeListener(this);
		this.add(getDefinitionButton);		
		definitionText = new RichTextField(RichTextField.USE_ALL_HEIGHT);
		this.add(definitionText);		
	}
	
	public void fieldChanged(Field field, int context) {
		if (field == getDefinitionButton) {
			Thread thread = new GetDefinitionThread(this);
			thread.start();
		}		
	}
}
