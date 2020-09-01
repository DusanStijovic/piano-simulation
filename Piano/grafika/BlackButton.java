package piano.grafika;

import java.awt.Color;
import java.awt.Graphics;

import piano.Load;

public class BlackButton extends PianoButton {

	private int xoffset, yoffset;
		protected BlackButton(Dirke canvas, int xoffset, int yoffset) {
			super(canvas);
			this.xoffset = xoffset;
			this.yoffset = yoffset;
			buttonColor = Color.BLACK;
		}

		@Override
		public void drawButton() {
			Graphics g = canvas.getGraphics();
			g.setColor(buttonColor);
			g.fillRect(xoffset, yoffset,14,170 );
			g.setColor(Color.BLACK);
			g.fillRect(xoffset + 6, yoffset + 170,2,130);
				if(canvas.showKeyOnKeboard) {
					g.setColor(Color.WHITE);
					g.drawString(keyOnKeboard, xoffset + 3, yoffset + 150);
				}
		}

		@Override
		public boolean checkIfPressed(int offsetx, int offsety) {
			if(offsety>170) return false;
			if(offsetx>=xoffset && offsetx<=xoffset + 14) {
				buttonColor = Color.RED;
				canvas.midiToPlay = Load.keyToNote.get(Load.noteToKey.get(noteDescription)).getMidi();
				return true;
			}
			
			return false;
		}

		@Override
		public synchronized void setColor() {
			buttonColor = Color.BLACK;
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
