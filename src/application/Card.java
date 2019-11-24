package application;

import java.security.SecureRandom;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class Card extends VBox {
		private int cardId;
		private int cardSize;
		private int i=0;
		private int j=0;
		private int playerId;
		private boolean bingoFlag = false; //to recognize if someone pressed the Bingo button
		Button bArr[][]; //reference to array of buttons
		Button bingoB;
		
		//card constructor
		public Card(int cardSize, int playerId, int cardId) {
			this.cardSize = cardSize;
			this.playerId = playerId;
			
			bArr = new Button[cardSize][cardSize]; //array of buttons
			SecureRandom randNum = new SecureRandom(); //to generates random numbers
			
			bingoB = new Button("Bingo"); //create the bingo button
			Text text = new Text("Player " + playerId); //id for the card
			text.setStroke(Color.BURLYWOOD);
			
			//create the matrix witch contains he buttons
			GridPane gridPane = new GridPane();
			//add the card id and bingo button beneath the matrix
			HBox hBox = new HBox();
			hBox.getChildren().add(text);
			hBox.getChildren().add(bingoB);
			hBox.setAlignment(Pos.CENTER); //set card id and bingo button at the center
			hBox.setPadding(new Insets(3,5,5,5)); //set padding for card id and bingo button 
			
			//create the card
			for(i=0; i<cardSize; i++) {
				for(j=0; j<cardSize; j++) {
					//create button and initialize it with random number between 1 and 100
					Button button = new Button("" + (randNum.nextInt(100)+1));
					bArr[i][j] = button;
					button.setMaxSize(40,0); //set button max size
					button.setOnMouseClicked(e-> {buttonHandler(e);});
					//attach the button to the matrix 
					gridPane.add(button , i, j);
				}
			}//end card constructor
			
			//pressing on bingo button
			bingoB.setOnMouseClicked(e->{bingoHandler(e);});
			//attach the matrix plus card id and bingo button to VBox
			getChildren().add(gridPane);
			getChildren().add(hBox);
		}
		
		//bingo button handler
		private void bingoHandler(MouseEvent e) {
			if(bingoB == e.getSource()) {
				bingoB.setStyle("-fx-background-color: Green"); //set the bingo button color to green after pressing 
				bingoFlag = true; // confirm that the bingo button has been pressed
				if(bingoFlag) {
				bingoB.setDisable(true); //disable the bingo button to prevent multiple pressing 
				
				Alert alert = new Alert(AlertType.INFORMATION); //pop up an information window
				alert.setContentText("Checking Card... \nPlease presse Finish to get your final resulte!!");
				alert.setHeaderText(null);
				alert.show(); //show the alert
				}
				
			}
		}
		
		//numbers in matrix pressed handler
		public void buttonHandler(MouseEvent e) {
			int i;
			int j;
			for(i=0; i<cardSize; i++) {
				for(j=0; j<cardSize; j++) {
					if(bArr[i][j] == e.getSource()) {
						if(bArr[i][j].getTextFill()!=Color.RED) //pressing it for the first time
							bArr[i][j].setTextFill(Color.RED); //mark it in red
						else
							bArr[i][j].setTextFill(Color.BLACK);//pressing the button while its red to return it black
						
					}
				}
			}
		}
		//return card id
		public int getCardId() {
			return cardId;
		}
		//return player id
		public int getPlayerId() {
			return playerId;
		}
		//return card size
		public int getCardSize() {
			return cardSize;
		}
		//return bingoFlag to confirm that the bingo button has been preesed
		public boolean getBingoFlag() {
			return bingoFlag;
		}
		
}
