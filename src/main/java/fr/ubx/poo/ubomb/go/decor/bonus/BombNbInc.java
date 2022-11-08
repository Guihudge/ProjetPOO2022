package fr.ubx.poo.ubomb.go.decor.bonus;

import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.character.Player;

public class BombNbInc extends Bonus{
    public BombNbInc(Position position){super(position);}

    @Override
    public void takenBy(Player player) {
        player.take(this);
    }
}
