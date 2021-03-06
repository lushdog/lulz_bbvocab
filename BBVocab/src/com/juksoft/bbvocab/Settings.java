package com.juksoft.bbvocab;

import java.util.Hashtable;
import java.util.Vector;

import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;

public class Settings {

	private static final String WORD_LIST = "wordList";
	public static final Settings INSTANCE = new Settings();
	private Hashtable settingsBag = null;
	private PersistentObject persistentObject = null;
	
	protected Settings() {
		
		persistentObject = PersistentStore.getPersistentObject(Util.appKey);
		Object settings = persistentObject.getContents();
		
		if (settings == null) {
			settingsBag = initSettings();
		}
		else {
			settingsBag = (Hashtable)settings;
		}
	}
	
	private static Hashtable initSettings() {
		Hashtable hashtable = new Hashtable();
		hashtable.put(WORD_LIST, new Vector());
		return hashtable;
	}
	
	public Vector getWordList() {
		return (Vector) settingsBag.get(WORD_LIST);
	}
	
	public void addWordToList(String word) {
		Vector wordList = (Vector)settingsBag.get(WORD_LIST);
		if (wordList.indexOf((String)word) == -1) {
			wordList.addElement(word);
		}
		settingsBag.put(WORD_LIST, wordList);
	}
	
	public void deleteWordFromList(String word) {
		Vector wordList = (Vector) settingsBag.get(WORD_LIST);
		wordList.removeElement(word);
		settingsBag.put(WORD_LIST, wordList);
	}
	
	public void saveSettings() {
		persistentObject.setContents(settingsBag);
		persistentObject.commit();		
	}
}
