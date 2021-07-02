package controllers;

import Exceptions.MyException;
import models.Player;

import java.util.Random;

public class SetMainAndSlide {

    public void setMainToSlide(int mainNumber, Player player) throws MyException {
        if (player.getNumberOfUnusedCard() > 45)
            player.setMainToSlide(mainNumber);
        else throw new MyException("you cant have less than 45 card in main deck");
    }

    public void setSlideToMain(int slideNumber, Player player) throws MyException {
        if (player.getNumberOfUnusedCard() < 60)
            player.setSlideToMain(slideNumber);
        else throw new MyException("you cant have more than 60 card in main deck");
    }

    public void setAICard() {
        Player AI = Game.secondPlayer;
        for (int i = 1; i <= 7; i++) {
            Random random = new Random();
            int slideCard = (random.nextInt(10) % 10) + 1;
            int mainCard = (random.nextInt(40) % 40) + 1;
            AI.setSlideToMain(slideCard);
            AI.setMainToSlide(mainCard);
        }
        AI.setHandCard();
    }
}
