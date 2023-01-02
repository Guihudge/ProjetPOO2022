package fr.ubx.poo.ubomb.go.decor;

import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.character.Player;


public class Door extends Decor{

    private boolean isOpen;
    private final boolean isPrev;

    public Door(Position position) {
        super(position);
        isOpen = false;
        isPrev = false;
    }

    public Door(Position position, boolean value){
        super(position);
        isOpen = value;
        isPrev = false;
    }

    public Door(Position position, boolean value, boolean prev){
        super(position);
        isOpen = value;
        isPrev = prev;
    }

    @Override
    public Position getPosition() {
        return super.getPosition();
    }

    public boolean getIsOpen(){
        return isOpen;
    }

    public boolean getIsPrev(){
        return isPrev;
    }

    public void open() {
        isOpen = true;
    }

    @Override
    public boolean walkableBy(Player player) { return isOpen; }

}
