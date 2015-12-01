package com.example.chessreactor;

public class Pion extends Piece{

	public Pion(couleurPiece c, int ligne, int col) {
		super(typePiece.PION, c, ligne, col);
	}
	
	@Override
	protected boolean mouvAutorise()
	{
		// deplacement lateral de plus d'une case interdit de toutes les facons
		if(Math.abs(this.posY0-this.posY) > 1)
			return false;
		
		// les pions avancent droit
		if(Math.abs(this.posY0-this.posY)==0)
		{
			if(mouvAvance())
				return true;
		}
		
		// les pions peuvent se decaler d'une colonne
		if(Math.abs(this.posY0-this.posY)==1)
		{
			if(mouvAttaque())
				return true;
		}

		return false;
	}
	
	
	public boolean mouvAvance()
	{			
		// si il y a un obstacle devant le pion le deplacement est interdit
		if(Echiquier.getPiece(this.posX-1, this.posY-1)!=null)
			return false;

		// le sens de deplacement depend de la couleur
		if(this.cPiece == couleurPiece.BLANCHE)
		{
			// avancer d'une case en avant est autorise
			if(this.posX-this.posX0 == 1)
				return true;
			
			// avancer de 2 case est autorise pour le premier deplacement du pion
			if((this.posX-this.posX0 == 2)&&(this.posX0 == 2))
				return true;
		}
		else
		{
			// avancer d'une case en avant est autorise
			if(this.posX0-this.posX == 1)
				return true;
			
			// avancer de 2 case est autorise pour le premier deplacement du pion
			if((this.posX0-this.posX == 2)&&(this.posX0 == 7))
				return true;
		}
		
		return false;
	}
	
	public boolean mouvAttaque()
	{
		int offset = 0;
		
		if(this.cPiece == couleurPiece.BLANCHE)
			offset = -1;
		else
			offset = 1;
		
		// s'ils prennent une piece adverse 
		Piece pr = Echiquier.getPiece(this.posX-1, this.posY-1);
		
		// par la "prise en passant"
		if(pr == null)
		{
			// il y a une piece sur la case derriere le pion
			pr = Echiquier.getPiece(this.posX-1 + offset, this.posY-1);

			// on verifie que la piece adverse est un pion et qu'il vient d'avancer de 2 cases
			if(pr == null)
				return false;
			else
			{
				// si ce n'est pas un pion ou qu'il n'a jamais bouge ou qu'il a deja bouge plus d'une fois
				if((pr.tPiece != typePiece.PION) || (pr.history.size() != 2))
					return false;

				int[] p1 = pr.history.get(pr.history.size()-1);
				int[] p2 = pr.history.get(pr.history.size()-2);
				
				// si son deplacement n'a pas eu lieu au coup precedent
				if((p1[0]+1) != Echiquier.getNumCoup())
					return false;

				if(Math.abs(p2[1]-p1[1]) == 2)
				{
					Echiquier.capturePiece(pr, false);
					return true;
				}
			}
		}
		else
		{
			// prise "classique" en diagonale
			// le sens de deplacement depend de la couleur
			if(this.cPiece == couleurPiece.BLANCHE)
			{
				if(this.posX-this.posX0 == 1)
					return true;
			}
			else
			{
				if(this.posX0-this.posX == 1)
					return true;
			}	
		}
		
		return false;
	}

	// indique si un pion controle une case 
	public boolean mouvControle(int ligne, int col)
	{
		if(this.cPiece == couleurPiece.BLANCHE)
		{
			if(ligne == this.posX+1)
				if(Math.abs(col-this.posY) == 1 )
					return true;
		}
		else
		{
			if(ligne == this.posX-1)
				if(Math.abs(col-this.posY) == 1 )
					return true;
		}
		
		return false;
	}
}
