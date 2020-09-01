package piano.grafika;

import java.awt.Button;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.io.IOException;

import piano.Composition;
import piano.Load;
import piano.exception.PianoExceptions;
import piano.formatter.MidiFormatter;
import piano.formatter.TxtFormatter;

public class PianoSettings extends Panel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Button[] choices = new Button[5];
	protected TextField ulaznaKompozicija = new TextField();
	protected TextField izlaznaKompozicija = new TextField();
	protected Checkbox prikaziNote, prikaziSlova;
	protected Checkbox prikaziNoteKompozicje, prikaziSlovaKompozicije;
	protected Checkbox midiExport, txtExport;
	protected String fileName;
	private Load loader;
	private Piano piano;
	public PianoSettings(Piano piano) {
		this.piano = piano;
		setSize(1260, 200);
		setBackground(Color.WHITE);
		setLayout(new GridLayout(0,3, 15,15));
		Panel panel = new Panel();
		Panel ucitavanje = new Panel(new GridLayout(0,1));
		ucitavanje.add((choices[0] = new Button("UcitajKompoziciju")));
		Panel imeFajla = new Panel(new GridLayout(1,0));
		imeFajla.add(new Label("Ime ulaznog fajla:"));
		imeFajla.add(ulaznaKompozicija);
		ucitavanje.add(imeFajla);
		add(ucitavanje);
		add(choices[1] = new Button("Pusti kompoziciju"));
		add(choices[2] = new Button("Snimi kompoziciju"));
		Panel ucitavanje1 = new Panel(new GridLayout(0,1));
		ucitavanje1.add(choices[3] = new Button("EksportujKompoziciju"));
		Panel imeFajla1 = new Panel(new GridLayout(1,0));
		imeFajla1.add(new Label("Ime Izlaznog fajla:"));
		imeFajla1.add(izlaznaKompozicija);
		ucitavanje1.add(imeFajla1);
		add(ucitavanje1);
		add(choices[4] = new Button("Privremeno zaustavi"));
		for(int i =0;i<5;i++) {
			choices[i].setBackground(Color.BLUE);
			choices[i].setForeground(Color.GREEN);
		}
		Panel helpPiano = new Panel(new GridLayout(0,3));
		add(helpPiano);
		helpPiano.add(new Label("Pomoc pri sviranju"));
		helpPiano.add(new Label("Nacin eksportovanja"));
		helpPiano.add(new Label("Prikaz kompozicije"));
		helpPiano.add(prikaziNote = new Checkbox("Prikazi nazive nota"));
		helpPiano.add(midiExport = new Checkbox("MIDI"));
		CheckboxGroup prikazKompozicije = new CheckboxGroup();
		helpPiano.add(prikaziNoteKompozicje = new Checkbox("Prikazi note", prikazKompozicije, true));
		helpPiano.add(prikaziSlova = new Checkbox("Prikazi slova"));
		helpPiano.add(txtExport = new Checkbox("TXT"));
		helpPiano.add(prikaziSlovaKompozicije = new Checkbox("Prikazi slova", prikazKompozicije, false));
		panel.setSize(200, 30);
		prikaziNote.addItemListener(l->{
			if(prikaziNote.getState()) {
				piano.dirke.showNoteDescription = true;
				piano.dirke.repaint();
			}else {
				piano.dirke.showNoteDescription = false;
				piano.dirke.repaint();
			}
			piano.dirke.requestFocus();
		});
		prikaziSlova.addItemListener(l->{
			if(prikaziSlova.getState()) {
				piano.dirke.showKeyOnKeboard = true;
				piano.dirke.repaint();
			}else {
				piano.dirke.showKeyOnKeboard = false;
				piano.dirke.repaint();
			}
			piano.dirke.requestFocus();
		});
		prikaziNoteKompozicje.addItemListener(l->{
				piano.compositionShower.setType(1);
				piano.compositionShower.setComposition(Piano.composition);
				piano.dirke.requestFocus();
		});
		prikaziSlovaKompozicije.addItemListener(l->{
				piano.compositionShower.setType(2);
				piano.compositionShower.setComposition(Piano.composition);
				piano.dirke.requestFocus();
		});
		midiExport.addItemListener(checked->piano.dirke.requestFocus());
		txtExport.addItemListener(checked->piano.dirke.requestFocus());
		try {
			loader = new Load();
			loader.laodKeyToNoteMap("map.csv");
		} catch (IOException e) {
			new PianoExceptions("Fajl ne postoji!");
		}
		for(int i = 0;i<5;i++) {
			for(int j = 0;j<12;j++) {
				piano.dirke.octaves[i].getButton(j).setKeyOnKeyboard(Load.noteToKey.get(piano.dirke.octaves[i].getButton(j).getDescription()));
			}
		}
		choices[0].addActionListener(pressed->{
			try {
				Piano.composition = loader.loadComposition(ulaznaKompozicija.getText());
				if(Piano.composition == null) return;
				choices[0].setLabel("Ucitaj novu kompoziciju");
				piano.settings.remove(piano.compositionShower);
				piano.settings.add( piano.compositionShower = new CompositionShower(Piano.composition, Piano.composition.getMaxNumerOfNotes()),0);
				piano.settings.validate();
				piano.showComposition();
				
			} catch (IOException e) {
				new PianoExceptions("Dati fajl ne postoji!");
			}
			piano.dirke.requestFocus();
		});
		choices[1].addActionListener(pressed->{
			try {
			if(pressed.getActionCommand().equals("Pusti kompoziciju")){
				if(Piano.composition == null) throw new PianoExceptions("Niste ucitali kompoziciju");
				if(choices[2].getLabel().equals("Prekini snimanje kompozicije")) throw new PianoExceptions("Ne mozete pokrenuti kompoziciju dok snimate novu!");
			choices[1].setLabel("Prekini kompoziciju");
		   piano.dirke.removeMouseListener(piano.dirke.mouseListener);
		   piano.dirke.removeKeyListener(piano.dirke.keyListener);
			piano.playComposition();
			}
			else {
				piano.stopComposition();
				piano.dirke.addMouseListener(piano.dirke.mouseListener);
				piano.dirke.addKeyListener(piano.dirke.keyListener);
			}
			}catch (PianoExceptions e) {
				return;
			}
			piano.dirke.requestFocus();
		});
		choices[4].addActionListener(pressed->{
			try {
			if(pressed.getActionCommand().equals("Privremeno zaustavi")) {
				if(Piano.composition == null) throw new PianoExceptions("Niste ucitali kompoziciju");
				if(choices[1].getLabel().equals("Pusti kompoziciju")) throw new PianoExceptions("Ne mozete privremeno zaustaviti kompozciju koju niste pustili!");
				piano.privremenoZaustavi();
				piano.dirke.addMouseListener(piano.dirke.mouseListener);
				piano.dirke.addKeyListener(piano.dirke.keyListener);
				choices[4].setLabel("Pokreni kompoziciju");
			}else {
				piano.dirke.removeMouseListener(piano.dirke.mouseListener);
				piano.dirke.removeKeyListener(piano.dirke.keyListener);
				piano.pokreniZaustavljeno();
				choices[4].setLabel("Privremeno zaustavi");
			}
			}catch (PianoExceptions e) {
				return;
			}
			piano.dirke.requestFocus();
		});
		choices[2].addActionListener(pressed->{
			try {
			if(choices[2].getActionCommand().equals("Snimi kompoziciju")) {
				if(choices[1].getLabel().equals("Prekini kompoziciju")) throw new PianoExceptions("Ne mozete snimati kompozicju dok je druga pustena!");
			piano.snimajKompoziciju = true;
			piano.dirke.chord = null;
			piano.newComposition = new Composition();
			choices[2].setLabel("Prekini snimanje kompozicije");
			}else{
				choices[2].setLabel("Snimi kompoziciju");
				piano.snimajKompoziciju = false;
				piano.addToNewComposition(piano.dirke.chord);
				piano.dirke.chord = null;
			}
			}catch (PianoExceptions e) {
				// TODO: handle exception
			}
			piano.dirke.requestFocus();
		});
		choices[3].addActionListener(pressed->{
			try {
				if(piano.newComposition == null) throw new PianoExceptions("Niste snimili novu kompozicju, ne mozete eksportovati!");
				if(piano.snimajKompoziciju) throw new PianoExceptions("Snimanje u toku ne mozete eksportovati!");
				if(!midiExport.getState() && !txtExport.getState()) throw new PianoExceptions("Nise izabrali ni jedan nacin eksportovanja!");
				if(izlaznaKompozicija.getText().length()==0) throw new PianoExceptions("Ne moze prazan sting za ime fajla!");
				choices[3].setLabel("Exportovanje u toku");
			if(midiExport.getState()) {
				piano.pianoFormatter = new MidiFormatter(piano.newComposition);	
				piano.pianoFormatter.exportComposition(izlaznaKompozicija.getText());
				piano.pianoFormatter = null;
			}
			if(txtExport.getState()) {
				piano.pianoFormatter = new TxtFormatter(piano.newComposition);
				piano.pianoFormatter.exportComposition(izlaznaKompozicija.getText());
			
				piano.pianoFormatter = null;
			}
			Thread.sleep(300);
			choices[3].setLabel("Exportovanje zavrseno");
			Thread.sleep(300);
			choices[3].setLabel("Exportuj kompoziciju");
			}catch(InterruptedException | PianoExceptions | IOException p){}
			piano.dirke.requestFocus();
		});
	}
}
