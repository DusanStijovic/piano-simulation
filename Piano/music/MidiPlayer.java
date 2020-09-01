package piano.music;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

public class MidiPlayer {
private static final int DEFAULT_INSTRUMENT = 1;
private MidiChannel channel;
public MidiPlayer() {
	this(DEFAULT_INSTRUMENT);
}
public MidiPlayer(int instrument) {
	try {
		channel = getChannel(instrument);
	} catch (MidiUnavailableException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

private static MidiChannel getChannel(int instrument) throws MidiUnavailableException{
	Synthesizer synthesizer = MidiSystem.getSynthesizer();
	synthesizer.open();
	return synthesizer.getChannels()[instrument];
}
public void playNote(final int midiNumber, final long length) throws InterruptedException {
	play(midiNumber);
	Thread.sleep(length);
	release(midiNumber);
}
public void release(int midiNumber) {
	channel.noteOff(midiNumber, 50);
}
public void play(int midiNumber) {
	channel.noteOn(midiNumber, 50);
}
}
