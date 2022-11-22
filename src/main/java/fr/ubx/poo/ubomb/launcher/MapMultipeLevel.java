package fr.ubx.poo.ubomb.launcher;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Properties;

public class MapMultipeLevel{
    int numberOfLevel;
    ArrayList<MapLevel> levels = new ArrayList<>();

    public MapMultipeLevel(Properties config) throws IOException {
        this.numberOfLevel = Integer.parseInt(config.getProperty("levels", "1"));
        boolean compressedString = Boolean.parseBoolean(config.getProperty("compression", "false"));

        for (int i = 0; i < numberOfLevel; i++) {
            String propName = "level" + (i+1);
            String level = config.getProperty(propName);
            System.out.println("propName: " + propName + "\n levelString: " + level + "\n");
            if (compressedString){
                levels.add(loadFromString(decompressString(level)));
            }
            else {
                levels.add(loadFromString(level));
            }
        }
        System.out.println("load " + levels.size() + "maps");
    }

    public MapLevel getLevel(int levelId){
        if (levelId <= 0 || levelId > levelId){
            System.err.println("Invalid level ID in MapMultipleLevel!");
            return null;
        }
        else {
            return levels.get(levelId-1);
        }
    }

    private String decompressString(String s){
        StringBuilder decompressedString = new StringBuilder();

        for (int i = 0; i < s.length(); i++){
            if (!Character.isDigit(s.charAt(i))){
                decompressedString.append(s.charAt(i));
            }
            else{
                for (int rep = 1; rep < Integer.parseInt(String.valueOf(s.charAt(i))); rep++){
                    decompressedString.append(s.charAt(i-1));
                }
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
