package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ui.JottoGUI;

/**
 * 
 * Controller is the listener for client guess input.
 * It starts a new thread whenever a new guess has been entered.
 *
 */
public class Controller implements ActionListener {

	private final JottoGUI jottoGUI;

	public Controller(JottoGUI jottoGUI) {
		this.jottoGUI = jottoGUI;
	}

	public void actionPerformed(ActionEvent e) {
		GuessControllerThread thread = new GuessControllerThread(jottoGUI);
		thread.start();
	}
}