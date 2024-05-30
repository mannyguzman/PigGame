package org.example.piggame;

import java.time.LocalDateTime;

public class GameRecords{

    private String name;
    private String dateTime;
    private int score;
    private String winLose;

    public GameRecords(String name, String dateTime, int score, String winLose) {
        this.name = name;
        this.dateTime = dateTime;
        this.score = score;
        this.winLose = winLose;
    }

    public String getName() {
        return name;
    }
    public String getDateTime() {return dateTime;}
    public int getScore() {
        return score;
    }
    public String getWinLose() {
        return winLose;
    }


}
