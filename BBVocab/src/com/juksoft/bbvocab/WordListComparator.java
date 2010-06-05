package com.juksoft.bbvocab;

import net.rim.device.api.util.Comparator;

public class WordListComparator implements Comparator {

	public int compare(Object o1, Object o2) {
		int compareInt = 0;
		if(o1 != null && o2 != null) {
			String firstString = (String)o1;
			String secondString = (String)o2;
			return firstString.compareTo(secondString);
		}
		return compareInt;
	}
}
