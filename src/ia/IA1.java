/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ia;

/**
 *
 * @author sylvainestebe
 */
public class IA1 {
    
    
    private static int heuristicScore(int actualScore, int numberOfEmptyCells, int scoreVoisin) {
     int score = (int) (actualScore+Math.log(actualScore)*numberOfEmptyCells -scoreVoisin);
     return Math.max(score, Math.min(actualScore, 1));
     
}
    
    //  CASE : scoreVoisin (La valeur de la case - la valeur de son/ses voisins) ensuite on fait la moyenne pour une case, on fait ça pour chaque case, moyenne pour une grille et ensuite pour les 
    // 3 grilles
    // https://blog.datumbox.com/using-artificial-intelligence-to-solve-the-2048-game-java-code/
    // 
    //
    //
    //
    
}
