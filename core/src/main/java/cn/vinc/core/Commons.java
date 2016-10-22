package cn.vinc.core;

import java.awt.*;
import java.util.Random;

/**
 * Created by Administrator on 2016/10/22.
 */
public class Commons {
    public static final int BORDER_WIDTH = 600;
    public static final int BORDER_HEIGHT = 500;
    public static final int FRAME_LOCATION_X = 300;
    public static final int FRAME_LOCATION_Y = 100;
    public static final int FRAME_WIDTH = 700;
    public static final int FRAME_HEIGHT = 600;
    public static final int BODY_LENGH = 10;

    public static DIRECT randomDirect() {
        Random random = new Random();
        Commons.DIRECT[] values = Commons.DIRECT.values();
        int i = random.nextInt(values.length);
        Commons.DIRECT value = values[i];
        return value;
    }

    public static Color randomColor() {
        Color[] colors = {Color.blue, Color.PINK, Color.ORANGE, Color.LIGHT_GRAY, Color.GREEN,Color.cyan,Color.ORANGE,Color.GRAY};
        return colors[new Random().nextInt(colors.length)];
    }

    public enum DIRECT{
        LEFT,UP,RIGHT,DOWN
    }



}
