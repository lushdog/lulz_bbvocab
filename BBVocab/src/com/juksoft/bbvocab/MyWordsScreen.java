package com.juksoft.bbvocab;

import java.util.Enumeration;
import java.util.Vector;

import net.rim.device.api.ui.component.ObjectListField;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.util.SimpleSortingVector;

public class MyWordsScreen extends MainScreen {

	ObjectListField objectListField;
	
	public MyWordsScreen()  {
		Vector wordVec = (Vector) Settings.INSTANCE.getWordList();
		Enumeration enumerator = wordVec.elements();
		SimpleSortingVector wordList = new SimpleSortingVector();
		while(enumerator.hasMoreElements()) {
			wordList.addElement((String)enumerator.nextElement());
		}		
		wordList.setSortComparator(new WordListComparator());
		if (wordList.size() > 0)
			wordList.reSort();
		objectListField = new ObjectListField(wordList.size());
		String[] words = new String[wordList.size()];
		wordList.copyInto(words);
		objectListField.set(words);
		this.add(objectListField);
	}

}
