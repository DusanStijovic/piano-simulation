package piano.formatter;

import java.io.File;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;
import javax.sound.midi.Track;

import piano.Composition;
import piano.MusicalSymbol;
import piano.formatter.Formatter;

public class MidiFormatter extends Formatter {

	private Sequence sequence;
	private Track track;
	private long time;

	public enum MIDIDURATION {
		QUARTER(120), EIGHT(60);
		private int value;

		MIDIDURATION(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	private void createMidiSequance() throws InvalidMidiDataException {
		sequence = new Sequence(Sequence.PPQ, 24);
	}

	private void createMidiTrack() {
		track = sequence.createTrack();
	}

	private void createMidiSoundSet() throws InvalidMidiDataException {
		byte[] b = { (byte) 0xF0, 0x7E, 0x7F, 0x09, 0x01, (byte) 0xF7 };
		SysexMessage sysexMessage = new SysexMessage();
		sysexMessage.setMessage(b, 6);
		MidiEvent midiEvent = new MidiEvent(sysexMessage, (long) 0);
		track.add(midiEvent);
	}

	private void setTempo() throws InvalidMidiDataException {
		MetaMessage metaMessage = new MetaMessage();
		byte[] bt = { 0x02, (byte) 0x00, 0x00 };
		metaMessage.setMessage(0x51, bt, 3);
		MidiEvent midiEvent = new MidiEvent(metaMessage, (long) 0);
		track.add(midiEvent);

	}

	public void setTrackName() throws InvalidMidiDataException {
		MetaMessage metaMessage = new MetaMessage();
		String TrackName = new String("midifile track");
		metaMessage.setMessage(0x03, TrackName.getBytes(), TrackName.length());
		MidiEvent midiEvent = new MidiEvent(metaMessage, (long) 0);
		track.add(midiEvent);

	}

	public void setOmniOn() throws InvalidMidiDataException {
		ShortMessage shortMessage = new ShortMessage();
		shortMessage.setMessage(0xB0, 0x7D, 0x00);
		MidiEvent midiEvent = new MidiEvent(shortMessage, (long) 0);
		track.add(midiEvent);

	}

	public void setPolyOn() throws InvalidMidiDataException {
		ShortMessage shortMessage = new ShortMessage();
		shortMessage.setMessage(0xB0, 0x7F, 0x00);
		MidiEvent midiEvent = new MidiEvent(shortMessage, (long) 0);
		track.add(midiEvent);

	}

	public void setInstumentToPiano() throws InvalidMidiDataException {
		ShortMessage shortMessage = new ShortMessage();
		shortMessage.setMessage(0xC0, 0x00, 0x00);
		MidiEvent midiEvent = new MidiEvent(shortMessage, (long) 0);
		track.add(midiEvent);

	}

	public void playNote(int midiNumber) throws InvalidMidiDataException {
		ShortMessage shortMessage = new ShortMessage();
		shortMessage.setMessage(0x90, midiNumber, 0x60);
		MidiEvent midiEvent = new MidiEvent(shortMessage, time);
		track.add(midiEvent);

	}

	public void stopNote(int midiNumber) throws InvalidMidiDataException {
		ShortMessage shortMessage = new ShortMessage();
		shortMessage.setMessage(0x80, midiNumber, 0x40);
		MidiEvent midiEvent = new MidiEvent(shortMessage, time);
		track.add(midiEvent);

	}

	public void setEndOfTrack(long time) throws InvalidMidiDataException {
		MetaMessage metaMessage = new MetaMessage();
		byte[] bet = {};
		metaMessage.setMessage(0x2F, bet, 0);
		MidiEvent midiEvent = new MidiEvent(metaMessage, time + 19);
		track.add(midiEvent);
	}

	@Override
	public void exportComposition(String fileName) throws IOException {
		try {
			createMidiSequance();
			createMidiTrack();
			createMidiSoundSet();
			setTempo();
			setTrackName();
			setOmniOn();
			setPolyOn();
			setInstumentToPiano();
			putCompositionOnTrack();
			setEndOfTrack(time);
			File midiOutput = new File(fileName);
			MidiSystem.write(sequence, 1, midiOutput);
		} catch (InvalidMidiDataException e) {
		}
	}

	private void putCompositionOnTrack() {
		for (MusicalSymbol symbol : composition.getSymbols()) {
			symbol.putOnMidi(this);
		}
	}

	public MidiFormatter(Composition composition) {
		super(composition);
	}

	public long getTime() {
		return time;
	}

	public void setTime(long l) {
		time = l;
	}

}
