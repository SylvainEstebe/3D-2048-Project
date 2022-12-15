package multijoueur.serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import multijoueur.Routes;

/**
 * Connexion d'un client au serveur
 * Gestion des requêtes coté serveur
 * 
 * @author Manon
 */
public class Connexion implements Runnable {
    private Socket socket;
    private Serveur serveur;
    private BufferedReader in;
    private PrintWriter out;
    private boolean debug = false;
    
    // Données de base
    private String pseudo;
    private boolean pret = false;
    
    // Données de parties compétitives
    private int score = 0;
    private int valeurMax = 0;
    private boolean victoire = false;
    private int classement = 0;
    private boolean defaite = false;

    /**
     * Constructeur
     * 
     * @param s Socket de la connexion entre le client et le serveur
     * @param srv Serveur
     */
    public Connexion(Socket s, Serveur srv) {
        this.socket = s;
        this.serveur = srv;
        
        try {
            this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.out = new PrintWriter(this.socket.getOutputStream(), true);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    /**
     * Lancement de la connexion
     */
    public void run() {
        try {
            while (true) {
                String requete = this.in.readLine();
                if (this.debug) System.out.println("[SERVEUR] Recu : " + (requete.length() > 32 ? requete.substring(0, 32) : requete));
                
                if (requete == null) break;
                
                /* Routes de base */
                if (requete.equals(Routes.EST_VERSUS)) { // Demande du mode de jeu
                    this.estCompetitif();
                } if (requete.startsWith(Routes.EST_VERSUS)) { // Changement de mode de jeu
                    this.setCompetitif(requete);
                } else if (requete.startsWith(Routes.CHAT)) { // Message
                    this.envoyerATous(this.escapeRequest(requete));
                } else if (requete.startsWith(Routes.VERIF_PSEUDO)) { // Vérification disponibilité pseudo
                    this.pseudoDispo(this.escapeRequest(requete));
                } else if (requete.startsWith(Routes.ENREG_PSEUDO)) { // Enregistrement pseudo
                    this.enregistrerJoueur(this.escapeRequest(requete));
                } else if (requete.equals(Routes.PRET_A_COMMENCER)) { // Joueur prêt
                    this.setJoueurPret(true);
                } else if (requete.equals(Routes.JOUEURS_PRETS)) { // Vérifications que tous les joueurs sont prêts
                    this.joueursPrets();
                } else if (requete.equals(Routes.COMMENCER_PARTIE)) { // Lancement de la partie
                    this.commencerPartie();
                } else 
                
                /* Routes de parties compétitives */
                if (requete.startsWith(Routes.ENVOYER_SCORE)) { // Envoi du score
                    this.scoreEnvoye(this.escapeRequest(requete));
                } else if (requete.equals(Routes.VICTOIRE_VERSUS)) { // Victoire en compétitif
                    this.victoireVersus();
                } else if (requete.equals(Routes.DEFAITE_VERSUS)) { // Défaite en compétitif
                    this.defaiteVersus();
                } else
                
                /* Routes de parties coopératives */
                if (requete.startsWith(Routes.ENVOYER_JEU)) { // Envoi du jeu après un tour terminé
                    this.envoyerJeu(requete);
                } else if (requete.equals(Routes.VICTOIRE_COOP)) { // Victoire en ccop
                    this.victoireCoop();
                } else if (requete.equals(Routes.DEFAITE_COOP)) { // Défaire en coop
                    this.defaiteCoop();
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                this.socket.close();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    /**
     * Envoie un message à tous les autres clients
     * 
     * @param message Message à envoyer  
     */
    private void envoyerATous(String message) {
        for (Connexion client : this.serveur.getClients()) {
            // Pour les clients autres que celui qui a envoyé le message (equals a implémenter pour aue cela fonctionne)
            if (!client.equals(this)) {
                client.out.println(message);
            }
        }
    }
    
    /**
     * Demande du mode de jeu par un client
     */
    private void estCompetitif() {
        this.out.println(Routes.EST_VERSUS + " " + this.serveur.estCompetitif());
    }
    
    /**
     * Actualise le mode de jeu et transmet l'information autres joueurs
     * 
     * @param data 
     */
    private void setCompetitif(String data) {
        this.serveur.setCompetitif(data.equals(Routes.EST_VERSUS + " true"));
        this.envoyerATous(data);
    }
    
    /**
     * Vérifie la disponibilité d'un pseudo
     * @param p 
     */
    private void pseudoDispo(String p) {
        boolean dispo = true;
        
        for (Connexion client : this.serveur.getClients()) {
            if (dispo && client.pseudo != null && client.pseudo.toLowerCase().equals(p.toLowerCase())) dispo = false;
        }
        
        this.out.println(Routes.VERIF_PSEUDO + " " + dispo);
    }
    
    /**
     * Enregistre le pseudo du joueur associé à cette connexion
     * 
     * @param p Pseudo 
     */
    private void enregistrerJoueur(String p) {
        this.pseudo = p;
        
        this.out.println(p + " enregistré");
    }
    
    /**
     * Setter pour le statut prêt du joueur
     * 
     * @param p Statut prêt
     */
    private void setJoueurPret(boolean p) {
        this.pret = p;
    }
    
    /**
     * Vérifie que tous les joueurs sont prêts
     */
    private void joueursPrets() {
        this.pret = true;
        
        boolean prets = true;
        
        for (Connexion client : this.serveur.getClients()) {
            if (prets && !client.pret) prets = false;
        }
        
        if (this.serveur.getClients().size() < 2) prets = false;
        
        this.out.println(Routes.JOUEURS_PRETS + " " + prets);
    }
    
    /**
     * Lance une partie multijoueur après confirmation 
     */
    private void commencerPartie() {
        for (Connexion client : this.serveur.getClients()) {
            client.out.println(Routes.COMMENCER_PARTIE + " " + this.serveur.estCompetitif());
        }
        
        // Notification du prochain joueur s'il s'agit d'une partie coopérative
        if (!this.serveur.estCompetitif()) {
            Connexion nextClient = this.serveur.getNextClient();
            for (Connexion client : this.serveur.getClients()) {
                client.out.println(Routes.AU_TOUR_DE + " " + nextClient.pseudo);
            }
        }
    }
    
    /**
     * Enregistre le score envoyé par le client et transmet l'information aux autres joueurs
     * 
     * @param data Données envoyées par le client 
     */
    private void scoreEnvoye(String data) {
        int splitter = data.indexOf("|");
        this.score = Integer.parseInt(data.substring(0, splitter));
        this.valeurMax = Integer.parseInt(data.substring(splitter + 1));
        this.envoyerATous(Routes.ENVOYER_SCORE + " " + this.pseudo + "#" + this.score + "|" + this.valeurMax);
    }
    
    /**
     * Enregistre la victoire, détermine le classement, transmet l'information aux autres joueurs et donne le classement au joueur concerné
     */
    private void victoireVersus() {
        this.victoire = true;
        
        // Calcul du classement
        for (Connexion client : this.serveur.getClients()) {
            if (client.victoire) this.classement++;
        }
        
        this.envoyerATous(Routes.VICTOIRE_VERSUS_AUTRE + " " + this.pseudo + "#" + this.classement);
        this.out.println(Routes.VICTOIRE_VERSUS + " " + this.classement);
    }
    
    /**
     * Enregistre la défaite, détermine le classement, transmet l'information aux autres joueurs et donne le classement au joueur concerné
     */
    private void defaiteVersus() {
        this.defaite = true;
        
        this.classement = this.serveur.getClients().size() + 1;
        
        // Calcul du classement
        for (Connexion client : this.serveur.getClients()) {
            if (client.defaite) this.classement--;
        }
        
        this.envoyerATous(Routes.DEFAITE_VERSUS_AUTRE + " " + this.pseudo + "#" + this.classement);
        this.out.println(Routes.DEFAITE_VERSUS + " " + this.classement);
    }
    
    /**
     * Envoie le jeu aux autres joueurs et annonce le joueur suivant
     * 
     * @param requete Requête envoyée par le client
     */
    private void envoyerJeu(String requete) {
        this.envoyerATous(requete);
        Connexion nextClient = this.serveur.getNextClient();
        for (Connexion client : this.serveur.getClients()) {
            client.out.println(Routes.AU_TOUR_DE + " " + nextClient.pseudo);
        }
    }
    
    /**
     * Enregistre la victoire et transmet l'information aux autres joueurs
     */
    private void victoireCoop() {
        for (Connexion client : this.serveur.getClients()) {
            client.victoire = true;
        }
        
        this.envoyerATous(Routes.VICTOIRE_COOP + " " + this.pseudo);
    }
    
    /**
     * Enregistre la défaite et transmet l'information aux autres joueurs
     */
    private void defaiteCoop() {
        for (Connexion client : this.serveur.getClients()) {
            client.defaite = true;
        }
        
        this.envoyerATous(Routes.VICTOIRE_COOP + " " + this.pseudo);
    }
    
    /**
     * Setter pour le débuggage (affichage de requêtes recus)
     * 
     * @param d Active ou non le débuggage
     */
    public void setDebug(boolean d) {
        this.debug = d;
    }
    
    /**
     * Getter du pseudo
     * 
     * @return Pseudo du joueur associé à cette connexion
     */
    public String getPseudo() {
        return this.pseudo;
    }
    
    /**
     * Getter du score du client
     * 
     * @return Score 
     */
    public int getScore() {
        return this.score;
    }
    
    /**
     * Getter de la valeur max du client
     * 
     * @return Valeur max 
     */
    public int getValeurMax() {
        return this.valeurMax;
    }
    
    /**
     * Vérifie l'égalite avec une autre connexion (vérification de l'égalité entre les sockets)
     * 
     * @param c Autre connexion
     * @return true si égaux, false sinon
     */
    public boolean equals(Connexion c) {
        return this.socket.equals(c.socket);
    }
    
    /**
     * Supprime l'entête de la requête
     * 
     * @param request Requête
     * @return Données de la requête sans l'entête
     */
    private String escapeRequest(String request) {
        int premierEspace = request.indexOf(" ");
        return request.substring(premierEspace + 1);
    }
}
