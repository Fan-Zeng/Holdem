package holdem;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class Holdem {
    enum BEST_HAND_TYPE {
        ROYAL_FLUSH, 
        STRAIGHT_FLUSH, 
        FOUR_OF_A_KIND,
        FULL_HOUSE, 
        FLUSH, 
        STRAIGHT, 
        THREE_OF_A_KIND, 
        TWO_PAIR, 
        PAIR, 
        HIGH_CARD
    };
    


    private final Deck deck;
    private int posInDeck;
    int playerNum;
    public Player[] players = null;
    String[] board = null;
    String[] flop = null;
    String turn = "";
    String river = "";
    public int pot;
    List<Integer> winners = new ArrayList<>();

    public Holdem(int playerNum_) {
        int initialStack = 1000;
        playerNum = playerNum_;
        deck = new Deck();
        deck.shuffle();
        posInDeck = 0;
        board = new String[5];
        players = new Player[playerNum];
        for (int i = 0; i < playerNum; i++) {
            Player player = new Player(initialStack);
            players[i] = player;
        }
        pot = 0;
        winners = new ArrayList<>();
    }
    
    public void shuffle() {
        deck.shuffle();
        posInDeck = 0;
        for (int i = 0; i < playerNum; i++) {
            players[i].privateCard = null;
        }
        flop = null;
        turn = "";
        river = "";
        board = new String[5];
        winners = new ArrayList<>();
    }
    
    private String getCardAt(int i) {
        if (i < deck.cards.length) {
            return deck.cards[i].toString();
        } else {
            return "i = " + i + " is invalid";
        }
    }

    private String dealCard() {
        System.out.println(getCardAt(posInDeck));
        return getCardAt(posInDeck++);
    }

    public void dealCardForPlayer(int playerID) {
        if (players[playerID].privateCard == null) {
            players[playerID].privateCard = new String[2];
            for (int i = 0; i < 2; i++) {
                players[playerID].privateCard[i] = dealCard();
            }
        }
    }
    public String[] getPlayerCard(int playerID) {
        return players[playerID].privateCard;
    }

       
    public String[] getFlop() {
        if (flop == null) {
            flop = new String[3];
            for (int i = 0; i < 3; i++) {
                flop[i] = dealCard();
            }
        }
        System.arraycopy(flop, 0, board, 0, flop.length);
        return flop;
    }
    
    public String dealTurn() {
        if (turn.isEmpty()) {
           turn = dealCard();
        }
        board[3] = turn;
        return turn;
    }
    
    public String dealRiver() {
        if (river.isEmpty()) {
           river = dealCard();
        }
        board[4] = river;
        return river;
    }
    
    public String[] showDown() {
        String[] showDownResultString = new String[Holdem.this.players.length + 1]; // result for each player and an overall result
        int[] showDownResultValue = new int[Holdem.this.players.length];

        for (int i = 0; i < Holdem.this.players.length; i++) {
            Player player = Holdem.this.players[i];
            ShowDown showDown = new ShowDown();
            ShowDown.ShowDownResult showDownResult = showDown.getshowDownResult(player, board);
            showDownResultString[i] = "bestHand of Player " + (i+1) + ": " + showDownResult.toString();
            showDownResultValue[i] = showDownResult.getValue();
            System.out.println(showDownResultString[i]);
        }
        int maxShowDownResultValue = 0;
        for (int i = 0; i < Holdem.this.players.length; i++) {
            if (showDownResultValue[i] > maxShowDownResultValue) {
                maxShowDownResultValue = showDownResultValue[i];
            }
        }
        
        showDownResultString[showDownResultString.length-1] = "Player ";
        for (int i = 0; i < Holdem.this.players.length; i++) {
            if (showDownResultValue[i] == maxShowDownResultValue) {
                String winnerString = "";
                if (!winners.isEmpty()) {
                    winnerString += ", ";
                }
                winnerString += Integer.toString(i+1);
                showDownResultString[showDownResultString.length-1] += winnerString;
                winners.add(i);
            } 
        }
        showDownResultString[showDownResultString.length-1] += " has the best hand.";

        return showDownResultString;
    }

    public List<Integer> getWinners() {
        return winners;
    }


}
