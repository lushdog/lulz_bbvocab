package com.juksoft.bbvocab;

import java.util.Enumeration;
import java.util.Vector;

import com.juksoft.bbvocab.VocabMainScreen.GoToMyWordsMenuItem;

import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.component.ObjectListField;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.util.SimpleSortingVector;

public class MyWordsScreen extends MainScreen {

	ObjectListField wordListField;
	Vector wordVec = (Vector) Settings.INSTANCE.getWordList();
	
	public MyWordsScreen()  {
		String[] words = wordListVectorToArray(wordVec);
		wordListField = new ObjectListField(words.length);
		wordListField.set(words);
		this.add(wordListField);
	}
	
	private String[] wordListVectorToArray(Vector vector)  {
		Enumeration enumerator = vector.elements();
		SimpleSortingVector wordList = new SimpleSortingVector();
		while(enumerator.hasMoreElements()) {
			wordList.addElement((String)enumerator.nextElement());
		}		
		wordList.setSortComparator(new WordListComparator());
		if (wordList.size() > 0)
			wordList.reSort();
		String[] words = new String[wordList.size()];
		wordList.copyInto(words);
		return words;
	}
	
	protected void makeMenu(Menu menu, int instance) {
		super.makeMenu(menu, instance);
		menu.add(new DeleteWordMenuItem());
	}
	
	public class DeleteWordMenuItem extends MenuItem	{
		public DeleteWordMenuItem() {
			super("Delete", 1, 1);
		}

		public void run() {
			int[] selectedIndexes = wordListField.getSelection();
			for(int i = 0; i < selectedIndexes.length; i++) {
				String word = wordListField.get(wordListField, selectedIndexes[i]).toString();
				wordVec.removeElement(word);				
			}
			String[] words = wordListVectorToArray(wordVec);
			wordListField.set(words);			
		}		
	}
}
