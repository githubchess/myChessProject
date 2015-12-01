package com.example.chessreactor;

public class Reine extends Piece{

	public Reine(couleurPiece c, int ligne, int col) {
		super(typePiece.REINE, c, ligne, col);
	}

	@Override
	protected boolean mouvAutorise()
	{
		if(mouvRectiligne(this.posY0, this.posY, this.posX0, this.posX))
			return true;
		
		if(mouvDiagonale(this.posY0, this.posY, this.posX0, this.posX))
			return true;
			
		return false;
	}
	
}
