/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubomb.engine;

import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.character.Monster;
import fr.ubx.poo.ubomb.go.character.Player;
import fr.ubx.poo.ubomb.go.decor.Bomb;
import fr.ubx.poo.ubomb.go.decor.Door;
import fr.ubx.poo.ubomb.view.*;
import javafx.animation.AnimationTimer;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;


public final class GameEngine {

    private static AnimationTimer gameLoop;
    private final Game game;
    private final Player player;
    private final List<Sprite> sprites = new LinkedList<>();
    private final Set<Sprite> cleanUpSprites = new HashSet<>();
    private List<Bomb> bombs = new ArrayList<>();
    private final Stage stage;
    private StatusBar statusBar;
    private Pane layer;
    private Input input;

    private Timer timerMonster;

    public GameEngine(Game game, final Stage stage) {
        this.stage = stage;
        this.game = game;
        this.player = game.player();
        initialize();
        buildAndSetGameLoop();
        timerMonster = new Timer(1000 / game.configuration().monsterVelocity());
    }

    private void initialize() {
        Group root = new Group();
        layer = new Pane();

        int height = game.grid().height();
        int width = game.grid().width();
        int sceneWidth = width * ImageResource.size;
        int sceneHeight = height * ImageResource.size;
        Scene scene = new Scene(root, sceneWidth, sceneHeight + StatusBar.height);
        scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());

        stage.setScene(scene);
        stage.setResizable(false);
        stage.sizeToScene();
        stage.show();

        input = new Input(scene);
        root.getChildren().add(layer);
        statusBar = new StatusBar(root, sceneWidth, sceneHeight, game);

        // Create sprites
        sprites.clear();
        for (var decor : game.grid().values()) {
            sprites.add(SpriteFactory.create(layer, decor));
            decor.setModified(true);
        }

        for (Monster monster : game.getMonsterList()) {
            sprites.add(new SpriteMonster(layer, monster));
        }

        sprites.add(new SpritePlayer(layer, player));
    }

    void buildAndSetGameLoop() {
        gameLoop = new AnimationTimer() {
            public void handle(long now) {
                // Check keyboard actions
                processInput(now);

                // Do actions
                update(now);
                createNewBombs(now);
                checkCollision(now);
                checkExplosions();

                // Graphic update
                cleanupSprites();
                render();
                statusBar.update(game);
            }
        };
    }

    private void checkExplosions() {

    }

    private void animateExplosion(Position src, Position dst) {
        ImageView explosion = new ImageView(ImageResource.EXPLOSION.getImage());
        TranslateTransition tt = new TranslateTransition(Duration.millis(200), explosion);
        tt.setFromX(src.x() * Sprite.size);
        tt.setFromY(src.y() * Sprite.size);
        tt.setToX(dst.x() * Sprite.size);
        tt.setToY(dst.y() * Sprite.size);
        tt.setOnFinished(e -> {
            layer.getChildren().remove(explosion);
        });
        layer.getChildren().add(explosion);
        tt.play();
    }

    private void createNewBombs(long now) {
        if(player.getRequestbomb()){
            player.setRequestBomb(false);
            if (player.getAvailableBomb() > 0) {
                player.setAvailableBomb(player.getAvailableBomb() - 1);
                Bomb bomb = new Bomb(game, player.getPosition());
                game.grid().set(player.getPosition(), bomb);
                sprites.add(new SpriteBomb(layer,bomb));
                bombs.add(bomb);
                player.setModified(true);
            }
        }
    }

    private void checkCollision(long now) {
        for (Monster monster : game.getMonsterList()) {
            if (game.player().getPosition().equals(monster.getPosition())) {
                player.takeDommage();
            }
        }
    }

    private void processInput(long now) {
        if (input.isExit()) {
            gameLoop.stop();
            Platform.exit();
            System.exit(0);
        } else if (input.isMoveDown()) {
            player.requestMove(Direction.DOWN);
        } else if (input.isMoveLeft()) {
            player.requestMove(Direction.LEFT);
        } else if (input.isMoveRight()) {
            player.requestMove(Direction.RIGHT);
        } else if (input.isMoveUp()) {
            player.requestMove(Direction.UP);
        } else if (input.isKey()) {
            if (game.grid().get(player.getPosition()) instanceof Door) {
                Door door = (Door) game.grid().get(player.getPosition());
                if (player.getKeys() >= 1 && !door.getIsOpen()) {
                    door.open();
                    door.setModified(true);
                    player.useKey();
                }
                Position playerPos = new Position(0, 0);
                if (door.getIsOpen()) {
                    if (!door.getIsPrev()) {
                        game.nextLevel();
                        playerPos = getNewPlayerPosition();
                    }
                    if (door.getIsPrev()) {
                        game.prevLevel();
                        playerPos = getPrevPlayerPosition();
                    }
                    initialize();
                    player.setPosition(playerPos);
                }
            }
        }else if (input.isBomb()){
            player.requestBomb();
        }
        input.clear();
    }

    private Position getNewPlayerPosition() {
        for (int x = 0; x < game.grid().width(); x++) {
            for (int y = 0; y < game.grid().height(); y++) {
                if (game.grid().get(new Position(x, y)) instanceof Door) {
                    Door door = (Door) game.grid().get(new Position(x, y));
                    if (door.getIsPrev()) {
                        return new Position(x, y);
                    }
                }
            }
        }
        return null;
    }

    private Position getPrevPlayerPosition() {
        for (int x = 0; x < game.grid().width(); x++) {
            for (int y = 0; y < game.grid().height(); y++) {
                if (game.grid().get(new Position(x, y)) instanceof Door) {
                    Door door = (Door) game.grid().get(new Position(x, y));
                    if (!door.getIsPrev()) {
                        return new Position(x, y);
                    }
                }
            }
        }
        return null;
    }

    private void showMessage(String msg, Color color) {
        Text waitingForKey = new Text(msg);
        waitingForKey.setTextAlignment(TextAlignment.CENTER);
        waitingForKey.setFont(new Font(60));
        waitingForKey.setFill(color);
        StackPane root = new StackPane();
        root.getChildren().add(waitingForKey);
        Scene scene = new Scene(root, 400, 200, Color.WHITE);
        stage.setScene(scene);
        input = new Input(scene);
        stage.show();
        new AnimationTimer() {
            public void handle(long now) {
                processInput(now);
            }
        }.start();
    }


    private void update(long now) {
        player.update(now);
        Position playerPos = player.getPosition();
        if (!timerMonster.isRunning()) {
            timerMonster.start();
        }
        timerMonster.update(now);

        if (timerMonster.remaining() <= 0) {
            for (Monster monster : game.getMonsterList()) {
                monster.update(now);
                timerMonster.start();
            }
        }

        for (int i = 0; i < bombs.size(); i++) {
            bombs.get(i).update(now);
            if(bombs.get(i).isDeleted()) {
                player.setAvailableBomb(player.getAvailableBomb() + 1);
                bombs.remove(bombs.get(i));
            }
        }

        checkCollision(now);

        if (player.getLives() == 0) {
            gameLoop.stop();
            showMessage("Perdu!", Color.RED);
        }

        if (player.isHadPrincess()) {
            gameLoop.stop();
            showMessage("You Win", Color.GREEN);
        }
    }

    public void cleanupSprites() {
        sprites.forEach(sprite -> {
            if (sprite.getGameObject().isDeleted()) {
                game.grid().remove(sprite.getPosition());
                cleanUpSprites.add(sprite);
            }
        });
        cleanUpSprites.forEach(Sprite::remove);
        sprites.removeAll(cleanUpSprites);
        cleanUpSprites.clear();
    }

    private void render() {
        sprites.forEach(Sprite::render);
    }

    public void start() {
        gameLoop.start();
    }
}