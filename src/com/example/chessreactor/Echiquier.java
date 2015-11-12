package com.example.chessreactor;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;

import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.graphics.Rect;

import com.example.chessreactor.Piece.typePiece;
import com.example.chessboss.R;
import com.example.chessreactor.Case.couleurCase;
import com.example.chessreactor.Piece.couleurPiece;

public class Echiquier extends SurfaceView  implements SurfaceHolder.Callback{
	
	private static Case[][] plateau = null;
	private static List<Piece> pieces = null;
	
	boolean flag = false;
	int numCoup = 0;
	
	private static int wScreen = 0;
	private static int hScreen = 0;
	private static int rot = 0;
	static int wRect = 0;
	
	static Display display ;
	
	//static Bitmap CaseBlanche;
	//static Bitmap CaseNoire;
	static Bitmap imgPionBlanc;
	static Bitmap imgPionNoir;
	static Bitmap imgTourBlanc;
	static Bitmap imgTourNoir;
	static Bitmap imgCavalierBlanc;
	static Bitmap imgCavalierNoir;
	static Bitmap imgFouBlanc;
	static Bitmap imgFouNoir;
	static Bitmap imgRoiBlanc;
	static Bitmap imgRoiNoir;
	static Bitmap imgReineBlanc;
	static Bitmap imgReineNoir; 
	
	
	DrawingThread mThread;
	private static SurfaceHolder mSurfaceHolder;
	private static Paint mPaint; 
	
	public void Initialise()
	{
		int i = 0;
		int j = 0;
		
		///////////////////////// recuperation orientation ecran /////////////////////////
		
		rot = display.getRotation();
		
		///////////////////////// instanciation des pieces /////////////////////////
		
		for(i = 1;i<=8;i++)
			pieces.add(new Pion(couleurPiece.BLANCHE, 2, i));
		for(i = 1;i<=8;i++)
			pieces.add(new Pion(couleurPiece.NOIRE, 7, i));
		
		pieces.add(new Tour(couleurPiece.BLANCHE, 1, 1));
		pieces.add(new Tour(couleurPiece.BLANCHE, 1, 8));
		pieces.add(new Tour(couleurPiece.NOIRE, 8, 1));
		pieces.add(new Tour(couleurPiece.NOIRE, 8, 8));
		
		pieces.add(new Cavalier(couleurPiece.BLANCHE, 1, 2));
		pieces.add(new Cavalier(couleurPiece.BLANCHE, 1, 7));
		pieces.add(new Cavalier(couleurPiece.NOIRE, 8, 2));
		pieces.add(new Cavalier(couleurPiece.NOIRE, 8, 7));
		
		pieces.add(new Fou(couleurPiece.BLANCHE, 1, 3));
		pieces.add(new Fou(couleurPiece.BLANCHE, 1, 6));
		pieces.add(new Fou(couleurPiece.NOIRE, 8, 3));
		pieces.add(new Fou(couleurPiece.NOIRE, 8, 6));
		
		pieces.add(new Reine(couleurPiece.BLANCHE, 1, 4));
		pieces.add(new Reine(couleurPiece.NOIRE, 8, 4));
		
		pieces.add(new Roi(couleurPiece.BLANCHE, 1, 5));
		pieces.add(new Roi(couleurPiece.NOIRE, 8, 5));
		
		///////////////////////// creation des cases de l'echiquier /////////////////////////
				
		for(i=0;i<8;i++){
			for(j=0;j<8;j++){
				Case.couleurCase couleur;
				
				if(((i+j)%2) == 0)
					couleur = couleurCase.NOIRE;
				else
					couleur = couleurCase.BLANCHE;
				
				plateau[i][j] = new Case(couleur,null);	
			}	
		}
		
		for(Piece p : pieces){			
			plateau[p.posY-1][p.posX-1].setOccupant(p);
		}
		

			
		
		Log.i("initialise", "init finished");
	}
	
	public Echiquier(Context context) {
		
		super(context);
		
		Log.i("Echiquier", "constructeur");

		plateau =  new Case[8][8];
		
		pieces = new ArrayList<Piece>();

		mSurfaceHolder = getHolder();
		mSurfaceHolder.addCallback(this);
		
		mThread = new DrawingThread();

		mPaint = new Paint();
		mPaint.setStyle(Paint.Style.FILL);
		
		Point size = new Point();
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    	display = wm.getDefaultDisplay();
    	display.getSize(size);
    	
    	wScreen = size.x;
    	hScreen = size.y;

    	
		/*CaseBlanche = BitmapFactory.decodeResource(getResources(), R.drawable.case_blanc);
		Bitmap.createScaledBitmap(CaseBlanche, wScreen/8, wScreen/8, true);
		CaseNoire = BitmapFactory.decodeResource(getResources(), R.drawable.case_noire);
		Bitmap.createScaledBitmap(CaseNoire, wScreen/8, wScreen/8, true);*/	
		
		
    	scale_img();

		this.Initialise();
	}
	
	static void dessiner(Canvas pCanvas)
	{		
		
		if(rot == Surface.ROTATION_0)
			wRect = wScreen/8;
		else
			wRect = hScreen/8;
		
		pCanvas.drawColor(Color.GRAY);
		pCanvas.drawColor(Color.rgb(0, 0, 50));
		
		if(plateau != null){
			//for(Case c : plateau){
			for(int i=1;i<=8;i++){
				for(int j=1;j<=8;j++){
				
					// construction du rectangle selon le numero de case
					
					switch(plateau[i-1][j-1].getcCase()){
					case BLANCHE :
						mPaint.setColor(Color.WHITE);
						//pCanvas.drawBitmap(CaseBlanche,null, r, null); 
						break;
					case NOIRE :
						mPaint.setColor(Color.rgb(100,100,100));
						//pCanvas.drawBitmap(CaseNoire,null, r, null);
						break;
					default :
						break;
					}
	
					pCanvas.drawRect(new Rect((i-1)*wRect,(8-j)*wRect,wRect*i,(9-j)*wRect), mPaint);

				}

			}

			
			for(Piece p : pieces){
				
				int col = p.getPosY();
				int ligne = p.getPosX();
				
				Rect r;
				
				if(!p.enDeplacement)
					r = new Rect((col-1)*wRect,(8-ligne)*wRect,wRect*col,(9-ligne)*wRect);
				else
					r = new Rect(col-wRect/2,ligne-wRect/2,col+wRect/2,wRect/2+ligne);
			
				switch(p.tPiece){
				case PION :
					if(p.getcPiece() == couleurPiece.BLANCHE)
						pCanvas.drawBitmap(imgPionBlanc,null, r, null);
					else if (p.getcPiece() == couleurPiece.NOIRE)
						pCanvas.drawBitmap(imgPionNoir,null, r, null);
					break;
				case TOUR :
					if(p.getcPiece() == couleurPiece.BLANCHE)
						pCanvas.drawBitmap(imgTourBlanc,null, r, null);
					else if (p.getcPiece() == couleurPiece.NOIRE)
						pCanvas.drawBitmap(imgTourNoir,null, r, null);
					break;
				case FOU :
					if(p.getcPiece() == couleurPiece.BLANCHE)
						pCanvas.drawBitmap(imgFouBlanc,null, r, null);
					else if (p.getcPiece() == couleurPiece.NOIRE)
						pCanvas.drawBitmap(imgFouNoir,null, r, null);
					break;
				case CAVALIER :
					if(p.getcPiece() == couleurPiece.BLANCHE)
						pCanvas.drawBitmap(imgCavalierBlanc,null, r, null);
					else if (p.getcPiece() == couleurPiece.NOIRE)
						pCanvas.drawBitmap(imgCavalierNoir,null, r, null);
					break;
				case REINE :
					if(p.getcPiece() == couleurPiece.BLANCHE)
						pCanvas.drawBitmap(imgReineBlanc,null, r, null);
					else if (p.getcPiece() == couleurPiece.NOIRE)
						pCanvas.drawBitmap(imgReineNoir,null, r, null);
					break;
				case ROI :
					if(p.getcPiece() == couleurPiece.BLANCHE)
						pCanvas.drawBitmap(imgRoiBlanc,null, r, null);
					else if (p.getcPiece() == couleurPiece.NOIRE)
						pCanvas.drawBitmap(imgRoiNoir,null, r, null);
					break;
					
				default :
					break;
				}
			}
		}

	}
	


	@Override
	public void surfaceChanged(SurfaceHolder pHolder, int pFormat, int pWidth, int pHeight) {
		Log.i("surfaceChanged","surfaceChanged"); 
	}

	@Override
	public void surfaceCreated(SurfaceHolder pHolder) {
		mThread.keepDrawing = true;
		
		if(!flag)
			mThread.start();
		else
		{
			scale_img();
			Initialise();
			
			mThread = new DrawingThread();
			mThread.start();
			
			
		}
		
		flag = true;

		
	    Log.i("surfaceCreated","surfaceCreated"); 
	    
	}


	@Override
	public void surfaceDestroyed(SurfaceHolder pHolder) {
		mThread.keepDrawing = false;
		boolean retry = true;
		while (retry) {
			try {
				mThread.join();
				retry = false;
			} catch (InterruptedException e) {}
		}
		

		//CaseBlanche.recycle();
		//CaseNoire.recycle();
		
		recycle_img();
		
		//mThread.interrupt();
		

		Log.i("surfaceDestroyed","surfaceDestroyed"); 
		
	}




	private static class DrawingThread extends Thread {
		boolean keepDrawing = true;

		@Override
		public void run() {
			Canvas canvas;

			while (keepDrawing) {
				canvas = null;
				
				try {
					canvas = mSurfaceHolder.lockCanvas();
					synchronized (mSurfaceHolder) {
						dessiner(canvas);
					}
				}
				catch(Exception e){
					e.printStackTrace();
				}
				finally {
					if (canvas != null)
						mSurfaceHolder.unlockCanvasAndPost(canvas);
					else{
						Log.e("finally", "canvas == null");
					}
						
				}

				// Pour dessiner à 50 fps
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) { 
					Log.e("InterruptedException on sleep operation",e.getMessage()); 
				}
			}
		}

	}
	
	// attention les indices de plateau vont de 0 a 7
	public static Piece getPiece(int l, int c)
	{
		if(l<8 && c<8 && l>=0 && c>=0)
			return plateau[l][c].getOccupant();
		else
			return null;
	}
	
	public static List<Piece> getPieces() {
		return pieces;
	}

	public Piece getDeplacement()
	{
		for(Piece p : pieces)
		{
			if(p.enDeplacement)
				return p;
		}
		return null;
	}
	
	public void majPos(Piece pf)
	{		
		if(pf == null)
		{
			Log.i("majPos","piece == null");
			return;
		}
		
		// remplit la case de destination
		plateau[pf.posY-1][pf.posX-1].setOccupant(pf);
		// libere la case d'origine
		plateau[pf.posY0-1][pf.posX0-1].setOccupant(null); 
		
		// repositionne la position d'origine sur la destination
		pf.posX0 = pf.posX;
		pf.posY0 = pf.posY;
		
		// alimente l'historique de la piece
		pf.history.add(new int[]{this.numCoup,pf.posY,pf.posX});
	}
	
	// retourne un nombre positif si la piece a ete deplacee (0 sur une case vide, 1 prend une piece adverse, 2 roc, 3 transformation, 4 position d'origine)
	// sinon un nombre negatif (-1 ne doit pas arriver, -2 hors plateau, -3 place occupee, -4 deplacement non autorise pour la piece, -5 decouvre roi)
	public int verifDeplacement(Piece p)
	{
		Piece prise = null;
		
		// normalise la position de la piece
		p.posY = 8-p.posY/wRect;
		p.posX = p.posX/wRect+1;
		
		if((p.posX == p.posX0)&&(p.posY == p.posY0))
			return 4;
		
		// si la piece n'etait pas en deplacement (erreur a priori)
		if(!p.enDeplacement)
			return -1;
		
		// piece reposee hors plateau
		if( (p.posY > 8) || (p.posX > 8) ||(p.posY <= 0) ||(p.posX <= 0))
			return -2;		
		
		if(!p.mouvAutorise())
			return -4;					
		
		// place occupee 
		prise = getPiece(p.posY-1, p.posX-1);
		if(prise != null)
		{
			//par une piece de la meme couleur
			if(prise.cPiece == p.cPiece)
				return -3;
			else
			{
				// place occupee par une piece adverse (le deplacement a ete valide jusque la)
				prise.posX = 0;
				prise.posX0 = 0;
				prise.posY = 0;
				prise.posY0 = 0;
			}
		}

		// deplacement special roque : si le roi se deplace de 2 case et mouvAutorise est true
		if(p.tPiece == typePiece.ROI && Math.abs(p.posX-p.posX0)==2)
		{
			if(this.roque((Roi)p))
				return 2;
			else
				return -4;
		}
		
		majPos(p);
		
		return 0;
	}
	
	
	private boolean roque(Roi p)
	{		
		// on verifie que le roi n'a pas bouge
		if(p.history.size() > 1)
			return false;
		
		// on determine le cote du roque
		if(p.posX-p.posX0 > 0)
		{
			// petit roque
			if(p.cPiece == couleurPiece.BLANCHE)
			{
				// si la tour a bouge on interdit le deplacement
				if(plateau[0][7].occupant.history.size()>1)
					return false;
				
				// blancs la tour h1 viens en f1
				plateau[0][7].occupant.posX = 6;				
				plateau[0][5].setOccupant(plateau[0][7].occupant);
				plateau[0][7].setOccupant(null);
				majPos(plateau[0][5].occupant);
			}
			else
			{
				if(plateau[7][7].occupant.history.size()>1)
					return false;
				
				// noirs la tour h8 viens en f8
				plateau[7][7].occupant.posX = 6;
				plateau[7][5].setOccupant(plateau[7][7].occupant);
				plateau[7][7].setOccupant(null);
				majPos(plateau[7][5].occupant);
			}
		}
		else
		{
			// grand roque
			if(p.cPiece == couleurPiece.BLANCHE)
			{
				if(plateau[0][0].occupant.history.size()>1)
					return false;
				
				// blancs la tour a1 viens en c1
				plateau[0][0].occupant.posX = 4;
				plateau[0][2].setOccupant(plateau[0][0].occupant);
				plateau[0][0].setOccupant(null);
				majPos(plateau[0][2].occupant);
			}
			else
			{
				if(plateau[7][0].occupant.history.size()>1)
					return false;
				
				// noirs la tour a8 viens en c8
				plateau[7][0].occupant.posX = 4;
				plateau[7][2].setOccupant(plateau[7][0].occupant);
				plateau[7][0].setOccupant(null);
				majPos(plateau[7][2].occupant);
			}
		}		
		
		
		
		// on deplace le roi
		plateau[p.posY-1][p.posX-1].setOccupant(p);
		// libere la case d'origine
		plateau[p.posY0-1][p.posX0-1].setOccupant(null); 
		majPos(p);
		
		
		return true;
	}
	
	public void recycle_img()
	{
		imgPionBlanc.recycle();
		imgPionNoir.recycle();
		imgTourBlanc.recycle();
		imgTourNoir.recycle();
		imgCavalierBlanc.recycle();
		imgCavalierNoir.recycle();
		imgFouBlanc.recycle();
		imgFouNoir.recycle();
		imgRoiBlanc.recycle();
		imgRoiNoir.recycle();
		imgReineBlanc.recycle();
		imgReineNoir.recycle(); 
	}
	
	public void scale_img()
	{
		
		imgPionBlanc = BitmapFactory.decodeResource(getResources(), R.drawable.pion_blanc);
		Bitmap.createScaledBitmap(imgPionBlanc, wScreen/8, wScreen/8, true);
		imgPionNoir = BitmapFactory.decodeResource(getResources(), R.drawable.pion_noir);
		Bitmap.createScaledBitmap(imgPionNoir, wScreen/8, wScreen/8, true);		
		imgTourBlanc = BitmapFactory.decodeResource(getResources(), R.drawable.tour_blanc);
		Bitmap.createScaledBitmap(imgTourBlanc, wScreen/8, wScreen/8, true);
		imgTourNoir = BitmapFactory.decodeResource(getResources(), R.drawable.tour_noir);
		Bitmap.createScaledBitmap(imgTourNoir, wScreen/8, wScreen/8, true);		
		imgFouBlanc = BitmapFactory.decodeResource(getResources(), R.drawable.fou_blanc);
		Bitmap.createScaledBitmap(imgFouBlanc, wScreen/8, wScreen/8, true);		
		imgFouNoir = BitmapFactory.decodeResource(getResources(), R.drawable.fou_noir);
		Bitmap.createScaledBitmap(imgFouNoir, wScreen/8, wScreen/8, true);		
		imgCavalierBlanc = BitmapFactory.decodeResource(getResources(), R.drawable.cavalier_blanc);
		Bitmap.createScaledBitmap(imgCavalierBlanc, wScreen/8, wScreen/8, true);		
		imgCavalierNoir = BitmapFactory.decodeResource(getResources(), R.drawable.cavalier_noir);
		Bitmap.createScaledBitmap(imgCavalierNoir, wScreen/8, wScreen/8, true);		
		imgReineBlanc = BitmapFactory.decodeResource(getResources(), R.drawable.reine_blanc);
		Bitmap.createScaledBitmap(imgReineBlanc, wScreen/8, wScreen/8, true);		
		imgReineNoir = BitmapFactory.decodeResource(getResources(), R.drawable.reine_noir);
		Bitmap.createScaledBitmap(imgReineNoir, wScreen/8, wScreen/8, true);		
		imgRoiBlanc = BitmapFactory.decodeResource(getResources(), R.drawable.roi_blanc);
		Bitmap.createScaledBitmap(imgRoiBlanc, wScreen/8, wScreen/8, true);		
		imgRoiNoir = BitmapFactory.decodeResource(getResources(), R.drawable.roi_noir);
		Bitmap.createScaledBitmap(imgRoiNoir, wScreen/8, wScreen/8, true);
		
	}


}
