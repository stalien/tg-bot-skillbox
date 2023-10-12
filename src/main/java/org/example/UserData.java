package org.example;

import lombok.Data;

@Data
public class UserData {
    private int questionNumber;
    private int score;

    public UserData() {
        this.questionNumber = 1;
        this.score = 0;
    }
}
