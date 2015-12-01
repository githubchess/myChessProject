package com.example.chessreactor;


public class Cavalier extends Piece{

	public Cavalier(couleurPiece c, int ligne, int col) {
		super(typePiece.CAVALIER, c, ligne, col);
	}
	
	@Override
	protected boolean mouvAutorise()
	{
		// la cavalier se deplace en "L"
		if(Math.abs(this.posY-this.posY0)==1)
			if(Math.abs(this.posX-this.posX0)==2)
				return true;
		
		if(Math.abs(this.posY-this.posY0)==2)
			if(Math.abs(this.posX-this.posX0)==1)
				return true;
		
		return false;
	}

}
