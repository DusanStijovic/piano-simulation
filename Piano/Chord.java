package piano;

import java.awt.Button;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.sound.midi.InvalidMidiDataException;

import piano.formatter.MidiFormatter;
import piano.formatter.MidiFormatter.MIDIDURATION;
import piano.grafika.Dirke;
import piano.music.MidiPlayer;


public class Chord extends MusicalSymbol {
private List<Note> notes = new LinkedList<>();

    @Override
public void setMidiPlayer(MidiPlayer midiPlayer) {
	super.setMidiPlayer(midiPlayer);
	for(Note note:notes) {
		note.setMidiPlayer(midiPlayer);
	}
}
	public Chord(Fraction duration) {
        super(duration);
        super.description = "";
    }
    public void addNote(Note note){
        try {
            notes.add(note.clone());
        }catch (CloneNotSupportedException e){}
    }

    @Override
	public void setDirke(Dirke dirke) {
    	super.setDirke(dirke);
    	for(Note note:notes) {
			note.setDirke(dirke);
		}	
    	
	}
	@Override
    public String toString() {
        notes.forEach(s->System.out.print(s + " "));
        return "";
    }
	@Override
	public int setDescription(Button[][] buttons, int position, int type) {
		int a  = super.setDescription(buttons,position, type);
		int noteNumber = 0;
		for(Note note:notes) {
			if(type == 1) {
			buttons[noteNumber++][position].setLabel(note.description());
			}else {
				buttons[noteNumber++][position].setLabel(note.keyOnKeyboard);
			}
			}
		return a;
	}
	
	@Override
	public void setKeyOnKeyboard() {
		super.setKeyOnKeyboard();
		for(Note note:notes) {
			note.setKeyOnKeyboard();
		}
	}
	@Override
	public void pressButton() throws InterruptedException {
		for(Note note:notes) {
			dirke.buttonPressed(note);
		}
		for(Note note:notes)
			midiPlayer.play(note.getMidi());
		Thread.sleep(100/duration.den() * 20);
		for(Note note:notes)
			midiPlayer.release(note.getMidi());
		for(Note note:notes) {
			dirke.buttonReleased(note);
		}
	}
	public int size() {
		return notes.size();
	}
	public Note getNote(int i) {
		return notes.get(i - 1);
	}
	@Override
	public boolean equals(Object obj) {
		 if (this == obj) return true;
	     if (obj == null || getClass() != obj.getClass()) return false;
	     Chord chord = (Chord) obj;
	     if(!duration.equals(chord.duration))return false;
	     if(this.size()!=chord.size()) return false;
	     Set<String> chord1 = new HashSet<>();
	     Set<String> chord2 =  new HashSet<>();
	     for(Note note:notes)
	    	 chord1.add(note.description());
	     for(Note note:chord.notes)
	    	 chord2.add(note.description());
	     return chord1.containsAll(chord2);
	     
	}
	@Override
	public String getTxtDescription() {
		StringBuilder chordDescription = new StringBuilder();
		chordDescription.append("[");
		for(Note note:notes) {
		chordDescription.append(note.getTxtDescription());
			
		}
		chordDescription.append("]");
		return chordDescription.toString();
	}
	@Override
	public void putOnMidi(MidiFormatter midiFormatter) {
		try {
		for(Note note:notes) {
			midiFormatter.playNote(note.getMidi());
		}
		midiFormatter.setTime(midiFormatter.getTime() + (duration==Fraction.QUARTER ? MIDIDURATION.QUARTER.getValue():MIDIDURATION.EIGHT.getValue()));
		for(Note note:notes) {
			midiFormatter.stopNote(note.getMidi());
		}
		}catch(InvalidMidiDataException invalid) {}
	}
	
}
