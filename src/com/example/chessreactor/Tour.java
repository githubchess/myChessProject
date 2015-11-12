package com.example.chessreactor;



public class Tour extends Piece{

	public Tour(couleurPiece c, int ligne, int col) {
		super(typePiece.TOUR, c, ligne, col);
	}

	@Override
	protected boolean mouvAutorise()
	{
		if(mouvRectiligne(this.posX0, this.posX, this.posY0, this.posY))
			return true;
			
		return false;
	}
		
		
}