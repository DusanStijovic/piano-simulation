package piano;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import piano.exception.PianoExceptions;

public class Load {
	private boolean restPossible = false;
	private static Composition composition;
	public static Map<String, Note> keyToNote;
	public static Map<String, String> noteToKey;

	public Map<String, Note> laodKeyToNoteMap(String fileName) throws IOException {
		Map<String, Note> keyToNote = new HashMap<String, Note>();
		Map<String, String> noteToKey = new HashMap<>();
		File keyToNoteFile = new File(fileName);
		BufferedReader readAllLines = new BufferedReader(new FileReader(keyToNoteFile));
		Stream<String> linesOfData = readAllLines.lines();
		linesOfData.forEach(line -> {
			String s = line.split(",")[0];
			Note note = Note.parseNote(line);
			keyToNote.put(s, note);
			noteToKey.put(note.description(), s);
		});
		Load.keyToNote = keyToNote;
		Load.noteToKey = noteToKey;
		readAllLines.close();
		return keyToNote;

	}

	public void processSymbol(String toProcess) throws PianoExceptions {
		try {
			if (toProcess == null)
				return;
			if (toProcess.equals("|")) {
				composition.addSymbol(new Rest(Fraction.QUARTER));
				return;
			}
			if (toProcess.equals("_")) {
				composition.addSymbol(new Rest(Fraction.EIGHT));
				return;
			}
			if (toProcess.charAt(0) != '[') {
				for (char c : toProcess.toCharArray()) {
					Note note = keyToNote.get(Character.toString(c));
					if (note == null)
						throw new PianoExceptions(
								"Ulazni fajl sadrzi karaktere koji se ne nalaze u konfiguraciji klavira ili nije u dobrom fomrmatu.Greska: "
										+ Character.toString(c));
					note = note.clone();
					note.setDuration(Fraction.QUARTER);
					composition.addSymbol(note);
				}
				return;
			}

			if (toProcess.contains(" ") || toProcess.length() == 3) {
				for (int i = 1; i < toProcess.length() - 1; i += 2) {
					Note note = keyToNote.get(Character.toString(toProcess.charAt(i)));
					if (note == null)
						throw new PianoExceptions(
								"Ulazni fajl sadrzi karaktere koji se ne nalaze u konfiguraciji klavira ili nije u dobrom fomrmatu.Greska: "
										+ Character.toString(toProcess.charAt(i)));
				    note = note.clone();
					note.setDuration(Fraction.EIGHT);
					composition.addSymbol(note);
				}
				return;
			}
			Chord chord = new Chord(Fraction.QUARTER);
			for (int i = 1; i < toProcess.length() - 1; i++) {
				Note note = keyToNote.get(Character.toString(toProcess.charAt(i)));
				if (note == null)
					throw new PianoExceptions(
							"Ulazni fajl sadrzi karaktere koji se ne nalaze u konfiguraciji klavira ili nije u dobrom fomrmatu.Greska: "
									+ Character.toString(toProcess.charAt(i)));
				note = note.clone();
				note.setDuration(Fraction.QUARTER);
				chord.addNote(note);
			}
			if (chord.size() > composition.getMaxNumerOfNotes())
				composition.setMaxNumerOfNotes(chord.size());
			composition.addSymbol(chord);

		} catch (CloneNotSupportedException e) {
		}
	}

	public void processBrackets(String toProcess) throws PianoExceptions {
		if (toProcess == null)
			return;
		restPossible = false;
		Pattern pattern = Pattern.compile("([^ ]+)?( )?");
		Matcher match = pattern.matcher(toProcess);
		while (match.find()) {
			if (match.group(1) != null) {
				if (!match.group(1).equals("|") && restPossible)
					processSymbol("_");
				processSymbol(match.group(1));
				restPossible = false;
			}
			if (match.group(2) != null && match.group(2).equals(" ")
					&& (match.group(1) == null || !match.group(1).equals("|")))
				restPossible = true;
		}
	}

	public Composition loadComposition(String fileName) throws IOException {
		composition = new Composition();
		composition.setMaxNumerOfNotes(1);
		File symbolsFile = new File(fileName);
		BufferedReader readAllLines = new BufferedReader(new FileReader(symbolsFile));
		Stream<String> linesOfData = readAllLines.lines();
		linesOfData.forEach(line -> {
			if(composition == null) return;
			Pattern pattern = Pattern.compile("([^\\[]+)?(\\[[^\\]]+\\])?");
			Matcher matcher = pattern.matcher(line);
			try {
				while (matcher.find()) {

					processBrackets(matcher.group(1));
					if (restPossible) {
						processSymbol("_");
						restPossible = false;
					}
					processSymbol(matcher.group(2));
				}
			} catch (PianoExceptions e) {
				composition = null;
			}
		});
		if (composition != null)
			for (MusicalSymbol symbol : composition.getSymbols()) {
				symbol.setKeyOnKeyboard();
			}
		readAllLines.close();
		return composition;
	}

	public static void main(String[] argv)  {
		try {
			Load l = new Load();
			l.laodKeyToNoteMap("map.csv");
			l.loadComposition("we_wich_you_a_merry_christmas.txt");
		} catch (IOException e) {
		}
		;
	}

}
