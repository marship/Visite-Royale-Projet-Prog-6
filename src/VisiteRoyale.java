// import Controleur.ControleurMediateur;
import java.util.Scanner;

import Global.Element;
import Modele.Jeu;
// import Vue.CollecteurEvenements;
// import Vue.InterfaceGraphique;

public class VisiteRoyale {

    static void test(Jeu jeu){
        Scanner sc = new Scanner(System.in);
        while(!Jeu.estGagnant()){
            System.out.println("Au tour de : " + Jeu.plateau().joueurCourant);
            Jeu.plateau().afficherPlateau();
            Jeu.plateau().paquet.afficherMain(Jeu.plateau().joueurCourant);
            System.out.println("0/7 : La carte à jouer");
            System.out.println("8 : Le pouvoir du fou");
            System.out.println("9 : Le pouvoir du sorcier");
            int carte = sc.nextInt();
            if(carte == 9){
                System.out.println("Vous pouvez TP :");
                System.out.println("1 : ROI : " + Jeu.estPouvoirSorcierActivable(Element.ROI));
                System.out.println("2 : GARDE GAUCHE : " + Jeu.estPouvoirSorcierActivable(Element.GARDE_GAUCHE));
                System.out.println("3 : GARDE DROIT : " + Jeu.estPouvoirSorcierActivable(Element.GARDE_DROIT));
                int tp = sc.nextInt();
                switch (tp) {
                    case 1:
                        if(Jeu.estPouvoirSorcierActivable(Element.ROI)){
                            Jeu.teleportationPouvoirSorcier(Element.ROI);
                            Jeu.finDeTour();
                        }
                        break;

                    case 2:
                        if(Jeu.estPouvoirSorcierActivable(Element.GARDE_GAUCHE)){
                            Jeu.teleportationPouvoirSorcier(Element.GARDE_GAUCHE);
                            Jeu.finDeTour();
                        }
                        break;

                    case 3:
                        if(Jeu.estPouvoirSorcierActivable(Element.GARDE_DROIT)){
                            Jeu.teleportationPouvoirSorcier(Element.GARDE_DROIT);
                            Jeu.finDeTour();
                        }
                        break;
                
                    default:
                        break;
                }
            }
            else{
                if(carte == 8){
                    if(Jeu.estPouvoirFouActivable()){
                        System.out.println("Que doivet bouger les carte fou ?");
                        System.out.println("1 : ROI");
                        System.out.println("2 : GARDE GAUCHE");
                        System.out.println("3 : GARDE DROIT");
                        System.out.println("4 : SORCIER");
                        System.out.println("5 : FOU");
                        int fou = sc.nextInt();
                        switch (fou) {
                            case 1:
                                Jeu.personnageManipulerParLeFou(Element.ROI);
                                break;

                            case 2:
                                Jeu.personnageManipulerParLeFou(Element.GARDE_GAUCHE);
                                break;

                            case 3:
                                Jeu.personnageManipulerParLeFou(Element.GARDE_DROIT);
                                break;

                            case 4:
                                Jeu.personnageManipulerParLeFou(Element.SORCIER);
                                break;

                            case 5:
                                Jeu.personnageManipulerParLeFou(Element.FOU);
                                break;
                        
                            default:
                                break;
                        }
                    }
                    else{
                        System.out.println("Pouvoir du fou impossible à utiliser !");
                    }
                }
                else{
                    int i = -8;
                    int[] a = Jeu.listeDeplacementPossiblesAvecCarte(carte);
                    while(i <= 8){
                        if(i >= 0){
                            System.out.print(" ");
                        }
                        System.out.print(i + " ");
                        i++;
                    }
                    System.out.println(" ");
                    i = 0;
                    while(i != 17){
                        System.out.print(" " + a[i] + " ");
                        i++;
                    }
                    System.out.println("Que jouez vous ?");
                    int jouer = sc.nextInt();
                    Element el = Jeu.plateau().paquet.mainJoueur(Jeu.plateau().joueurCourant)[carte].personnage();
                    if(el == Element.FOU){
                        Jeu.jouerCarte(Jeu.personnageManipulerParLeFou, jouer, carte);
                    }
                    else{
                        Jeu.jouerCarte(el, jouer, carte);
                    }
                    Jeu.finDeTour();
                }
            }
        }
        sc.close();
    }
    public static void main(String[] args) throws Exception {

        try {
            Jeu jeu = new Jeu();

            test(jeu);


            // CollecteurEvenements controleurMediateur = new ControleurMediateur(plateau);
            // InterfaceGraphique.demarrer(plateau, controleurMediateur);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
