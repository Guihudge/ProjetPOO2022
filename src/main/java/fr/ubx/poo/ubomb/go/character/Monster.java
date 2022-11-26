package fr.ubx.poo.ubomb.go.character;

import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.Movable;
import fr.ubx.poo.ubomb.go.decor.Box;
import fr.ubx.poo.ubomb.go.decor.Decor;
import fr.ubx.poo.ubomb.go.decor.Stone;
import fr.ubx.poo.ubomb.go.decor.Tree;

import java.util.Random;

import static fr.ubx.poo.ubomb.game.Direction.*;

public class Monster extends GameObject implements Movable {
    private int life = 1;
    private Direction direction = UP;

    public Monster(Game game, Position position){
        super(game, position);
    }
    public Monster(Game game, Position position, int life){
        super(game, position);
        this.life = life;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public boolean canMove(Direction direction) {
        Decor pos = game.grid().get(direction.nextPosition(getPosition()));
        if (game.grid().inside(direction.nextPosition(getPosition()))) {
            if (pos instanceof Tree || pos instanceof Stone || pos instanceof Box){
                return true;
            }
            return false;
        }
        return true;
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
            switch (randValue){
                case 0:
                    direction = UP;
                    break;
                case 1:
                    direction = DOWN;
                    break;
                case 2:
                    direction = LEFT;
                    break;
                case 3:
                    direction = RIGHT;
                    break;
            }

        }while (canMove(direction));
        this.direction = direction;
        doMove(direction);
    }
}
