import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Main {
    static int width = 494, height = 446;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            TetrisPanel panel = new TetrisPanel(null, width, height, frame);
            TetrisLogic logic = new TetrisLogic(panel);
            panel.logic = logic;


            frame.add(panel);
            frame.setSize(width, height);
            frame.setVisible(true);
            frame.setResizable(false);
            frame.setFocusable(true);
            frame.requestFocus();
            frame.addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent e) {
                    switch(e.getKeyCode()) {
                        case KeyEvent.VK_LEFT  -> logic.moveLeft();
                        case KeyEvent.VK_RIGHT -> logic.moveRight();
                        case KeyEvent.VK_UP    -> logic.rotate();
                        case KeyEvent.VK_DOWN  -> logic.fallDown();
                    }
                }
            });
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            new Thread(logic).start();
        });
    }
}