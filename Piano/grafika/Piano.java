 package piano.grafika;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import piano.Chord;
import piano.Composition;
import piano.Fraction;
import piano.Load;
import piano.MusicalSymbol;
import piano.Note;
import piano.Rest;
import piano.exception.PianoExceptions;
import piano.music.MidiPlayer;

public class Piano extends Frame implements Runnable {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
protected Dirke dirke = new Dirke(this, 70);
protected static Composition composition;
protected CompositionShower compositionShower;
protected MidiPlayer midiPlayer = new MidiPlayer();
protected PianoSettings pianoSettings;
protected Thread playCompositionThread;
private boolean privremenoZaustavi, trajnoZaustavi;
protected MusicalSymbol currentSymbol;
protected boolean snimajKompoziciju;
protected Composition newComposition;
protected piano.formatter.Formatter pianoFormatter;
protected Panel settings;
	Piano(){
		super("Piano");
		setBackground(Color.BLACK);
		setSize(1260, 800);
		addWindowListener(new WindowAdapter(
				) {
					@Override
					public void windowClosing(WindowEvent e) {
						dispose();
					}
					
		});
     addComponentListener(new ComponentAdapter() {

		@Override
		public void componentResized(ComponentEvent e) {
			if(dirke!=null)
		    Piano.this.remove(dirke);
			dirke = new Dirke(Piano.this,(Piano.this.dirke.getWidth() - 1085)/2);
			Piano.this.add(dirke,BorderLayout.SOUTH);
			Piano.this.validate();
			for(int i = 0;i<5;i++) {
				for(int j = 0;j<12;j++) {
					Piano.this.dirke.octaves[i].getButton(j).setKeyOnKeyboard(Load.noteToKey.get(Piano.this.dirke.octaves[i].getButton(j).getDescription()));
				}
			}
			dirke.showKeyOnKeboard = pianoSettings.prikaziSlova.getState();
			dirke.showNoteDescription = pianoSettings.prikaziNote.getState();
			dirke.repaint();
		}
     
     });
     Button restartujKompoziciju = new Button();
     restartujKompoziciju.setBackground(Color.GREEN);
     restartujKompoziciju.setForeground(Color.RED);
     restartujKompoziciju.setLabel("Restartuj prikaz kompozicije");
     restartujKompoziciju.addActionListener(pressed->{
    	 try {
    		 if(composition == null) throw new PianoExceptions("Kompozicija nije ucitana");
    	     if(compositionShower.offset == 0) throw new PianoExceptions("Vec se nalazite na pocetku kompozicije!");
    	 synchronized (Piano.this) {
			if(playCompositionThread!=null) throw new PianoExceptions("Ne mozete restartovati prikaz kompozcije u sred pustanja kompozicije");
		}
    	 compositionShower.offset = 0;
    	currentSymbol = compositionShower.setComposition(composition);
    	
    	 }catch (PianoExceptions e) {
			// TODO: handle exception
		}
    	 dirke.requestFocus();
    	 });
     restartujKompoziciju.setSize(1260, 40);
     add(restartujKompoziciju, BorderLayout.NORTH);
		add(dirke, BorderLayout.SOUTH);
		settings = new Panel(new GridLayout(2, 1));
		settings.add(compositionShower = new CompositionShower(composition, 10),0);
		Panel something = new Panel(new BorderLayout());
		something.add(pianoSettings = new PianoSettings(this),BorderLayout.CENTER);
		Canvas traka = new Canvas() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void paint(Graphics g) {
				g.setColor(Color.BLACK);
				int sizeOfOne = (Piano.this.getWidth() - 28)/45;
				int offset = 2 * sizeOfOne; 
				for(int i = 0;i<100;i++) {
				g.drawLine(14 + offset, 0, 14 + offset, 40);
				offset+= 2 * sizeOfOne;
				}
			}
			
		};
		traka.setSize(1260,30);
		traka.setBackground(Color.WHITE);
		traka.repaint();
		something.add(traka, BorderLayout.NORTH);
		settings.add(something,1);
		add(settings, BorderLayout.CENTER);
	    setVisible(true);
	}
	
	
	public static void main(String [] argv) {
		new Piano();
	}


	@Override
	public void run() {
		try {
		int i = 0;
			List<MusicalSymbol> symbol = composition.getSymbols();
		while(true) {
			synchronized(this) {
				while(privremenoZaustavi){
			        checkIfMove(new Rest(Fraction.QUARTER));
					wait();
				}
			}
			if(trajnoZaustavi) break;
			if(i>=symbol.size()) break;
			MusicalSymbol help = symbol.get(i++);
			help.setDirke(dirke);
			help.setMidiPlayer(midiPlayer);			
			help.pressButton();
			currentSymbol = compositionShower.moveToNextSymbol();
			if(trajnoZaustavi) break;
		}
		
	pianoSettings.choices[1].setLabel("Pusti kompoziciju");
	pianoSettings.choices[4].setLabel("Privremeno zaustavi");
	}catch (InterruptedException e) {
		// TODO: handle exception
	}
	}
public void privremenoZaustavi() {
	privremenoZaustavi = true;
}
public void pokreniZaustavljeno() {
	synchronized (this) {
	privremenoZaustavi = false;
	notifyAll();
	}
	}
	public void showComposition() {
		currentSymbol = compositionShower.setComposition(composition);
	}


	public void playComposition() {
		trajnoZaustavi = false;
		playCompositionThread =  new Thread(this);
		playCompositionThread.start();
		
	}


	public void stopComposition() {
		synchronized (playCompositionThread) {
			if(playCompositionThread!=null) {
			synchronized (this) {
				privremenoZaustavi = false;
				trajnoZaustavi = true;
				notifyAll();
			}
				try {
					playCompositionThread.join();
					playCompositionThread = null;
					compositionShower.offset = 0;
				currentSymbol = compositionShower.setComposition(composition);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}


	public boolean checkIfMove(MusicalSymbol symbol) {
		if(symbol.equals(currentSymbol)) {
			currentSymbol = compositionShower.moveToNextSymbol();
			MusicalSymbol rest = new Rest(Fraction.QUARTER);
			checkIfMove(rest);
		    return true;
		}
		
		return false;	
	}
public void addtoNewComposition(Rest rest) {
	newComposition.addSymbol(rest);
}
	public void addToNewComposition(Chord chord) {
		if(chord == null) return;
		if(chord.size()==1) {
			newComposition.addSymbol(chord.getNote(1));
		}else {
			newComposition.addSymbol(chord);
		}
		
	
}
	public void addtoNewComposition(Note note) {
		newComposition.addSymbol(note);
	}
}
