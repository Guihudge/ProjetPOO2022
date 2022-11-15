package fr.ubx.poo.ubomb.go.decor;

import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.Movable;

public class Box extends Decor implements Movable {
    public Box (Position position){super(position);}

    public Box (Game game, Position position) {super(game,position);}

    @Override
    public boolean canMove(Direction direction) {
        /*if(game != null)
            if (game.grid().get(direction.nextPosition(getPosition())) instanceof Box box) {
                Decor pos = game.grid().get(direction.nextPosition(getPosition()));
                if (game.grid().inside(direction.nextPosition(getPosition()))) {
                    return !(pos instanceof Stone) && !(pos instanceof Tree);
                }
            }
        return false;*/
        return true;
    }

    @Override
    public void doMove(Direction direction) {
        if(canMove(direction)) {
            this.remove();
            this.setPosition(direction.nextPosition(this.getPosition()));
            /*if(game != null) {
                remove();
                game.grid().set(direction.nextPosition(getPosition()), new Box(direction.nextPosition(getPosition())));
            }*/
        }
    }
}
