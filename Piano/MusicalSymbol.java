package piano;

import java.awt.Button;
import java.awt.Color;

import piano.formatter.MidiFormatter;
import piano.grafika.CompositionShower;
import piano.grafika.Dirke;
import piano.music.MidiPlayer;

public abstract class MusicalSymbol implements  Cloneable{
    /**Klasa koja predstavlja muzicki simbol**/

    protected Fraction duration;
    /**Postoje samo dva objekta razlomka u sistemu, za cetvrtinu i za osminu**/

    protected Dirke dirke;
    protected MidiPlayer midiPlayer;
    protected String keyOnKeyboard;
	protected String description;
    public MusicalSymbol(Fraction duration){
        this.duration = duration;
    }
public  void setDuration(Fraction duration){
        this.duration = duration;
}
public void setDirke(Dirke dirke) {
	this.dirke = dirke;
}
public void setKeyOnKeyboard() {
	this.keyOnKeyboard = Load.noteToKey.get(description);
}
    @Override 
    protected MusicalSymbol clone() throws CloneNotSupportedException {
        return (MusicalSymbol) super.clone();
    }
	public  int setDescription(Button[][] symbolDescription, int position, int type) {
		
		for(int i =0;i<CompositionShower.numberOfRows;i++) {
			symbolDescription[i][position].setLabel("");
		}
		Color color = (duration == Fraction.QUARTER? Color.GREEN:Color.RED);	
		for(int i =0;i<CompositionShower.numberOfRows;i++) {
			symbolDescription[i][position].setBackground(color);
			//if(color == Color.GREEN)  symbolDescription[i + 1][position].setBackground(color);
		}
		int a =  color==Color.GREEN? 1:0;
		if(a == 1 && position<44) {
			for(int i =0;i<CompositionShower.numberOfRows;i++) {
				symbolDescription[i][position + 1].setLabel("");
				symbolDescription[i][position + 1].setBackground(color);
			}
		   
		}
		return a;
		
	}
	public abstract void pressButton() throws InterruptedException;
	public void setMidiPlayer(MidiPlayer midiPlayer) {
		this.midiPlayer = midiPlayer;
	}
	public String description() {
	  return description;
	}
	@Override
	public abstract boolean equals(Object obj);
	public  Fraction getDuration() {;
	return duration;
	}
	public  String getTxtDescription() {
		return keyOnKeyboard;
	}
	public abstract void putOnMidi(MidiFormatter midiFormatter);
} 
