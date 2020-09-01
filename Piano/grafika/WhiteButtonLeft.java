package piano.grafika;

import java.awt.Color;
import java.awt.Graphics;

import piano.Load;

public class WhiteButtonLeft extends PianoButton {

private int xoffset, yoffset;
	protected WhiteButtonLeft(Dirke canvas, int xoffset, int yoffset) {
		super(canvas);
		this.xoffset = xoffset;
		this.yoffset = yoffset;
		buttonColor = Color.WHITE;
	}

	@Override
	public void drawButton() {
		Graphics g = canvas.getGraphics();
		g.setColor(buttonColor);
		g.fillRect(xoffset, yoffset,23 ,170 );
		g.fillRect(xoffset, yoffset + 170,29 ,110 );
		g.fillOval(xoffset, yoffset + 270, 29 ,20);
		if(canvas.showNoteDescription) {
			g.setColor(Color.BLACK);
			g.drawString(noteDescription, xoffset + 5, yoffset + 240);
			}
			if(canvas.showKeyOnKeboard) {
				g.setColor(Color.BLACK);
				g.drawString(keyOnKeboard, xoffset + 10, yoffset + 260);
			}
	}

	@Override
	public boolean checkIfPressed(int offsetx, int offsety) {
		if(offsety <=170 && offsetx >= xoffset && offsetx <= xoffset + 23) {
			buttonColor = Color.RED;
			canvas.midiToPlay = Load.keyToNote.get(Load.noteToKey.get(noteDescription)).getMidi();
			return true;
		}
		if(offsety>=170 && offsetx >= xoffset && offsetx <= xoffset + 29) {
			buttonColor = Color.RED;
			canvas.midiToPlay = Load.keyToNote.get(Load.noteToKey.get(noteDescription)).getMidi();
			return true;
		}
		return false;
	}
	@Override
	public synchronized void setColor() {
		buttonColor = Color.WHITE;
	}
	@Override
	public int getStartX() {
		return xoffset;
	}
	@Override
	public void setRepaint() {
		buttonColor = Color.RED;
	}
}
