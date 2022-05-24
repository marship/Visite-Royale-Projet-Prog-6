package Adaptateur;

import javax.swing.*;

import Vue.CollecteurEvenements;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdaptateurJoueur implements ActionListener {
	
    CollecteurEvenements collecteurEvenements;
	JToggleButton boutonPoussable;
	int numeroJoueurCourant;

	AdaptateurJoueur(CollecteurEvenements cEvenements, JToggleButton bPoussable, int nJoueurCourant) {
		collecteurEvenements = cEvenements;
		boutonPoussable = bPoussable;
		numeroJoueurCourant = nJoueurCourant;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (boutonPoussable.isSelected()) {
            collecteurEvenements.changerJoueurCourant(numeroJoueurCourant, 1);
        } else {
            collecteurEvenements.changerJoueurCourant(numeroJoueurCourant, 0);
        }
	}
}
