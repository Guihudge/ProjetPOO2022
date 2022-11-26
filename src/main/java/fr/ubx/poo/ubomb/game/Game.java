package fr.ubx.poo.ubomb.game;

import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.character.Monster;
import fr.ubx.poo.ubomb.go.character.Player;
import fr.ubx.poo.ubomb.launcher.MapMultipeLevel;

import java.util.LinkedList;
import java.util.List;

public class Game {

    private final Configuration configuration;
    private final Player player;
    private Grid grid;

    private final MapMultipeLevel levelsList;

    private Integer levelId = 1;

    private final List<Monster> monsterList = new LinkedList<>();

    public Game(Configuration configuration, Grid grid) {
        this.configuration = configuration;
        this.grid = grid;
        levelsList = null;
        player = new Player(this, configuration.playerPosition());
        updateMonster((Level) grid);
    }

    public Game(Configuration configuration, MapMultipeLevel levelsList) {
        this.configuration = configuration;
        player = new Player(this, configuration.playerPosition());
        this.levelsList = levelsList;
        this.grid = levelsList.getLevel(levelId);
        updateMonster(levelsList.getLevel(levelId));
    }

    public Configuration configuration() {
        return configuration;
    }

    // Returns the player, monsters and bomb at a given position
    public List<GameObject> getGameObjects(Position position) {
        List<GameObject> gos = new LinkedList<>();
        if (player().getPosition().equals(position))
            gos.add(player);
        return gos;
    }

    public Grid grid() {
        return grid;
    }

    public Player player() {
        return this.player;
    }

    private void updateMonster(Level level){
        monsterList.clear();
        for (Position monterPos: level.getMonsterPositionList()) {
            monsterList.add(new Monster(this, monterPos, levelId/2));
        }
    }

    public void nextLevel(){
        if (levelsList == null){
            System.err.println("No multiple level!");
        }
        if (levelId+1 > levelsList.getNumberOfLevel()){
            System.err.println("Level index out of range!");
        }
        levelId++;
        updateMonster(levelsList.getLevel(levelId));
        this.grid = levelsList.getLevel(levelId);

    }

    public void prevLevel(){
        if (levelsList == null){
            System.err.println("No multiple level!");
            return;
        }
        if (levelId-1 <= 0){
            System.err.println("Level index out of range!");
            return;
        }
        levelId--;
        updateMonster(levelsList.getLevel(levelId));
        this.grid = levelsList.getLevel(levelId);

    }

    public List<Monster> getMonsterList() {
        return monsterList;
    }
}
