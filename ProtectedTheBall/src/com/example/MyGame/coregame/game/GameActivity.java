package com.example.MyGame.coregame.game;
import com.example.MyGame.coregame.framework.Screen;
import com.example.MyGame.coregame.framework.impl.*;

/**
 * Created by Admin on 2015/4/6.
 */
public class GameActivity extends AndroidGame {
    @Override
    public Screen getStartScreen() {
        return new GameScreen(this);
    }
}
