package com.example.MyGame.coregame.framework;

/**
 * Created by Admin on 2015/4/7.
 */
public abstract class Screen {
    public final Game game;

    protected Screen(Game game) {
        this.game = game;
    }

    public abstract void update(float deltaTime);
    public abstract void present(float deltaTime);
    public abstract void pause();
    public abstract void resume();
    public abstract void dispose();
}
