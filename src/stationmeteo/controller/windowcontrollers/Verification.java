package stationmeteo.controller.windowcontrollers;

import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import stationmeteo.java.metier.Icapteur;
import stationmeteo.java.algorithmes.Algorithme;

/**
 * Classe offrant une méthode de vérification des informations rentrées dans une fenêtre d'ajout ou de modification de capteur.
 * @author Clément
 */
class Verification {

    private final static String REGNUM="^-?[0-9]*(.[0-9]+)?$";  //Expression régulière correspondant à un Float
    private final static String REGINT="^[0-9]+$";              //Expression régulière correspondant à un Int strictement positif ou nul
    private final static String REGPOS="^[1-9.]*(.[0-9]+)?$";   //Expression régulière correspondant à un Float strictement supérieur ou égal à 1

    /**
     * Fonction vérifiant les informations envoyées par l'utilisateur.
     * @param selectedAlgo L'algorithme sélectionné
     * @param nomCapteur Le nom choisi
     * @param idCapteur L'id attribuée
     * @param actualisationCapteur L'actualisation désirée
     * @param temperatureCapteur La température paramétrée
     * @param onAlgoFixeAfficher1 Dans le cas d'un algorithme de type Fixe, la borne minimale
     * @param onAlgoFixeAfficher2 Dans le cas d'un algorithme de type Fixe, la borne maximale
     * @param intervalleAlgo Dans le cas d'un algorithme de type Réaliste, la taille de la fenêtre
     * @return booléen indiquant la validité des informations
     */
    public static boolean verifInfos(Algorithme selectedAlgo, TextField nomCapteur, TextField idCapteur, TextField actualisationCapteur,
                                     TextField temperatureCapteur, TextField onAlgoFixeAfficher1, TextField onAlgoFixeAfficher2,
                                     TextField intervalleAlgo) {
        {
            if (selectedAlgo == null) return true;

            if (nomCapteur.getText().isEmpty()
                    || idCapteur.getText().isEmpty()
                    || actualisationCapteur.getText().isEmpty()) //Si un des textFiels nom, id ou actualisation est vide, infos fausses
                return true;

            if (temperatureCapteur.getText().isEmpty()
                    || !temperatureCapteur.getText().matches(REGNUM)) //Si la température n'est pas spécifiée ou ne correspond pas à REGNUM,
                                                                        // la température est 0 par défaut
                temperatureCapteur.setText("0");

            if (!idCapteur.getText().matches(REGINT)
                    || !actualisationCapteur.getText().matches(REGINT)) //Si l'id ne correspond pas à REGINT
                                                                        // ou l'actualisation ne correspond pas à REGPOS, infos fausses
                return true;

            if (!onAlgoFixeAfficher1.isDisable()) {                     //Si l'algorithme est de type Fixe,

                if (!onAlgoFixeAfficher1.getText().matches(REGNUM) || !onAlgoFixeAfficher2.getText().matches(REGNUM))
                    return true;   //et que un des champs n'est pas conforme à REGNUM, infos fausses

                if (Float.parseFloat(onAlgoFixeAfficher1.getText()) >= Float.parseFloat(onAlgoFixeAfficher2.getText()))
                    return true;   //et que la borne minimale est supérieure ou égale à la borne maximale, infos fausses

            }

            return !intervalleAlgo.isDisable() &&
                    (!intervalleAlgo.getText().matches(REGPOS) ||
                            (intervalleAlgo.getText().isEmpty())); //Retourne Vrai si l'algo n'est pas réaliste
                                                                                        //ou la taille de la fenêtre de variation est spécifiée
                                                                                        //et conforme et à REGPOS
        }
    }
    /**
     * Fonction vérifiant les informations envoyées par l'utilisateur.
     * @param nomCapteur Le nom choisi
     * @param idCapteur L'id attribuée
     * @param selectedCapteurs le capteurChoisi
     */
    public static boolean verifInfoSuperCapteur( TextField nomCapteur, TextField idCapteur,ListView<Icapteur> selectedCapteurs) {
        //Si un des textFiels nom  ou id est vide, OU que l'id n'est pas valide, OU qu'aucun capteur n'a été sélectionné, FAUX
        return !nomCapteur.getText().isEmpty()
                && !idCapteur.getText().isEmpty() && idCapteur.getText().matches(REGINT) &&
                    !selectedCapteurs.getItems().isEmpty();

    }
    
}
