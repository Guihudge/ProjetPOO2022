package fr.ubx.poo.ubomb.go.character;

import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.Movable;
import fr.ubx.poo.ubomb.go.decor.*;

import java.util.Random;

import static fr.ubx.poo.ubomb.game.Direction.*;

public class Monster extends GameObject implements Movable {
    private int life;
    private Direction direction = UP;

    public Monster(Game game, Position position, int life){
        super(game, position);
        this.life = life;
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public boolean canMove(Direction direction) {
        Decor pos = game.grid().get(direction.nextPosition(getPosition()));
        if (game.grid().inside(direction.nextPosition(getPosition()))) {
            return pos == null;
        }
        return false;
    }

    @Override
    public void doMove(Direction direction){
        setPosition(direction.nextPosition(getPosition()));
    }

    public void update(long now){
        Random rand = new Random();
        int maxRand = 4;
        Direction direction = UP;
        do {
            int randValue = rand.nextInt(maxRand);
            switch (randValue) {
                case 0 -> direction = UP;
                case 1 -> direction = DOWN;
                case 2 -> direction = LEFT;
                case 3 -> direction = RIGHT;
            }

        }while (!canMove(direction));
        this.direction = direction;
        doMove(direction);
    }

    @Override
    public void explode() {
        this.life --;
        if (life <= 0){
            this.remove();
        }
    }
}
