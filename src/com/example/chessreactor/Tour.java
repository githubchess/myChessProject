package com.example.chessreactor;



public class Tour extends Piece{

	public Tour(couleurPiece c, int ligne, int col) {
		super(typePiece.TOUR, c, ligne, col);
	}


	protected boolean mouvAutorise()
	{
		if(mouvRectiligne(this.posY0, this.posY, this.posX0, this.posX))
			return true;
			
		return false;
	}
		
		
}