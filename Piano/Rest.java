package piano;

import java.awt.Button;

import piano.formatter.MidiFormatter;
import piano.formatter.MidiFormatter.MIDIDURATION;

public class Rest extends  MusicalSymbol implements Cloneable {
    public Rest(Fraction duration) {
        super(duration);
        super.description = "";
    }

    @Override
    public Rest clone() throws CloneNotSupportedException {
        return (Rest) super.clone();
    }

    @Override
    public String toString() {
        System.out.println(duration == Fraction.QUARTER ? " | " : "-");
        return  "";
    }

	@Override
	public int setDescription(Button[][] buttons, int position, int type) {
		return super.setDescription(buttons, position, type);
	}

	@Override
	public void pressButton() throws InterruptedException {
		
		Thread.sleep(100/duration.den() * 20);
	}

	@Override
	public boolean equals(Object obj) {
		 if (this == obj) return true;
	     if (obj == null || getClass() != obj.getClass()) return false;
	     try {
			Thread.sleep(100/duration.den() * 10);
		} catch (InterruptedException e) {}
	     return true;
	}

	@Override
	public String getTxtDescription() {
		return duration==Fraction.QUARTER?" | ":" ";
	}

	@Override
	public void putOnMidi(MidiFormatter midiFormatter) {
		midiFormatter.setTime(midiFormatter.getTime() + (duration==Fraction.QUARTER ? MIDIDURATION.QUARTER.getValue():MIDIDURATION.EIGHT.getValue()));
		
	}

	

}
