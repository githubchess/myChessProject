package com.example.chessreactor;

import java.util.ArrayList;
import java.util.List;


public class Piece {
	
	public enum couleurPiece{
		BLANK,
		NOIRE,
		BLANCHE
	}
	
	public enum typePiece 
	{
		BLANK,
		PION,
		CAVALIER,
		FOU,
		TOUR,
		REINE,
		ROI		
	}
	
	typePiece tPiece = typePiece.BLANK;
	couleurPiece cPiece = couleurPiece.BLANK;
	
	// ATTENTION les coordonnees sont en pixel lors du deplacement
	
	// position actuelle
	int posX = 0;
	int posY = 0;
	
	// position d'origine sur un deplacement
	int posX0 = 0;
	int posY0 = 0;
	
	// historique des positions
	List <int[]>  history;
	
	boolean enDeplacement = false;
	boolean capturee = false;
	
	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public typePiece gettPiece() {
		return tPiece;
	}

	public void settPiece(typePiece tPiece) {
		this.tPiece = tPiece;
	}
	
	public couleurPiece getcPiece() {
		return this.cPiece;
	}

	public void setcPiece(couleurPiece cPiece) {
		this.cPiece = cPiece;
	}

	public Piece(typePiece p, couleurPiece c, int ligne, int col)
	{
		this.tPiece = p;
		this.cPiece = c;
		this.posX = ligne;
		this.posY = col;
		this.posX0 = this.posX;
		this.posY0 = this.posY;

		this.history = new ArrayList<int[]>();
		this.history.add(new int[]{0,this.posX,this.posY});
	}

	// todo override pour chaque piece
	protected boolean mouvAutorise()
	{
		return true;
	}
	
	protected boolean mouvRectiligne(int X0, int Xf, int Y0, int Yf)
	{
		// si la position de depart et d'origine sont identiques
		if(X0 == Xf && Y0 == Yf)
			return false;
		
		// si le deplacement n'est pas rectiligne
		if((X0 != Xf) && (Y0 != Yf))
			return false;
		
		// si il y a une piece sur la trajectoire de la piece (quelque soit sa couleur)
		if(X0 == Xf)
		{
			for(int Yi=(Y0<Yf?Y0:Yf)+1; Yi<(Yf>Y0?Yf:Y0); Yi++)
				if(Echiquier.getPiece(Yi-1, X0-1)!=null)
					return false;
		}
		else
		{
			for(int Xi=(X0<Xf?X0:Xf)+1; Xi<(Xf>X0?Xf:X0); Xi++)
				if(Echiquier.getPiece(Y0-1, Xi-1)!=null)
					return false;
		}
				
		return true;
	}
	
	protected boolean mouvDiagonale(int X0, int Xf, int Y0, int Yf)
	{
		// si la position de depart et d'origine sont identiques
		if(X0 == Xf && Y0 == Yf)
			return false;
		
		// si le deplacement n'est pas diagonal
		if(Math.abs(X0-Xf) != Math.abs(Y0-Yf))
			return false;
		
		// si il y a une piece sur la trajectoire de la piece (quelque soit sa couleur)
		for(int Xi=(X0<Xf?X0:Xf)+1; Xi<(Xf>X0?Xf:X0); Xi++)
			for(int Yi=(Y0<Yf?Y0:Yf)+1; Yi<(Yf>Y0?Yf:Y0); Yi++)
				if(Math.abs(X0-Xi) == Math.abs(Y0-Yi))
					if(Echiquier.getPiece(Yi-1, Xi-1)!=null)
						return false;
				
		return true;
	}
	
}
