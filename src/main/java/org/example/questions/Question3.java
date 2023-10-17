package org.example.questions;

public class Question3 extends AbstractQuestion{
    public Question3() {
        super("С помощью какой команды в системе контроля версий Git можно просмотреть авторов различных строк в одном файле?");
    }

    @Override
    public boolean checkAnswer(String answer) {
        return answer.toLowerCase().equals("blame");
    }
}
