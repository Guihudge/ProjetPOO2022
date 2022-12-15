package fr.ubx.poo.ubomb.go.decor;

import fr.ubx.poo.ubomb.engine.Timer;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.character.Player;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
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

    public int[] getExplosionAnimationRange(int explosionRange){
        Position bombPos = this.getPosition();
        int[] affectedTiles = new int[4];
        Arrays.fill(affectedTiles, explosionRange);
        for (int i = 1; i <= explosionRange; i++) {
            if (game.grid().get(new Position(bombPos.x() + i, bombPos.y())) instanceof Tree || game.grid().get(new Position(bombPos.x() + i, bombPos.y())) instanceof Stone || game.grid().get(new Position(bombPos.x() + i - 1, bombPos.y())) instanceof Box) {
                affectedTiles[0] = i - 1;
                break;
            }
        }
        for (int i = 1; i <= explosionRange; i++) {
            if(game.grid().get(new Position(bombPos.x()-i, bombPos.y())) instanceof Tree || game.grid().get(new Position(bombPos.x()-i, bombPos.y())) instanceof Stone || game.grid().get(new Position(bombPos.x()-i+1, bombPos.y())) instanceof Box) {
                affectedTiles[1] = i-1;
                break;
            }
        }
        for (int i = 1; i <= explosionRange; i++) {
            if(game.grid().get(new Position(bombPos.x(), bombPos.y()+i)) instanceof Tree || game.grid().get(new Position(bombPos.x(), bombPos.y()+i)) instanceof Stone || game.grid().get(new Position(bombPos.x(), bombPos.y()+i-1)) instanceof Box) {
                affectedTiles[2] = i-1;
                break;
            }
        }
        for (int i = 1; i <= explosionRange; i++) {
            if(game.grid().get(new Position(bombPos.x(), bombPos.y()-i)) instanceof Tree || game.grid().get(new Position(bombPos.x(), bombPos.y()-i)) instanceof Stone || game.grid().get(new Position(bombPos.x(), bombPos.y()-i+1)) instanceof Box) {
                affectedTiles[3] = i-1;
                break;
            }
        }
        return affectedTiles;
    }

    public List<Position> getExplosionAffectedPosition(int explosionRange) {
        Position bombPos = this.getPosition();
        List<Position> affectedTiles = new ArrayList<>();
        affectedTiles.add(bombPos);
        for (int i = 1; i <= explosionRange; i++) {
            if (!(game.grid().get(new Position(bombPos.x() + i, bombPos.y())) instanceof Tree || game.grid().get(new Position(bombPos.x() + i, bombPos.y())) instanceof Stone)) {
                if (game.grid().get(new Position(bombPos.x() + i, bombPos.y())) != null && game.grid().get(new Position(bombPos.x() + i - 1, bombPos.y())) instanceof Box)
                    break;
                affectedTiles.add(new Position(bombPos.x() + i, bombPos.y()));
            } else
                break;
        }
        for (int i = 1; i <= explosionRange; i++) {
            if (!(game.grid().get(new Position(bombPos.x() - i, bombPos.y())) instanceof Tree || game.grid().get(new Position(bombPos.x() - i, bombPos.y())) instanceof Stone)) {
                if (game.grid().get(new Position(bombPos.x() - i, bombPos.y())) != null && game.grid().get(new Position(bombPos.x() - i + 1, bombPos.y())) instanceof Box)
                    break;
                affectedTiles.add(new Position(bombPos.x() - i, bombPos.y()));
            } else
                break;
        }
        for (int i = 1; i <= explosionRange; i++) {
            if (!(game.grid().get(new Position(bombPos.x(), bombPos.y() + i)) instanceof Tree || game.grid().get(new Position(bombPos.x(), bombPos.y() + i)) instanceof Stone)) {
                if (game.grid().get(new Position(bombPos.x(), bombPos.y() + i)) != null && game.grid().get(new Position(bombPos.x(), bombPos.y() + i - 1)) instanceof Box)
                    break;
                affectedTiles.add(new Position(bombPos.x(), bombPos.y() + i));
            } else
                break;
        }
        for (int i = 1; i <= explosionRange; i++) {
            if (!(game.grid().get(new Position(bombPos.x(), bombPos.y() - i)) instanceof Tree || game.grid().get(new Position(bombPos.x(), bombPos.y() - i)) instanceof Stone)) {
                if (game.grid().get(new Position(bombPos.x(), bombPos.y() - i)) != null && game.grid().get(new Position(bombPos.x(), bombPos.y() - i + 1)) instanceof Box)
                    break;
                affectedTiles.add(new Position(bombPos.x(), bombPos.y() - i));
            } else
                break;
        }
        return affectedTiles;
    }

    @Override
    public void explode(){
        remove();
    }
}