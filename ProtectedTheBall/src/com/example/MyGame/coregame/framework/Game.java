package com.example.MyGame.coregame.framework;

/**
 * Created by Admin on 2015/4/6.
 */
public interface Game {
    public Input getInput();
    public Vibration getVibration();
    public Audio getAudio();
    public FileIO getFileIO();
    public Graphics getGraphics();
    public void setScreen(Screen screen);
    public Screen getCurrentScreen();
    public Screen getStartScreen();
}
