package fr.ubx.poo.ubomb.go.decor;

import fr.ubx.poo.ubomb.engine.Timer;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.character.Player;

public class Bomb extends Decor{

    private Timer timer;
    public Bomb(Game game, Position position) {
        super(game, position);
        timer = new Timer(1000);
    }


}