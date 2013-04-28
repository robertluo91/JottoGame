package controller;

import java.util.List;
import model.JottoModel;
import ui.JottoGUI;


/**
 * GuessControllerThread: one thread per guess
 * In each thread, the client's guess is forwarded to the server, and the guess table is updated 
 * 
 * For thread-safty statment, please see JottoGUI.java
 *
 */
public class GuessControllerThread extends Thread {

	private final JottoGUI jottoGUI;

	// constructor: start a new thread on the GUI
	public GuessControllerThread(JottoGUI jottoGUI) {
		this.jottoGUI = jottoGUI;
	}

	public void run() {
		String userGuess = jottoGUI.getGuess();
		userGuess = userGuess.trim();
		
		// After the client hits "enter", clear the text field 
		jottoGUI.resetGuessField();
		
		int puzzleNumber = jottoGUI.getCurrentPuzzleNumber();
		int rowNumber = jottoGUI.addHistory(userGuess, "", "");
		
		String feedBack = JottoModel.makeGuess(userGuess, puzzleNumber);
		List<String> twoColumnfeedback = JottoModel.convertResponse(feedBack);
		if (feedBack.equals(JottoModel.victory)) {
			System.out.println("you win!");
		}
		if (jottoGUI.getCurrentPuzzleNumber() == puzzleNumber) {
			// in case of "guess 5 5", twoColumnfeedback.get(0) = "you win"
			// twoColumnfeedback.get(1)) = ""
			jottoGUI.updateHistory(rowNumber, userGuess, twoColumnfeedback.get(0),
			        twoColumnfeedback.get(1));
		}
	}

}
