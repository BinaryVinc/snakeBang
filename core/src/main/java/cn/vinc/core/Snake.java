package cn.vinc.core;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by vinc on 2016/10/21.
 */
public class Snake {

    public Commons.DIRECT direct = Commons.DIRECT.DOWN;

    public int len = 3;

    public int[] xs = new int[7];

    public int[] ys = new int[7];

    public int own = 0; //1me,0machine

    public int id;

    public boolean alive = true;

    public Color color;

    public Snake(int id, Color color, int own) {
        this.color = color;
        this.id = id;
        Random random = new Random();
        int posx = random.nextInt(Commons.FRAME_LOCATION_X / Commons.BODY_LENGH);
        int posy = random.nextInt(Commons.FRAME_LOCATION_Y / Commons.BODY_LENGH);
        this.direct = Commons.randomDirect();
        xs[0] = posx;
        ys[0] = posy;
        xs[1] = posx + 1;
        ys[1] = posy;
        xs[2] = posx + 2;
        ys[2] = posy;
        this.own = own;
    }

    public Snake(int own) {
        this(-1, Color.BLACK, 1);
        xs[0] = Commons.FRAME_LOCATION_X / Commons.BODY_LENGH;
        ys[0] = Commons.FRAME_LOCATION_Y / Commons.BODY_LENGH;
        xs[1] = Commons.FRAME_LOCATION_X / Commons.BODY_LENGH + 1;
        ys[1] = Commons.FRAME_LOCATION_Y / Commons.BODY_LENGH;
        xs[2] = Commons.FRAME_LOCATION_X / Commons.BODY_LENGH + 2;
        ys[2] = Commons.FRAME_LOCATION_Y / Commons.BODY_LENGH;
    }

    public boolean move(Commons.DIRECT location, List<Egg> eggs, Snake[] snakes) {
        ensureCapicity();
        int _x, _y;
        _x = xs[0];
        _y = ys[0];

        if (this.direct == Commons.DIRECT.RIGHT && location == Commons.DIRECT.LEFT)
            location = Commons.DIRECT.RIGHT;
        if (this.direct == Commons.DIRECT.LEFT && location == Commons.DIRECT.RIGHT)
            location = Commons.DIRECT.LEFT;
        if (this.direct == Commons.DIRECT.UP && location == Commons.DIRECT.DOWN)
            location = Commons.DIRECT.UP;
        if (this.direct == Commons.DIRECT.DOWN && location == Commons.DIRECT.UP)
            location = Commons.DIRECT.DOWN;


        switch (location) {
            case LEFT:
                direct = Commons.DIRECT.LEFT;
                if (!isDead(--_x, _y, snakes)) {
                    freshBody(eggs, _x, _y);
                    return true;
                } else {
                    alive = false;
                }
                break;
            case UP:
                direct = Commons.DIRECT.UP;
                if (!isDead(_x, --_y, snakes)) {
                    freshBody(eggs, _x, _y);
                    return true;
                } else {
                    alive = false;
                }
                break;
            case RIGHT:
                direct = Commons.DIRECT.RIGHT;
                if (!isDead(++_x, _y, snakes)) {
                    freshBody(eggs, _x, _y);
                    return true;
                } else {
                    alive = false;
                }
                break;
            case DOWN:
                direct = Commons.DIRECT.DOWN;
                if (!isDead(_x, ++_y, snakes)) {
                    freshBody(eggs, _x, _y);
                    return true;
                } else {
                    alive = false;
                }
                break;
            default:
                break;
        }
        return false;
    }

    private void freshBody(List<Egg> eggs, int _x, int _y) {
        int index;
        boolean eat = false;
        if ((index = this.crashFood(_x, _y, eggs)) != -1) {
            eggs.remove(index);
            eat = true;
        }
        if (!eat) {
            for (int i = len; i > 0; i--) {
                xs[i] = xs[i - 1];
                ys[i] = ys[i - 1];
            }
        } else {
            for (int i = len; i > 0; i--) {
                xs[i] = xs[i - 1];
                ys[i] = ys[i - 1];
            }
            this.len++;
        }
        xs[0] = _x;
        ys[0] = _y;
    }

    private synchronized int crashFood(int x, int y, List<Egg> eggs) {
        Rectangle snakeRec = new Rectangle(x * Commons.BODY_LENGH, y * Commons.BODY_LENGH, Commons.BODY_LENGH, Commons.BODY_LENGH);
        for (int i = 0; i < eggs.size(); i++) {
            Egg eg = eggs.get(i);
            Rectangle tmpRt = new Rectangle(eg.x * Commons.BODY_LENGH, eg.y * Commons.BODY_LENGH, Commons.BODY_LENGH, Commons.BODY_LENGH);
            if (tmpRt.intersects(snakeRec)) {
                return i;
            }
        }
        return -1;
    }

    private boolean isDead(int x, int y, Snake[] snakes) {

//        for (int i = len; i > 0; i--) {
//            if (xs[i] == x && ys[i] == y) {
//                return true;
//            }
//        }
        for (Snake snake : snakes) {
            if (snake.id == this.id) continue;
            for (int i = 1; i < snake.xs.length; i++) {
                if (x == snake.xs[i] && y == snake.ys[i]) {
                    return true;
                }
            }
        }

        return x == -1 || x * Commons.BODY_LENGH == Commons.BORDER_WIDTH || y == -1 || y * Commons.BODY_LENGH == Commons.BORDER_HEIGHT;

    }

    private void ensureCapicity() {
        if (len > xs.length / 2) {
            int[] newXs = new int[xs.length * 3 + 1];
            int[] newYs = new int[ys.length * 3 + 1];
            System.arraycopy(xs, 0, newXs, 0, xs.length);
            System.arraycopy(ys, 0, newYs, 0, ys.length);
            xs = newXs;
            ys = newYs;
        }
    }


}
