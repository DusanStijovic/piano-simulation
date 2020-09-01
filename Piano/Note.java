package piano;

import java.awt.Button;

import javax.sound.midi.InvalidMidiDataException;

import piano.formatter.MidiFormatter;
import piano.formatter.MidiFormatter.MIDIDURATION;


public class Note extends  MusicalSymbol implements Cloneable{

private String pitch;
private int octave;
private int midiNumber;
private boolean isSharp;
    public Note(Fraction duration, String pitch, int octave,boolean isSharp,  int midiNumber) {
        super(duration);
        this.pitch = pitch;
        this.octave = octave;
        this.midiNumber = midiNumber;
        this.isSharp = isSharp;
        super.description = description();
    }

    @Override
	public Note clone() throws CloneNotSupportedException {
        return (Note) super.clone();
    }

    public static  Note parseNote(String noteInfo){
        String [] data = noteInfo.split(",");
        int octave = Character.getNumericValue(data[1].charAt(data[1].length() - 1));
        int midi = Integer.parseInt(data[2]);
        String pitch = Character.toString(data[1].charAt(0));
        boolean sharp = (data[1].length() == 3 ? true:false);
        return new Note(Fraction.QUARTER, pitch, octave, sharp, midi);

    }

    @Override
    public String toString() {
        return "Note{" +
                "pitch='" + pitch + '\'' +
                ", octave=" + octave +
                ", midiNumber=" + midiNumber +
                ", isSharp=" + isSharp +
                '}';
    }

	public int getOctave() {
		return octave;
	}

	public String getDesc() {
		return pitch + (isSharp == true? "#":"");
	}

	@Override
	public int setDescription(Button[][] buttons, int position, int type) {
		int a = super.setDescription(buttons, position, type);
		int i = buttons.length/2;
		if(type == 1) {
			buttons[i][position].setLabel(pitch + (isSharp?"#":"") + octave);
		} else {
			buttons[i][position].setLabel(keyOnKeyboard);
		}
		return a;
	}

	public String description() {
		return pitch + (isSharp?"#":"") +  octave;
	}

	@Override
	public void pressButton() throws InterruptedException {	
		dirke.buttonPressed(this);
		midiPlayer.playNote(midiNumber,100/duration.den() *20);
		dirke.buttonReleased(this);
	}

	public int getMidi() {
		return midiNumber;
	}

	@Override
	public boolean equals(Object obj) {
	    if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        if(description.equals(((Note) obj).description) && duration.equals(((Note) obj).duration)) 
        	return true;
        return false;
	}

	@Override
	public void putOnMidi(MidiFormatter midiFormatter) {
		try {
			midiFormatter.playNote(midiNumber);
			midiFormatter.setTime(midiFormatter.getTime() + (duration==Fraction.QUARTER ? MIDIDURATION.QUARTER.getValue():MIDIDURATION.EIGHT.getValue()));
			midiFormatter.stopNote(midiNumber);
		} catch (InvalidMidiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
}
