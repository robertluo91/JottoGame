package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * JottoModel takes in puzzle number + guess from the
 * client and converts the response message from the server into 
 * a 2-column format.
 *
 */
public class JottoModel {
	public final static String victory="guess 5 5";
	/**
	 * @param guess: a string to be submitted to the server
	 * 	
	 * @param puzzleNumber: the puzzle ID to be requested from the server
	 * 				
	 * @return (in String format) response or feedback from the server
	 * 			
	 * @throws RuntimeException("IOException") when failing server connection
	 * @throws RuntimeException("URLFailure") when URL is ill-formed
	 * @throws RuntimeException ("Null/Empty Response") 
	 * @throws RuntimeException ("invalid feedback")	
	 */
    
    public static String makeGuess(String guess, int puzzleID) {
    	BufferedReader readin = null;
		URL Link = null;
		String response;
		try {
			Link = new URL("http://courses.csail.mit.edu/"
					+ "6.005/jotto.py?puzzle=" + puzzleID + "&guess="
					+ guess);
			readin = new BufferedReader(
			        new InputStreamReader(Link.openStream()));
			
			while ((response = readin.readLine()) != null && response.trim() == "") {
				//to get the concrete server response
			}
			readin.close();
		} catch (MalformedURLException e) {
			throw new RuntimeException("Invalid URL: " + Link);
		} catch (IOException e) {
			throw new RuntimeException("IOException");
		} finally {
			try {//use finally to ensure the close of readin
				readin.close();
			} catch (Exception e) {
			}
		}
		if (response == null) {
			throw new RuntimeException("null/empty response!");
		}
		response = response.trim();
		if (response == "") {
			throw new RuntimeException("null/empty response!");
		}
		
		// use regex expression to determine valid responses or a specific type of error
		String validresponse = "(guess [0-5] [0-5])|(error [0-3].*)";
		if(!response.matches(validresponse )) {
			throw new RuntimeException("invalid response from server!");
        }
		return response;       
	}
    /**
	 * 
	 * @param response: response from the server
	 * @return a two-element list containing the number of correct characters
	 *          and the number of correct positions
	 * @throws RuntimeException ("invalid response format")
	 * 
	 * rep invariant: the output is a 2-column arraylist containing
	 *                the parsed feedback message from the server
	 */
	public static List<String> convertResponse(String response){
		String guessM = "(guess [0-5] [0-5])";
		String errorM = "(error [0-3].*)";	
		List<String> Output=new ArrayList<String>();
		if (response.equals(victory)){
			Output.add("you win!");
			Output.add("");
		}
		else if (response.matches(guessM)){
			Output.add(response.substring(6, 7)); // # of correct guesses
			Output.add(response.substring(8, 9));  // # of correct positions
		}else if (response.matches(errorM)){
			Output.add(response.substring(8)); // full body of error message
			Output.add("");
		}else{
			/*revised to make the exception easier to follow (based on code review comment)*/
			throw new RuntimeException("invalid response from the server"); 
		}
		return Output;
	}
    
}
