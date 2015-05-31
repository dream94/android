package com.example.MyGame.coregame.game;

import android.util.Log;
import com.example.MyGame.coregame.framework.*;
import com.example.MyGame.coregame.framework.impl.AndroidAudio;
import com.example.MyGame.coregame.framework.Input.TouchEvent;
import com.example.MyGame.coregame.framework.Input.KeyEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by Admin on 2015/4/7.
 */
public class World {
    private Random random = new Random();
    private Game game;
    private final int DOTS_COUNT = 10;

    public List<Dot> dots = new ArrayList<Dot>(DOTS_COUNT);
    public Core core = new Core();
    public float offScreenRadius;
    private final float SHELD_FACTOR = 20.0F;
    private final float ENERGY = 6.0F;

    private float time = 0.0F;

    public enum GameState{
        Ready, Running, Pause, GameOver;
    }
    public GameState state = GameState.Ready;
    private float diffculty = 0.04F;   //Max 0.1F

    private Sound core_hurt;
    private Sound core_shield;
    private Sound core_health;
    private Sound game_over;
    private Sound shield_collision;

    public World(Game game) {
        this.game = game;
        Graphics graphics = game.getGraphics();
        core.coords = new VectorF((float)graphics.getWidth()/2, (float)graphics.getHeight()/2);     //屏幕中心
        core.shieldRadius = (float)graphics.getWidth()/4;
        core.maxRadius = core.shieldRadius * 0.7F;
        core.angle = 0.0F;
        core.health = 1.0F;
        core.shieldEnergy = 0.0F;
        offScreenRadius =(float) Math.hypot((double)graphics.getWidth()/2, (double)graphics.getHeight()/2);   //最大半径
        Dot.maxRadius = core.maxRadius/8.0F;
        loadSounds();
    }
    private void loadSounds(){
        Audio audio = game.getAudio();
        core_hurt = audio.newSound("core_hurt.wav");
        core_shield = audio.newSound("core_shield.wav");
        core_health = audio.newSound("core_health.wav");
        game_over = audio.newSound("game_over.wav");
        shield_collision = audio.newSound("shield_collision.wav");
    }
    //Restart the game
    public void renew(){
        dots.clear();
        core.health = 1.0F;
        core.shieldEnergy = 0.0F;
        time = 0.0F;
        state = GameState.Ready;
        diffculty = 0.04F;
        generateStartDots(DOTS_COUNT);
    }
    public void generateStartDots(int count){
        for(int t=0; t<count; ++t){
            generateNewDot(true);
        }
    }
    public void generateNewDot(boolean flag){
        float linearSpeed = 10.0F * diffculty;      //线性速度,难度越大，小点的速度越快
        Dot dot = new Dot();
        if(flag){
            dot.coords = generateNewDotInRandomPlace();
        }else{
            dot.coords = generateNewDotAtOffScreenRadius();
            increasediffculty();
        }
        VectorF speed = new VectorF(linearSpeed*(-dot.coords.x/dot.coords.length()), linearSpeed*(-dot.coords.y/dot.coords.length()));
        dot.speed = speed;
        dot.coords.addThis(core.coords);
        dot.energy = random.nextFloat();
        if(dot.energy <= 0.3F){
            dot.energy = 0.3F;
        }
        float dot_type = random.nextFloat();
        Dot.Type type;
        if(dot_type>=0.9F){
            type = Dot.Type.Shield;
        }else if(dot_type>=0.8F){
            type = Dot.Type.Health;
        }else{
            type = Dot.Type.Enemy;
        }
        dot.type = type;
        dots.add(dot);
    }

    private VectorF generateNewDotInRandomPlace(){
        double angel = random.nextDouble()*2*Math.PI;
        VectorF vectorF = new VectorF((float)Math.cos(angel), (float)Math.sin(angel));
        vectorF.multiplyThis(core.shieldRadius+(offScreenRadius-core.shieldRadius)*random.nextFloat());
        return vectorF;
    }
    private VectorF generateNewDotAtOffScreenRadius(){
        double angel = random.nextDouble()*2*Math.PI;
        VectorF vectorF = new VectorF(offScreenRadius*((float)Math.cos(angel)), offScreenRadius*((float)Math.sin(angel)));
        return vectorF;
    }
    private void increasediffculty(){
        diffculty += 0.00005F;
    }

    public void update(float deltatime){
        if(state == GameState.Ready){
            updateReady(deltatime);
        }
        if(state == GameState.Running){
            updateRunning(deltatime);
        }
        if(state == GameState.Pause){
            updatePause(deltatime);
        }
    }

    private void updateReady(float deltatime){
        if(checkTouchUp()||checkMenuUp()){
            state = GameState.Running;
        }
    }
    private boolean checkTouchUp(){
        for(TouchEvent touchEvent:game.getInput().getTouchEvents()){
            if(touchEvent.type == TouchEvent.TOUCH_UP){
                Log.i("huang", "checkTouchUp touch");
                return true;
            }
        }
        return false;
    }
    private boolean checkMenuUp(){
        for(KeyEvent keyEvent:game.getInput().getKeyEvents()){
            if(keyEvent.type == KeyEvent.KEY_UP){
                Log.i("huang", "checkKeyEvent KeyEvent");
                return true;
            }
        }
        return false;
    }

    private void updateRunning(float deltatime){                 //核心运行流程
        checkTouchUp();       //清除一下缓冲区的触摸事件Buffer
        if(checkMenuUp()){
            state = GameState.Pause;
        }
        countTime(deltatime);
        doInput();
        generateNewDots(DOTS_COUNT);
        handleCollisions();
        moveDots(deltatime);
        decreaseShieldEnergy(deltatime);
    }


    private void countTime(float deltatime){
        time += deltatime;
    }
    private void doInput(){
        float orienAngle = game.getInput().getAzimuth();
        if(game.getInput().isTouchDown()){
            double TouchX = game.getInput().getTouchX();
            double TouchY = game.getInput().getTouchY();
            core.angle = normAngle((float)(Math.atan2(-(TouchY-core.coords.y), TouchX-core.coords.x)/Math.PI*180.0)-core.GAP_ANGLE/2F);
        }else{
            core.angle = stabilizeAngle(orienAngle - Core.GAP_ANGLE/2F, core.angle, 8F);
        }
    }
    private float normAngle(float angle){        //规范角度
        float angle1 = angle;
        if(angle1>360.0F){
            angle1 -= 360.0F;
        }else if(angle<0.0F){
            angle1 += 360.0F;
        }
        return angle1;
    }
    private float stabilizeAngle(float real, float current, float factor){              //稳定角度
        real = normAngle(real);
        current = normAngle(current);
        if(current - real>180F){
            real += 360F;
        }
        if(real - current > 180F){
            real -= 360F;
        }
        return normAngle((current*factor+real)/(1F+factor));
    }
    private void generateNewDots(int count){
        if(count>dots.size()){
            generateNewDot(false);
        }
    }
    private void handleCollisions(){
        Iterator<Dot> iterator = dots.iterator();
        while(iterator.hasNext()){
            handleCollision(iterator.next(), iterator);
        }
    }
    private void handleCollision(Dot dot, Iterator<Dot>iterator){
        float lengthToCoreCenter = (float)Math.hypot((double)(core.coords.x-dot.coords.x),(double)(core.coords.y-dot.coords.y));
        if(lengthToCoreCenter-core.shieldRadius>=0&& lengthToCoreCenter-core.shieldRadius<= dot.maxRadius*dot.energy+Core.SHEILD_WITH){
            checkCollisionWithShield(dot, iterator);
        }else if (lengthToCoreCenter-core.maxRadius * core.health<=dot.maxRadius * dot.energy)
            handleCollisionWithCore(dot, iterator);
    }
    private void checkCollisionWithShield(Dot dot, Iterator<Dot>iterator){
        if(core.shieldEnergy > 0.0F){
            iterator.remove();
            shield_collision.play(dot.energy);         //播放碰撞的音乐
            game.getVibration().vibrate(30);
        }else{
            float dotAngle = (float) Math.atan2((double)-(dot.coords.y - core.coords.y),(double)(dot.coords.x - core.coords.x)); //因为这里的y轴是倒置的，向下为正
            dotAngle = dotAngle*180.0F/(float)Math.PI;     //转换为角度
            dotAngle = normAngle(dotAngle);
            core.angle = normAngle(core.angle);
            while(dotAngle<core.angle){
                dotAngle += 360.0F;
            }
           if(!((dotAngle > core.angle)&&(dotAngle < core.angle + core.GAP_ANGLE)))
            {
                iterator.remove();
                shield_collision.play(dot.energy);
                game.getVibration().vibrate(30);
            }
        }
    }
    private void handleCollisionWithCore(Dot dot, Iterator<Dot>iterator){        //处理碰撞到核心
        if(dot.type == Dot.Type.Enemy){
            core.health -= dot.energy/ENERGY;
            if(core.health<0.0F){
                state = GameState.GameOver;
                game_over.play(1.0F);
                game.getVibration().vibrate(10);
                game.getVibration().vibrate(40);
                game.getVibration().vibrate(100);
                core.health = 0.0F;
            }
            core_hurt.play(dot.energy);
        }else if(dot.type == Dot.Type.Health){
            core.health += dot.energy/ENERGY;
            if(core.health>1.0F){
                core.health = 1.0F;
            }
            core_health.play(dot.energy);
        }else if(dot.type == Dot.Type.Shield){
            core.shieldEnergy += dot.energy/ENERGY;
            if(core.shieldEnergy>1.0F){
                core.shieldEnergy = 1.0F;
            }
            core_shield.play(dot.energy);
        }
        iterator.remove();
        game.getVibration().vibrate(30);
    }
    private void moveDots(float deltatime){
        for(Dot dot:dots){
            float x = dot.speed.x * deltatime * 100.0F;
            float y = dot.speed.y * deltatime * 100.0F;
            VectorF f = new VectorF(x, y);
            dot.coords.addThis(f);
        }
    }
    private void decreaseShieldEnergy(float deltatime){
        if(core.shieldEnergy>0.0F){
            core.shieldEnergy -= deltatime/SHELD_FACTOR;
            if(core.shieldEnergy<0.0F){
                core.shieldEnergy = 0.0F;
            }
        }
    }
    private void updatePause(float deltatime){
        if(checkMenuUp()||checkTouchUp()){
            state = GameState.Running;
        }
    }
    private void updateGameOver(float deltatime){
        if(checkMenuUp()||checkTouchUp()){
            renew();
        }
    }
    public String getTime(){
        int seconds = (int)time;
        int minute = seconds/60;
        int second = seconds%60;
        StringBuilder result = new StringBuilder();
        if(minute>0){
            result.append(minute+":");
        }
        result.append(String.format("%02d", second));
        return result.toString();
    }

}
