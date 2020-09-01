

package piano.grafika;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import piano.Chord;
import piano.Fraction;
import piano.Load;
import piano.Note;
import piano.Rest;

public class Dirke extends Canvas {
	protected Octave[] octaves;
	private static final long serialVersionUID = 1L;
	int updateOctave;
	int updateButton;
	protected Piano piano;
	boolean painted = true;
	String[] str = { "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B" };
	protected boolean showNoteDescription = false;
	protected boolean showKeyOnKeboard = false;
	protected Chord chord;
	protected long time = -1;
	protected long timeWhenPressed;
	protected long timeWhenReleased;
	protected int midiToPlay;
	protected int offsetLeft;
	protected KeyListener keyListener;
	protected MouseListener mouseListener;
	protected  enum PlayTime{CHORDDELAY(200), QUATREDURATION(100),QUATREREST(800), EIGHTREST(550);
		private int value;
		PlayTime(int value){
			this.value = value;
		}
		public int getValue() {return value;}
	}

	Dirke(Piano piano, int offsetLeft) {
		this.piano = piano;
		setSize(1260, 300);
		setBackground(Color.BLACK);
		this.offsetLeft = offsetLeft;
		int a = offsetLeft;
		octaves = new Octave[5];
		octaves[0] = (new Octave(this, 0 + a, 0));
		octaves[1] = (new Octave(this, 217 + a, 0));
		octaves[2] = (new Octave(this, 434 + a, 0));
		octaves[3] = (new Octave(this, 651 + a, 0));
		octaves[4] = (new Octave(this, 868 + a, 0));
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 12; j++) {
				octaves[i].getButton(j).setNoteDescriptionSt(str[j] + (i + 2));
			}
		}

		repaint();

		addMouseListener(mouseListener = new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				synchronized (Dirke.this) {
					if (midiToPlay != -1) {
						octaves[updateOctave].setButtonColor(updateButton);
						Note note = Load.keyToNote.get(Load.noteToKey.get(octaves[updateOctave].getButton(updateButton).getDescription()));
						if (note == null)
							return;
						piano.midiPlayer.release(midiToPlay);
						midiToPlay = -1;
						try {
							timeWhenReleased = e.getWhen();
							updateReleasedButton(note);
							time = e.getWhen();
						} catch (CloneNotSupportedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} 
					repaint();
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				synchronized (Dirke.this) {
					timeWhenPressed = e.getWhen();
					pressed(e.getX(), e.getY());
					piano.midiPlayer.play(midiToPlay);
					Note note = Load.keyToNote.get(Load.noteToKey.get(octaves[updateOctave].getButton(updateButton).getDescription()));
						if (note == null)
							return;
					updatePressedButton();
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				synchronized (Dirke.this) {
					if (midiToPlay != -1) {
						octaves[updateOctave].setButtonColor(updateButton);
						Note note = Load.keyToNote.get(Load.noteToKey.get(octaves[updateOctave].getButton(updateButton).getDescription()));
						if (note == null)
							return;
						piano.midiPlayer.release(midiToPlay);
						midiToPlay = -1;
					try {
						timeWhenReleased = e.getWhen();
						updateReleasedButton(note);
						time = e.getWhen();
					} catch (CloneNotSupportedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					}
				}
				repaint();
			}

		});

		addKeyListener(keyListener = new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent arg0) {
				synchronized (Dirke.this) {
					Note note = Load.keyToNote.get(Character.toString(arg0.getKeyChar()));
					if (note == null)
						return;
					timeWhenPressed = arg0.getWhen();
					piano.midiPlayer.play(note.getMidi());
					buttonPressed(note);
					updatePressedButton();
				}
			}

			
			@Override
			public void keyReleased(KeyEvent e) {
				synchronized (Dirke.this) {
					try {
						Note note = Load.keyToNote.get(Character.toString(e.getKeyChar()));
						if (note == null)
							return;
						timeWhenReleased = e.getWhen();
						buttonReleased(note);
						updateReleasedButton(note);
						time = e.getWhen();
					}catch (CloneNotSupportedException cl) {
						
					}
				}
			}

			@Override
			public void keyTyped(KeyEvent r) {
			}

		});

	}
    public void updateReleasedButton(Note note) throws CloneNotSupportedException {
		piano.midiPlayer.release(note.getMidi());
		if (piano.currentSymbol == null && !piano.snimajKompoziciju)
			return;
		if (chord != null) {
			if (timeWhenPressed - time < PlayTime.CHORDDELAY.getValue()) {
				chord.addNote(note.clone());
			} else {
				if(piano.snimajKompoziciju)
				piano.addToNewComposition(chord);
				chord = null;
			}
		}
		if (chord == null) {
			Fraction duration = Fraction.EIGHT;
			if (timeWhenReleased - timeWhenPressed > PlayTime.QUATREDURATION.getValue())
				duration = Fraction.QUARTER;
			note.setDuration(duration);
			if (!piano.snimajKompoziciju) {
				if (!piano.checkIfMove(note)) {
					chord = new Chord(Fraction.QUARTER);
					chord.addNote(note);
				} else {
					chord = null;
				}
			} else {
				chord = new Chord(Fraction.QUARTER);
				chord.addNote(note);
			}
		}
		if (chord != null) {
			if (!piano.snimajKompoziciju && piano.checkIfMove(chord))
				chord = null;
		}
    }
	public void updatePressedButton() {
		if (time != -1) {
			long timePassed = timeWhenPressed - time;
			int numberOfQuarters = (int) (timePassed / PlayTime.QUATREREST.getValue());
			int numberOfEights = (int) (timePassed - numberOfQuarters * PlayTime.QUATREREST.getValue() ) /PlayTime.EIGHTREST.getValue() ;
			Rest quarter = new Rest(Fraction.QUARTER);
			Rest eight = new Rest(Fraction.EIGHT);
			if(piano.snimajKompoziciju && piano.newComposition.getSymbols().size()!=0) {
			try {
				while (numberOfQuarters-- > 0)
					piano.newComposition.addSymbol(quarter.clone());
				while (numberOfEights-- > 0)
					piano.newComposition.addSymbol(eight.clone());
			} catch (CloneNotSupportedException clone) {
			}
			}
		}
	}
    
    
    public void buttonReleased(Note note) {
		int updateOctave = note.getOctave() - 2;
		String buttonDes = note.getDesc();
		int number = 0;
		for (int i = 0; i < 12; i++) {
			if (buttonDes.equals(str[i])) {
				number = i;
				break;
			}
		}
		octaves[updateOctave].setButtonColor(number);
		repaint();
		painted = false;
	}

	public void buttonPressed(Note note) {
		int updateOctave = note.getOctave() - 2;
		String buttonDes = note.getDesc();
		int number = 0;
		for (int i = 0; i < 12; i++) {
			if (buttonDes.equals(str[i])) {
				number = i;
				break;
			}
		}
		int updateButton = number;
		octaves[updateOctave].setRepaint(updateButton);
		repaint();
		painted = false;
	}

	@Override
	public void paint(Graphics g) {
		for (Octave octave : octaves) {
			octave.drawOctave();
		}
	}

	public synchronized void pressed(int x, int y) {
		if(x<offsetLeft || x>getWidth()-offsetLeft) return;
		int octaveNumber = (x - offsetLeft) / Octave.OCTAVE_SIZE;
		updateOctave = octaveNumber;
		octaves[octaveNumber].octavePressed(x, y);
		repaint();
	}

	public synchronized void setUpdateButton(int buttonNumber) {
		updateButton = buttonNumber;
	}
}
