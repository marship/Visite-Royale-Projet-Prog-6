package Adaptateur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Vue.CollecteurEvenements;

public class AdaptateurTemps implements ActionListener {
	
    CollecteurEvenements collecteurEvenements;

	public AdaptateurTemps(CollecteurEvenements cEvenements) {
		collecteurEvenements = cEvenements;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		collecteurEvenements.tictac();
	}
}
