package com.juksoft.bbvocab;

import net.rim.device.api.ui.UiApplication;

public class MainScreenUpdaterThread extends Thread {

	VocabAppMainScreen mainScreen;
	
	public MainScreenUpdaterThread(VocabAppMainScreen mainScreen) {
		this.mainScreen = mainScreen;
	}
	
	public void run() {
		for(int i=0; i<10; i++) {
			try {
				Thread.sleep(5000);
			}
			catch (InterruptedException ex) {
				
			}
			
			//Queues up task on the event/GUI thread
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					//mainScreen.changeLabel("blah blah");
				}
			});
			
		}
	}
}
