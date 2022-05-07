
public class TestEvaluationPlateau {

    // Vrai = JoueurDroite
    // Faux = JoueurGauche
    static boolean joueurCourant = true;

    static double resSorcier = 0;
    static double resNbPièces = 0;
    static double resNbPiècesChateau = 0;
    static double resRoi = 0;
    static double resGardes = 0;
    static double resFou = 0;
    static double resCouronne = 0;
    
    static int positionGardeGauche = -8;
    static int positionGardeDroit = 8;
    static int positionRoi = 6;
    static int positionFou = 7;
    static int positionSorcier = 7;
    static int positionCouronne = 5;
    // 

    public static void main(String[] args) throws Exception {

        // ===============
        // Théorème Sorcier
        // ===============
        if (!((positionGardeGauche < positionSorcier) && (positionSorcier < positionGardeDroit)) && !sorcierChezNous()) {
            System.out.println("A CHIER");
            resSorcier = resSorcier - 1;
        }

        if (sorcierDansChateau() && tpGardePossible()) {
            resSorcier = resSorcier + 3;
        }

        if (configSorcierVainqueur()) {
            resSorcier = resSorcier + 6;
        }

        if (sortirPersonnageChateauAdverse()) {
            resSorcier = resSorcier + 1.5;
        }

        if (!(!((positionGardeGauche < positionSorcier) && (positionSorcier < positionGardeDroit)) && !sorcierChezNous())) {
            Double distMax = (double) 0;
            if (joueurCourant) {

                if((positionGardeDroit != positionSorcier) && positionRoi < positionSorcier){
                    // TP GARDE DROIT
                    int distGardeDroit = positionSorcier - positionGardeDroit;
                    distGardeDroit = Math.abs(distGardeDroit);
                    distMax = Math.max(distMax, distGardeDroit);
                }

                if((positionGardeGauche != positionSorcier) && positionRoi > positionSorcier){
                    // TP GARDE GAUCHE
                    int distGardeGauche = positionSorcier - positionGardeGauche;
                    distGardeGauche = Math.abs(distGardeGauche);
                    distMax = Math.max(distMax, distGardeGauche);
                }

                if( (positionGardeGauche < positionSorcier) && (positionGardeDroit > positionSorcier)){
                    // TP ROI
                    if(positionRoi < positionSorcier){
                        int distRoi = positionSorcier - positionRoi;
                        distRoi = Math.abs(distRoi);
                        distMax = Math.max(distMax, distRoi);
                    }
                }
            } else {
                if((positionGardeDroit != positionSorcier) && positionRoi < positionSorcier){
                    // TP GARDE DROIT
                    int distGardeDroit = positionSorcier - positionGardeDroit;
                    distGardeDroit = Math.abs(distGardeDroit);
                    distMax = Math.max(distMax, distGardeDroit);
                }

                if((positionGardeGauche != positionSorcier) && positionRoi > positionSorcier){
                    // TP GARDE GAUCHE
                    int distGardeGauche = positionSorcier - positionGardeGauche;
                    distGardeGauche = Math.abs(distGardeGauche);
                    distMax = Math.max(distMax, distGardeGauche);
                }

                if( (positionGardeGauche < positionSorcier) && (positionGardeDroit > positionSorcier)){
                    // TP ROI
                    if(positionRoi > positionSorcier){
                        int distRoi = positionSorcier - positionRoi;
                        distRoi = Math.abs(distRoi);
                        distMax = Math.max(distMax, distRoi);
                    }
                }
            }
            distMax = distMax / 2;
            resSorcier = resSorcier + distMax;
        }

        // ===============
        // Théorème NBPièce
        // ===============
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

        // ==========================
        // Théorème NBPièceDansChateau
        // ==========================
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

        // ===========
        // Théorème Roi
        // ===========
        if (joueurCourant) {
            resRoi = positionRoi;
        } else {
            resRoi = -positionRoi;
        }

        // =================
        // Théorème Couronne
        // =================
        if (joueurCourant) {
            if(positionCouronne > -3 && positionCouronne < 3){
                if(positionCouronne > 0){
                    resCouronne = resCouronne + 1;
                }
                else{
                    if(positionCouronne < 0){
                        resCouronne = resCouronne - 1;
                    }
                    else{
                        resCouronne = resCouronne + 0;
                    }
                }
            }
            else{
                if(positionCouronne > 2){
                    resCouronne = resCouronne + positionCouronne - 1;
                }
                else{
                    resCouronne = resCouronne + positionCouronne + 1;
                }
            }
        } else {
            if(positionCouronne > -3 && positionCouronne < 3){
                if(positionCouronne > 0){
                    resCouronne = resCouronne - 1;
                }
                else{
                    if(positionCouronne < 0){
                        resCouronne = resCouronne + 1;
                    }
                    else{
                        resCouronne = resCouronne + 0;
                    }
                }
            }
            else{
                if(positionCouronne > 2){
                    resCouronne = resCouronne - positionCouronne + 1;
                }
                else{
                    resCouronne = resCouronne - positionCouronne - 1;
                }
            }
        }
        if (joueurCourant) {
            if( ((positionCouronne == 6) && (resNbPiècesChateau >= 4)) || ((positionCouronne == 5) && (resNbPiècesChateau >= 6)) ){
                resCouronne = resCouronne + 6;
            }
        }
        else{
            if( ((positionCouronne == -6) && (resNbPiècesChateau >= 4)) || ((positionCouronne == -5) && (resNbPiècesChateau >= 6))) {
                System.out.println(resCouronne);
                resCouronne = resCouronne + 6;
            }
        }

        // ===========
        // Théorème Fou
        // ===========

        if (!fouUtilisable()) {
            resFou = resFou + 0;
        } else {
            if (joueurCourant) {
                double somme = 0.0;
                int nbChateau = 2;
                if (!gardeDroitDansChateau()) {
                    double deplacementGardeDroit = 8 - positionGardeDroit;
                    deplacementGardeDroit = Math.abs(deplacementGardeDroit);
                    double res = Math.round(deplacementGardeDroit / 3.14);
                    deplacementGardeDroit = multipliCoeff(res, deplacementGardeDroit);
                    somme += deplacementGardeDroit;
                    nbChateau++;
                }

                if(!sorcierDansChateau()){
                    double deplacementSorcier = 8 - positionSorcier;
                    deplacementSorcier = Math.abs(deplacementSorcier);
                    double res = Math.round(deplacementSorcier / 3.14);
                    deplacementSorcier = multipliCoeff(res, deplacementSorcier);
                    somme += deplacementSorcier;
                    nbChateau++;
                }

                double deplacementRoi = tpRoi();
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

                if( (positionFou > positionRoi) && (positionGardeDroit == 8) && (positionFou + 4 >= 7) ){
                    resFou = resFou + 6;
                }

            } else {

                double somme = 0.0;
                int nbChateau = 2;
                if (!gardeGaucheDansChateau()) {
                    double deplacementGardeGauche = -8 - positionGardeGauche;
                    deplacementGardeGauche = Math.abs(deplacementGardeGauche);
                    double res = Math.round(deplacementGardeGauche / 3.14);
                    deplacementGardeGauche = multipliCoeff(res, deplacementGardeGauche);
                    somme += deplacementGardeGauche;
                    nbChateau++;
                }

                if(!sorcierDansChateau()){
                    double deplacementSorcier = -8 - positionSorcier;
                    deplacementSorcier = Math.abs(deplacementSorcier);
                    double res = Math.round(deplacementSorcier / 3.14);
                    deplacementSorcier = multipliCoeff(res, deplacementSorcier);
                    somme += deplacementSorcier;
                    nbChateau++;
                }

                double deplacementRoi = tpRoi();
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

                if( (positionFou < positionRoi) && (positionGardeGauche == -8) && (positionFou - 4 <= -7) ){
                    resFou = resFou + 6;
                }
            }
        }


        // ==============
        // Théorème Gardes
        // ==============
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

        System.out.println("");
        System.out.println("=========================================");
        System.out.println("========= EVALUATION DU PLATEAU =========");
        System.out.println("=========================================");
        System.out.println("");

        double resPlateau = resSorcier + resNbPièces + resNbPiècesChateau + resRoi + resFou + resGardes + resCouronne;
        if (joueurCourant) {
            System.out.println("Evaluation Joueur de Droite : " + resPlateau);
        } else {
            System.out.println("Evaluation Joueur de Gauche : " + resPlateau);
        }

        System.out.println("");
        System.out.println("================ Détails ================");
        System.out.println("");

        System.out.println("Sorcier : " + resSorcier);
        System.out.println("Nb pieces : " + resNbPièces);
        System.out.println("Nb pieces chateau : " + resNbPiècesChateau);
        System.out.println("Roi : " + resRoi);
        System.out.println("Fou : " + resFou);
        System.out.println("Gardes : " + resGardes);
        System.out.println("Couronne : " + resCouronne);

        System.out.println("");
        System.out.println("========================================");

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

    private static double tpRoi() {
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

    private static boolean fouUtilisable() {
        if (joueurCourant) {
            return positionFou > positionRoi;
        } else {
            return positionFou < positionRoi;
        }
    }

    private static boolean sortirPersonnageChateauAdverse() {
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

    private static boolean configSorcierVainqueur() {
        if (joueurCourant) {
            return (positionGardeDroit == 8) && (positionSorcier == 7);
        }
        return (positionGardeGauche == -8) && (positionSorcier == -7);
    }

    private static boolean tpGardePossible() {
        if (joueurCourant) {
            return (positionGardeDroit != 7) && (positionGardeDroit != 8);
        }
        return (positionGardeGauche != -7) && (positionGardeGauche != -8);
    }

    private static boolean sorcierDansChateau() {
        if (joueurCourant) {
            return positionSorcier > 6;
        }
        return positionSorcier < -6;
    }

    private static boolean gardeDroitDansChateau() {
        if (joueurCourant) {
            return positionGardeDroit > 6;
        }
        return positionGardeDroit < -6;
    }

    private static boolean gardeGaucheDansChateau() {
        if (joueurCourant) {
            return positionGardeGauche > 6;
        }
        return positionGardeGauche < -6;
    }

    private static boolean sorcierChezNous() {
        if (joueurCourant) {
            return positionSorcier > 0;
        }
        return positionSorcier < 0;
    }
}
