package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ui.JottoGUI;

/**
 * PuzzleNumberController is the listener for client specification of puzzle number
 * 
 * It updates the puzzle number based on the client inputs and resets the 
 * guessTable in the View. 
 *
 */
public class PuzzleNumController implements ActionListener{
	public JottoGUI jottoGUI;
	public PuzzleNumController(JottoGUI jottoGUI)
	{
		this.jottoGUI = jottoGUI;
	}
	public void actionPerformed(ActionEvent e)
	{
		int PuzzleNumber=jottoGUI.getPuzzleIDNumber();
		jottoGUI.setPuzzleIDNumber(PuzzleNumber); 
	}
}