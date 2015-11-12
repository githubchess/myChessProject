package com.example.chessreactor;

import com.example.chessboss.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


public class MainActivity extends Activity implements View.OnTouchListener, View.OnClickListener{

	Echiquier jeu; 
	
	// pour savoir si le joueur a fait glisser son doigt sur l'ecran
	boolean moveAction = false;
	
	// enregistre l'etat du plateau dans un fichier texte
	private void storeBoard()
	{
		String eol = System.getProperty("line.separator");
		BufferedWriter writer = null;
		String path = null;
		File file = null;
	    String state = Environment.getExternalStorageState();
	    
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
			path = Environment.getExternalStorageDirectory().toString() + "/keep.txt";
	    } else {
			path = this.getApplicationContext().getFilesDir() + "/keep.txt";
	    }

		file = new File(path);
		
		try {
			writer = new BufferedWriter(new FileWriter(file));

			// on sauvegarde les informations d'odre general sur la premiere ligne
			writer.write(jeu.numCoup+"-"+eol);
			
			// pour chaque piece on ecrit son historique
			for(Piece p : Echiquier.getPieces()){
				for(int[] iTab : p.history){
					writer.write(iTab[0]+","+iTab[1]+","+iTab[2]+"-");
				}
				writer.write(eol);
			}			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	// charge l'etat du plateau a partir d'un fichier texte
	private void loadBoard()
	{
		BufferedReader input = null;
		
		String path = null;
		File file = null;
	    String state = Environment.getExternalStorageState();
	    
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
			path = Environment.getExternalStorageDirectory().toString() + "/keep.txt";
	    } else {
			path = this.getApplicationContext().getFilesDir() + "/keep.txt";
	    }

		file = new File(path);

		try {
			input = new BufferedReader(new FileReader(file));
			String line;
			int i, len;
			String str[];		
			List<Piece> list = Echiquier.getPieces();
			
			for(Piece p : list){
				if((line = input.readLine()) != null){
					i = 1;
					str = line.split("-");
					len = str.length;

					if(len > 1){
						while( i < len){
							p.history.add(new int[] {(str[i].charAt(0)-'0'),(str[i].charAt(2)-'0'),(str[i].charAt(4)-'0')});
							i++;
						}
						
						// remettre la piece a sa place d'avant la destruction du contexte
						p.setPosX(p.history.get(p.history.size()-1)[1]);
						p.setPosY(p.history.get(p.history.size()-1)[2]);
						
						jeu.majPos(p);
					}

				}else{
					// todo gerer les erreurs
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		jeu = new Echiquier(this);
		setContentView(jeu);
		
		jeu.setOnTouchListener(this);
		jeu.setOnClickListener(this);
		
		this.loadBoard();
		
	}
	
	//////////////  later  ////////////////////////////
	/*
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}*/
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		Log.i("onResume","onResume called");
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		
		// enregistrement de l'etat de la partie dans un fichier
		this.storeBoard();
		
		super.onDestroy();
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		
		Log.i("onRestoreInstanceState","onRestoreInstanceState called");
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		Log.i("onStart","onStart called");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		Log.i("onPause","onPause called");
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		
		Log.i("onRestart","onRestart called");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i("onStop","onStop called");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// affecter a des controles
		
		Log.i("onClick","clic");
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch(event.getAction())
		{
			// lorsque'le doigt ne touche plus l'ecran
			case MotionEvent.ACTION_UP :
				
				Piece pf = jeu.getDeplacement();
				
				int vDep = 255;
				
				// indique que la piece n'est plus en deplacement et la repose
				if(pf!=null){
					
					// si la piece a bouge
					if(moveAction)
						// verifie la validite du deplacement et normalise la position de la piece
						vDep = jeu.verifDeplacement(pf);
					else
						vDep = -255;
					
					if(vDep < 0)
					{
						// deplacement annule
						pf.posX = pf.posX0;
						pf.posY = pf.posY0;
					}
					else
					{
						// on incremente le compteur de coup
						jeu.numCoup++;
					}
					
					pf.enDeplacement = false;
					
					Log.i("ACTION_UP","X = "+pf.posX+" Y = "+pf.posY);
				}
					
				Log.i("ACTION_UP","verifDeplacement retourne : "+vDep);
				
				return true;
				
			// lorsqu'on touche l'ecran 	
			case MotionEvent.ACTION_DOWN :	
				
				// init du marqueur pour detecter ACTION_MOVE
				moveAction = false;
				
				// coordonnees en pixel
				int X0 = (int)event.getRawX();
				int Y0 = (int)event.getRawY();				
				
				int col0 = X0/Echiquier.wRect;
				int ligne0 = 7 - Y0/Echiquier.wRect;

				Piece p0 = null;
				
				// si il y a une piece la ou on touche on la recupere
				p0 = Echiquier.getPiece(ligne0,col0);
				
				if(p0 == null)
				{
					Log.i("ACTION_DOWN","y a personne ici");
				}
				else
				{
					// on garde la position d'origine de la piece
					p0.posX0 = col0 + 1;
					p0.posY0 = ligne0 + 1;
					
					// la piece est maintenue par le joueur
					p0.enDeplacement = true;
					
					Log.i("ACTION_DOWN","||||||||||||||||||| nouvelle action |||||||||||||||||||");
					Log.i("ACTION_DOWN","X0 = "+p0.posX0+" Y0 = "+p0.posY0);
				}
				
				return true;
				
			// lorsque le doigt se deplace sur l'ecran	
			case MotionEvent.ACTION_MOVE :
				
				// influe sur le traitement de ACTION_UP
				moveAction = true;
				
				// coordonnees en pixel
				int X = (int)event.getRawX();
				int Y = (int)event.getRawY();				
				
				Piece pDep = jeu.getDeplacement();
				
				if(pDep!=null)
				{
					// la piece est collee au doigt de l'utilisateur jusqu'a ce qu'il la lache sur une case permise
					pDep.setPosX(Y);
					pDep.setPosY(X);
				}
				
				break;
				
			default:
				Log.i("onTouch","default action --> Problem !!!!!");
				break;
		}
		return false;
	}


}


