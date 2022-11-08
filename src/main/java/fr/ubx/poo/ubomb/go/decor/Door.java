package fr.ubx.poo.ubomb.go.decor;

import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;

public class Door extends Decor{

    private boolean isOpen;

    public Door(Position position) {
        super(position);
        isOpen = false;
    }

    public Door(Position position, boolean value){
        super(position);
        isOpen = value;
    }

    @Override
    public Position getPosition() {
        return super.getPosition();
    }

    public void open() {
        if(canOpen())
            isOpen = true;
    }

    public boolean canOpen(){
        return game.player().getKeys() > 0;
    }
}
