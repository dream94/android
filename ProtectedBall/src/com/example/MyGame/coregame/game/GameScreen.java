package com.example.MyGame.coregame.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import com.example.MyGame.coregame.R;
import com.example.MyGame.coregame.framework.Game;
import com.example.MyGame.coregame.framework.Screen;

import java.util.Iterator;

/**
 * Created by Admin on 2015/4/7.
 */
public class GameScreen extends Screen {
    private long starttime = System.nanoTime();
    private World world;
    private Paint paint = new Paint();
    private RectF rect = new RectF();
    private GradientDrawable gradient;
    private Context r;
    public GameScreen(Game game) {
        super(game);
        r = (Context)game;
        world = new World(game);
        rect.top = world.core.coords.y - world.core.shieldRadius;
        rect.left = world.core.coords.x - world.core.shieldRadius;
        rect.bottom = world.core.coords.y + world.core.shieldRadius;
        rect.right = world.core.coords.x + world.core.shieldRadius;

        paint.setAntiAlias(true);
        paint.setStrokeWidth(0.0F);

        gradient = new GradientDrawable(GradientDrawable.Orientation.TL_BR,
                new int[]{0xff001319, 0xff013e3f});
        gradient.setGradientType(GradientDrawable.RADIAL_GRADIENT);
        gradient.setGradientRadius((int) world.offScreenRadius);
        gradient.setDither(false);
        gradient.setGradientCenter(0.5F, 0.5F);
        gradient.setBounds(new Rect(0, 0, game.getGraphics().getWidth(),
                game.getGraphics().getHeight()));

        paint.setTextSize(((float)game.getGraphics().getHeight()) / 16F);
        paint.setTextAlign(Paint.Align.CENTER);

    }

    private void drawMessage(String message, Canvas c)
    {
        float y = paint.getTextSize();
        for(String line: message.split("\n"))
        {
            // Draw black stroke      用黑色描边
            paint.setStrokeWidth(2F);    //边长
            paint.setColor(0xff000000);
            paint.setStyle(Paint.Style.STROKE);

            c.drawText(line, c.getWidth()/2F, y, paint);
            // Draw white text            用白色填充内容
            paint.setStrokeWidth(0.0F);
            paint.setColor(0xffffffff);
            paint.setStyle(Paint.Style.FILL);   //填充内容

            c.drawText(line, c.getWidth()/2F, y, paint);

            y += paint.getTextSize();
        }
    }
    @Override
    public void update(float deltaTime) {
        world.update(deltaTime);
    }

    @Override
    public void present(float deltaTime) {         //绘制画面
        Canvas c = game.getGraphics().getCanvas();
        gradient.draw(c);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);    //填充和描边
        if(world.core.shieldEnergy > 0.0F)
        {
            paint.setColor(0xff003cca);
            paint.setAlpha((int) (80.0F +              //透明度在0-255
                    (255.0F - 80.0F) * world.core.shieldEnergy));
            c.drawCircle(world.core.coords.x, world.core.coords.y,
                    world.core.shieldRadius, paint);
            paint.setAlpha(255);
        }
        paint.setColor(0xff19dbe2);
        c.drawCircle(world.core.coords.x, world.core.coords.y,
                world.core.maxRadius * world.core.health,
                paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(0xffffffff);
        paint.setStrokeWidth(Core.SHEILD_WITH);
        c.drawArc(rect, (360.0F - world.core.angle),
                (360.0F - world.core.GAP_ANGLE), false, paint);
        paint.setStrokeWidth(0.0F);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        Iterator<Dot> iterator = world.dots.iterator();
        while(iterator.hasNext())
        {
            int color = 0;
            Dot dot = iterator.next();
            if(dot.type == Dot.Type.Enemy)
                color = 0xffe2192e;
            else if(dot.type == Dot.Type.Health)
                color = 0xff19dbe2;
            else if(dot.type == Dot.Type.Shield)
                color = 0xff003cca;
            paint.setColor(color);
            c.drawCircle(dot.coords.x, dot.coords.y,
                    dot.maxRadius * dot.energy, paint);
        }

        if(world.state == World.GameState.Running)
            drawMessage(world.getTime(), c);
        else if (world.state == World.GameState.Ready)
            drawMessage(r.getString(R.string.ready), c);
        else if (world.state == World.GameState.Pause)
            drawMessage(r.getString(R.string.paused), c);
        else if (world.state == World.GameState.GameOver)
            drawMessage(r.getString(R.string.game_over) +
                    "\n" +
                    r.getString(R.string.your_time) + " " + world.getTime() +
                    "\n" + r.getString(R.string.game_url), c);
    }

    @Override
    public void pause() {
        world.state = World.GameState.Pause;
    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
