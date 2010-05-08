package com.juksoft.bbvocab;

import net.rim.device.api.ui.UiApplication;

public class VocabApp extends UiApplication {

	public VocabApp() {
		this.pushScreen(new VocabMainScreen());
	}

	public static void main(String[] args) {
		VocabApp app = new VocabApp();
		app.enterEventDispatcher();
	}
}
