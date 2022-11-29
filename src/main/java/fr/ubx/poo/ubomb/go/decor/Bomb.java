package fr.ubx.poo.ubomb.go.decor;

import fr.ubx.poo.ubomb.engine.Timer;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.character.Player;

public class Bomb extends Decor{

    private Timer timer;
    private int detonationPhase;
    public Bomb(Game game, Position position) {
        super(game, position);
        timer = new Timer(1000);
        detonationPhase = 3;
        timer.start();
    }

    public int getDetonationPhase() {
        return detonationPhase;
    }

    public void update(long now){
        timer.update(now);
        if(timer.remaining() <= 0) {
            detonationPhase--;
            setModified(true);
            if(detonationPhase > 0) {
                timer.start();
            }else
                remove();
        }
    }
}