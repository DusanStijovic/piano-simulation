package piano.grafika;

public class Octave {
	private Dirke canvas;
	private int xoffset, yoffset;
	private PianoButton[] buttons;
	public static int OCTAVE_SIZE = 217;
public Octave(Dirke canvas, int xoffset, int yoffset) {
	this.canvas = canvas;
	this.xoffset = xoffset;
	this.yoffset = yoffset;
	addButtons();
	canvas.repaint();
}
private void addButtons() {
	buttons = new PianoButton[12];
	buttons[0] = (new WhiteButtonLeft(canvas, xoffset, yoffset));
	buttons[1] = (new BlackButton(canvas, xoffset += 23, yoffset));
	buttons[2] = (new WhiteButtonBoth(canvas, xoffset += 8, yoffset));
	buttons[3] = (new BlackButton(canvas, xoffset += 23, yoffset));
	buttons[4] = (new WhiteButtonRight(canvas, xoffset+=8, yoffset));
	buttons[5] = (new WhiteButtonLeft(canvas, xoffset +=31, yoffset));
	buttons[6] = (new BlackButton(canvas, xoffset +=23, yoffset));
	buttons[7] = (new WhiteButtonBoth(canvas, xoffset +=8, yoffset));
	buttons[8] = (new BlackButton(canvas, xoffset +=23, yoffset));
	buttons[9] = (new WhiteButtonBoth(canvas, xoffset +=8, yoffset));
	buttons[10] = (new BlackButton(canvas, xoffset +=23, yoffset));
	buttons[11] = (new WhiteButtonRight(canvas, xoffset +=8, yoffset));
}

public void drawOctave() {
	for(int i = 0;i<12;i++)
		buttons[i].drawButton();
}
public void octavePressed(int offsetx, int offsety) {
	for(int buttonNumber =0;buttonNumber<12;buttonNumber++) {
		if (buttons[buttonNumber].checkIfPressed(offsetx, offsety)) {
			canvas.setUpdateButton(buttonNumber);
			break;
		}
	}
}
public void drawButton(int updateButton) {
	buttons[updateButton].drawButton();
	
}
public void setButtonColor(int updateButton) {
	buttons[updateButton].setColor();
	
}
public int getStartX(int number) {
	return buttons[number].getStartX();
}
public void setRepaint(int number) {
	buttons[number].setRepaint();
}
public PianoButton getButton(int j) {
	return buttons[j];
}
}
