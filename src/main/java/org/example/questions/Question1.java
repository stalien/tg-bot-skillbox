package org.example.questions;

public class Question1 extends AbstractQuestion{
    public Question1() {
        super("Сколько в языке Java есть примитивов?");
    }

    @Override
    public boolean checkAnswer(String answer) {
        return answer.equals("8");
    }
}
