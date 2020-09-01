package piano.grafika;

import java.awt.Color;

public abstract class PianoButton  {
protected Dirke canvas;
protected Color buttonColor;
protected String noteDescription = "C#2";
protected String keyOnKeboard = "a";
protected PianoButton(Dirke canvas) {
	this.canvas = canvas;	
	}

public abstract void  drawButton();
public void setColor(Color newColor) {
	buttonColor = newColor;
	
}
public abstract boolean checkIfPressed(int offsetx, int offsety);
public abstract void setColor();
public abstract int getStartX();
public abstract void setRepaint();
public void setNoteDescriptionSt(String noteDescription) {
	this.noteDescription = noteDescription;
};

public void setKeyOnKeyboard(String keyOnKeyboard) {
	this.keyOnKeboard = keyOnKeyboard;
}

public String getDescription() {
	return noteDescription;
	// TODO Auto-generated method stub
}
}
