package application;

import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Manager  extends VBox implements Runnable{
	
	private int i=0;
	private int playerId;
	private boolean winner = false; //to confirm if there is a winner
	List<Integer> lst = new ArrayList<Integer>();//list to save the lottery numbers
	Player players[];
	Card cards[];
	Buffer buffer;
	Button nextB;
	TextField textField;
	Button finishB;
	SecureRandom random = new SecureRandom(); //to generate random numbers (1-100)
	ExecutorService ex = Executors.newCachedThreadPool();
	private Stage stage;
	
	//GmaeFrame constructor
	public Manager(int n,int playersNum,Stage stage) {
		buffer = new BufferArray(playersNum);
		this.stage = stage;
		//set the first flowPane with tow buttons and one textField
		FlowPane flowPane = new FlowPane();
		flowPane.setPrefWrapLength(900);
		
		//create Next button
		nextB = new Button("Next");
		//create textField
		textField = new TextField();
		textField.setMaxSize(50, 100);
		textField.setAlignment(Pos.CENTER);
		
		//set handler for Next button
		nextB.setOnMouseClicked(e -> {handler(e);});
		
		//create Finish button
		Button finishB = new Button("Finish");
		//set handler for Finish button
		finishB.setOnMouseClicked(e -> {finishButtonHandler(e);});
		//attach them 
		flowPane.getChildren().addAll(nextB, textField, finishB);
		flowPane.setPadding(new Insets(5,0,0,0));
		flowPane.setAlignment(Pos.CENTER);
		flowPane.setHgap(20);
		//attach flowPane to the first row in VBox
		getChildren().add(flowPane);
		
		//create the second flowPane for the cards
		FlowPane flowPane2 = new FlowPane();
		//set the cards horizontally
		
		//making array of players and cards
		players = new Player[playersNum+1];
		cards = new Card[playersNum+1];
		
		//initialize the cards and players
		for(i=0; i<playersNum; i++) {
			cards[i+1] = new Card(n,i+1, i+1); // creating new card and saving it to cards array
			players[i+1] = new Player(i+1,cards[i+1],buffer, this);//creating new player and saving it to players array
			ex.execute(players[i+1]); //execute the players threads
			flowPane2.getChildren().add(cards[i+1]);
			flowPane2.setHgap(10);
			//flowPane2.setVgap(10);
		}
		
		flowPane2.setPrefWrapLength(900);
		flowPane2.setPadding(new Insets(5,5,5,5));
		getChildren().add(flowPane2);
		
	}
	
	//finish button handled
	private void finishButtonHandler(MouseEvent e) {
		//pop up an information window whith finish button
		 Alert alert = new Alert(AlertType.INFORMATION,"",ButtonType.FINISH);
		 alert.setHeaderText(null);
		 if(winner) //check if there is winner
		    alert.setContentText("Congratulations!!! player " + playerId + " Won");
		 else { //there is no winner
			    alert.setContentText("Unfortunately!!! player " + playerId + " didn't Won\nGAME OVER");
		 }
		    alert.show(); //show the window
		    if(alert.getResult() == ButtonType.FINISH) { //finish button handle
		    	alert.close(); //close the window after pressing on finish
		    }
		    stage.close();//close the main game window after pressing finish button 
	}
	
	//Next button handler 
	public void handler(MouseEvent e) {
		if(nextB == e.getSource()) {
			lst.add(random.nextInt(100)+1); //save the chosen numbers in list
			textField.setText(""+lst.get(lst.size()-1)); //showing the number in textField after pressing Next
		}
	}
	
	//manager thread
	@Override
	public void run() {
		try {
			Thread.sleep(random.nextInt(5000));
			int id;
			int i;
			id = buffer.get(); //get the id of the player who has pressed bingo
			this.playerId =id; // save it
			if(id!=0) {
				//checking rows and columns
				for(i=0; i<cards[id].getCardSize(); i++) {
					if(checkRow(id,i) || checkCol(id,i)) {
						confirmWinner(true);//confirming that there is a winner
						return;
					}
				}
				//check the main diagonal
				if(checkMainDiagonal(id)) {
					confirmWinner(true);//confirming that there is a winner
					return;
				}				
				//check the second diagonal
				if(checkSecondaryDiagonal(id)) {
					confirmWinner(true);//confirming that there is a winner
					return;
				}
				confirmWinner(false);//there is No winner
			}
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	// confirmation if there is a winner or not
	public void confirmWinner(boolean winner) {
		this.winner = winner;
	}

	public boolean checkRow(int cardId, int row) {
		int i;
		for(i=0; i<cards[cardId].getCardSize(); i++) {
			//check if the button is pressed
			if(cards[cardId].bArr[i][row].getTextFill() != Color.RED)
				return false;
			else {
				//get the number of button and convert it from string to int
				String str = cards[cardId].bArr[i][row].getText();
				int buttonNum = Integer.parseInt(str);
				System.out.println(buttonNum + " ");
				if(!lst.contains(buttonNum)) { //check if the number is actually  appears in the lottery  
					return false;
				}
			}
		}
		//mark the row in yellow
		for(i=0; i<cards[cardId].getCardSize(); i++) {
			cards[cardId].bArr[i][row].setStyle("-fx-background-color: yellow;");
		}
		return true;
	}
	public boolean checkCol(int cardId, int col) {
		
		int i;
		for(i=0; i<cards[cardId].getCardSize(); i++) {
			//check if the button is pressed
			if(cards[cardId].bArr[col][i].getTextFill() != Color.RED)
				return false;
			else {
				//get the number of button and convert it from string to int
				String str = cards[cardId].bArr[col][i].getText();
				int buttonNum = Integer.parseInt(str);
				System.out.println(buttonNum + " ");
				if(!lst.contains(buttonNum)) {//check if the number is actually  appears in the lottery
					return false;
				}
			}
		}
		//mark the column in yellow
		for(i=0; i<cards[cardId].getCardSize(); i++) {
			cards[cardId].bArr[col][i].setStyle("-fx-background-color: yellow;");
		}
		return true;
	}
	public boolean checkMainDiagonal(int cardId) {
		
		int i;
		for(i=0; i<cards[cardId].getCardSize(); i++) {
			//check if the button is pressed
			if(cards[cardId].bArr[i][i].getTextFill() != Color.RED)
				return false;
			else {
				//get the number of button and convert it from string to int
				String str = cards[cardId].bArr[i][i].getText();
				int buttonNum = Integer.parseInt(str);
				System.out.println(buttonNum + " ");
				if(!lst.contains(buttonNum)) {//check if the number is actually  appears in the lottery
					return false;
				}
			}
		}
		//mark the diagonal in yellow
		for(i=0; i<cards[cardId].getCardSize(); i++) {
			cards[cardId].bArr[i][i].setStyle("-fx-background-color: yellow;");

		}
		return true;
	}
	public boolean checkSecondaryDiagonal(int cardId) {
		
		int i,j;
		int n = cards[cardId].getCardSize(); 
		
		for(i=0,j= (n - 1); i<n; i++,j--) {
			//check if the button is pressed
			if(cards[cardId].bArr[j][i].getTextFill() != Color.RED)
				return false;
			else {
				//get the number of button and convert it from string to int
				String str = cards[cardId].bArr[j][i].getText();
				int buttonNum = Integer.parseInt(str);
				System.out.println(buttonNum + " ");
				if(!lst.contains(buttonNum)) {//check if the number is actually  appears in the lottery
					return false;
				}
			}
		}
		//mark the diagonal in yellow
		for(i=0,j= (n - 1); i<n; i++,j--) {
			cards[cardId].bArr[j][i].setStyle("-fx-background-color: yellow;");
		}
		return true;
	}
	

}
