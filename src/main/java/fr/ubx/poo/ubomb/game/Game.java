package fr.ubx.poo.ubomb.game;

import fr.ubx.poo.ubomb.go.character.Monster;
import fr.ubx.poo.ubomb.go.character.Player;
import fr.ubx.poo.ubomb.launcher.MapMultipleLevel;

import java.util.LinkedList;
import java.util.List;

public class Game {

    private final Configuration configuration;
    private final Player player;
    private Grid grid;

    private final MapMultipleLevel levelsList;

    private Integer levelId = 1;

    private List<Monster> monsterList = new LinkedList<>();

    public Game(Configuration configuration, Grid grid) {
        this.configuration = configuration;
        this.grid = grid;
        levelsList = null;
        player = new Player(this, configuration.playerPosition());
        updateMonster((Level) grid);
    }

    public Integer getLevelId() {
        return levelId;
    }

    public Game(Configuration configuration, MapMultipleLevel levelsList) {
        this.configuration = configuration;
        player = new Player(this, configuration.playerPosition());
        this.levelsList = levelsList;
        this.grid = levelsList.getLevel(levelId);
        updateMonster(levelsList.getLevel(levelId));
    }

    public Configuration configuration() {
        return configuration;
    }

    public Grid grid() {
        return grid;
    }

    public Player player() {
        return this.player;
    }

    private void updateMonster(Level level) {
        List<Monster> monsters = null;
        if (levelsList == null) {
            monsters = new LinkedList<>();
            for (Position monterPos : level.getMonsterPositionList()) {
                monsters.add(new Monster(this, monterPos, levelId / 2));
            }
        } else {
            monsters = levelsList.getMonsterList(levelId);
            if (monsters == null) {
                monsters = new LinkedList<>();
                for (Position monterPos : level.getMonsterPositionList()) {
                    monsters.add(new Monster(this, monterPos, levelId / 2));
                }
                levelsList.addMonsterList(monsters, levelId);
            }
        }
        monsterList = monsters;
    }

    public void nextLevel() {
        if (levelsList == null) {
            System.err.println("No multiple level!");
        }
        if (levelId + 1 > levelsList.getNumberOfLevel()) {
            System.err.println("Level index out of range!");
        }
        levelId++;
        updateMonster(levelsList.getLevel(levelId));
        this.grid = levelsList.getLevel(levelId);

    }

    public void prevLevel() {
        if (levelsList == null) {
            System.err.println("No multiple level!");
            return;
        }
        if (levelId - 1 <= 0) {
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
