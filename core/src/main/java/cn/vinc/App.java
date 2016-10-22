package cn.vinc;

import cn.vinc.core.Commons;
import cn.vinc.core.Egg;
import cn.vinc.core.Snake;

import javax.swing.*;
import java.awt.*;
import java.awt.List;
import java.awt.event.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by vinc on 2016/10/22.
 */
public class App extends JFrame {


    public static java.util.Vector<Egg> foods = new java.util.Vector<Egg>();

    public static final Snake[] snakes = new Snake[6];

    private boolean stop = false;

    public App(String title) {

        this.setTitle(title);

        initFrame();

    }

    private void initFrame( ) {

        Snake own = new Snake(1);
        snakes[0] = own;

        for (int i = 1; i < snakes.length; i++) {
            snakes[i] = new Snake(i, Commons.randomColor(), 0);
        }

        final DrawPanel jp = new DrawPanel(snakes);

        addKeyListener(new MyKeyListener(snakes[0], jp));
        final Random random = new Random();

        new Thread(new Runnable() {
            public void run() {
                while (!stop) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Snake snake0 = snakes[0];
                    snake0.move(snake0.direct, foods, snakes);
                    for (int i = 1; i < snakes.length; i++) {
                        Snake snake = snakes[i];
                        int change = random.nextInt(5);
                        snake.direct = (change > 3 ? Commons.randomDirect() : snake.direct);
                        snake.move(snake.direct, foods, snakes);
                    }

                    jp.repaint();
                }
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                while (!stop) {
                    moreEggs(snakes);
                }
            }
        }).start();

        this.setResizable(false);

        jp.setPreferredSize(new Dimension(Commons.BORDER_WIDTH, Commons.BORDER_HEIGHT));
        jp.setBackground(Color.white);
        add(jp);

        setLayout(new FlowLayout());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocation(Commons.FRAME_LOCATION_X, Commons.FRAME_LOCATION_Y);
        setSize(Commons.FRAME_WIDTH, Commons.FRAME_HEIGHT);
        setVisible(true);

//        JButton agin = new JButton("-----agin-----");
//        agin.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                stop = false;
//            }
//        });
//        add(agin);
        JButton stopBt = new JButton("-------stop-------");
        stopBt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stop = true;
            }
        });
        add(stopBt);
    }

    public static void main(String[] args) throws InterruptedException {
//        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//        GraphicsDevice[] screenDevices = ge.getScreenDevices();
//        GraphicsConfiguration[] configurations = null;
//        for (GraphicsDevice gd : screenDevices) {
//            configurations = gd.getConfigurations();
//        }
//        GraphicsConfiguration gc = configurations[0];
//        new App("snakeBang", gc);
        new App("snakeBang");
    }


    class DrawPanel extends JPanel {

        private Snake[] snakes;

        public DrawPanel() {
        }
        public DrawPanel(Snake[] snakes) {
            this.snakes = snakes;
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Dimension size = getSize();
            g.setColor(Color.BLACK);
            g.drawRect(0, 0, size.width, size.height);
            for (int i = 0; i < size.width; i++) {
                g.drawLine(Commons.BODY_LENGH * i, 0, Commons.BODY_LENGH * i, size.width);
                g.drawLine(0, Commons.BODY_LENGH * i, size.width, Commons.BODY_LENGH * i);
            }
            for (int i = 0; i < snakes.length; i++) {
                Snake snake = snakes[i];
                for (int j = 0; j < snake.len; j++) {
                    if (snake.alive) {
                        g.setColor(snake.color);
                        if (j == 0) {
                            g.fillOval(snake.xs[j] * Commons.BODY_LENGH, snake.ys[j] * Commons.BODY_LENGH, Commons.BODY_LENGH, Commons.BODY_LENGH);
                        } else
                            g.fill3DRect(snake.xs[j] * Commons.BODY_LENGH, snake.ys[j] * Commons.BODY_LENGH, Commons.BODY_LENGH, Commons.BODY_LENGH, true);
                    } else {
                        foods.add(new Egg(snake.xs[j], snake.ys[j]));
                        snakes[i] = new Snake(i, Commons.randomColor(), 0);
                    }
                }
            }

            for (int i = 0; i < foods.size(); i++) {
                Egg egg = foods.get(i);
                g.setColor(Color.RED);
                g.fillOval(Commons.BODY_LENGH * egg.x, Commons.BODY_LENGH * egg.y, Commons.BODY_LENGH, Commons.BODY_LENGH);
            }

        }

    }


    class MyKeyListener implements KeyListener {
        private Snake snake;
        private DrawPanel jp;

        public MyKeyListener(Snake snake, DrawPanel jp) {
            this.snake = snake;
            this.jp = jp;
        }

        public void keyTyped(KeyEvent e) {
        }
        public void keyPressed(KeyEvent e) {

            switch (e.getKeyCode()) {
                case 37:
                    snake.move(Commons.DIRECT.LEFT, foods, snakes);
                    break;
                case 38:
                    snake.move(Commons.DIRECT.UP, foods, snakes);
                    break;
                case 39:
                    snake.move(Commons.DIRECT.RIGHT, foods, snakes);
                    break;
                case 40:
                    snake.move(Commons.DIRECT.DOWN, foods, snakes);
                    break;
                default:
                    break;
            }
            jp.repaint();
        }

        public void keyReleased(KeyEvent e) {
        }
    }

    private void moreEggs(Snake[] snakes) {
        if (foods.size() < 8) {
            Random random = new Random();
            int fx = random.nextInt(Commons.BORDER_WIDTH / Commons.BODY_LENGH - 1) + 1;
            int fy = random.nextInt(Commons.BORDER_HEIGHT / Commons.BODY_LENGH - 1) + 1;

            for (Snake sn : snakes) {
                int[] xs = sn.xs;
                int[] ys = sn.ys;
                for (int j = 0; j < xs.length; j++) {
                    if (xs[j] == fx && ys[j] == fy) {
                        continue;
                    }
                }
            }
            foods.add(new Egg(fx, fy));
        }
    }
}

