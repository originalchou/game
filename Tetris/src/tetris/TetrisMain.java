package tetris;

import javax.swing.*;

/**
 * Created by bwbecker on 2016-09-19.
 */
public class TetrisMain extends JFrame {

    public static void main(String[] args) {
        System.out.println("Hello, Tetris!");
//        System.out.println(1.5*2);
        try {
            ProgramArgs a = ProgramArgs.parseArgs(args);
            Tetris tetris = new Tetris(a.getFPS(), a.getSpeed(), a.getSequence());
            TetrisMain frame = new TetrisMain();
            frame.setSize(400,550);
            frame.add(tetris);
            frame.setVisible(true);
        } catch (IllegalArgumentException e) {
            System.out.println(e);
        }
    }
}


