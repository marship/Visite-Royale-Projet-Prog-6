import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
//import org.junit.Rule;
import org.junit.Test;
//import org.junit.rules.ExpectedException;

//import Global.Deplacement;
import Global.Element;
import Global.InfoPlateau;
import Modele.Jeu;
//import Modele.Plateau;
//import Modele.Personnage;

public class TestPlateau {

    Jeu jeu;
    
    @Before
    public void init() {
		jeu = new Jeu(); 
	}
    
    @Test
    public void testInitialisation() {
        // Fonctionne

        assertEquals(-2, jeu.obtenirPositionElement(Element.GARDE_GAUCHE));
        assertEquals(1, jeu.obtenirPositionElement(Element.SORCIER));
        assertEquals(0, jeu.obtenirPositionElement(Element.ROI));
        assertEquals(-1, jeu.obtenirPositionElement(Element.FOU));
        assertEquals(2, jeu.obtenirPositionElement(Element.GARDE_DROIT));

        assertEquals(Element.GARDE_GAUCHE, jeu.obtenirElementPosition(-2));
        assertEquals(Element.FOU, jeu.obtenirElementPosition(-1));
        assertEquals(Element.ROI, jeu.obtenirElementPosition(0));
        assertEquals(Element.SORCIER, jeu.obtenirElementPosition(1));
        assertEquals(Element.GARDE_DROIT, jeu.obtenirElementPosition(2));

    }

    @Test
    public void testEstPartieTerminee() { 
        // Fonctionne

        assertFalse(jeu.estPartieTerminee());
        jeu.deplacerElement(Element.GARDE_GAUCHE,-8);
        jeu.finDeTour();
        jeu.deplacerElement(Element.ROI,-7);
        jeu.finDeTour();
        assertTrue(jeu.estPartieTerminee());

        jeu = new Jeu(); 
        jeu.deplacerElement(Element.GARDE_GAUCHE,-1);
        jeu.finDeTour();
        assertFalse(jeu.estPartieTerminee());
        jeu.deplacerCouronne(7);
        jeu.finDeTour();
        assertTrue(jeu.estPartieTerminee());

        jeu = new Jeu(); 
        jeu.deplacerCouronne(8);
        jeu.finDeTour();
        assertTrue(jeu.estPartieTerminee());

        jeu = new Jeu(); 
        jeu.deplacerCouronne(-5);
        jeu.finDeTour();
        assertFalse(jeu.estPartieTerminee());        

    }

    @Test
    public void testEstPartieEnCours() { 
        // Fonctionne 

        assertTrue(jeu.estPartieEnCours());
        jeu.deplacerElement(Element.GARDE_GAUCHE,-8);
        jeu.finDeTour();
        jeu.deplacerElement(Element.ROI,-7);
        jeu.finDeTour();
        assertFalse(jeu.estPartieEnCours());

        jeu = new Jeu();
        assertTrue(jeu.estPartieEnCours());
        jeu.deplacerElement(Element.GARDE_GAUCHE,-1);
        jeu.finDeTour();
        assertTrue(jeu.estPartieEnCours());
        jeu.deplacerCouronne(7);
        jeu.finDeTour();
        assertFalse(jeu.estPartieEnCours());

        jeu = new Jeu(); 
        assertTrue(jeu.estPartieEnCours());
        jeu.deplacerCouronne(8);
        jeu.finDeTour();
        assertFalse(jeu.estPartieEnCours());

        jeu = new Jeu(); 
        assertTrue(jeu.estPartieEnCours());
        jeu.deplacerCouronne(-5);
        jeu.finDeTour();
        assertTrue(jeu.estPartieEnCours());

    }

    @Test
    public void testChangerEtatPartie() {
        // Fonctionne

        jeu.changerEtatPartie();
        assertFalse(jeu.estPartieEnCours());

        jeu.changerEtatPartie();
        assertTrue(jeu.estPartieEnCours());

    }

    @Test
    public void testActionAutoriser() {
        // Fonctionne

        assertTrue(jeu.actionAutoriser());
        jeu.changerEtatPartie();
        assertFalse(jeu.actionAutoriser());

        jeu = new Jeu();
        assertTrue(jeu.actionAutoriser());
        jeu.deplacerElement(Element.GARDE_GAUCHE,-8);
        jeu.finDeTour();
        assertTrue(jeu.actionAutoriser());
        jeu.deplacerElement(Element.ROI,-7);
        jeu.finDeTour();
        assertFalse(jeu.actionAutoriser());

        jeu = new Jeu();
        assertTrue(jeu.actionAutoriser());
        jeu.deplacerCouronne(8);
        jeu.finDeTour();
        assertFalse(jeu.actionAutoriser());

    }

    @Test
    public void testDeplacerElement() {
        // Fonctionne

        jeu.deplacerElement(Element.GARDE_DROIT,2);
        jeu.finDeTour();
        assertEquals(4,jeu.obtenirPositionElement(Element.GARDE_DROIT));

        jeu.deplacerElement(Element.GARDE_GAUCHE,-2);
        jeu.finDeTour();
        assertEquals(-4,jeu.obtenirPositionElement(Element.GARDE_GAUCHE));

        jeu.deplacerElement(Element.SORCIER,1);
        jeu.finDeTour();
        assertEquals(2,jeu.obtenirPositionElement(Element.SORCIER));

        jeu.deplacerElement(Element.ROI,-1);
        jeu.finDeTour();
        assertEquals(-1,jeu.obtenirPositionElement(Element.ROI));

        jeu.deplacerElement(Element.SORCIER,-1);
        jeu.finDeTour();
        assertEquals(1,jeu.obtenirPositionElement(Element.SORCIER));

        jeu.deplacerElement(Element.FOU,7);
        jeu.finDeTour();
        assertEquals(6,jeu.obtenirPositionElement(Element.FOU));      
    }

    @Test
    public void testValidationDeplacement() { 
        // Fonctionne

        assertTrue(jeu.validationDeplacement(Element.GARDE_GAUCHE,-2));
        assertTrue(jeu.validationDeplacement(Element.SORCIER,7));
        assertTrue(jeu.validationDeplacement(Element.ROI,1));
        assertFalse(jeu.validationDeplacement(Element.ROI,2));
        assertFalse(jeu.validationDeplacement(Element.FOU,-8));
        assertFalse(jeu.validationDeplacement(Element.GARDE_DROIT,7));

    }

    @Test
    public void testObtenirPositionElement() { 
        // Fonctionne 
        
        assertEquals(-2, jeu.obtenirPositionElement(Element.GARDE_GAUCHE));
        assertEquals(1, jeu.obtenirPositionElement(Element.SORCIER));
        assertEquals(0, jeu.obtenirPositionElement(Element.ROI));
        assertEquals(-1, jeu.obtenirPositionElement(Element.FOU));
        assertEquals(2, jeu.obtenirPositionElement(Element.GARDE_DROIT));

        jeu.deplacerElement(Element.GARDE_GAUCHE,-1);
        jeu.finDeTour();
        assertEquals(-3, jeu.obtenirPositionElement(Element.GARDE_GAUCHE));

        jeu.deplacerElement(Element.SORCIER,3);
        jeu.finDeTour();
        assertEquals(4, jeu.obtenirPositionElement(Element.SORCIER));

        jeu.deplacerElement(Element.ROI,-1);
        jeu.finDeTour();
        assertEquals(-1, jeu.obtenirPositionElement(Element.ROI));

        jeu.deplacerElement(Element.FOU,-5);
        jeu.finDeTour();
        assertEquals(-6, jeu.obtenirPositionElement(Element.FOU));

        jeu.deplacerElement(Element.GARDE_DROIT,2);
        jeu.finDeTour();
        assertEquals(4, jeu.obtenirPositionElement(Element.GARDE_DROIT));

    }

    @Test
    public void testObtenirElementPosition() {
        // Fnctionne (a revoir)

        assertEquals(Element.GARDE_GAUCHE, jeu.obtenirElementPosition(-2));
        assertEquals(Element.FOU, jeu.obtenirElementPosition(-1));
        assertEquals(Element.ROI, jeu.obtenirElementPosition(0));
        assertEquals(Element.SORCIER, jeu.obtenirElementPosition(1));
        assertEquals(Element.GARDE_DROIT, jeu.obtenirElementPosition(2));

        jeu.deplacerElement(Element.GARDE_GAUCHE,-1);
        jeu.finDeTour();
        assertEquals(Element.GARDE_GAUCHE, jeu.obtenirElementPosition(-3));

        jeu.deplacerElement(Element.SORCIER,3);
        jeu.finDeTour();
        assertEquals(Element.SORCIER, jeu.obtenirElementPosition(4));

        jeu.deplacerElement(Element.ROI,-1);
        jeu.finDeTour();
        assertEquals(Element.ROI, jeu.obtenirElementPosition(-1));

        jeu.deplacerElement(Element.FOU,-5);
        jeu.finDeTour();
        assertEquals(Element.FOU, jeu.obtenirElementPosition(-6));

        jeu.deplacerElement(Element.GARDE_DROIT,3);
        jeu.finDeTour();
        assertEquals(Element.GARDE_DROIT, jeu.obtenirElementPosition(5));

        /*jeu.deplacerElement(Element.GARDE_DROIT,2);
        jeu.finDeTour();
        assertEquals(Element.GARDE_DROIT, jeu.obtenirElementPosition(4));
        Bug si plusieurs pieces sur la meme case */

    }

    @Test
    public void testObtenirPersonnageElement() {
        // Fonctionne 

        jeu.obtenirPersonnageElement(Element.GARDE_GAUCHE).deplacerPersonnage(-1);
        jeu.finDeTour();
        assertEquals(-3, jeu.obtenirPositionElement(Element.GARDE_GAUCHE));

        jeu.obtenirPersonnageElement(Element.SORCIER).deplacerPersonnage(3);
        jeu.finDeTour();
        assertEquals(4, jeu.obtenirPositionElement(Element.SORCIER));

        jeu.obtenirPersonnageElement(Element.ROI).deplacerPersonnage(-1);
        jeu.finDeTour();
        assertEquals(-1, jeu.obtenirPositionElement(Element.ROI));

        jeu.obtenirPersonnageElement(Element.FOU).deplacerPersonnage(-5);
        jeu.finDeTour();
        assertEquals(-6, jeu.obtenirPositionElement(Element.FOU));

        jeu.obtenirPersonnageElement(Element.GARDE_DROIT).deplacerPersonnage(2);
        jeu.finDeTour();
        assertEquals(4, jeu.obtenirPositionElement(Element.GARDE_DROIT));

    }

    @Test
    public void testObtenirInfoPlateau() {
        // Fonctionne

        assertEquals(17,jeu.obtenirInfoPlateau(InfoPlateau.TAILLE_DU_PLATEAU));
        assertEquals(0,jeu.obtenirInfoPlateau(InfoPlateau.CENTRE_DU_PLATEAU));
        assertEquals(-8,jeu.obtenirInfoPlateau(InfoPlateau.EXTREMITE_GAUCHE_DU_PLATEAU));
        assertEquals(8,jeu.obtenirInfoPlateau(InfoPlateau.EXTREMITE_DROITE_DU_PLATEAU));
        assertEquals(-6,jeu.obtenirInfoPlateau(InfoPlateau.ENTREE_CHATEAU_GAUCHE));
        assertEquals(6,jeu.obtenirInfoPlateau(InfoPlateau.ENTREE_CHATEAU_DROIT));

    }

    @Test
    public void testEchangerFouSorcier() {
        // Fonctionne

        jeu.echangerFouSorcier();
        assertEquals(-1,jeu.obtenirPositionElement(Element.SORCIER));
        assertEquals(1,jeu.obtenirPositionElement(Element.FOU));

    }

    @Test
    public void testChoixPremierJoueur() {
        // Fonctionne

        jeu.choixPremierJoueur(jeu.plateau().joueurCourant);
        assertEquals(1,jeu.plateau().joueurCourant);

        jeu.choixPremierJoueur(0);
        assertEquals(0,jeu.plateau().joueurCourant);
        assertEquals(-1,jeu.obtenirPositionElement(Element.SORCIER));
        assertEquals(1,jeu.obtenirPositionElement(Element.FOU));

    }

    @Test
    public void testNumeroJoueurValide() {
        // Fonctionne
        
        assertEquals(1, jeu.plateau().joueurCourant);
        assertTrue(jeu.numeroJoueurValide(1));
        assertTrue(jeu.numeroJoueurValide(0));
        assertFalse(jeu.numeroJoueurValide(2));
        assertFalse(jeu.numeroJoueurValide(-2));

    }

    @Test
    public void testChangerJoueurCourant() {
        // Fonctionne

        assertEquals(1,jeu.plateau().joueurCourant);
        jeu.changerJoueurCourant();
        assertEquals(0,jeu.plateau().joueurCourant);     
        jeu.changerJoueurCourant();
        assertEquals(1,jeu.plateau().joueurCourant); 

    }  

    @Test
    public void testJoueurCourant() {
        // Fonctionne

        assertEquals(1,jeu.joueurCourant());
        jeu.deplacerElement(Element.GARDE_GAUCHE,-1);
        jeu.finDeTour();
        assertEquals(0,jeu.joueurCourant());

    }

    @Test
    public void testEstGagnant() { 
        // Fonctionne 

        assertFalse(jeu.estGagnant());
        jeu.deplacerElement(Element.GARDE_GAUCHE,-8);
        jeu.finDeTour();
        jeu.deplacerElement(Element.ROI,-7);
        jeu.finDeTour();
        assertTrue(jeu.estGagnant());

        jeu = new Jeu(); 
        jeu.deplacerElement(Element.GARDE_GAUCHE,-1);
        jeu.finDeTour();
        assertFalse(jeu.estGagnant());
        jeu.deplacerCouronne(7);
        jeu.finDeTour();
        assertTrue(jeu.estGagnant());

        jeu = new Jeu(); 
        jeu.deplacerCouronne(8);
        jeu.finDeTour();
        assertTrue(jeu.estGagnant());

        jeu = new Jeu(); 
        jeu.deplacerCouronne(-5);
        jeu.finDeTour();
        assertFalse(jeu.estGagnant());

    }

    @Test
    public void testTraiterGagnant() {
        // Fonctionne

        jeu.traiterGagnant();
        assertFalse(jeu.estPartieTerminee());
        assertTrue(jeu.estPartieEnCours());

        jeu.deplacerElement(Element.GARDE_GAUCHE,-8);
        jeu.finDeTour();
        jeu.deplacerElement(Element.ROI,-7);
        jeu.finDeTour();
        jeu.traiterGagnant();
        assertTrue(jeu.estPartieTerminee());
        assertFalse(jeu.estPartieEnCours());
        assertEquals(0, jeu.joueurGagnant);

        jeu = new Jeu(); 
        jeu.deplacerCouronne(8);
        jeu.finDeTour();
        jeu.traiterGagnant();
        assertTrue(jeu.estPartieTerminee());
        assertFalse(jeu.estPartieEnCours());
        assertEquals(1, jeu.joueurGagnant);

    }

    @Test
    public void testFinDeTour() {
        
        

    }    

    @Test           
    public void testDeplacerCouronne() { 
        // Fonctionne

        jeu.deplacerCouronne(2);
        jeu.finDeTour();
        assertEquals(2,jeu.getPositionCouronne());

        jeu.deplacerCouronne(3);
        jeu.finDeTour();
        assertEquals(5,jeu.getPositionCouronne());

        jeu.deplacerCouronne(-7);
        jeu.finDeTour();
        assertEquals(-2,jeu.getPositionCouronne());

        jeu.deplacerCouronne(-7);
        jeu.finDeTour();
        assertEquals(-8,jeu.getPositionCouronne());

    }

    @Test
    public void testChangerEtatCouronne() {
        // Fonctionne

        assertTrue(jeu.getEtatCouronne());
        jeu.changerEtatCouronne();
        assertFalse(jeu.getEtatCouronne());
    } 

    @Test
    public void testJouerCarte() {
        // (à revoir)
        jeu.recupererMainJoueur(jeu.joueurCourant());

        jeu.jouerCarte(Element.GARDE_GAUCHE, -3, 5);
        assertEquals(-3, jeu.obtenirPositionElement(Element.GARDE_GAUCHE));        

        jeu.jouerCarte(Element.SORCIER, 4, 5);
        assertEquals(4, jeu.obtenirPositionElement(Element.SORCIER)); 

        jeu.jouerCarte(Element.ROI, -1, 5);
        assertEquals(-1, jeu.obtenirPositionElement(Element.ROI)); 

        jeu.jouerCarte(Element.FOU, -6, 5);
        assertEquals(-6, jeu.obtenirPositionElement(Element.FOU)); 

        jeu.jouerCarte(Element.GARDE_DROIT, -4, 5);
        assertEquals(-4, jeu.obtenirPositionElement(Element.GARDE_DROIT)); 

    }
     
    @Test
    public void testJouerSequenceCarte() {
        // (à revoir)

        jeu.recupererMainJoueur(jeu.joueurCourant());

    }

    @Test
    public void testPoserCarte() {
        // (à revoir)

        jeu.recupererMainJoueur(jeu.joueurCourant());

    }

    @Test
    public void testRecupererMainJoueur() {



    }

    @Test
    public void testInitialiserDernierTypeDePersonnageJouer() {
        // Fonctionne

        jeu.initialiserDernierTypeDePersonnageJouer();
        assertEquals(Element.VIDE, jeu.dernierTypeDePersonnageJouer);

    }

    @Test
    public void testMajDernierTypeDePersonnageJouer() {
        // (à revoir)

        jeu.recupererMainJoueur(jeu.joueurCourant());
        //jeu.majDernierTypeDePersonnageJouer();
        assertEquals(jeu.recupererMainJoueur(jeu.joueurCourant())[5], jeu.dernierTypeDePersonnageJouer);

    }

    @Test
    public void testListeCarteJouable() {



    }

    @Test
    public void testInitialiserTableau() {



    }

    @Test
    public void testPositionPlus8() {



    }

    @Test
    public void testPositionsPourCour() {



    }

    @Test
    public void testListeDeplacementPossiblesAvecCarte() {



    }

    @Test
    public void testPersonnageManipulerParLeFou() {



    }

    @Test
    public void testEstPouvoirFouActivable() {



    }

    @Test
    public void testTeleportationPouvoirSorcier() {
        // Fonctionne

        jeu.teleportationPouvoirSorcier(Element.ROI);
        assertEquals(1,jeu.obtenirPositionElement(Element.ROI));
        jeu.deplacerElement(Element.ROI,-1);
        jeu.finDeTour();
        jeu.teleportationPouvoirSorcier(Element.GARDE_DROIT);
        assertEquals(1,jeu.obtenirPositionElement(Element.GARDE_DROIT));
        jeu.deplacerElement(Element.GARDE_DROIT,1);
        jeu.finDeTour();
        jeu.teleportationPouvoirSorcier(Element.GARDE_GAUCHE);
        assertEquals(-2,jeu.obtenirPositionElement(Element.GARDE_GAUCHE));

        jeu.deplacerElement(Element.SORCIER,4);
        jeu.finDeTour();
        jeu.teleportationPouvoirSorcier(Element.GARDE_GAUCHE);
        assertEquals(-2,jeu.obtenirPositionElement(Element.GARDE_GAUCHE));
        jeu.teleportationPouvoirSorcier(Element.ROI);
        assertEquals(0,jeu.obtenirPositionElement(Element.ROI));
        jeu.deplacerElement(Element.GARDE_DROIT,6);
        jeu.finDeTour();
        jeu.teleportationPouvoirSorcier(Element.ROI);
        assertEquals(5,jeu.obtenirPositionElement(Element.ROI));
        jeu.deplacerElement(Element.ROI,-1);
        jeu.finDeTour();
        jeu.teleportationPouvoirSorcier(Element.GARDE_DROIT);
        assertEquals(5,jeu.obtenirPositionElement(Element.GARDE_DROIT));

        jeu.deplacerElement(Element.SORCIER,-8);
        jeu.finDeTour();
        jeu.teleportationPouvoirSorcier(Element.GARDE_DROIT);
        assertEquals(5,jeu.obtenirPositionElement(Element.GARDE_DROIT));
        jeu.teleportationPouvoirSorcier(Element.ROI);
        assertEquals(4,jeu.obtenirPositionElement(Element.ROI));
        jeu.teleportationPouvoirSorcier(Element.GARDE_GAUCHE);
        assertEquals(-3,jeu.obtenirPositionElement(Element.GARDE_GAUCHE));

    }

    @Test
    public void testEstPouvoirSorcierActivable() {
        // Fonctionne

        assertTrue(jeu.estPouvoirSorcierActivable(Element.GARDE_DROIT));
        assertTrue(jeu.estPouvoirSorcierActivable(Element.ROI));
        assertFalse(jeu.estPouvoirSorcierActivable(Element.GARDE_GAUCHE));

        jeu.deplacerElement(Element.SORCIER,4);
        jeu.finDeTour();
        assertTrue(jeu.estPouvoirSorcierActivable(Element.GARDE_DROIT));
        assertFalse(jeu.estPouvoirSorcierActivable(Element.ROI));       
        assertFalse(jeu.estPouvoirSorcierActivable(Element.GARDE_GAUCHE));

        jeu.deplacerElement(Element.SORCIER,-8);
        jeu.finDeTour();
        assertFalse(jeu.estPouvoirSorcierActivable(Element.GARDE_DROIT));
        assertFalse(jeu.estPouvoirSorcierActivable(Element.ROI));       
        assertTrue(jeu.estPouvoirSorcierActivable(Element.GARDE_GAUCHE));

    }

    @Test
    public void testEstTeleportationValide() {
        // Fonctionne

        assertTrue(jeu.estTeleportationValide(Element.GARDE_DROIT,-1));
        assertTrue(jeu.estTeleportationValide(Element.ROI,1));      
        assertFalse(jeu.estTeleportationValide(Element.GARDE_GAUCHE,3));

        jeu.deplacerElement(Element.SORCIER,4);
        jeu.finDeTour();
        assertTrue(jeu.estTeleportationValide(Element.GARDE_DROIT,3));
        assertFalse(jeu.estTeleportationValide(Element.ROI,5));       
        assertFalse(jeu.estTeleportationValide(Element.GARDE_GAUCHE,7));

    }

    @Test
    public void testCalculerTeleportation() {
        // Fonctionne

        assertEquals(-1,jeu.calculerTeleportation(Element.GARDE_DROIT));
        assertEquals(1,jeu.calculerTeleportation(Element.ROI));

        jeu.deplacerElement(Element.GARDE_DROIT,6);
        jeu.finDeTour();
        jeu.deplacerElement(Element.ROI,6);
        jeu.finDeTour();
        assertEquals(-5,jeu.calculerTeleportation(Element.ROI));
        assertEquals(-7,jeu.calculerTeleportation(Element.GARDE_DROIT));

        jeu.deplacerElement(Element.GARDE_GAUCHE,-5);
        jeu.finDeTour();
        jeu.deplacerElement(Element.SORCIER,-5 );
        jeu.finDeTour();
        assertEquals(-4,jeu.calculerTeleportation(Element.ROI));
        assertEquals(3,jeu.calculerTeleportation(Element.GARDE_GAUCHE));

    }

    @Test
    public void testGetPositionCouronne() {
        // Fonctionne

        assertEquals(0, jeu.getPositionCouronne());

        jeu.deplacerCouronne(5);
        jeu.finDeTour();
        assertEquals(5, jeu.getPositionCouronne());

        jeu.deplacerCouronne(-3);
        jeu.finDeTour();
        assertEquals(2, jeu.getPositionCouronne());

        jeu.deplacerCouronne(4);
        jeu.finDeTour();
        assertEquals(6, jeu.getPositionCouronne());

        jeu.deplacerCouronne(4);
        jeu.finDeTour();
        assertEquals(8, jeu.getPositionCouronne());

    }

    @Test
    public void testGetEtatCouronne() {
        // Fonctionne

        assertTrue(jeu.getEtatCouronne());
        jeu.changerEtatCouronne();
        assertFalse(jeu.getEtatCouronne());

    }
    
}
