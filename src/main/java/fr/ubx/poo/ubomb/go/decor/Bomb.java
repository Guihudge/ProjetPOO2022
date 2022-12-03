package fr.ubx.poo.ubomb.go.decor;

import fr.ubx.poo.ubomb.engine.Timer;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.character.Player;

import java.util.ArrayList;
import java.util.List;

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

    public List<Position> getExplosionAffectedPosition(int explosionRange){
        Position bombPos = this.getPosition();
        List<Position> affectedTiles = new ArrayList<>();
        affectedTiles.add(bombPos);
        for (int i = 1; i <= explosionRange; i++){
            affectedTiles.add(new Position(bombPos.x()+i, bombPos.y()));
            affectedTiles.add(new Position(bombPos.x()-i, bombPos.y()));
            affectedTiles.add(new Position(bombPos.x(), bombPos.y()+i));
            affectedTiles.add(new Position(bombPos.x(), bombPos.y()-i));
        }
        return affectedTiles;
    }

    @Override
    public void explode(){
        remove();
    }
}