package com.example.chessreactor;

public class Pion extends Piece{

	public Pion(couleurPiece c, int ligne, int col) {
		super(typePiece.PION, c, ligne, col);
	}
	
	@Override
	protected boolean mouvAutorise()
	{
		if(Math.abs(this.posX0-this.posY) > 1)
			return false;
				
		// le sens de deplacement depend de la couleur
		if(this.cPiece == couleurPiece.BLANCHE)
		{		
			// les pions avancent droit
			if(Math.abs(this.posX0-this.posY)==0)
			{
				// avancer d'une case en avant est autorise
				if(this.posX-this.posY0 == 1)
					return true;
				// avancer de 2 case est autorise pour le premier deplacement du pion
				if((this.posX-this.posY0 == 2)&&(this.posY0 == 2))
					return true;
			}
			
			// les pions peuvent se decaler d'une colonne
			if(Math.abs(this.posX0-this.posY)==1)
			{
				// s'ils prennent une piece adverse 
				Piece pr = Echiquier.getPiece(this.posX-1, this.posY-1);
				if(pr == null)
				{
					// par la "prise en passant"
					pr = Echiquier.getPiece(this.posX-2, this.posY-1);

					if(pr == null)
						return false;
					else
					{
						// on verifie que la piece adverse est un pion et qu'il vient d'avancer de 2 cases
						if( (pr.tPiece != tPiece.PION) || (pr.history.size() < 2) )
							return false;
						
						int[] p1 = pr.history.get(pr.history.size()-1);
						int[] p2 = pr.history.get(pr.history.size()-2);

						if(p2[0]-p1[0] == 2)
							return true;

					}
				}
				else
				{
					// avancer d'une case en avant est autorise
					if(this.posX-this.posY0 == 1)
						return true;

				}
				
			}
			else
			{						
				// avancer d'une case en avant est autorise
				if(this.posX-this.posY0 == 1)
					return true;
				// avancer de 2 case est autorise pour le premier deplacement du pion
				if((this.posX-this.posY0 == 2)&&(this.posY0 == 2))
					return true;
			}

			
		}
		if(this.cPiece == couleurPiece.NOIRE) 				// todo prise en passant noirs
		{
			if(this.posY0-this.posX == 1)
				return true;
			if((this.posY0-this.posX == 2)&&(this.posY0 == 7))
				return true;
		}
		
		// todo verifier obstacles pr avancer
		
		return false;
	}

}
