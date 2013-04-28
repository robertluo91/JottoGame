package ui;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.GroupLayout;
import javax.swing.table.DefaultTableModel;

import controller.Controller;
import controller.PuzzleNumController;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;

/**
 * The GUI for Jotto game. The GUI contains elements like labels, buttons, text fields and tables.
 * A user can type in a text field "PuzzleNumber" and "guess" and hit enter, or click a button "newPuzzleButton" 
 * to change the GUI relying on the action listener.
 * 
 * GUI contains the following main elements:
 * (1) newPuzzleButton: press to generate new puzzle question
 * (2) newPuzzleNumber: textField where the player can specify puzzle number and
 * "listens" to action "enter" to update puzzle puzzleNumber
 * (3) guess: client input the guess string. It also listens to action "enter" 
 * and creates a new thread for each new action. 
 * (4) guessTable: records guess and feedback history
 * (5) PuzzleNumbernow: stores the current puzzle number. default to 1
 * (6) tableModel: tableModel used by guessTable
 * 
 */

/**
 * 
 * Thread-Safety Argument
 * The threads in this program are:
 * - one thread per new guess input
 * - a background thread for the GUI
 * Since the underlying data structures of Java Swing are inherently unsafe, we use
 * SwingUtilities.invokeLater() to make it thread safe by queuing messages to run in the single 
 * designated GUI thread.
 * 
 * All fields in the GUI are private final. The only shared mutable datatype
 * is the guessTable, which is continuously updated through new guess inputs. 
 * The rep invariants (please see below) for the guessTable, however, are always 
 * preserved even in concurrent conditions. Specifically:
 * (1) GUI.updateHistory() is written such that the guess string and its associated
 *     server feedback always have the same row number in the table, even if the feedback is delayed from the server
 * (2) The last row of guessTable corresponds to the most recent guess entered.
 * 		Whenever a new guess is entered, a new row will be appended to the table 
 *     even if the server has not yet returned.
 *     
 * Therefore, in a multi-threaded setting, the rep invariants are still preserved without race conditions. 
 *
 */


@SuppressWarnings("serial")
public class JottoGUI extends JFrame {

	private final JButton newPuzzleButton;
	private final JTextField newPuzzleNumber;
	private final JLabel puzzleNumber;
	private final JTextField guess;
	private final JTable guessTable;
	private final JLabel guessCommand;
	private final PuzzleNumController puzzleNumberController;
	private final Controller guessController;

	private int PuzzleNumbernow = 1;
	final private DefaultTableModel tableModel = new DefaultTableModel();

	public JottoGUI() {
		
		// puzzleNumber
		puzzleNumber = new JLabel("Puzzle #" + PuzzleNumbernow);
		puzzleNumber.setName("puzzleNumber");
		getContentPane().add(puzzleNumber);

		// action listener for newPuzzleButton
		puzzleNumberController = new PuzzleNumController(this);

		// newPuzzleButton
		newPuzzleButton = new JButton("New Puzzle");
		newPuzzleButton.setName("newPuzzleButton");
		getContentPane().add(newPuzzleButton);
		newPuzzleButton.addActionListener(puzzleNumberController);

		// text field for inputing new puzzle number
		newPuzzleNumber = new JTextField("");
		newPuzzleNumber.setName("newPuzzleNumber");
		getContentPane().add(newPuzzleNumber);
		newPuzzleNumber.addActionListener(puzzleNumberController);

		// "type a guess here"
		guessCommand = new JLabel("Type a guess here:");
		getContentPane().add(guessCommand);

		// action listener for guess
		guessController = new Controller(this);
		// text field for inputting guess string
		guess = new JTextField("guess");
		guess.setName("guess");
		getContentPane().add(guess);
		guess.addActionListener(guessController);

		// guessTable
		guessTable = new JTable(tableModel);
		tableModel.addColumn("Your Guess");
		tableModel.addColumn("Number of Letters in Common");
		tableModel.addColumn("Number.. of Letters in Position");
		guessTable.setName("guessTable");
		getContentPane().add(guessTable);

		// make scrollable guess history
		JScrollPane scrollPane = new JScrollPane(guessTable);
		guessTable.scrollRectToVisible(guessTable.getCellRect(
				guessTable.getRowCount() - 1, 0, true));
		getContentPane().add(scrollPane);

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(layout.createParallelGroup()
				.addGroup(layout.createSequentialGroup()
								.addComponent(puzzleNumber)
								.addComponent(newPuzzleButton)
								.addComponent(newPuzzleNumber))
				.addGroup(layout.createSequentialGroup()
								.addComponent(guessCommand).addComponent(guess))
				.addComponent(scrollPane));

		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup().addComponent(puzzleNumber)
								.addComponent(newPuzzleButton)
								.addComponent(newPuzzleNumber))
				.addGroup(layout.createParallelGroup().addComponent(guessCommand)
								.addComponent(guess)).addComponent(scrollPane));

		setSize(getPreferredSize());
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

	}
	/**
	 * set the GUI into a new puzzle ID, and reset the previous guess history
	 * @param n new puzzle number
	 */
	public void setPuzzleIDNumber(int n) {
		PuzzleNumbernow = n;
		puzzleNumber.setText("Puzzle #" + n);
		Historyreset();
	}

	/**
	 * If no number is provided or the input is not a positive integer, pick a
	 * random positive integer. Make sure you can generate at least 10,000
	 * different numbers.
	 * 
	 * @return the puzzle number requested by the client; or a randomly generated
	 * number if the client request is invalid
	 */
	public int getPuzzleIDNumber() {
		final String userInput = newPuzzleNumber.getText();
		int newPuzzleNumber = 0;
		try {
			newPuzzleNumber = Integer.parseInt(userInput);
		} catch (Exception e) {//test field is empty
			final Random generator = new Random();
			newPuzzleNumber = generator.nextInt(100000); 
		}
		if (newPuzzleNumber == 0) {
			throw new RuntimeException("puzzle number failure!");
		}
		return newPuzzleNumber;

	}

	/**
	 * 
	 * @return the puzzle number on which the game is running
	 */
	public int getCurrentPuzzleNumber() {
		return PuzzleNumbernow;
	}

	/**
	 * 
	 * @return the guess string inputed by the client
	 */
	public String getGuess() {
		String userGuess = guess.getText();
		return userGuess;
	}

	public void resetGuessField() {
		guess.setText("");
	}

	/**
	 * 
	 * @param s1 string for column 1
	 * @param s2 string for column 2
	 * @param s3 string for column 3
	 * @return the number of rows in the current table
	 */
	public int addHistory(String s1, String s2, String s3) {

		tableModel.addRow(new Object[] { s1, s2, s3 });
		System.out.println("addHistory " + (tableModel.getRowCount() - 1));
		return tableModel.getRowCount() - 1;
	}

	
	/**
	 * 
	 * @param row the row to insert the guess information into
	 * @param s1 string for column 1
	 * @param s2 string for column 2
	 * @param s3 string for column 3
	 */
	public void updateHistory(int row, String s1, String s2, String s3) {
		System.out.println("updateHistory");
		tableModel.removeRow(row);
		tableModel.insertRow(row, new Object[] { s1, s2, s3 });

	}

	/**
	 * reset table to zero
	 */
	public void Historyreset() {
		tableModel.setRowCount(0);
	}

	public static void main(final String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JottoGUI main = new JottoGUI();

				main.setVisible(true);
			}
		});
	}
}
