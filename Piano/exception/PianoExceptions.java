package piano.exception;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Label;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class PianoExceptions extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PianoExceptions(String exception) {
		Dialog greska = new Dialog((Window)null);
		greska.setSize(910, 100);
		greska.setModal(true);
		Label label = new Label(exception);
		label.setSize(910, 100);
		greska.add(label, BorderLayout.CENTER);
		greska.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				greska.dispose();
			}
			
		});
		greska.setVisible(true);
		
	}
	
}
