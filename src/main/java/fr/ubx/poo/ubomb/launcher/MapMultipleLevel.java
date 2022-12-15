package fr.ubx.poo.ubomb.launcher;

import fr.ubx.poo.ubomb.game.Level;
import fr.ubx.poo.ubomb.go.character.Monster;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MapMultipleLevel {
    private final int numberOfLevel;
    private final ArrayList<Level> levels = new ArrayList<>();

    private final ArrayList<List<Monster>> monsterList = new ArrayList<>();

    public MapMultipleLevel(Properties config) {
        this.numberOfLevel = Integer.parseInt(config.getProperty("levels", "1"));
        boolean compressedString = Boolean.parseBoolean(config.getProperty("compression", "false"));

        for (int i = 0; i < numberOfLevel; i++) {
            String propName = "level" + (i+1);
            String level = config.getProperty(propName);
            System.out.println("propName: " + propName + "\n levelString: " + level + "\n");
            if (compressedString){
                levels.add(new Level(loadFromString(decompressString(level))));
            }
            else {
                levels.add(new Level(loadFromString(level)));
            }
        }
        System.out.println("load " + levels.size() + "maps");
    }

    public Level getLevel(int levelId){
        if (levelId <= 0 || levelId > numberOfLevel){
            System.err.println("Invalid level ID in MapMultipleLevel!");
            return null;
        }
        else {
            return levels.get(levelId-1);
        }
    }
    public List<Monster> getMonsterList(int levelId){
        if (levelId <= 0 || levelId > numberOfLevel){
            System.err.println("Invalid level ID in getMonsterList!");
            return null;
        }
        if (levelId > monsterList.toArray().length){
            System.out.println("creat new monster...");
            return null;
        }
        else {
            return monsterList.get(levelId-1);
        }
    }

    public void addMonsterList(List<Monster> newMonsterList, int id){
        monsterList.add(id-1, newMonsterList);
    }

    public int getNumberOfLevel() {
        return numberOfLevel;
    }

    private String decompressString(String s){
        StringBuilder decompressedString = new StringBuilder();

        for (int i = 0; i < s.length(); i++){
            if (!Character.isDigit(s.charAt(i))){
                decompressedString.append(s.charAt(i));
            }
            else{
                decompressedString.append(String.valueOf(s.charAt(i - 1)).repeat(Math.max(0, Integer.parseInt(String.valueOf(s.charAt(i))) - 1)));
            }
        }
        return  decompressedString.toString();
    }

    private MapLevel loadFromString(String s){

        String[] world = s.split("x");
        int nbLine = world.length;
        int nbCol = world[0].length();
        MapLevel level = new MapLevel(nbCol, nbLine);
        for (int y = 0; y<nbLine; y++){
            char[] line = world[y].toCharArray();
            for (int x = 0; x<nbCol; x++) {
                level.set(x,y,Entity.fromCode(line[x]));
        }}
        return level;
    }
}
