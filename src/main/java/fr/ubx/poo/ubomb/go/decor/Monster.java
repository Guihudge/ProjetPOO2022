package fr.ubx.poo.ubomb.go.decor;

import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.game.Position;

public class Monster extends Decor {
    private Direction direction;

    public Monster(Position position){
        super(position);
        this.direction = Direction.DOWN;
    }

    public Direction getDirection() {
        return direction;
    }
}
