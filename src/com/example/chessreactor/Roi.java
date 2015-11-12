package com.example.chessreactor;


public class Roi extends Piece{
	
	public Roi(couleurPiece c, int ligne, int col) {
		super(typePiece.ROI, c, ligne, col);
	}
	
	@Override
	protected boolean mouvAutorise()
	{
		// le roi ne se deplace que d'une case sauf dans le cas du roque
		if(Math.abs(this.posX-this.posX0)>2||(Math.abs(this.posY-this.posY0)>1))
			return false;
			
		return true;
	}

}
