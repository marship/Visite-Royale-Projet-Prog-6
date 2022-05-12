import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
//import org.junit.Rule;
import org.junit.Test;
//import org.junit.rules.ExpectedException;

import Global.Deplacement;
import Global.Element;
import Modele.Jeu;
import Modele.Plateau;

public class TestPlateau {

    Jeu jeu;
    
    @Before
    public void init() {
		jeu = new Jeu(); 
	}

    @Test
    public void testReset() {   
        assertEquals(-2, jeu.obtenirPositionElement(Element.GARDE_GAUCHE));
        assertEquals(1, jeu.obtenirPositionElement(Element.SORCIER));
        assertEquals(0, jeu.obtenirPositionElement(Element.ROI));
        assertEquals(-1, jeu.obtenirPositionElement(Element.FOU));
        assertEquals(2, jeu.obtenirPositionElement(Element.GARDE_DROIT));
    }

    @Test
    public void testEstPartieTerminee() {   
        assertFalse(jeu.estPartieTerminee());
    }

    @Test
    public void testEstPartieEnCours() {  
        assertTrue(jeu.estPartieEnCours());
    }
/*
    @Test
    public void testDeplacerElement() {
        plateau.deplacerElement(plateau.gardeDroit.typePersonnage(),2);
        assertEquals(4,plateau.gardeDroit.positionPersonnage());

        plateau.deplacerElement(plateau.gardeGauche.typePersonnage(),-2);
        assertEquals(-4,plateau.gardeGauche.positionPersonnage());

        plateau.deplacerElement(plateau.sorcier.typePersonnage(),1);
        assertEquals(2,plateau.sorcier.positionPersonnage());

        plateau.deplacerElement(plateau.roi.typePersonnage(),-1);
        assertEquals(-1,plateau.roi.positionPersonnage());

        plateau.deplacerElement(plateau.sorcier.typePersonnage(),-1);
        assertEquals(1,plateau.sorcier.positionPersonnage());

        plateau.deplacerElement(plateau.fou.typePersonnage(),7);
        assertEquals(6,plateau.fou.positionPersonnage());      
    }

    @Test
    public void testValidationDeplacement() { 
        assertTrue(plateau.validationDeplacement(plateau.gardeGauche.typePersonnage(),-2));
        assertTrue(plateau.validationDeplacement(plateau.sorcier.typePersonnage(),7));
        assertFalse(plateau.validationDeplacement(plateau.roi.typePersonnage(),2));
        assertFalse(plateau.validationDeplacement(plateau.fou.typePersonnage(),-8));
    }
*/
    @Test
    public void testDeplacerCouronne() { 
        plateau.deplacerCouronne(Deplacement.DEUX);
        assertEquals(2,plateau.couronne.positionCouronne);
    }

    @Test
    public void testChangerEtatCouronne() {
        plateau.changerEtatCouronne();
        assertFalse(plateau.couronne.etatCouronne);
    }

    @Test
    public void testChoixPremierJoueur() {
        plateau.choixPremierJoueur(plateau.joueurCourant);
        assertEquals(1,plateau.joueurCourant);

        plateau.choixPremierJoueur(0);
        assertEquals(0,plateau.joueurCourant);
        assertEquals(-1,plateau.sorcier.positionPersonnage());
        assertEquals(1,plateau.fou.positionPersonnage());
    }

    @Test
    public void testChangerJoueurCourant() {
        plateau.changerJoueurCourant(plateau.joueurCourant);
        assertEquals(1,plateau.joueurCourant);        
    }

    @Test
    public void testeChangerFouSorcier() {
        plateau.echangerFouSorcier();
        assertEquals(-1,plateau.sorcier.positionPersonnage());
        assertEquals(1,plateau.fou.positionPersonnage());
    }

}
