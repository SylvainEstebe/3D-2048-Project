/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ia;

import modele.Jeu;

/**
 * Classe qui instancie l'IA qui respecte le 1er algorithme
 *
 * @author Mouna
 */
public class IA1 {

    String joueur1 = "Max";
    String joueur2 = "Min";
    Jeu jeu;

    /**
     * Constructeur de l'IA
     *
     * @param j jeu associé à l'IA
     */
    public IA1(Jeu j) {
        jeu = j;
    }

    public void IA1() {
    }

    /*  public static void IA1() throws CloneNotSupportedException {
        int wins = 0;
        int total = 10;
        System.out.println("Running " + total + " games to estimate the accuracy:");

        for (int i = 0; i < total; ++i) {
            int hintDepth = 7;
            Board theGame = new Board();
            Direction hint = AIsolver.findBestMove(theGame, hintDepth);
            ActionStatus result = ActionStatus.CONTINUE;
            while (result == ActionStatus.CONTINUE || result == ActionStatus.INVALID_MOVE) {
                result = theGame.action(hint);
                if (result == ActionStatus.CONTINUE || result == ActionStatus.INVALID_MOVE) {
                    hint = AIsolver.findBestMove(theGame, hintDepth);
                }
            }

            if (result == ActionStatus.WIN) {
                ++wins;
                System.out.println("Game " + (i + 1) + " - won");
            } else {
                System.out.println("Game " + (i + 1) + " - lost");
            }
        }

        System.out.println(wins + " wins out of " + total + " games.");
    }*/ 
   /* public static Direction findBestMove(Board theBoard, int depth) throws CloneNotSupportedException {
        //Map<String, Object> result = minimax(theBoard, depth, Player.USER);
        
      //  Map<String, Object> result = alphabeta(theBoard, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, Player.USER);
        
        return (Direction)result.get("Direction");
    }
    
    
      private static Map<String, Object> minimax(Board theBoard, int depth, Player player) throws CloneNotSupportedException {
        Map<String, Object> result = new HashMap<>();
        
        Direction bestDirection = null;
        int bestScore;
        
        if(depth==0 || theBoard.isGameTerminated()) {
            bestScore=heuristicScore(theBoard.getScore(),theBoard.getNumberOfEmptyCells(),calculateClusteringScore(theBoard.getBoardArray()));
        }
        else {
            if(player == Player.USER) {
                bestScore = Integer.MIN_VALUE;

                for(Direction direction : Direction.values()) {
                    Board newBoard = (Board) theBoard.clone();

                    int points=newBoard.move(direction);
                    
                    if(points==0 && newBoard.isEqual(theBoard.getBoardArray(), newBoard.getBoardArray())) {
                    	continue;
                    }

                    Map<String, Object> currentResult = minimax(newBoard, depth-1, Player.COMPUTER);
                    int currentScore=((Number)currentResult.get("Score")).intValue();
                    if(currentScore>bestScore) { //maximize score
                        bestScore=currentScore;
                        bestDirection=direction;
                    }
                }
            }
            else {
                bestScore = Integer.MAX_VALUE;

                List<Integer> moves = theBoard.getEmptyCellIds();
                if(moves.isEmpty()) {
                    bestScore=0;
                }
                int[] possibleValues = {2, 4};

                int i,j;
                int[][] boardArray;
                for(Integer cellId : moves) {
                    i = cellId/Board.BOARD_SIZE;
                    j = cellId%Board.BOARD_SIZE;

                    for(int value : possibleValues) {
                        Board newBoard = (Board) theBoard.clone();
                        newBoard.setEmptyCell(i, j, value);

                        Map<String, Object> currentResult = minimax(newBoard, depth-1, Player.USER);
                        int currentScore=((Number)currentResult.get("Score")).intValue();
                        if(currentScore<bestScore) { //minimize best score
                            bestScore=currentScore;
                        }
                    }
                }
            }
        }
        
        result.put("Score", bestScore);
        result.put("Direction", bestDirection);
        
        return result;
    }*/

    private static int scoreHeuristique(int scoreGeneral, int nbCasesVides, int scoreCases) {
        int score = (int) (scoreGeneral + Math.log(scoreGeneral) * nbCasesVides - scoreCases);
        return Math.max(score, Math.min(scoreGeneral, 1));
    }

}
