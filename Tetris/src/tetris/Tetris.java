package tetris;

import javax.swing.*;
import javax.swing.text.html.ObjectView;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by bwbecker on 2016-09-19.
 */
public class Tetris extends JPanel {
    private double speedDefault;
    private int fps = 30;
    private double speed = 7.0;
    private String sequence = "ILOJSZT";
    private int sequenceL = 7;
    private int currentSeq = 0;
    private int width = 400;
    private int height = 550;
    private int map[][] = new int[10][24];
    private double mult = 1;
    private int type = 0;
    private int typeState = 0;
    private int x = 0;
    private int y = 0;
    private Timer timerD;
    private int flag = 0;
    /* pieces type */
    private final int[][][] shape = new int[][][] {
            //I
            {
                    {0,1,0,0,0,1,0,0,0,1,0,0,0,1,0,0},
                    {0,0,0,0,1,1,1,1,0,0,0,0,0,0,0,0},
                    {0,0,1,0,0,0,1,0,0,0,1,0,0,0,1,0},
                    {0,0,0,0,0,0,0,0,1,1,1,1,0,0,0,0}
            },

            //T
            {
                    {0,0,0,0,0,2,2,2,0,0,2,0,0,0,0,0},
                    {0,0,0,0,0,0,2,0,0,2,2,0,0,0,2,0},
                    {0,0,0,0,0,0,0,0,0,2,0,0,2,2,2,0},
                    {0,2,0,0,0,2,2,0,0,2,0,0,0,0,0,0}
            },

            //L
            {
                    {0,3,0,0,0,3,0,0,0,3,3,0,0,0,0,0},
                    {0,0,0,0,0,3,3,3,0,3,0,0,0,0,0,0},
                    {0,0,0,0,0,3,3,0,0,0,3,0,0,0,3,0},
                    {0,0,0,0,0,0,3,0,3,3,3,0,0,0,0,0}
            },

            //J
            {
                    {0,0,4,0,0,0,4,0,0,4,4,0,0,0,0,0},
                    {0,0,0,0,0,4,0,0,0,4,4,4,0,0,0,0},
                    {0,0,0,0,0,4,4,0,0,4,0,0,0,4,0,0},
                    {0,0,0,0,4,4,4,0,0,0,4,0,0,0,0,0}
            },

            //S
            {
                    {0,0,0,0,0,0,5,5,0,5,5,0,0,0,0,0},
                    {0,0,0,0,0,5,0,0,0,5,5,0,0,0,5,0},
                    {0,0,0,0,0,5,5,0,5,5,0,0,0,0,0,0},
                    {0,5,0,0,0,5,5,0,0,0,5,0,0,0,0,0}
            },

            //Z
            {
                    {0,0,0,0,0,6,6,0,0,0,6,6,0,0,0,0},
                    {0,0,0,0,0,0,6,0,0,6,6,0,0,6,0,0},
                    {0,0,0,0,6,6,0,0,0,6,6,0,0,0,0,0},
                    {0,0,6,0,0,6,6,0,0,6,0,0,0,0,0,0}
            },

            //O
            {
                    {0,0,0,0,0,7,7,0,0,7,7,0,0,0,0,0},
                    {0,0,0,0,0,7,7,0,0,7,7,0,0,0,0,0},
                    {0,0,0,0,0,7,7,0,0,7,7,0,0,0,0,0},
                    {0,0,0,0,0,7,7,0,0,7,7,0,0,0,0,0}
            }
    };

    /* constructor */
    public Tetris(int fps, double speed, String sequence) {
        this.fps = fps;
        this.speed = speed;
        this.speedDefault = speed;
        this.sequence = sequence;
        this.sequenceL = sequence.length();
        initMap();
        newpiece();
        this.addComponentListener(new componentEvent());
        this.addKeyListener(new keyComponent());
        this.setFocusable(true);
        timerD = new Timer(1000/5, new timerComponentD());
        timerD.start();
    }

    /* get piece from seq */
    public void getNextPiece() {
        typeState = (int) (Math.random() * 1000) % 4;
        switch (sequence.charAt(currentSeq)) {
            case 'I':
                type = 0;
                currentSeq++;
                break;

            case 'T':
                type = 1;
                currentSeq++;
                break;

            case 'L':
                type = 2;
                currentSeq++;
                break;

            case 'J':
                type = 3;
                currentSeq++;
                break;

            case 'S':
                type = 4;
                currentSeq++;
                break;

            case 'Z':
                type = 5;
                currentSeq++;
                break;

            case 'O':
                type = 6;
                currentSeq++;
                break;
        }
        if (currentSeq == sequenceL) {
            currentSeq = 0;
        }
    }

    /* generate pieces */
    synchronized void newpiece() {
        getNextPiece();
        x = 3;
        y = -1;
        gameover();
    }

    /*init game grid*/
    private void initMap() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 24; j++) {
                map[i][j] = 0;
            }
        }
    }

    /* rotate right */
    public void rotateR() {
        int typeBefore = typeState;
        typeState++;
        if (typeState > 3) {
            typeState = 0;
        }
        if (collision() == false) {
            repaint();
        } else {
            typeState = typeBefore;
        }
    }

    /* rotate left */
    public void rotateL() {
        int typeBefore = typeState;
        typeState--;
        if (typeState < 0) {
            typeState = 3;
        }
        if (collision() ==  false) {
            repaint();
        } else {
            typeState = typeBefore;
        }
    }

    /* move left */
    public void moveL() {
        x--;
        if (collision() == false) {
            repaint();
        } else {
            x++;
        }
    }

    /* move right */
    public void moveR() {
        x++;
        if (collision() == false) {
            repaint();
        } else {
            x--;
        }
    }

    /* drop */
    public void drop() {
        while (true) {
            y++;
            if (collision()) {
                y--;
                repaint();
                break;
            }
        }
    }

    /* check game over */
    public void gameover() {
        if (collision()) {
            int result = JOptionPane.showConfirmDialog(this, "Do you want to restart a new game?",
                    "Game Over", JOptionPane.YES_NO_OPTION);
            timerD.stop();
            if (result == JOptionPane.YES_OPTION) {
                speed = speedDefault;
                initMap();
                newpiece();
                timerD.start();
            } else {
                System.exit(0);
            }
        }
    }

    /* start/stop */
    public void startStop() {
        if (flag == 0) {
            timerD.stop();
            flag = 1;
        } else {
            timerD.start();
            flag = 0;
        }
    }

    /* add pieces to map */
    synchronized void addPiece() {
        int index = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if(x+j >= 0 && y+i >= 0 && x+j <= 9 && y+i <= 23 && y+i >= 0 && map[x+j][y+i] == 0) {
                    map[x+j][y+i] = shape[type][typeState][index];
                    System.out.println(map[x+j][y+i]);
                }
                index++;
            }
        }
    }

    /* delete line */
    public void deleteLine() {
        int count = 0;
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 10; j++) {
                if(map[j][i] != 0) {
                    count++;
                    if (count == 10) {
                        for (int a = i; a > 0; a--) {
                            for (int b = 0; b < 10; b++) {
                                map[b][a] = map[b][a-1];
                            }
                        }
                    }
                }
            }
            count = 0;
        }
    }

    /* collision */
    public boolean collision () {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if ((shape[type][typeState][i*4+j]!=0 && (x+j > 9||x+j < 0||y+i > 23))||
                        (shape[type][typeState][i*4+j]!=0 && ((x+j <= 9 && x+j >= 0 && y+i <= 23 && y+i >= 0) && map[x+j][y+i]!=0))) {
                    return true;
                }
            }
        }
        return false;
    }

    /* pick color for existing pieces */
    public Color pickColor(int color) {
        switch (color) {
            case 1:
                return Color.CYAN;

            case 2:
                return Color.PINK;

            case 3:
                return Color.ORANGE;

            case 4:
                return  Color.BLUE;

            case 5:
                return  Color.GREEN;

            case 6:
                return  Color.RED;

            case 7:
                return  Color.YELLOW;
        }
        return Color.black;
    }


    /* paint component */
    protected void paintComponent(Graphics g) {
        System.out.println("y: "+this.y);
        super.paintComponent(g);
        Color currentColor = Color.BLACK;
        int size = (int) (20*mult);

        //pick right color for current piece
        switch (type) {
            case 0:
                currentColor = Color.CYAN;
                break;

            case 1:
                currentColor = Color.PINK;
                break;

            case 2:
                currentColor = Color.ORANGE;
                break;

            case 3:
                currentColor = Color.BLUE;
                break;

            case 4:
                currentColor = Color.GREEN;
                break;

            case 5:
                currentColor = Color.RED;
                break;

            case 6:
                currentColor = Color.YELLOW;
                break;
        }

        //draw grids and existing pieces
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 24; j++) {
                if (map[i][j] == 0) {
                    g.drawRect(i * size, j * size, size, size);
                } else  {
                    g.setColor(pickColor(map[i][j]));
                    g.fillRect(i * size, j * size, size, size);
                    g.setColor(Color.black);
                    g.drawRect(i * size, j * size, size, size);
                }
            }
        }

        //draw current piece
        for (int j = 0; j < 16; j++) {
            if (shape[type][typeState][j] != 0) {
                g.setColor(currentColor);
                g.fillRect((j % 4 + x) * size, (j / 4 + y) * size, size, size);
                g.setColor(Color.black);
                g.drawRect((j % 4 + x) * size, (j / 4 + y) * size, size, size);
            }
        }


    }

    /* time component drop */
    class timerComponentD implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            y++;
            if (!collision()) {
                repaint();
            } else {
                y--;
                addPiece();
                deleteLine();
                newpiece();
            }
        }
    }

    /* key component */
    class keyComponent implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    rotateR();
                    break;

                case KeyEvent.VK_DOWN:
                    rotateL();
                    break;

                case KeyEvent.VK_RIGHT:
                    moveR();
                    break;

                case KeyEvent.VK_LEFT:
                    moveL();
                    break;

                case KeyEvent.VK_SPACE:
                    drop();
                    break;

                case KeyEvent.VK_P:
                    startStop();
                    break;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }

    /* resize component */
    class componentEvent implements ComponentListener {

        @Override
        public void componentResized(ComponentEvent e) {
            if (height!=0) {
                if ((double) e.getComponent().getHeight() / (double) height < (double) e.getComponent().getWidth() / (double)width) {
                    mult = (double) e.getComponent().getHeight() / (double) height;
                } else {
                    mult = (double) e.getComponent().getWidth() / (double)width;
                }
            }
            repaint();
            System.out.println(e.getComponent().getHeight());
            System.out.println(mult);
        }

        @Override
        public void componentMoved(ComponentEvent e) {

        }

        @Override
        public void componentShown(ComponentEvent e) {

        }

        @Override
        public void componentHidden(ComponentEvent e) {

        }
    }
}
