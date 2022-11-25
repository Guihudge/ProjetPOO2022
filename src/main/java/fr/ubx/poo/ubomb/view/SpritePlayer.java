/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubomb.view;

import fr.ubx.poo.ubomb.go.character.Player;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class SpritePlayer extends Sprite {
    private boolean temp = false;

    public SpritePlayer(Pane layer, Player player) {
        super(layer, null, player);
        updateImage();
    }

    @Override
    public void updateImage() {
        Player player = (Player) getGameObject();
        Image image = ImageResourceFactory.getPlayer(player.getDirection()).getImage();
        if(player.isDamagetaken()){
            if(temp) {
                image = null;
                temp = false;
            }else{
                temp = true;
            }
        }
        setImage(image);
    }
}

