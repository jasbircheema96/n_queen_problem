package model;

import java.util.ArrayList;
import java.util.List;

import constant.Constants;

public class State {
	private boolean[][] board;

	public boolean[][] getBoard() {
		return this.board;
	}

	public State() {
		this.board = new boolean[Constants.BOARD_LENGTH][Constants.BOARD_LENGTH];
	}
	
	public State(State state) {
		this.board = new boolean[Constants.BOARD_LENGTH][Constants.BOARD_LENGTH];
		for(int i=0;i<Constants.BOARD_LENGTH;i++) {
			for(int j=0;j<Constants.BOARD_LENGTH;j++) {
				this.board[i][j]=state.board[i][j];
			}
		}
	}
	
	public void print() {
		for(int i=0;i<Constants.BOARD_LENGTH;i++) {
			for(int j=0;j<Constants.BOARD_LENGTH;j++) {
				System.out.print(this.board[i][j]+" ");
			}
			System.out.println();
		}
	}
	
	//number of queens attacking each other
	public int distanceFromGoal() {
		int distance=0;
		List<Cell> queenLocations=this.getQueenLocations();
		for(Cell queenLocation:queenLocations) {
			distance+=this.numberOfCollisions(queenLocation);
		}
		return distance;
	}

	public List<State> nextPossibleStates() {
		List<State> nextPossibleStates = new ArrayList<State>();
		List<Cell> queenLocations=this.getQueenLocations();
		
		for(Cell queenLocation:queenLocations) {
			Move upMove=new Move(queenLocation,Direction.UP);
			Move downMove=new Move(queenLocation,Direction.DOWN);
			if(this.isValidMove(upMove))
				nextPossibleStates.add(this.makeMove(upMove));
			if(this.isValidMove(downMove))
				nextPossibleStates.add(this.makeMove(downMove));
		}
		return nextPossibleStates;
	}
	
	private State makeMove(Move move) {
		State newState=new State(this);
		int row=move.getCell().getRow();
		int col=move.getCell().getCol();
		if(move.getDirection()==Direction.UP) {
			newState.getBoard()[row-1][col]=true;
		}else if(move.getDirection()==Direction.DOWN) {
			newState.getBoard()[row+1][col]=true;
		}
		newState.getBoard()[row][col]=false;
		return newState;
	}

	public void randomlyPlaceQueens() {
		this.removeAllQueens();
		for (int col = 0; col < Constants.BOARD_LENGTH; col++) {
			int row = (int) (Math.random() * Constants.BOARD_LENGTH);
			//System.out.println(row);
			board[row][col] = true;
		}
	}
	
	private void removeAllQueens() {
		for(int row=0;row<Constants.BOARD_LENGTH;row++)
			for(int col=0;col<Constants.BOARD_LENGTH;col++)
				this.board[row][col]=false;
	}

	public boolean isGoalState() {
		for (int row = 0; row < Constants.BOARD_LENGTH; row++) {
			for (int col = 0; col < Constants.BOARD_LENGTH; col++) {
				if(board[row][col]&& !isQueenSafe(new Cell(row,col)))
					return false;
			}
		}
		return true;
	}
	
	private List<Cell> getQueenLocations(){
		List<Cell> locations=new ArrayList<Cell>();
		for(int i=0;i<Constants.BOARD_LENGTH;i++) {
			for(int j=0;j<Constants.BOARD_LENGTH;j++) {
				if(this.board[i][j])
					locations.add(new Cell(i,j));
			}
		}
		return locations;
	}
	
	private int numberOfCollisions(Cell cell) {
		int row=cell.getRow();
		int col=cell.getCol();
		int distance=0;
		
		//horizontal check 
		for(int i=0;i<col;i++)
			if(this.board[row][i])
				distance++;
		for(int i=col+1;i<Constants.BOARD_LENGTH;i++)
			if(this.board[row][i])
				distance++;
		
		//vertical check
		for(int i=0;i<row;i++)
			if(this.board[i][col])
				distance++;
		for(int i=row+1;i<Constants.BOARD_LENGTH;i++)
			if(this.board[i][col])
				distance++;
		
		// check right lower diagonal
		for (int i = row + 1, j = col + 1; i < Constants.BOARD_LENGTH && j < Constants.BOARD_LENGTH; i++, j++)
			if (this.board[i][j])
				distance++;
		
		// check right upper diagonal
		for (int i = row - 1, j = col + 1; i >= 0 && j < Constants.BOARD_LENGTH; i--, j++)
			if (this.board[i][j])
				distance++;
		
		// check left lower diagonal
		for (int i = row + 1, j = col - 1; i < Constants.BOARD_LENGTH && j >= 0; i++, j--)
			if (this.board[i][j])
				distance++;

		// check left upper diagonal
		for (int i = row - 1, j = col - 1; i >= 0 && j >= 0; i--, j--)
			if (this.board[i][j])
				distance++;
		
		return distance;
	}
	private boolean isQueenSafe(Cell cell) {
		int row = cell.getRow();
		int col = cell.getCol();
		int count;

		// check horizontal
		count = 0;
		for (int i = 0; i < Constants.BOARD_LENGTH; i++)
			if (this.board[row][i])
				count++;
		if (count > 1)
			return false;

		// check vertical
		count = 0;
		for (int i = 0; i < Constants.BOARD_LENGTH; i++)
			if (this.board[i][col])
				count++;
		if (count > 1)
			return false;

		// check right lower diagonal
		for (int i = row + 1, j = col + 1; i < Constants.BOARD_LENGTH && j < Constants.BOARD_LENGTH; i++, j++)
			if (this.board[i][j])
				return false;
		// check right upper diagonal
		for (int i = row - 1, j = col + 1; i >= 0 && j < Constants.BOARD_LENGTH; i--, j++)
			if (this.board[i][j])
				return false;
		// check left lower diagonal
		for (int i = row + 1, j = col - 1; i < Constants.BOARD_LENGTH && j >= 0; i++, j--)
			if (this.board[i][j])
				return false;

		// check left upper diagonal
		for (int i = row - 1, j = col - 1; i >= 0 && j >= 0; i--, j--)
			if (this.board[i][j])
				return false;
		return true;
	}

	private boolean isValidMove(Move move) {
		int row = move.getCell().getRow();
		Direction direction = move.getDirection();
		if (direction == Direction.UP) {
			if (row <= 0)
				return false;
		} else if (direction == Direction.DOWN) {
			if (row >= Constants.BOARD_LENGTH - 1)
				return false;
		}
		return true;
	}
}
