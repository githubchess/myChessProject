package com.example.chessreactor;


public class Fou extends Piece{

	public Fou(couleurPiece c, int ligne, int col) {
		super(typePiece.FOU, c, ligne, col);
	}
	
	@Override
	protected boolean mouvAutorise()
	{
		if(mouvDiagonale(this.posX0, this.posY, this.posY0, this.posX))
			return true;
			
		return false;
	}

}
