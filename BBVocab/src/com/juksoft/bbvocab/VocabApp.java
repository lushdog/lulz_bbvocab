package com.juksoft.bbvocab;

import net.rim.device.api.ui.UiApplication;

public class VocabApp extends UiApplication {

	public VocabApp() {
		VocabMainScreen mainScreen = new VocabMainScreen();
		this.pushScreen(mainScreen);
	}

	public static void main(String[] args) {
		VocabApp app = new VocabApp();
		app.enterEventDispatcher();
	}
}
