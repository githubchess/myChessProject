package com.example.chessreactor;


public class Case {

	public enum couleurCase{
		BLANK,
		NOIRE,
		BLANCHE
	}
	
	private couleurCase cCase = couleurCase.BLANK;
	
	Piece occupant;

	public couleurCase getcCase() {
		return cCase;
	}

	public void setcCase(couleurCase cCase) {
		this.cCase = cCase;
	}

	public Piece getOccupant() {
		return this.occupant;
	}

	public void setOccupant(Piece occupant) {
			this.occupant = occupant;
	}

	public Case(couleurCase cCase, Piece occupant) {
		this.cCase = cCase;
		this.occupant = occupant;
	}
}
