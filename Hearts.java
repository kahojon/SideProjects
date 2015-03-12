//Code Written By: Jonathan Leung 
public class Hearts
{
  public static void main(String[] args)
  {
    System.out.println("Welcome to McGill Hearts!");
    Player[] players = new Player[4]; //Initiates the players array of size 4
    players[0] = new HumanPlayer(); 
    players[1] = new ComputerPlayer();
    players[2] = new ComputerPlayer();
    players[3] = new ComputerPlayer();

    boolean hasOneHundred = false;
    CardPile deck = new CardPile(); //Main deck cardpile is initialized
    CardPile currentTrick = new CardPile(); //CurrentTrick cardpile is initialized
    while (!hasOneHundred) // Loop terminates when a player has 100+ points
    {
      Card twoClubs = new Card(Suit.CLUBS,Value.TWO);
      deck = deck.makeFullDeck(); //Creates a full deck every round
      System.out.println("New Round!");
      for (int i = 0; i < 13; i++)
      {
        for (int j = 0; j < 4; j++)
        {
          players[j].dealCardTo(deck.removeCard(0)); //Deals 13 cards to every player by removing the card on the top of
                                                     //the cardpile: deck each time
        }
      }
      int playerNumber = 0;
      for (int t = 0; t < 13; t++) //Loop is in charge of repeating for every trick taken
      {
        for (int j = 0; j < 4; j++) //Loop keeps track of players turn
        {
          for (int i = 0; i < 4; i++) //Loop tries to find the player that has the two of clubs
          {
            boolean playerHasCard = players[i].containsCard(twoClubs);
            if (playerHasCard)
            { 
              System.out.println("Player " + i + " has the TWO of CLUBS!");
              System.out.println("Player " + i + ", it is your turn!");
              playerNumber = i; //Equates i to playerNumber in order to indicate which player started first
              Card choice = players[playerNumber].playLegalMove(currentTrick); //Player that starts first chooses card
              currentTrick.addToBottom(choice); //Chosen card is added to cardpile: currentTrick
              twoClubs=choice; //twoClubs is equated to chosen card to prevent player from starting first in next trick
              playerNumber++; //Adds one in order to pass the turn to the next person
              j++; //Adds one to j in order for the loop to only run three more times after this loop
              
            }
          }
          if (playerNumber > 3) //Ensures that highest playerNumber is 3 and not larger
          {
            playerNumber = playerNumber - 4;
          }
          System.out.println("Player " + playerNumber + ", it is your turn!"); 
          
          Card choice = players[playerNumber].playLegalMove(currentTrick); //player equal to playerNumber chooses card
          currentTrick.addToBottom(choice); //Chosen card is added to cardpile: currentTrick
          playerNumber++;
          if (playerNumber > 3)//Ensures that highest playerNumber is 3 and not larger after player takes turn. Variable
                               //playerNumber will be used later. After the last player plays the last card to the 
                               //currentTrick, above statement playerNumber++ adds one in order to indicate which player
                               //started first.
          {
            playerNumber = playerNumber - 4;
          }
        }
        int winnerCard = 0; 
        Suit initatedSuit = currentTrick.peak(0).getSuit();//The first suit played in trick is assigned to initiatedSuit
        for (int i = 0; i < 4; i++)
        {
          int greatestValue = currentTrick.peak(winnerCard).getValue().ordinal();//Gets the value of current winnerCard
          if (currentTrick.peak(i).getSuit() == initatedSuit && //Executes if card at index i of currentTrick cards                                                   
             (currentTrick.peak(i).getValue().ordinal()) > greatestValue) //array is higher than the current winnerCard
          {
            winnerCard = i; //Sets i as the current winnerCard if the if statement executes
          }
        }
        int trickTaker = winnerCard + playerNumber; //Takes into account which player started first in order 
                                                    //to determine the player who will take the currentTrick
        if (trickTaker > 3) //Ensures trickTaker does not exceed the value of 3 and cause an ArrayOutOfBounds error
                            
        {
          trickTaker = trickTaker - 4;
        }
        players[trickTaker].takeCards(currentTrick); //Player who won the trick takes the currentTrick 
        System.out.println("Player " + trickTaker + " wins the trick!");
        playerNumber = trickTaker; //Player who won the trick will play the first card in the next trick
      }
      System.out.println("Round Over! These are the points of each player!");
      for (int i = 0; i < 4; i++)
      {
        //This segment of code checks if a player shot the moon
        int counter = 0;
        Card queenOfSpades = new Card(Suit.SPADES,Value.QUEEN);
        for (int j = 0; j < players[i].taken.getNumCards(); j++)
        {
          if(players[i].taken.peak(j).getSuit() == Suit.HEARTS) //Counter increases by one if a card with the suit: 
                                                                //hearts is encountered in the loop
          {
            counter++;
          }
        }
        if(players[i].taken.containsCard(queenOfSpades)) //Counter increases by one if the queen of spades is present
        {
          counter++;
        }
        if (counter == 14)//If the counter = 14, then the player took all of the hearts cards and the queen of spades
        {
          players[i].addPoints(-26); //Ensures that the player who shot the moon receives no points
          System.out.println("Player " + i + " shoots the moon!"); //Indicates which player shot the moon
          for (int k = 1; k < 4; k++)
          {
            int unfortunatePlayers = i+k;//The int variable indicates which three players receive 26 points using a loop
            if (unfortunatePlayers > 3) //Ensures that the value of the variable does not go over 3, prevents
                                        //ArrayOutOfBounds Exception
            {
              unfortunatePlayers = unfortunatePlayers - 4;
            }
            players[unfortunatePlayers].addPoints(26); //Adds 26 points to those unfortunate players!
          }
        }
        
        players[i].addPoints(players[i].countPoints());//Counts and adds points to each player based on their taken cards
        if (players[i].getPoints() >= 100) //Executes if a player reaches 100+ points. Returns hasOneHundred as true and 
                                           //breaks while loop
        {
          hasOneHundred = true;
        }
      }
      for (int i = 0; i < 4; i++) //Loop prints out the points that each player currently has after all 52 cards are 
                                  //taken for that round. After points are printed, the redeal method is called.
      {
        System.out.println("Player " + i + " has " + players[i].getPoints() + " points!");
        players[i].redeal();
      }
      if(!deck.isEmpty()) //To prevent any errors, the if statement checks if the deck is not empty. If it isn't it
                          //empty,the deck will be emptied. (Not a required if statement since the program will ensure 
                          //that the deck is emptied after all 52 cards are taken.)
      {
        deck.clear();
      }
    }
    int winner = 0;
    for (int i = 1; i < 4; i++) //Loop compares points of all players and determines which player has the lowest points
    {
      if (players[i].getPoints() < players[winner].getPoints()) //Executes when the tested player's points is lower than
                                                                //the current winner's points
      {
        winner = i; //The winner is then updated until the loop finishes. The winner will then be determined
      }
    }
    //Game ends and the winner is displayed
    System.out.println ("Game Over!");
    System.out.println ("The winner is Player " + winner + " with " + players[winner].getPoints() + " points!");
    
  }
}
