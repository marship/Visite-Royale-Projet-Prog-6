package Vue;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdaptateurTemps implements ActionListener {
	
    CollecteurEvenements collecteurEvenements;

	AdaptateurTemps(CollecteurEvenements cEvenements) {
		collecteurEvenements = cEvenements;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		collecteurEvenements.tictac();
	}
}
