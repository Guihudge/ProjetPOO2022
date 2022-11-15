package fr.ubx.poo.ubomb.launcher;

import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

public class MapMultipeLevel extends MapLevel{
    int numberOfLevel;
    public static final Entity[][][] levels;

    public MapMultipeLevel(Reader file) throws IOException {
        super(0,0);
        Properties config = new Properties();
        config.load(file);
        this.numberOfLevel = Integer.parseInt(config.getProperty("levels", "1"));
        boolean compressedString = Boolean.parseBoolean(config.getProperty("compression", "false"));

        for (int i = 0; i < numberOfLevel; i++) {
            String propName = "level" + i;
            if (compressedString){
                levels[i] = loadFromString(decompressString(config.getProperty(propName)));
            }
            else {
                levels[i] = loadFromString(config.getProperty(propName));
            }
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

    private Entity[][] loadFromString(String s){

        String[] world = s.split("x");
        int nbLine = world.length;
        int nbCol = world[0].length();

        Entity[][] level = new Entity[nbCol][nbLine];
        for (int y = 0; y<nbLine; y++){
            char[] line = world[y].toCharArray();
            for (int x = 0; x<nbCol; x++) {
                level[x][y] = Entity.fromCode(line[x]);
        }}
        return level;
    }
}
