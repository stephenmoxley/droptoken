package game_code;
import java.util.*;

/**
 * 
 * The user of DropToken can play one game of Drop Token. 
 * The user can submit "GET", "PUT #", where # is a number
 * 1-4, "BOARD", or "EXIT". Submitting "EXIT" will end the 
 * game. 
 * 
 * @author stephenmoxley
 *
 */
public class DropToken {

	public static void main(String[] args) {
		Scanner console = new Scanner(System.in);
		Game thisGame = new Game(4, console);
		boolean gameOver = false;
		
		// plays moves until "EXIT" is given
		while(!gameOver) {
			String instruction = console.next().toLowerCase();
			if (instruction.equals("exit")) {
				gameOver = true;
			} else {
				thisGame.playInstruction(instruction);
			}
		}
		
	}
	
	/**
	 * 
	 * Game represents one instance of a Drop Token game. Users of game can specify
	 * the size of the board, and submitting commands with System.in will play moves,
	 * show the board, or show the moves that have been played. Once a player has won,
	 * the user is no longer able to put tokens on the board.
	 * 
	 * @author Stephen Moxley
	 * 
	 */
	private static class Game {
		
		boolean won;
		int[][] board;
		List<Integer> columns;
		boolean playerOne;
		String[] instructions = {"get", "put", "board"};
		Scanner scanner;
		
		/**
		 * Initializes a new drop token game with a size x size board
		 * 
		 * @param size  the size of the game board
		 * @param scanner  the stdin scanner being used 
		 */
		public Game(int size, Scanner scanner) {
			board = new int[size][size];
			columns = new ArrayList<Integer>();
			playerOne = true;
			won = false;
			this.scanner = scanner;
		}
		
		/**
		 * Plays the next command on the scanner, if there is one.
		 * If the move is unable to be played or is not a valid 
		 * command, "> ERROR" is printed. 
		 * 
		 * @param  instruction  the instruction to play
		 * @return void
		 */
		public void playInstruction(String instruction) {
			if (instruction.equals(instructions[0])) {
				this.get();
			} else if (instruction.equals(instructions[1])) {
				if (won) {
					System.out.println();
					System.out.println("> ERROR: game is over");
					System.out.println();
					scanner.next();
				}else if (scanner.hasNextInt()) {
					int column = scanner.nextInt();
					System.out.println();
					this.put(column);
				} else {
					scanner.next();
					System.out.println();
					System.out.println("> ERROR");
					System.out.println();
				}
			} else if (instruction.equals(instructions[2])) {
				this.printBoard();
			} else {
				System.out.println();
				System.out.println("> ERROR");
				System.out.println();
			}
		}
		
		/**
		 * Prints the board on System.out
		 */
		public void printBoard() {
			System.out.println();
			for(int i = 0; i < board.length; i++) {
				System.out.print("| ");
				for (int j = 0; j < board.length; j++) {
					System.out.print(board[j][i] + " ");
				}
				System.out.println();
			}
			System.out.print("+");
			for (int i = 0; i < 2 * board.length; i++) {
				System.out.print("-");
			}
			System.out.println();
			System.out.print("  ");
			for (int i = 0; i < board.length; i++) {
				System.out.print((i + 1) + " ");
			}
			System.out.println();
			System.out.println();
		}
		
		/**
		 * Prints the moves played by users to System.out
		 */
		public void get() {
			if (columns.size() > 0) {
				System.out.println();
				for (int column : columns) {
					System.out.println(column);
				}
			}
			System.out.println();
		}
		
		/**
		 * If column is a valid column on the board, puts the user's
		 * token on the board and switches the user playing. If the 
		 * column is not a valid column, prints "> ERROR".
		 * 
		 * @param  column  the column the user is dropping a token in
		 * 
		 */
		public void put(int column) {
			if (column - 1 >= board.length || isBoardFull()) {
				System.out.println("> ERROR");
			} else {
				int[] columnToPut = board[column - 1];
				int valueToPut = playerOne ? 1 : 2;
				for (int i = columnToPut.length - 1; i >= 0; i--) {
					if (columnToPut[i] == 0) {
						columnToPut[i] = valueToPut;
						columns.add(column);
						break;
					}
				}
				playerOne = !playerOne;
				if (checkForWin()) {
					System.out.println("> WIN");
				} else if (isBoardFull()) {
					System.out.println("> DRAW");
				} else {
					System.out.println("> OK");
				}
			}
			
			System.out.println();
		}
		
		/**
		 * Checks the board to see if a player has won
		 * 
		 * @return true if a player has won, false otherwise
		 */
		public boolean checkForWin() {
			boolean rows = checkRows(true, true);
			boolean columns = checkColumns(true, true);
			boolean diagonals = checkDiagonals(true, true);
			return rows || columns || diagonals;
		}
		
		/**
		 * Checks the rows of the board to see if a player has one
		 * 
		 * @param  ones  a tracker for whether the first player has won
		 * @param  twos  a tracker for whether the second player has won
		 * @return  true if a player has won, false otherwise
		 */
		private boolean checkRows(boolean ones, boolean twos) {
			for (int i = 0; i < board.length; i++) {
				int[] column = board[i];
				for (int j = 0; j < column.length; j++) {
					if (column[j] != 1) {
						ones = false;
					}
					if (column[j] != 2) {
						twos = false;
					}
				}
				if (ones || twos) {
					won = true;
					return true;
				} else {
					ones = true;
					twos = true;
				}
			}
			return false;
		}
		
		/**
		 * Checks the columns of the board to see if a player has one
		 * 
		 * @param  ones  a tracker for whether the first player has won
		 * @param  twos  a tracker for whether the second player has won
		 * @return  true if a player has won, false otherwise
		 */
		private boolean checkColumns(boolean ones, boolean twos) {
			for (int i = 0; i < board.length; i++) {
				
				for (int j = 0; j < board.length; j++) {
					if (board[j][i] != 1) {
						ones = false;
					}
					if (board[j][i] != 2) {
						twos = false;
					}
				}
				
				if (ones || twos) {
					won = true;
					return true;
				} else {
					ones = true;
					twos = true;
				}
			}
			return false;
		}
		
		/**
		 * Checks the diagonals on the board to see if a user has won
		 * 
		 * @param  ones  a tracker for whether the first player has won
		 * @param  twos  a tracker for whether the second player has won
		 * @return  true if a player has won, false otherwise
		 */
		private boolean checkDiagonals(boolean ones, boolean twos) {
			for (int i = 0; i < board.length; i++) {
				if (board[i][i] != 1) {
					ones = false;
				}
				if (board[i][i] != 2) {
					twos = false;
				}
			}
			if (ones || twos) {
				won = true;
				return true;
			}
			ones = true;
			twos = true;
			for (int i = 0; i < board.length; i++) {
				if (board[board.length - 1 - i][i] != 1) {
					ones = false;
				}
				if (board[board.length - 1 - i][i] != 2) {
					twos = false;
				}
			}
			won = ones || twos;
			return ones || twos;
		}
		
		/**
		 * @return true if the board is full, false otherwise
		 */
		private boolean isBoardFull() {
			for (int i = 0; i < board.length; i++) {
				for (int j = 0; j < board[i].length; j++) {
					if (board[i][j] == 0) {
						return false;
					}
				}
			}
			return true;
		}
		
	}

}
