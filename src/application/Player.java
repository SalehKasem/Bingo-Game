package application;

import java.security.SecureRandom;

public class Player implements Runnable {
	private final int id;
	Card card;
	Buffer buffer;
	SecureRandom random = new SecureRandom();
	Manager manager;

	//player constructor
	public Player(int id, Card card,Buffer buffer, Manager manager) {	
		this.id = id;
		this.card = card; //each player has it own card
		this.buffer = buffer;
		this.manager =manager; //each player know his manager
	}
	
	@Override 
	public void run() {
		while(true) { //playing until the pressing Bingo button
			try {
				Thread.sleep(random.nextInt(4000));
				if(card.getBingoFlag()) { //check if the player presses bingo
					buffer.set(id); //let the manager know who presses bingo
				}
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		} 
	}
	//return the player id
	public int getId() {
		return id; 
	}
	
}
