package model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * 
 * Testing Strategy for JottoModel 
 * A. Test whether a perfect guess returns 'guess 5 5'
 * B. Test whether a legitimate guess gets valid response from
 *     the sever
 *     Case 1: without delay; Case 2: with delay (* included)
 * C. Test whether the server returns Type 1 Error if the puzzle
 *     ID is invalid (Puzzle ID <= 0)
 * D. Test whether the server still correctly returns even when 
 *     the puzzle ID is a very large number
 * E. Test for errors from the server  
 *     1) guess too long  
 *     2) guess too short
 *     3) guess contains illegal character(s)
 */  

public class JottoModelTest {
	@Test
    public void testPerfectGuess() {
		assertEquals("guess 5 5",
				JottoModel.makeGuess("cargo", 16952));
    }	
	@Test
	public void testGoodGuess1(){
		String expected = "guess 3 1";
		String result = JottoModel.makeGuess("crazy", 16952);
		assertEquals(expected,result);
	}
	@Test
	public void testGoodGuess2(){
		// guess with a place-holder *
		String expected = "guess 2 1";
		String result = JottoModel.makeGuess("c*azy", 16952);
		assertEquals(expected,result);
	}
	@Test
	public void invalidGuessID1(){
		// puzzle ID = 0 
		String expected = "error 1";
		String result = JottoModel.makeGuess("crazy", 0);
		assertEquals(expected,result.substring(0, 7));
	}
	
	@Test
	public void invalidGuessID2(){
		// puzzle ID < 0 
		String expectedresult = "error 1";
		String result = JottoModel.makeGuess("crazy", -1);
		assertEquals(expectedresult,result.substring(0, 7));
	}
	
	@Test
	public void validButLargeGuessID(){
		// puzzle ID very large 
		String expectedresult = "guess 1 1";
		String result = JottoModel.makeGuess("crazy", Integer.MAX_VALUE);
		assertEquals(expectedresult,result);
	}
	
	@Test
	public void error2Test(){
		// guess too long
		String expected = "error 2";
		String result = JottoModel.makeGuess("transformer", 16952);
		assertEquals(expected,result.substring(0, 7));	
	}	
	@Test
	public void error2Test2(){
		// guess too short
		String expected = "error 2";
		String result = JottoModel.makeGuess("*abc", 16952);
		assertEquals(expected,result.substring(0, 7));
	}
	
	@Test
	public void error2Test3(){
		// guess contains illegal characters
		String expected = "error 2";
		String result = JottoModel.makeGuess("!!!!", 16952);
		assertEquals(expected,result.substring(0, 7));
	}
}
