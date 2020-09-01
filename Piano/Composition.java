package piano;

import java.awt.Button;
import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import piano.grafika.CompositionShower;

public class Composition {
private List<MusicalSymbol> symbols = new LinkedList<>();
private int maxNumerOfNotes;

    @Override
    public String toString() {
       symbols.forEach(s->System.out.println(s));
       return "";
    }

    public void addSymbol(MusicalSymbol symbol){
        symbols.add(symbol);

}

	public int setSymbolDescriptionAt(Button[][] symbolDescription, int position, int i, int type) {
		if(position>=symbols.size()) {
			for(int i1 =0;i1<CompositionShower.numberOfRows;i1++) {
				symbolDescription[i1][i].setLabel("");
				symbolDescription[i1][i].setBackground(Color.BLUE);
				
			}
			return 0;
		}
		  return symbols.get(position).setDescription(symbolDescription,i, type);
		
	}

	public List<MusicalSymbol> getSymbols() {
		return symbols;		
	}

	public int getMaxNumerOfNotes() {
		return maxNumerOfNotes;
	}

	public void setMaxNumerOfNotes(int maxNumerOfNotes) {
		this.maxNumerOfNotes = maxNumerOfNotes;
	}
}
