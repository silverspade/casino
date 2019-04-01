//TO BE DONE: ADD IN CHIPS AND BETTING

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Blackjack {
	static Scanner in = new Scanner(System.in);

	public static void main(String[] args) {
		Deck cardDeck = intro();
		
		//Giving out chips
		int chips = 100;
		int bet = betting(100);
		
		play(cardDeck, chips, bet);
		
	}
	
	private static Deck intro() {
			//Setting up the deck and chips
			String[] ranks = new String[] {"2","3","4","5","6","7","8","9","10","jack","queen","king","ace"};
			String[] suits = new String[] {"hearts","clubs","diamonds","spades"};
			int[] values = new int[] {2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10, 1};
			Deck cardDeck = new Deck(ranks, suits, values);				

			System.out.println("Welcome to Blackjack! 1.Do you want instructions, 2.Go ahead and play");
			int choice = in.nextInt();
			if (choice == 1) {
				System.out.println("You will be dealt 2 cards to start.\nThe goal of the game is to get as close to 21 as possible without going over.\nYou can ask the dealer for more cards (one at a time).\nThe ace can be a 1 or an 11, however you see fit.\nLet's begin! ");
			} else {
				System.out.println("\nLet's begin!");
			}
			System.out.println("\nYou will start with 200 chips. We're a brand new casino, so each chip is $1");
			return cardDeck;
	}
	
	private static int betting(int chips) {
		System.out.println("How many chips would you like to bet?");
		int bet = in.nextInt();
		while (bet > 200 || bet < 0) {
			System.out.println("Please enter a valid number of chips to bet.");
			bet = in.nextInt();
		}
		return bet;
	}
	
	private static void play(Deck cardDeck, int chips, int bet) {
		//Dealing the first two cards - Player
				Card pFirst = cardDeck.deal();
				Card pSec = cardDeck.deal();
				ArrayList<Card> player = new ArrayList<Card>();
				player.add(pFirst);
				player.add(pSec);
				System.out.println("\nYour sum is " + addUp(player));
				sort(player);
				System.out.println("Your hand is " + player.toString());
				boolean blackjack = false;
				if (player.get(0).rank() == "king" || player.get(0).rank() == "queen" || player.get(0).rank() == "jack") {
					if (player.get(1).rank() == "ace") {
						blackjack = true;
					}
				} else if (player.get(0).rank() == "ace") {
					if (player.get(1).rank() == "king" || player.get(1).rank() == "queen" || player.get(1).rank() == "jack") {
						blackjack = true;
					}
				}
				if (blackjack == false) {
					int result = askHit(cardDeck, player);
					while (result == 0) {
						result = askHit(cardDeck, player);
					}
					if (result == -1) { 
						chips -= bet;
						System.out.println("Your number of chips is now " + chips);
					} else {
						Card dFirst = cardDeck.deal();
						Card dSec = cardDeck.deal();
						ArrayList<Card> dealer = new ArrayList<Card>();
						dealer.add(dFirst);
						dealer.add(dSec);
						System.out.println("\nThe dealer's sum is " + addUp(dealer));
						sort(dealer);
						System.out.println("The dealer's hand is " + dealer.toString());
						if (addUp(dealer) < 16) {
							result = hit(cardDeck, dealer);
							while (result == 0) {
								result = hit(cardDeck, dealer);
							}
							if (result == 1) {
								chips += bet;
								System.out.println("Your number of chips is now " + chips);
							} else {
								int pSum = addUp(player);
								int dSum = addUp(dealer);
								System.out.println("Let's compare: Player: " + pSum + ", Dealer: " + dSum);
								if (pSum > dSum) {
									System.out.println("You win! Congratulations!");
									chips += bet;
									System.out.println("Your number of chips is now " + chips);
								} else if (pSum == dSum) {
									System.out.println("You tie!");
									System.out.println("Your number of chips is now " + chips);
								} else if (pSum < dSum) {
									System.out.println("You lose!");
									chips -= bet;
									System.out.println("Your number of chips is now " + chips);
								}
							}
						} else {
							int pSum = addUp(player);
							int dSum = addUp(dealer);
							System.out.println("Let's compare: Player: " + pSum + ", Dealer: " + dSum);
							if (pSum > dSum) {
								System.out.println("You win! Congratulations!");
								chips += bet;
								System.out.println("Your number of chips is now " + chips);
							} else if (pSum == dSum) {
								System.out.println("You tie!");
								System.out.println("Your number of chips is now " + chips);
							} else if (pSum < dSum) {
								System.out.println("You lose!");
								chips -= bet;
								System.out.println("Your number of chips is now " + chips);
							}
						}
						
					} 
				} else {
					System.out.println("You win with Blackjack! Congratulations!");
					chips += (bet * 1.5);
					System.out.println("Your number of chips is now " + chips);
				}
				
				System.out.println("\nWould you like to cash out? 1.Yes, 2.No");
				int response = in.nextInt();
				while (response != 1 && response != 2) {
					System.out.println("Please enter 1 for Yes or 2 for No.");
					response = in.nextInt();
				}
				if (response == 1) {
					if (chips < 200) {
						System.out.println("You lost some money! Your total is $" + chips + "\nThank you for playing!");
					} else if (chips == 200) {
						System.out.println("You ended up with the starting amount! Your total is $" + chips + "\nThank you for playing!");
					} else {
						System.out.println("Congratulations! Your total is $" + chips + "\nThank you for playing!");
					}
				} else {
					System.out.println("Let's move on to a new round then!");
					bet = betting(chips);
					play(cardDeck, chips, bet);
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
				System.out.println("Your sum is now " + addUp(player));
				sort(player);
				System.out.println("Your hand is now " + player.toString());
				System.out.println("You busted! You lose!");
				return -1;
			} else {
				System.out.println("Your sum is now " + addUp(player));
				sort(player);
				System.out.println("Your hand is now " + player.toString());
				return 0;
			}
		} else if (choice == 2) {
			System.out.println("Alright, let's check the dealer's hand");
			return 1;
		} else {
			System.out.println("Please enter a valid response");
			return 0;
		}
	}
	
	private static int addUp(ArrayList<Card> hand) {
		int sum1 = 0;
		int result = 0;
		for (int i = 0; i < hand.size(); i++) {
			sum1 += hand.get(i).pointValue();
		}
		result = sum1;
		for (int i = 0; i < hand.size(); i++) {
			if (hand.get(i).rank() == "ace") {
				int sum2 = sum1 + 10;
				if (sum2 > 21) {
					sum2 = 0;
					hand.get(i).setPointValue(1);
				}
				if ((21 - sum1) > (21 - sum2)) {
					result = sum2;
					hand.get(i).setPointValue(11);
				} else {
					result = sum1;
					hand.get(i).setPointValue(1);
				}
			}
		}
		return result;
	}
	
	private static int hit(Deck cardDeck, ArrayList<Card> dealer) {
		Card extraCard = cardDeck.deal();
		dealer.add(extraCard);
		if (addUp(dealer) > 21) {
			System.out.println("Their sum is now " + addUp(dealer));
			sort(dealer);
			System.out.println("Their hand is now " + dealer.toString() + "\n");
			System.out.println("They busted! You win!");
			return 1;
		} else {
			if (addUp(dealer) < 17) {
				System.out.println("Their sum is now " + addUp(dealer));
				sort(dealer);
				System.out.println("Their hand is now " + dealer.toString() + "\n");
				return 0;
			} else {
				System.out.println("Their sum is now " + addUp(dealer));
				sort(dealer);
				System.out.println("Their hand is now " + dealer.toString() + "\n");
				return -1; 
			}
		}
	}
	
	public static ArrayList<Card> sort(ArrayList<Card> hand) {
		Collator c = Collator.getInstance();
		for (int m = 0; m < hand.size(); m++) {
			int min = m;
			for (int k = m; k < hand.size(); k++) {
				if ((int) hand.get(k).pointValue() < (int) hand.get(min).pointValue()) {
					min = k;
				}
			}
			Collections.swap(hand, min, m);
		}
		return hand;
	}
}