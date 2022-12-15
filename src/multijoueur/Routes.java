package multijoueur;

/**
 * Constantes des différentes routes/commandes disponible pour les fonctionnalités multijoueurs
 *
 * @author Manon
 */
public interface Routes {
    /* Routes de base */
    static final String EST_VERSUS = "isVersus";
    static final String CHAT = "aTous";
    static final String VERIF_PSEUDO = "check";
    static final String ENREG_PSEUDO = "register";
    static final String PRET_A_COMMENCER = "readyToStart";
    static final String JOUEURS_PRETS = "askAllReady";
    static final String COMMENCER_PARTIE = "startGame";
    
    /* Routes de parties compétitives */
    static final String ENVOYER_SCORE = "sendScore";
    static final String VICTOIRE_VERSUS = "versusVictory";
    static final String VICTOIRE_VERSUS_AUTRE = "otherVersusVictory";
    static final String DEFAITE_VERSUS = "versusDefeat";
    static final String DEFAITE_VERSUS_AUTRE = "otherVersusDefeat";
    
    /* Routes de parties coopératives */
    static final String ENVOYER_JEU = "sendGame";
    static final String AU_TOUR_DE = "turnOf";
    static final String VICTOIRE_COOP = "coopVictory";
    static final String DEFAITE_COOP = "coopDefeat";
}
