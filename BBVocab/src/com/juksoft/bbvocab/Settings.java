package com.juksoft.bbvocab;

import java.util.Hashtable;

import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;
import net.rim.device.api.util.SimpleSortingVector;

public class Settings {

	private static final String WORD_LIST = "wordList";
	public static final Settings INSTANCE = new Settings();
	private Hashtable settingsBag = null;
	private PersistentObject persistentObject = null;
	private static final long settingsKey = 0x195da3c07d98835L;
	
	protected Settings() {
		
		persistentObject = PersistentStore.getPersistentObject(settingsKey);
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
		hashtable.put(WORD_LIST, new SimpleSortingVector());
		return hashtable;
	}
	
	public SimpleSortingVector getWordList() {
		return (SimpleSortingVector) settingsBag.get(WORD_LIST);
	}
	
	public void addWordToList(String word) {
		SimpleSortingVector wordList = (SimpleSortingVector)settingsBag.get(WORD_LIST);
		if (wordList.indexOf((String)word) == -1) {
			wordList.addElement(word);
		}
		settingsBag.put(WORD_LIST, wordList);
	}
	
	public void deleteWordFromList(String word) {
		SimpleSortingVector wordList = (SimpleSortingVector) settingsBag.get(WORD_LIST);
		wordList.removeElement(word);
		settingsBag.put(WORD_LIST, wordList);
	}
	
	public void saveSettings() {
		persistentObject.setContents(settingsBag);
		persistentObject.commit();
	}
}
