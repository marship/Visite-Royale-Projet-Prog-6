package Modele;

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
    }

    public double note(int joueur) {
        double note = 0;
        boolean calcul;
        if (joueur == JOUEUR_DROIT) {
            calcul = true;
        } else {
            calcul = false;
        }

        // Sorcier
        double resSorcierGentil = calculSorcier(calcul);
        double resSorcierMechant = calculSorcier(!calcul);
        double noteSorcier = 0;
        noteSorcier = (((resSorcierGentil) - resSorcierMechant) / 2);
        // NbPièces
        double resNbPiècesGentil = calculPiece(calcul);
        double resNbPiècesMechant = calculPiece(!calcul);
        double notePiece = 0;
        notePiece = (((resNbPiècesGentil) - resNbPiècesMechant) / 2);
        // Chateau
        double resNbPiècesChateauGentil = calculChateau(calcul);
        double resNbPiècesChateauMechant = calculChateau(!calcul);
        double noteChateau = 0;
        noteChateau = (((resNbPiècesChateauGentil) - resNbPiècesChateauMechant) / 2);
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
    // Théorème Sorcier
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
    // Théorème NBPièce
    // ===============
    double calculPiece(boolean joueurCourant) {
        double resNbPièces = 0;
        if (joueurCourant) {
            if (positionRoi > 0) {
                resNbPièces = resNbPièces + 1;
            }
            if (positionGardeDroit > 0) {
                resNbPièces = resNbPièces + 1;
            }
            if (positionGardeGauche > 0) {
                resNbPièces = resNbPièces + 1;
            }
            if (positionFou > 0) {
                resNbPièces = resNbPièces + 1;
            }
            if (positionSorcier > 0) {
                resNbPièces = resNbPièces + 1;
            }
        } else {
            if (positionRoi < 0) {
                resNbPièces = resNbPièces + 1;
            }
            if (positionGardeDroit < 0) {
                resNbPièces = resNbPièces + 1;
            }
            if (positionGardeGauche < 0) {
                resNbPièces = resNbPièces + 1;
            }
            if (positionFou < 0) {
                resNbPièces = resNbPièces + 1;
            }
            if (positionSorcier < 0) {
                resNbPièces = resNbPièces + 1;
            }
        }
        return resNbPièces;
    }

    // ==========================
    // Théorème NBPièceDansChateau
    // ==========================
    double calculChateau(boolean joueurCourant) {
        double resNbPiècesChateau = 0;
        if (joueurCourant) {
            if (positionRoi > 6) {
                resNbPiècesChateau = resNbPiècesChateau + 1;
            }
            if (positionGardeDroit > 6) {
                resNbPiècesChateau = resNbPiècesChateau + 1;
            }
            if (positionGardeGauche > 6) {
                resNbPiècesChateau = resNbPiècesChateau + 1;
            }
            if (positionFou > 6) {
                resNbPiècesChateau = resNbPiècesChateau + 1;
            }
            if (positionSorcier > 6) {
                resNbPiècesChateau = resNbPiècesChateau + 1;
            }
        } else {
            if (positionRoi < -6) {
                resNbPiècesChateau = resNbPiècesChateau + 1;
            }
            if (positionGardeDroit < -6) {
                resNbPiècesChateau = resNbPiècesChateau + 1;
            }
            if (positionGardeGauche < -6) {
                resNbPiècesChateau = resNbPiècesChateau + 1;
            }
            if (positionFou < -6) {
                resNbPiècesChateau = resNbPiècesChateau + 1;
            }
            if (positionSorcier < -6) {
                resNbPiècesChateau = resNbPiècesChateau + 1;
            }
        }
        resNbPiècesChateau = resNbPiècesChateau * 2;
        return resNbPiècesChateau;
    }

    // ===========
    // Théorème Roi
    // ===========
    double calculRoi(boolean joueurCourant) {
        double resRoi = 0;
        double coeff = 1;
        if(taillePioche <= 15 && !etatCouronne){
            coeff = 2;
        }
        if (joueurCourant) {
            resRoi = positionRoi * coeff;
        } else {
            resRoi = -positionRoi * coeff;
        }
        return resRoi;
    }

    // =================
    // Théorème Couronne
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
        } else {
            if (((positionCouronne == -6) && (calculChateau(joueurCourant) >= 4))
                    || ((positionCouronne == -5) && (calculChateau(joueurCourant) >= 6))) {
                resCouronne = resCouronne + 6;
            }
        }
        return resCouronne;
    }

    // ===========
    // Théorème Fou
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
                    double res = Math.round(deplacementGardeDroit / 3.14);
                    deplacementGardeDroit = multipliCoeff(res, deplacementGardeDroit);
                    somme += deplacementGardeDroit;
                    nbChateau++;
                }

                if (!sorcierDansChateau(joueurCourant)) {
                    double deplacementSorcier = 8 - positionSorcier;
                    deplacementSorcier = Math.abs(deplacementSorcier);
                    double res = Math.round(deplacementSorcier / 3.14);
                    deplacementSorcier = multipliCoeff(res, deplacementSorcier);
                    somme += deplacementSorcier;
                    nbChateau++;
                }

                double deplacementRoi = tpRoi(joueurCourant);
                deplacementRoi = Math.abs(deplacementRoi);
                double res = Math.round(deplacementRoi / 3.14);
                deplacementRoi = multipliCoeff(res, deplacementRoi);
                somme += deplacementRoi;

                double deplacementGardeGauche = tpGardeGauche();
                deplacementGardeGauche = Math.abs(deplacementGardeGauche);
                res = Math.round(deplacementGardeGauche / 3.14);
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
                    double res = Math.round(deplacementGardeGauche / 3.14);
                    deplacementGardeGauche = multipliCoeff(res, deplacementGardeGauche);
                    somme += deplacementGardeGauche;
                    nbChateau++;
                }

                if (!sorcierDansChateau(joueurCourant)) {
                    double deplacementSorcier = -8 - positionSorcier;
                    deplacementSorcier = Math.abs(deplacementSorcier);
                    double res = Math.round(deplacementSorcier / 3.14);
                    deplacementSorcier = multipliCoeff(res, deplacementSorcier);
                    somme += deplacementSorcier;
                    nbChateau++;
                }

                double deplacementRoi = tpRoi(joueurCourant);
                deplacementRoi = Math.abs(deplacementRoi);
                double res = Math.round(deplacementRoi / 3.14);
                deplacementRoi = multipliCoeff(res, deplacementRoi);
                somme += deplacementRoi;

                double deplacementGardeDroit = tpGardeDroit();
                deplacementGardeDroit = Math.abs(deplacementGardeDroit);
                res = Math.round(deplacementGardeDroit / 3.14);
                deplacementGardeDroit = multipliCoeff(res, deplacementGardeDroit);
                somme += deplacementGardeDroit;

                somme = somme / nbChateau;

                resFou = resFou + somme;

                if ((positionFou < positionRoi) && (positionGardeGauche == -8) && (positionFou - 4 <= -7)) {
                    resFou = resFou + 6;
                }
            }
        }
        return resFou;
    }

    // ==============
    // Théorème Gardes
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
