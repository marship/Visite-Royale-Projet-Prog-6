package Modele;

import Global.Configuration;
import Global.Deplacement;
import Global.Element;
import Structures.Sequence;

public class Evaluation {

    // Vrai = JoueurDroite
    // Faux = JoueurGauche

    static int positionGardeGauche;
    static int positionGardeDroit;
    static int positionRoi;
    static int positionFou;
    static int positionSorcier;
    static int positionCouronne;
    static int taillePioche;
    static boolean etatCouronne;
    static double moyenneFou;
    boolean calcul;

    // ==================
    // ===== JOUEUR =====
    // ==================
    final int JOUEUR_GAUCHE = 0;
    final int JOUEUR_DROIT = 1;

    public Evaluation(Plateau p) {
        positionGardeGauche = p.gardeGauche.positionPersonnage();
        positionGardeDroit = p.gardeDroit.positionPersonnage();
        positionRoi = p.roi.positionPersonnage();
        positionFou = p.fou.positionPersonnage();
        positionSorcier = p.sorcier.positionPersonnage();
        positionCouronne = p.couronne.positionCouronne();
        taillePioche = p.paquet.pioche().taille();
        etatCouronne = p.couronne.etatCouronne;
        if (p.joueurCourant == JOUEUR_DROIT) {
            calcul = true;
        } else {
            calcul = false;
        }
        moyenneFou(p, calcul);
    }

    public double valeurCarteFou(Deplacement d, Boolean joueurCourant){
        switch (d) {
            case UN:
                return 1;
            case DEUX:
                return 2;
            case TROIS:
                return 3;
            case QUATRE:
                return 4;
            case CINQ:
                return 5;
            case MILIEU:
                if(joueurCourant){
                    if(positionFou < positionRoi){
                        return Math.abs(positionFou);
                    }
                    else{
                        int somme = 0;
                        int nb = 2;
                        somme = somme + Math.abs(positionFou);
                        somme = somme + Math.abs(positionSorcier);
                        if(positionRoi > 0){
                            somme = somme + Math.abs(positionGardeGauche);
                            nb++;
                        }
                        if(positionRoi < 0){
                            somme = somme + Math.abs(positionGardeDroit);
                            nb++;
                        }
                        if(positionGardeGauche < 0 && positionGardeDroit > 0){
                            somme = somme + Math.abs(positionRoi);
                            nb++;
                        }
                        return somme/nb;
                    }
                }
                else{
                    if(positionFou > positionRoi){
                        return Math.abs(positionFou);
                    }
                    else{
                        int somme = 0;
                        int nb = 2;
                        somme = somme + Math.abs(positionFou);
                        somme = somme + Math.abs(positionSorcier);
                        if(positionRoi > 0){
                            somme = somme + Math.abs(positionGardeGauche);
                            nb++;
                        }
                        if(positionRoi < 0){
                            somme = somme + Math.abs(positionGardeDroit);
                            nb++;
                        }
                        if(positionGardeGauche < 0 && positionGardeDroit > 0){
                            somme = somme + Math.abs(positionRoi);
                            nb++;
                        }
                        return somme/nb;
                    }
                }
            default:
                return 0;
        }
    }

    public void moyenneFou(Plateau p, boolean joueurCourant){
        int nbFou = 0;
        int i = 0;
        double somme = 0;
        while(i < 8){
            if(p.paquet.mainJoueur(p.joueurCourant)[i].personnage() == Element.FOU){
                nbFou ++;
                somme = somme + valeurCarteFou(p.paquet.mainJoueur(p.joueurCourant)[i].deplacement(), joueurCourant);
            }
            i++;
        }
        Sequence<Carte> liste = Configuration.instance().nouvelleSequence();
        while(!p.paquet.pioche.estVide()){
            liste.insereQueue(p.paquet.pioche.extraitTete());
        }
        while(!liste.estVide()){
            Carte carte = liste.extraitTete();
            if(carte.personnage() == Element.FOU){
                nbFou++;
                somme = somme + valeurCarteFou(carte.deplacement(), joueurCourant);
            }
            p.paquet.pioche.insereQueue(carte);
        }
        moyenneFou = somme / nbFou;
    }

    public double note(int joueur) {
        double note = 0;

        // Sorcier
        double resSorcierGentil = calculSorcier(calcul);
        double resSorcierMechant = calculSorcier(!calcul);
        double noteSorcier = 0;
        noteSorcier = (((resSorcierGentil) - resSorcierMechant) / 2);
        // NbPi??ces
        double resNbPi??cesGentil = calculPiece(calcul);
        double resNbPi??cesMechant = calculPiece(!calcul);
        double notePiece = 0;
        notePiece = (((resNbPi??cesGentil) - resNbPi??cesMechant) / 2);
        // Chateau
        double resNbPi??cesChateauGentil = calculChateau(calcul);
        double resNbPi??cesChateauMechant = calculChateau(!calcul);
        double noteChateau = 0;
        noteChateau = (((resNbPi??cesChateauGentil) - resNbPi??cesChateauMechant) / 2);
        // Roi
        double noteRoi = calculRoi(calcul);
        // Couronne
        double noteCouronne = calculCouronne(calcul);

        // Gardes
        double resGardeGentil = calculGarde(calcul);
        double resGardeMechant = calculGarde(!calcul);
        double noteGarde = 0;
        noteGarde = (((resGardeGentil) - resGardeMechant) / 2);

        // Fou
        double resFouGentil = calculFou(calcul);
        double resFouMechant = calculFou(!calcul);
        double noteFou = 0;
        noteFou = (((resFouGentil) - resFouMechant) / 2) / 2;
        
        note = noteChateau + noteCouronne + noteFou + notePiece + noteRoi + noteSorcier + noteGarde;
        return note;
    }
    // ===============
    // Th??or??me Sorcier
    // ===============

    double calculSorcier(boolean joueurCourant) {
        double resSorcier = 0;
        if (!((positionGardeGauche < positionSorcier) && (positionSorcier < positionGardeDroit))
                && !sorcierChezNous(joueurCourant)) {
            resSorcier = resSorcier - 1;
        }

        if (sorcierDansChateau(joueurCourant) && tpGardePossible(joueurCourant)) {
            resSorcier = resSorcier + 3;
        }

        if (configSorcierVainqueur(joueurCourant)) {
            resSorcier = resSorcier + 6;
        }

        if (sortirPersonnageChateauAdverse(joueurCourant)) {
            resSorcier = resSorcier + 1.5;
        }

        if(joueurCourant){
            resSorcier = resSorcier + (positionSorcier * 0.2);
        }
        else{
            resSorcier = resSorcier + (-positionSorcier * 0.2);
        }

        if (!(!((positionGardeGauche < positionSorcier) && (positionSorcier < positionGardeDroit))
                && !sorcierChezNous(joueurCourant))) {
            Double distMax = (double) 0;
            if (joueurCourant) {
                if ((positionGardeDroit != positionSorcier) && positionRoi < positionSorcier) {
                    // TP GARDE DROIT
                    int distGardeDroit = positionSorcier - positionGardeDroit;
                    distGardeDroit = Math.abs(distGardeDroit);
                    distMax = Math.max(distMax, distGardeDroit);
                }

                if ((positionGardeGauche != positionSorcier) && positionRoi > positionSorcier) {
                    // TP GARDE GAUCHE
                    int distGardeGauche = positionSorcier - positionGardeGauche;
                    distGardeGauche = Math.abs(distGardeGauche);
                    distMax = Math.max(distMax, distGardeGauche);
                }

                if ((positionGardeGauche < positionSorcier) && (positionGardeDroit > positionSorcier)) {
                    // TP ROI
                    if (positionRoi < positionSorcier) {
                        int distRoi = positionSorcier - positionRoi;
                        distRoi = Math.abs(distRoi);
                        distMax = Math.max(distMax, distRoi);
                    }
                }
            } else {
                if ((positionGardeDroit != positionSorcier) && positionRoi < positionSorcier) {
                    // TP GARDE DROIT
                    int distGardeDroit = positionSorcier - positionGardeDroit;
                    distGardeDroit = Math.abs(distGardeDroit);
                    distMax = Math.max(distMax, distGardeDroit);
                }

                if ((positionGardeGauche != positionSorcier) && positionRoi > positionSorcier) {
                    // TP GARDE GAUCHE
                    int distGardeGauche = positionSorcier - positionGardeGauche;
                    distGardeGauche = Math.abs(distGardeGauche);
                    distMax = Math.max(distMax, distGardeGauche);
                }

                if ((positionGardeGauche < positionSorcier) && (positionGardeDroit > positionSorcier)) {
                    // TP ROI
                    if (positionRoi > positionSorcier) {
                        int distRoi = positionSorcier - positionRoi;
                        distRoi = Math.abs(distRoi);
                        distMax = Math.max(distMax, distRoi);
                    }
                }
            }
            distMax = distMax / 2;
            resSorcier = resSorcier + distMax;
        }
        return resSorcier;
    }

    // ===============
    // Th??or??me NBPi??ce
    // ===============
    double calculPiece(boolean joueurCourant) {
        double resNbPi??ces = 0;
        if (joueurCourant) {
            if (positionRoi > 0) {
                resNbPi??ces = resNbPi??ces + 1;
            }
            if (positionGardeDroit > 0) {
                resNbPi??ces = resNbPi??ces + 1;
            }
            if (positionGardeGauche > 0) {
                resNbPi??ces = resNbPi??ces + 1;
            }
            if (positionFou > 0) {
                resNbPi??ces = resNbPi??ces + 1;
            }
            if (positionSorcier > 0) {
                resNbPi??ces = resNbPi??ces + 1;
            }
        } else {
            if (positionRoi < 0) {
                resNbPi??ces = resNbPi??ces + 1;
            }
            if (positionGardeDroit < 0) {
                resNbPi??ces = resNbPi??ces + 1;
            }
            if (positionGardeGauche < 0) {
                resNbPi??ces = resNbPi??ces + 1;
            }
            if (positionFou < 0) {
                resNbPi??ces = resNbPi??ces + 1;
            }
            if (positionSorcier < 0) {
                resNbPi??ces = resNbPi??ces + 1;
            }
        }
        return resNbPi??ces;
    }

    // ==========================
    // Th??or??me NBPi??ceDansChateau
    // ==========================
    double calculChateau(boolean joueurCourant) {
        double resNbPi??cesChateau = 0;
        if (joueurCourant) {
            if (positionRoi > 6) {
                resNbPi??cesChateau = resNbPi??cesChateau + 1;
            }
            if (positionGardeDroit > 6) {
                resNbPi??cesChateau = resNbPi??cesChateau + 1;
            }
            if (positionGardeGauche > 6) {
                resNbPi??cesChateau = resNbPi??cesChateau + 1;
            }
            if (positionFou > 6) {
                resNbPi??cesChateau = resNbPi??cesChateau + 1;
            }
            if (positionSorcier > 6) {
                resNbPi??cesChateau = resNbPi??cesChateau + 1;
            }
        } else {
            if (positionRoi < -6) {
                resNbPi??cesChateau = resNbPi??cesChateau + 1;
            }
            if (positionGardeDroit < -6) {
                resNbPi??cesChateau = resNbPi??cesChateau + 1;
            }
            if (positionGardeGauche < -6) {
                resNbPi??cesChateau = resNbPi??cesChateau + 1;
            }
            if (positionFou < -6) {
                resNbPi??cesChateau = resNbPi??cesChateau + 1;
            }
            if (positionSorcier < -6) {
                resNbPi??cesChateau = resNbPi??cesChateau + 1;
            }
        }
        resNbPi??cesChateau = resNbPi??cesChateau * 2;
        return resNbPi??cesChateau;
    }

    // ===========
    // Th??or??me Roi
    // ===========
    double calculRoi(boolean joueurCourant) {
        double resRoi = 0;
        double coeff = 1;
        if(taillePioche <= 15 && !etatCouronne){
            coeff = 2;
        }
        if (joueurCourant) {
            if(positionRoi > 6){
                resRoi = 1000;
            }
            else{
                resRoi = positionRoi * coeff;
            }
        } else {
            if(positionRoi < -6){
                resRoi = 1000;
            }
            else{
                resRoi = -positionRoi * coeff;
            }
        }
        return resRoi;
    }

    // =================
    // Th??or??me Couronne
    // =================
    double calculCouronne(boolean joueurCourant) {
        double resCouronne = 0;
        if (joueurCourant) {
            if (positionCouronne > -3 && positionCouronne < 3) {
                if (positionCouronne > 0) {
                    resCouronne = resCouronne + 1;
                } else {
                    if (positionCouronne < 0) {
                        resCouronne = resCouronne - 1;
                    } else {
                        resCouronne = resCouronne + 0;
                    }
                }
            } else {
                if (positionCouronne > 2) {
                    resCouronne = resCouronne + positionCouronne - 1;
                } else {
                    resCouronne = resCouronne + positionCouronne + 1;
                }
            }
        } else {
            if (positionCouronne > -3 && positionCouronne < 3) {
                if (positionCouronne > 0) {
                    resCouronne = resCouronne - 1;
                } else {
                    if (positionCouronne < 0) {
                        resCouronne = resCouronne + 1;
                    } else {
                        resCouronne = resCouronne + 0;
                    }
                }
            } else {
                if (positionCouronne > 2) {
                    resCouronne = resCouronne - positionCouronne + 1;
                } else {
                    resCouronne = resCouronne - positionCouronne - 1;
                }
            }
        }
        if (joueurCourant) {
            if (((positionCouronne == 6) && (calculChateau(joueurCourant) >= 4))
                    || ((positionCouronne == 5) && (calculChateau(joueurCourant) >= 6))) {
                resCouronne = resCouronne + 6;
            }
            if(positionCouronne == 7 || positionCouronne == 8){
                resCouronne = resCouronne + 1000;
            }
        } else {
            if (((positionCouronne == -6) && (calculChateau(joueurCourant) >= 4))
                    || ((positionCouronne == -5) && (calculChateau(joueurCourant) >= 6))) {
                resCouronne = resCouronne + 6;
            }
            if(positionCouronne == -7 || positionCouronne == -8){
                resCouronne = resCouronne + 1000;
            }
        }
        return resCouronne;
    }

    // ===========
    // Th??or??me Fou
    // ===========

    double calculFou(boolean joueurCourant) {
        double resFou = 0;
        if (!fouUtilisable(joueurCourant)) {
            resFou = resFou + 0;
        } else {
            if (joueurCourant) {
                double somme = 0.0;
                int nbChateau = 2;
                if (!gardeDroitDansChateau(joueurCourant)) {
                    double deplacementGardeDroit = 8 - positionGardeDroit;
                    deplacementGardeDroit = Math.abs(deplacementGardeDroit);
                    double res = Math.round(deplacementGardeDroit / moyenneFou);
                    deplacementGardeDroit = multipliCoeff(res, deplacementGardeDroit);
                    somme += deplacementGardeDroit;
                    nbChateau++;
                }

                if (!sorcierDansChateau(joueurCourant)) {
                    double deplacementSorcier = 8 - positionSorcier;
                    deplacementSorcier = Math.abs(deplacementSorcier);
                    double res = Math.round(deplacementSorcier / moyenneFou);
                    deplacementSorcier = multipliCoeff(res, deplacementSorcier);
                    somme += deplacementSorcier;
                    nbChateau++;
                }

                double deplacementRoi = tpRoi(joueurCourant);
                deplacementRoi = Math.abs(deplacementRoi);
                double res = Math.round(deplacementRoi / moyenneFou);
                deplacementRoi = multipliCoeff(res, deplacementRoi);
                somme += deplacementRoi;

                double deplacementGardeGauche = tpGardeGauche();
                deplacementGardeGauche = Math.abs(deplacementGardeGauche);
                res = Math.round(deplacementGardeGauche / moyenneFou);
                deplacementGardeGauche = multipliCoeff(res, deplacementGardeGauche);
                somme += deplacementGardeGauche;

                somme = somme / nbChateau;

                resFou = resFou + somme;

                if ((positionFou > positionRoi) && (positionGardeDroit == 8) && (positionFou + 4 >= 7)) {
                    resFou = resFou + 6;
                }

            } else {

                double somme = 0.0;
                int nbChateau = 2;
                if (!gardeGaucheDansChateau(joueurCourant)) {
                    double deplacementGardeGauche = -8 - positionGardeGauche;
                    deplacementGardeGauche = Math.abs(deplacementGardeGauche);
                    double res = Math.round(deplacementGardeGauche / moyenneFou);
                    deplacementGardeGauche = multipliCoeff(res, deplacementGardeGauche);
                    somme += deplacementGardeGauche;
                    nbChateau++;
                }

                if (!sorcierDansChateau(joueurCourant)) {
                    double deplacementSorcier = -8 - positionSorcier;
                    deplacementSorcier = Math.abs(deplacementSorcier);
                    double res = Math.round(deplacementSorcier / moyenneFou);
                    deplacementSorcier = multipliCoeff(res, deplacementSorcier);
                    somme += deplacementSorcier;
                    nbChateau++;
                }

                double deplacementRoi = tpRoi(joueurCourant);
                deplacementRoi = Math.abs(deplacementRoi);
                double res = Math.round(deplacementRoi / moyenneFou);
                deplacementRoi = multipliCoeff(res, deplacementRoi);
                somme += deplacementRoi;

                double deplacementGardeDroit = tpGardeDroit();
                deplacementGardeDroit = Math.abs(deplacementGardeDroit);
                res = Math.round(deplacementGardeDroit / moyenneFou);
                deplacementGardeDroit = multipliCoeff(res, deplacementGardeDroit);
                somme += deplacementGardeDroit;

                somme = somme / nbChateau;

                resFou = resFou + somme;

                if ((positionFou < positionRoi) && (positionGardeGauche == -8) && (positionFou - 4 <= -7)) {
                    resFou = resFou + 6;
                }
            }
        }
        if(joueurCourant){
            resFou = resFou + (positionFou * 0.2);
        }
        else{
            resFou = resFou + (-positionFou * 0.2);
        }
        return resFou;
    }

    // ==============
    // Th??or??me Gardes
    // ==============
    double calculGarde(boolean joueurCourant) {
        double resGardes = 0;
        if (joueurCourant) {
            if (positionGardeDroit == 8) {
                resGardes = resGardes + 2;
            } else if (positionGardeDroit == 7) {
                resGardes = resGardes + 0;
            } else {
                resGardes = resGardes - 2;
                for (int i = 6; i > positionGardeDroit; i--) {
                    resGardes = resGardes - 1;
                }
            }
        } else {
            if (positionGardeGauche == -8) {
                resGardes = resGardes + 2;
            } else if (positionGardeGauche == -7) {
                resGardes = resGardes + 0;
            } else {
                resGardes = resGardes - 2;
                for (int i = -6; i < positionGardeGauche; i++) {
                    resGardes = resGardes - 1;
                }
            }
        }
        return resGardes;
    }

    private static double tpGardeDroit() {
        double res = positionGardeDroit - positionRoi;
        res = Math.abs(res) - 1;
        return res;
    }

    private static double tpGardeGauche() {
        double res = positionGardeGauche - positionRoi;
        res = Math.abs(res) - 1;
        return res;
    }

    private static double tpRoi(boolean joueurCourant) {
        if (joueurCourant) {
            double res = positionFou - positionRoi;
            res = Math.abs(res);
            res += 4.0;

            double resGarde = positionGardeDroit - positionRoi - 1;
            resGarde = Math.abs(resGarde);
            return Math.min(res, resGarde);
        }
        double res = positionFou - positionRoi;
        res = Math.abs(res);
        res += 4.0;

        double resGarde = positionGardeGauche - positionRoi + 1;
        resGarde = Math.abs(resGarde);

        return Math.min(res, resGarde);
    }

    private static double multipliCoeff(double res, double deplacementGardeGauche) {
        switch ((int) res) {
            case 0:
                deplacementGardeGauche = deplacementGardeGauche * 1.5;
                break;

            case 1:
                deplacementGardeGauche = deplacementGardeGauche * 1.75;
                break;

            case 2:
                deplacementGardeGauche = deplacementGardeGauche * 1.5;
                break;

            case 3:
                deplacementGardeGauche = deplacementGardeGauche * 1;
                break;

            case 4:
                deplacementGardeGauche = deplacementGardeGauche * 0.5;
                break;

            default:
                deplacementGardeGauche = deplacementGardeGauche * 0.25;
                break;
        }
        return deplacementGardeGauche;
    }

    private static boolean fouUtilisable(boolean joueurCourant) {
        if (joueurCourant) {
            return positionFou > positionRoi;
        } else {
            return positionFou < positionRoi;
        }
    }

    private static boolean sortirPersonnageChateauAdverse(boolean joueurCourant) {
        if (joueurCourant) {
            if (positionRoi > positionSorcier) {
                return ((positionGardeGauche == -8 || positionGardeGauche == -7) && positionSorcier > -7);
            } else {
                return false;
            }
        } else {
            if (positionRoi < positionSorcier) {
                return ((positionGardeDroit == 8 || positionGardeDroit == 7) && positionSorcier < 7);
            } else {
                return false;
            }
        }
    }

    private static boolean configSorcierVainqueur(boolean joueurCourant) {
        if (joueurCourant) {
            return (positionGardeDroit == 8) && (positionSorcier == 7);
        }
        return (positionGardeGauche == -8) && (positionSorcier == -7);
    }

    private static boolean tpGardePossible(boolean joueurCourant) {
        if (joueurCourant) {
            return (positionGardeDroit != 7) && (positionGardeDroit != 8);
        }
        return (positionGardeGauche != -7) && (positionGardeGauche != -8);
    }

    private static boolean sorcierDansChateau(boolean joueurCourant) {
        if (joueurCourant) {
            return positionSorcier > 6;
        }
        return positionSorcier < -6;
    }

    private static boolean gardeDroitDansChateau(boolean joueurCourant) {
        if (joueurCourant) {
            return positionGardeDroit > 6;
        }
        return positionGardeDroit < -6;
    }

    private static boolean gardeGaucheDansChateau(boolean joueurCourant) {
        if (joueurCourant) {
            return positionGardeGauche > 6;
        }
        return positionGardeGauche < -6;
    }

    private static boolean sorcierChezNous(boolean joueurCourant) {
        if (joueurCourant) {
            return positionSorcier > 0;
        }
        return positionSorcier < 0;
    }
}
