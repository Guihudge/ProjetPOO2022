package fr.ubx.poo.ubomb.go.decor.bonus;

import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.character.Player;
import fr.ubx.poo.ubomb.go.decor.Decor;

public class Princess extends Bonus {
    public Princess(Position position){super(position);}

    @Override
    public void explode(){}

    @Override
    public void takenBy(Player player){
        player.take(this);
    }
}
