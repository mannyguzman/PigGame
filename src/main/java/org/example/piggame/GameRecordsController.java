package org.example.piggame;

import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.*;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Scanner;

public class GameRecordsController implements Initializable{

    @FXML
    private TableColumn<GameRecords, String> name;
    @FXML
    private TableColumn <GameRecords, Date> date;
    @FXML
    private TableColumn <GameRecords, Integer> score;
    @FXML
    private TableColumn <GameRecords, String> winLose;
    @FXML
    private TableView<GameRecords> tableView;
    @FXML
    private TextField rankingTextField;
    @FXML
    private Label rankingLabel;
    @FXML
    private Label rankingWinsLabel;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        loadDataFromFile();
        loadRanking();
    }


    private void loadDataFromFile(){
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("gameRecords.txt");
        ObservableList<GameRecords> data = FXCollections.observableArrayList();
        try {
            Scanner reader = new Scanner(inputStream);
            while (reader.hasNextLine()){
                String line = reader.nextLine();
                String[] tokens = line.split(",");
                GameRecords gameRecords = new GameRecords(tokens[0], tokens[1], Integer.parseInt(tokens[2]), tokens[3]);
                data.add(gameRecords);
            } reader.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        name.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getName()));
        date.setCellValueFactory(param ->{
            String dateTimeString = param.getValue().getDateTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy hh:mm");
            try {
                Date date = dateFormat.parse(dateTimeString);
                return new SimpleObjectProperty<>(date);
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }});
        score.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getScore()).asObject());
        winLose.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getWinLose()));

        tableView.setItems(data);
    }

    public void loadRanking() {
        HashMap<String, Integer> playerWins = new HashMap<>();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("gameRecords.txt");
        Scanner scanner = null;
        scanner = new Scanner(inputStream);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] tokens = line.split(",");

            String playerName = tokens[0].trim();
            String outcome = tokens[3].trim();

            if(outcome.equals("Win")){
                playerWins.put(playerName, playerWins.getOrDefault(playerName, 0) + 1);
            }
        }
        StringBuilder rankingTextNames = new StringBuilder();
        StringBuilder rankingTextWins = new StringBuilder();
        for (String name : playerWins.keySet()) {
            rankingTextNames.append(name).append("\n");
        }
        for (String name : playerWins.keySet()) {
            rankingTextWins.append(playerWins.get(name)).append("\n");
        }
        rankingLabel.setText(rankingTextNames.toString());
        rankingWinsLabel.setText(rankingTextWins.toString());
        System.out.println(rankingTextNames.toString());
        System.out.println(rankingTextWins.toString());
        scanner.close();
    }

    public void updateTableView(){
        loadDataFromFile();
    }

    //Scene Controller
    private Stage stage;
    private Scene scene;
    private Parent root;
    public void switchToTitleScreenScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("title-screen.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
