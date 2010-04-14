package com.juksoft.bbvocab;

import java.io.IOException;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.container.MainScreen;
//import com.juksoft.bbvocab.Dictionary;

public class VocabAppMainScreen extends MainScreen implements FieldChangeListener {
	
	/*private LabelField labelField;
	private BitmapField header;
	private SeparatorField seperator;
	private BasicEditField username;
	private PasswordEditField password;
	private ObjectChoiceField selectChoiceObject;
	private SeparatorField separator2;
	private HorizontalFieldManager horizontalFieldManager;
	private ButtonField reset;
	private ButtonField login;
	
	class LoginMenuItem extends MenuItem {
		public LoginMenuItem() {
			super("Login", 20, 10);
		}

		public void run() {
			Dialog.inform("Menu item called 'run.'");				
		}
	}
	
	class ClearMenuItem extends MenuItem {
		public ClearMenuItem() {
			super("Clear", 10, 20);
		}

		public void run() {
			Dialog.inform("Menu item called 'clear.'");				
		}
	}		
	*/
	
	public VocabAppMainScreen() {
		
		//Move to (initApp())
		/*Hashtable hashTable = new Hashtable();
		final long settingsKey = 0xc73a1ba59425265aL;
		PersistentObject persistentObject = PersistentStore.getPersistentObject(settingsKey);
		Object settings = persistentObject.getContents();
		persistentObject.setContents(hashTable);
		persistentObject.commit();
		hashTable.put("foo", "bar");
		persistentObject.commit();
		*/
		
		//MainScreenUpdaterThread mainScreenUpdaterThread = new MainScreenUpdaterThread(this);
		//mainScreenUpdaterThread.start();
		try {
			String[] errorMessage = new String[1];
			String[] definitions = Dictionary.getDefinition("apple", errorMessage);
		}
		catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
	}	
	
	/*public void changeLabel(String newLabel) {
	 
		labelField.setText(newLabel);
	}
*/
	public void fieldChanged(Field field, int context) {
		/*if (field == reset) {
			Dialog.inform("Reset button pressed");
			username.setText("");
			password.setText("");
		}	*/	
	}
	/*
	protected void makeMenu(Menu menu, int instance) {
		super.makeMenu(menu, instance);
		menu.add(new LoginMenuItem());
		menu.add(new ClearMenuItem());
	}
	*/
}
