package piano.formatter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import piano.Composition;
import piano.Fraction;
import piano.MusicalSymbol;
import piano.Rest;

public class TxtFormatter extends Formatter {
	@Override
	public void exportComposition(String fileName) throws IOException {
		   for(MusicalSymbol symbol:composition.getSymbols()) {
		    	symbol.setKeyOnKeyboard();
		    }
		   File compositionTxt = new File(fileName);
		   FileWriter writeCompositionToTxt = new FileWriter(compositionTxt);
		   StringBuilder compostionFormat = new StringBuilder();
		   boolean startEights = false;
		   String eights = "";
		   for(MusicalSymbol symbol:composition.getSymbols()) {
			   if(symbol.getDuration() == Fraction.EIGHT && symbol.getClass()!= Rest.class) {
				 if(!startEights) {
				   startEights = true;
				   eights+= "[" + symbol.getTxtDescription();
				 } else {
					 eights += " " + symbol.getTxtDescription();
				 }
			}else {
				if(startEights) {
					compostionFormat.append(eights + "]");
					startEights = false;
					eights = "";
				}
				compostionFormat.append(symbol.getTxtDescription());
			}
			   
			   
		   }
	  writeCompositionToTxt.write(compostionFormat.toString());
	  writeCompositionToTxt.close();
	}
	

	public TxtFormatter(Composition composition) {
		super(composition);
	}

}
