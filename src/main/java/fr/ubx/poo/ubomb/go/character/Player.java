/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubomb.go.character;

import fr.ubx.poo.ubomb.engine.Timer;
import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.Movable;
import fr.ubx.poo.ubomb.go.TakeVisitor;
import fr.ubx.poo.ubomb.go.Takeable;
import fr.ubx.poo.ubomb.go.decor.*;
import fr.ubx.poo.ubomb.go.decor.bonus.*;

public class Player extends GameObject implements Movable, TakeVisitor {

    private Direction direction;
    private boolean moveRequested = false;
    private int lives;
    private boolean damagetaken = false;
    final private Timer timer;
    private int keys;

    private int bombCapacity;
    private int bombRange;

    private int availableBomb;
    private boolean requestBomb = false;

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
        this.timer = new Timer(game.configuration().playerInvisibilityTime());
    }


    @Override
    public void take(Key key) {
        keys++;
        key.remove();
    }

    public void take(Princess princess) {
        hadPrincess = true;
        princess.remove();
    }

    public void take(BombRangeDec rangeDec) {
        if (bombRange > 1)
            bombRange--;
        rangeDec.remove();
    }

    public void take(BombRangeInc rangeInc) {
        bombRange++;
        rangeInc.remove();
    }

    public void take(BombNbInc numberInc) {
        bombCapacity++;
        availableBomb = bombCapacity;
        numberInc.remove();
    }

    public void take(BombNbDec numberDec) {
        bombCapacity--;
        availableBomb = bombCapacity;
        numberDec.remove();
    }

    public void take(Heart lives) {
        this.lives++;
        lives.remove();
    }

    public void doMove(Direction direction) {
        // This method is called only if the move is possible, do not check again
        Position nextPos = direction.nextPosition(getPosition());
        GameObject next = game.grid().get(nextPos);
        if (next instanceof Takeable takeable)
            takeable.takenBy(this);

        if (next instanceof Box box) {
            Position boxpos = direction.nextPosition(box.getPosition());
            game.grid().set(boxpos, box);
            game.grid().remove(nextPos);
            box.setPosition(boxpos);
        }
        setPosition(nextPos);
    }


    public int getLives() {
        return lives;
    }

    public boolean isDamagetaken() {
        return damagetaken;
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

    public void useKey() {
        keys--;
    }

    public void setAvailableBomb(int availableBomb) {
        this.availableBomb = availableBomb;
    }

    public Direction getDirection() {
        return direction;
    }

    public boolean getRequestbomb() {
        return requestBomb;
    }

    public void setRequestBomb(boolean requestBomb) {
        this.requestBomb = requestBomb;
    }

    public void requestMove(Direction direction) {
        if (direction != this.direction) {
            this.direction = direction;
            setModified(true);
        }
        moveRequested = true;
    }

    public void requestBomb() {
        requestBomb = true;
    }

    public final boolean canMove(Direction direction) {
        Decor pos = game.grid().get(direction.nextPosition(getPosition()));
        if (game.grid().inside(direction.nextPosition(getPosition()))) {
            if (pos instanceof Box) {
                Decor boxpos = game.grid().get(direction.nextPosition(pos.getPosition()));
                if (boxpos != null)
                    System.out.println(boxpos.getPosition());
                System.out.println(pos.getPosition());
                if (game.grid().inside(direction.nextPosition(pos.getPosition())))
                    return game.grid().get(direction.nextPosition(getPosition(), 2)) == null;
            }
            if (pos == null) {
                return true;
            }
            return pos.walkableBy(this);
        }
        return false;
    }

    public void update(long now) {
        if (moveRequested)
            if (canMove(direction))
                doMove(direction);

        moveRequested = false;

        if (damagetaken) {
            timer.update(now);
            if (timer.remaining() <= 0)
                damagetaken = false;

            setModified(true);
        }
    }

    public void takeDommage() {
        if (!damagetaken) {
            lives -= 1;
            timer.start();
            damagetaken = true;
        }
    }

    @Override
    public void explode() {
        // TODO
    }
}
