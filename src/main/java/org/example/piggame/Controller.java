package org.example.piggame;

import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;
import javafx.animation.RotateTransition;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.*;
import java.util.Random;

public class Controller {

    @FXML
    private ImageView playerDiceImage;
    @FXML
    private ImageView reactionsImageView;
    @FXML
    private ImageView musicIcon;
    @FXML
    Button playerRollButton;
    @FXML
    Button playerHoldButton;
    @FXML
    Button player2RollButton;
    @FXML
    Button player2HoldButton;
    @FXML
    private Label playerTotalScoreLabel;
    @FXML
    private Label player2TotalScoreLabel;
    @FXML
    private Label rollingPointsLabel;
    @FXML
    private Label playerNameLabel;
    @FXML
    private Label player2NameLabel;
    @FXML
    private TextField player1TextField;
    @FXML
    private TextField player2TextField;
    @FXML
    private Button pNameChangeButton;
    @FXML
    private Button p2NameChangeButton;
    @FXML
    private Button p1ConfirmButton;
    @FXML
    private Button p2ConfirmButton;
    @FXML
    private HBox player2Hbox;
    @FXML
    private HBox playerHbox;
    @FXML
    private Button seeHistoryButton;
    @FXML
    private Button playButton;
    @FXML
    private Label tittleScreenLabel;

    @FXML
    private TableView<GameRecords> tableView;

    //Default Variables
    private boolean gameFinished = false;
    private int rollingPoints = 0;
    private int playerTotalPoints = 0;
    private int player2TotalPoints = 0;
    private boolean isPlayer1Turn = true;
    private boolean isFirstRolledDone = false;
    private boolean muted = false;


    private String P1Name = "Player 1";
    private String P2Name= "Player 2";
    private String P1Outcome = "Not Finished";
    private String P2Outcome = "Not Finished";

    //Actions HOLD and ROLL
    public void DiceRolled(){

        //finishedGame();
        isFirstRolledDone = true;
        if (isFirstRolledDone){
            playerHoldButton.setDisable(false);
        }

        playDiceSoundEffect();

        rotateTransition();
        Random random =  new Random();
        int diceRoll =  random.nextInt(6) + 1;
        System.out.println("You Rolled a: " + diceRoll);

        displayImage(diceRoll);

        switch (diceRoll){
            case 1:
                System.out.println("You lost all your rollingPoints");
                rollingPoints = 0;
                playLostScoreSoundEffect();
                isPlayer1Turn = !isPlayer1Turn; //Change players turns
                System.out.println("\n isPlayer1Turn: " + isPlayer1Turn);
                if (isPlayer1Turn) {
                    player2Hbox.setVisible(false);
                    playerHbox.setVisible(true);
                }else{
                    player2Hbox.setVisible(true);
                    playerHbox.setVisible(false);
                }
                break;
            case 2:
                rollingPoints += 2;
                System.out.println("You rolled a 2!");
                break;
            case 3:
                rollingPoints += 3;
                System.out.println("You rolled a 3!");
                break;
            case 4:
                rollingPoints += 4;
                System.out.println("You rolled a 4!");
                break;
            case 5:
                rollingPoints += 5;
                System.out.println("You rolled a 5!");
                break;
            case 6:
                reactionImage();
                playBigScoreUpSoundEffect();
                rollingPoints += 6;
                System.out.println("You rolled a 6!");
                break;
        }
        reactionImage();
        String diceRollAsString = Integer.toString(rollingPoints);
        rollingPointsLabel.setText("+" + diceRollAsString);
        System.out.println("rollingPoints: " + rollingPoints);


    }
    public void TurnHolded(){

        resetReactionImage();

        if (rollingPoints > 0){playScoreUpSoundEffect();}

        if (isPlayer1Turn){

            playerTotalPoints = rollingPoints + playerTotalPoints;
            String playerTotalPointString = Integer.toString(playerTotalPoints);
            playerTotalScoreLabel.setText(playerTotalPointString + "/50");

            rollingPoints = 0;
            String diceRollAsString = Integer.toString(rollingPoints);
            rollingPointsLabel.setText(diceRollAsString);

            if (playerTotalPoints >= 50){
                P1Outcome = "Win";
                P2Outcome = "Lose";
                victoryReactionImage();
                finishedGame();
            }
            isPlayer1Turn = !isPlayer1Turn;
            player2Hbox.setVisible(true);
            playerHbox.setVisible(false);

        }else{

            player2TotalPoints = rollingPoints + player2TotalPoints;
            String player2TotalPointString = Integer.toString(player2TotalPoints);
            player2TotalScoreLabel.setText(player2TotalPointString + "/50");

            rollingPoints = 0;
            String diceRollAsString = Integer.toString(rollingPoints);
            rollingPointsLabel.setText(diceRollAsString);

            if (player2TotalPoints >= 50){
                P1Outcome = "Lose";
                P2Outcome = "Win";
                victoryReactionImage();
                finishedGame();
            }
            isPlayer1Turn = !isPlayer1Turn;
            player2Hbox.setVisible(false);
            playerHbox.setVisible(true);
        }
         //Change players turns
        System.out.println("\n isPlayer1Turn: " + isPlayer1Turn);
    }

    public void finishedGame() {
        gameFinished = true;
        playVictorySoundEffect();

        System.out.println("Sending data to .txt");

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy hh:mm");
        String formattedDateTime = formatter.format(now);

        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileOutputStream("src/main/resources/gameRecords.txt", true));
            writer.write(P1Name + "," + formattedDateTime + "," + playerTotalPoints + "," + P1Outcome + "\n");
            writer.write(P2Name + "," + formattedDateTime + "," + player2TotalPoints + "," + P2Outcome + "\n");
            // Add more lines for additional data if needed
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            System.out.println("Closing!!!");
            writer.close();
        }

        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Game Over");
        if (playerTotalPoints > player2TotalPoints){
            alert.setHeaderText(P1Name + " wins the game!");
        }else if (playerTotalPoints < player2TotalPoints){
            alert.setHeaderText(P2Name + " wins the game!");
        } else{
            alert.setHeaderText("GAME OVER");}
        alert.setContentText("Do you want to play again?");

        ButtonType playAgainButton = new ButtonType("Play Again");
        ButtonType exitButton = new ButtonType("Exit");

        alert.getButtonTypes().setAll(playAgainButton, exitButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == playAgainButton) {
            System.out.println("Resetting Variables to Default");
            gameFinished = false;
            rollingPoints = 0;
            playerTotalPoints = 0;
            player2TotalPoints = 0;
            isPlayer1Turn = true;
            P1Outcome = "Not Finished";
            P2Outcome = "Not Finished";
            isFirstRolledDone = false;
            playerTotalScoreLabel.setText("");
            player2TotalScoreLabel.setText("");
            rollingPointsLabel.setText("");
            player2Hbox.setVisible(false);
            playerHoldButton.setDisable(true);
            playerHbox.setVisible(true);
            resetReactionImage();
        } else {
            Platform.exit();
        }
    }

    //SETTING UP METHODS

    public void stopStartMusic(){
        if (muted) {
            muted = true;
            PigGameDemo.unmuteMediaPlayer();
        }else {
            muted = false;
            PigGameDemo.muteMediaPlayer();
        }
        muted = !muted;
        System.out.println("Muted: " + muted);
    }

    public void resetReactionImage(){
        String imagePath = "/images/" + "waiting.gif";
        Image reactionImage = new Image(getClass().getResourceAsStream(imagePath));
        reactionsImageView.setImage(reactionImage);
    }

    public void victoryReactionImage(){
        String imagePath = "/images/" + "celebrate.gif";
        Image reactionImage = new Image(getClass().getResourceAsStream(imagePath));
        reactionsImageView.setImage(reactionImage);
    }

    public void reactionImage(){
        String imageReaction;
        if (rollingPoints < 1) {
            imageReaction = "sad.gif";
        } else if (rollingPoints > 0 && rollingPoints < 6) {
            imageReaction = "nice.gif";
        } else if (rollingPoints >= 6 && rollingPoints < 13) {
            imageReaction = "happy.gif";
        } else if (rollingPoints >= 13 && rollingPoints < 25) {
            imageReaction = "wow.gif";
        }  else if (rollingPoints >= 25 && rollingPoints < 49) {
            imageReaction = "good.gif";
        } else {
            imageReaction = "waiting.gif";
        }
        String imagePath = "/images/" + imageReaction;
        Image reactionImage = new Image(getClass().getResourceAsStream(imagePath));
        reactionsImageView.setImage(reactionImage);

    }
    public void displayImage(int diceRoll){
        String imagePath = "/assets/" + diceRoll + ".png";
        Image diceImage = new Image(getClass().getResourceAsStream(imagePath));
        playerDiceImage.setImage(diceImage);
    }
    private void playDiceSoundEffect(){
        String soundEffectPath = "src/main/resources/music/soundEffects/RollingDice.mp3"; // Update with your sound effect file path
        Media soundEffectMedia = new Media(new File(soundEffectPath).toURI().toString());
        MediaPlayer soundEffectPlayer = new MediaPlayer(soundEffectMedia);
        soundEffectPlayer.play();
    }
    private void playBigScoreUpSoundEffect(){
        String soundEffectPath = "src/main/resources/music/soundEffects/ooh.mp3"; // Update with your sound effect file path
        Media soundEffectMedia = new Media(new File(soundEffectPath).toURI().toString());
        MediaPlayer soundEffectPlayer = new MediaPlayer(soundEffectMedia);
        soundEffectPlayer.play();
    }
    private void playScoreUpSoundEffect(){
        String soundEffectPath = "src/main/resources/music/soundEffects/ScoreUp.mp3"; // Update with your sound effect file path
        Media soundEffectMedia = new Media(new File(soundEffectPath).toURI().toString());
        MediaPlayer soundEffectPlayer = new MediaPlayer(soundEffectMedia);
        soundEffectPlayer.play();
    }
    private void playLostScoreSoundEffect(){
        String soundEffectPath = "src/main/resources/music/soundEffects/wrong12.mp3"; // Update with your sound effect file path
        Media soundEffectMedia = new Media(new File(soundEffectPath).toURI().toString());
        MediaPlayer soundEffectPlayer = new MediaPlayer(soundEffectMedia);
        soundEffectPlayer.play();
    }
    private void playVictorySoundEffect(){
        String soundEffectPath = "src/main/resources/music/soundEffects/KidsCheering.mp3"; // Update with your sound effect file path
        Media soundEffectMedia = new Media(new File(soundEffectPath).toURI().toString());
        MediaPlayer soundEffectPlayer = new MediaPlayer(soundEffectMedia);
        soundEffectPlayer.play();
    }

    private void rotateTransition(){
        RotateTransition rt = new RotateTransition();
        rt.setByAngle(360);
        rt.setNode(playerDiceImage);
        rt.setDuration(Duration.millis(600));
        rt.play();

        // Move the dice image to a random position
        Random random = new Random();
        int randomX = random.nextInt(301) - 150; //Generate number between 200 and -200
        int randomY = random.nextInt(41) - 20;// Generate random number between -20 and 20
        TranslateTransition tt = new TranslateTransition(Duration.millis(600), playerDiceImage);
        tt.setToY(randomY);
        tt.setToX(randomX);
        tt.play();
    }

    public void changePlayer1Name(){
        player1TextField.setVisible(true);
        pNameChangeButton.setVisible(false);
        p1ConfirmButton.setVisible(true);
    }
    public void p1Confirm(){
        String name = player1TextField.getText();
        playerNameLabel.setText(name);
        P1Name = name;
        System.out.println("P1 Name: " + name);

        player1TextField.setVisible(false);
        pNameChangeButton.setVisible(true);
        p1ConfirmButton.setVisible(false);
    }
    public void changePlayer2Name(){
        player2TextField.setVisible(true);
        p2NameChangeButton.setVisible(false);
        p2ConfirmButton.setVisible(true);
    }
    public void p2Confirm(){
        String name2 = player2TextField.getText();
        player2NameLabel.setText(name2);
        P2Name = name2;
        System.out.println("P2 Name: " + name2);

        player2TextField.setVisible(false);
        p2NameChangeButton.setVisible(true);
        p2ConfirmButton.setVisible(false);
    }

    //Scene Switches
    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchToGameScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void switchToTitleScreenScene(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("title-screen.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void switchToTitleGameRecordsScene(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("game-records.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
