package fr.ubx.poo.ubomb.launcher;

import fr.ubx.poo.ubomb.game.Configuration;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Level;
import fr.ubx.poo.ubomb.game.Position;

import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

public class GameLauncher {

    public static Game load() {
        Configuration configuration = new Configuration(new Position(0, 0), 3, 5, 4000, 5, 1000);
        return new Game(configuration, new Level(new MapLevelDefault()));
    }

    public static Game load(Reader file){

        return null;
    }

    private Configuration loadConfigFromFile(Reader file) throws IOException {
        Properties config = new Properties();
        config.load(file);
        String player = config.getProperty("player");
        int bombBagCapacity = Integer.getInteger( config.getProperty("bombBagCapacity", "3"));
        int playerLives = Integer.getInteger( config.getProperty("playerLives", "5"));
        int playerInvisibilityTime = Integer.getInteger( config.getProperty("playerInvisibilityTime", "4000"));
        int monsterVelocity = Integer.getInteger( config.getProperty("monsterVelocity", "5"));
        int monsterInvisibilityTime = Integer.getInteger( config.getProperty("monsterInvisibilityTime", "1000"));
        int player_x = Integer.getInteger(player.split("x")[0]);
        int player_y = Integer.getInteger(player.split("x")[1]);
        Position player_pos = new Position(player_x, player_y);

        return new Configuration(player_pos, bombBagCapacity, playerLives, playerInvisibilityTime, monsterVelocity, monsterInvisibilityTime);
    }

    private

}
