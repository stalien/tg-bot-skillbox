package org.example.questions;

public class Question4 extends AbstractQuestion{

    String[] methods = {"get", "post", "put", "patch", "delete"};

    public Question4() {
        super("Вопрос 4. Какие методы HTTP-запросов вы знаете?");
    }

    @Override
    public boolean checkAnswer(String answer) {
        answer = answer.toLowerCase();

        for (String method : methods) {
            if (!answer.contains(method)) {
                return false;
            }
        }

        return true;
    }
}
