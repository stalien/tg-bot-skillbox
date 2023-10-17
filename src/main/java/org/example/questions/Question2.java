package org.example.questions;

public class Question2 extends AbstractQuestion{
    public Question2() {
        super("Сколько в реляционных базах данных существует типов связей между таблицами?");
    }

    @Override
    public boolean checkAnswer(String answer) {
        return answer.equals("3");
    }
}
