package org.example.piggame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import java.io.File;
import java.io.IOException;

public class PigGameDemo extends Application{

    private static MediaPlayer mediaPlayer;

    @Override
    public void start(Stage stage) throws IOException{
        try {
            Parent root = FXMLLoader.load(getClass().getResource("title-screen.fxml"));
            Scene scene1 = new Scene(root);
            stage.setScene(scene1);
            stage.setTitle("Pig Game");
            System.out.println(System.getProperty("user.dir"));

            String css = this.getClass().getResource("application.css").toExternalForm();
            scene1.getStylesheets().add(css);

            initializeMediaPlayer();

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main (String[]args){launch();}


    public static void initializeMediaPlayer(){

        String mediaPath = new File("src/main/resources/music/QuerkyFun.mp3").toURI().toString();
        Media media = new Media(mediaPath);
        mediaPlayer= new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        playMediaPlayer();
    }
    public static void playMediaPlayer() {
        mediaPlayer.setVolume(0.3);
        mediaPlayer.play();
    }
    public static void muteMediaPlayer() {
        mediaPlayer.setVolume(0);
    }
    public static void unmuteMediaPlayer() {
        mediaPlayer.setVolume(0.3);
    }


}