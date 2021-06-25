package controllers;

import models.Player;

import java.util.Random;

public class AIController {
    public void setMainCard() {
        Player AI = Game.secondPlayer;
        for (int i = 1; i < 5; i++) {
            Random random = new Random();
            int slideCard = (random.nextInt() % 15) + 1;
            int mainCard = (random.nextInt() % 40) + 1;
            AI.setSlideToMain(slideCard, mainCard);
        }
        AI.setHandCard();
    }
}
