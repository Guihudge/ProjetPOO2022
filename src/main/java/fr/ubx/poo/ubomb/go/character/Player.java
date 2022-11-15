/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubomb.go.character;

import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Level;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.Movable;
import fr.ubx.poo.ubomb.go.TakeVisitor;
import fr.ubx.poo.ubomb.go.decor.*;
import fr.ubx.poo.ubomb.go.decor.bonus.*;

public class Player extends GameObject implements Movable, TakeVisitor {

    private Direction direction;
    private boolean moveRequested = false;
    private int lives;
    private int keys;

    private int bombCapacity;
    private int bombRange;

    private int availableBomb;

    private boolean hadPrincess;

    public int getKeys() {
        return keys;
    }

    public Player(Game game, Position position) {
        super(game, position);
        this.direction = Direction.DOWN;
        this.lives = game.configuration().playerLives();
        this.bombCapacity = game.configuration().bombBagCapacity();
        this.availableBomb = this.bombCapacity;
        this.bombRange = 1;
        this.keys = 0;
        this.hadPrincess = false;
    }


    @Override
    public void take(Key key) {
        System.out.println("Take the key ...");
        keys++;
        key.remove();
    }

    public void take(Princess princess) {
        System.out.println("Get a Princess!");
        hadPrincess = true;
        princess.remove();
    }

    public void take(BombRangeDec rangeDec) {
        System.out.println("Decrement bomb range");
        if (bombRange > 1)
            bombRange--;
        rangeDec.remove();
    }

    public void take(BombRangeInc rangeInc) {
        System.out.println("Increment bomb range");
        bombRange++;
        rangeInc.remove();
    }

    public void take(BombNbInc numberInc) {
        System.out.println("Bomb bag capacity +1");
        bombCapacity++;
        availableBomb = bombCapacity;
        numberInc.remove();
    }

    public void take(BombNbDec numberDec) {
        System.out.println("Bomb bag capacity -1");
        bombCapacity--;
        availableBomb = bombCapacity;
        numberDec.remove();
    }

    public void take(Heart lives) {
        System.out.println("Add lives");
        this.lives++;
        lives.remove();
    }

    public void doMove(Direction direction) {
        // This method is called only if the move is possible, do not check again
        Position nextPos = direction.nextPosition(getPosition());
        GameObject next = game.grid().get(nextPos);
        if (next instanceof Bonus bonus) {
            bonus.takenBy(this);
        }
        if (next instanceof Monster) {
            lives -= 1;
        }
        setPosition(nextPos);
    }


    public int getLives() {
        return lives;
    }

    public int getBombCapacity() {
        return bombCapacity;
    }

    public int getBombRange() {
        return bombRange;
    }

    public boolean isHadPrincess() {
        return hadPrincess;
    }

    public int getAvailableBomb() {
        return availableBomb;
    }

    public void setAvailableBomb(int availableBomb) {
        this.availableBomb = availableBomb;
    }

    public Direction getDirection() {
        return direction;
    }

    public void requestMove(Direction direction) {
        if (direction != this.direction) {
            this.direction = direction;
            setModified(true);
        }
        moveRequested = true;
    }

    public final boolean canMove(Direction direction) {
        Decor pos = game.grid().get(direction.nextPosition(getPosition()));
        if (game.grid().inside(direction.nextPosition(getPosition()))) {
            if (pos instanceof Box) {
                Box box = (Box) pos;
                if (box.canMove(direction)){
                    box.doMove(direction);
                }

            }
            return !(pos instanceof Stone) && !(pos instanceof Tree);
        }
        return false;
    }

    public void openDoor(){
        if(game.grid().get(getPosition()) instanceof Door)
            if(((Door) game.grid().get(getPosition())).canOpen(game)) {
                ((Door) game.grid().get(getPosition())).open(game);
                keys--;
            }
    }

    public void update(long now) {
        if (moveRequested) {
            if (canMove(direction)) {
                doMove(direction);
            }
        }
        moveRequested = false;
    }

    @Override
    public void explode() {
        // TODO
    }
}
