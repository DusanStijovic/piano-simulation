package piano.grafika;

import java.awt.Button;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Panel;

import piano.Composition;
import piano.MusicalSymbol;


public class CompositionShower extends Panel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Composition composition;
	private Button[][]symbolDescription;
	public static int numberOfRows;
	protected int offset = 0;
	protected int type = 1;
	private int numberOfCol = 45;
	public CompositionShower(Composition composition, int numberOfRows) {
		setLayout(new GridLayout(numberOfRows,numberOfCol ,0,0));
		this.composition = composition;
		CompositionShower.numberOfRows = numberOfRows;
		symbolDescription = new Button[numberOfRows][numberOfCol];
		for(int i = 0;i<numberOfRows;i++) {
			for(int j = 0;j<numberOfCol;j++) {
				symbolDescription[i][j] = new Button();
			    symbolDescription[i][j].setBackground(Color.BLUE);	
			    symbolDescription[i][j].setForeground(Color.BLACK);
			    symbolDescription[i][j].setFocusable(false);
			    add(symbolDescription[i][j]);
			}
		}
	}
	public void setType(int type ) {this.type = type;}
	public MusicalSymbol setComposition(Composition composition) {
		this.composition  = composition;
		int number = 0;
		for(int i = 0;i<numberOfRows;i++) {
			for(int j = 0;j<numberOfCol;j++) {
			    symbolDescription[i][j].setForeground(Color.BLACK);
			    symbolDescription[i][j].setFocusable(false);
			    symbolDescription[i][j].setLabel("");
			}
		}
	 for(int i = 0;i<numberOfCol;i++)
			 i+=this.composition.setSymbolDescriptionAt(symbolDescription,number++ + offset,i, type);
	return this.composition.getSymbols().get(offset);
	 
	 
	}
	public MusicalSymbol moveToNextSymbol() {
		for(int i = 0;i<numberOfRows;i++) {
			for(int j = 0;j<numberOfCol;j++) {
			   // symbolDescription[i][j].setBackground(Color.BLUE);	
			    symbolDescription[i][j].setForeground(Color.BLACK);
			    symbolDescription[i][j].setFocusable(false);
			    symbolDescription[i][j].setLabel("");
			}
		}
		offset++;
		if(offset>=composition.getSymbols().size())
		     offset = 0;
		int number = 0;
		 for(int i = 0;i<numberOfCol;i++)
			 i+= composition.setSymbolDescriptionAt(symbolDescription, number++ + offset, i, type);
		 return this.composition.getSymbols().get(offset);
	}
}
