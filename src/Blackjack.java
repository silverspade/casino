import java.util.ArrayList;
import java.util.Scanner;

public class Blackjack {
	static Scanner in = new Scanner(System.in);

	public static void main(String[] args) {
		//Setting up the deck
		String[] ranks = new String[] {"2","3","4","5","6","7","8","9","10","jack","queen","king","ace"};
		String[] suits = new String[] {"hearts","clubs","diamonds","spades"};
		int[] values = new int[] {2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10, 1};
		Deck cardDeck = new Deck(ranks, suits, values);
		
		System.out.println("Welcome to Blackjack! 1.Do you want instructions, 2.Go ahead and play");
		int choice = in.nextInt();
		if (choice == 1) {
			System.out.println("You will be dealt 2 cards to start.\nThe goal of the game is to get as close to 21 as possible without going over.\nYou can ask the dealer for more cards (one at a time).\nThe ace can be a 1 or an 11, however you see fit.\nLet's begin! ");
		} else {
			System.out.println("Let's begin!");
		}
		
		//Dealing the first two cards - Player
		Card pFirst = cardDeck.deal();
		Card pSec = cardDeck.deal();
		ArrayList<Card> player = new ArrayList<Card>();
		player.add(pFirst);
		player.add(pSec);
		System.out.println("\nYour hand is " + player.toString() + "and your sum is " + addUp(player));
		int result = askHit(cardDeck, player);
		if (result == -1) {
			System.out.println("Thank you for playing!");
		} else {
			Card dFirst = cardDeck.deal();
			Card dSec = cardDeck.deal();
			ArrayList<Card> dealer = new ArrayList<Card>();
			dealer.add(dFirst);
			dealer.add(dSec);
			System.out.println("\nThe dealer's hand is " + dealer.toString() + "and their sum is " + addUp(dealer));
			if (addUp(dealer) < 16) {
				result = hit(cardDeck, dealer);
				if (result == 1) {
					System.out.println("Thank you for playing!");
				}
			}
			System.out.println("Let's compare");
			if (addUp(player) > addUp(dealer)) {
				System.out.println("You win! Congratulations!");
			} else {
				System.out.println("You lose!");
			}
			System.out.println("Thank you for playing!");
		}

	}
	
	private static void askAce(Card ace) {
		System.out.println("Would you like the ace to be 1 or 11?");
		int choice = in.nextInt();
		if (choice == 1) {
			ace.setPointValue(1);
		} else if (choice == 11){
			ace.setPointValue(11);
		} else {
			System.out.println("Please enter 1 or 11");
			askAce(ace);
		}
	}
	
	private static int askHit(Deck cardDeck, ArrayList<Card> player) {
		System.out.println("Would you like another card? 1.Yes, 2. No");
		int choice = in.nextInt();
		if (choice == 1) {
			Card extraCard = cardDeck.deal();
			player.add(extraCard);
			System.out.println("You got " + extraCard.toString());
			if (addUp(player) > 21) {
				System.out.println("Your hand is now " + player.toString() + " and your sum is now " + addUp(player));
				System.out.println("You busted! You lose!");
				return -1;
			} else {
				System.out.println("Your hand is now " + player.toString() + " and your sum is now " + addUp(player));
				askHit(cardDeck, player);
				return 0;
			}
		} else if (choice == 2) {
			System.out.println("Alright, let's check the dealer's hand");
			return 1;
		} else {
			System.out.println("Please enter a valid response");
			askHit(cardDeck, player);
			return 0;
		}
	}
	
	private static int addUp(ArrayList<Card> hand) {
		int sum = 0;
		for (int i = 0; i < hand.size(); i++) {
			sum += hand.get(i).pointValue();
		}
		return sum;
	}
	
	private static int hit(Deck cardDeck, ArrayList<Card> dealer) {
		Card extraCard = cardDeck.deal();
		dealer.add(extraCard);
		if (addUp(dealer) > 21) {
			System.out.println("Their hand is now " + dealer.toString() + " and their sum is now " + addUp(dealer));
			System.out.println("They busted! You win!");
			return 1;
		} else {
			if (addUp(dealer) < 17) {
				System.out.println("Their hand is now " + dealer.toString() + " and their sum is now " + addUp(dealer));
				hit(cardDeck, dealer);
				return 0;
			} else {
				System.out.println("Their hand is now " + dealer.toString() + " and their sum is now " + addUp(dealer));
				return -1; 
			}
		}
	}
}