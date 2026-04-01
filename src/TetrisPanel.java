import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class TetrisPanel extends JPanel {
    public TetrisLogic logic;

    private BufferedImage canvas;
    public Graphics2D g2;
    private int blockSize =24;
    public int flashLine=-1;
    public boolean flash=false;
    public int c = 0;
    public int cl=0;
    public JLabel counter = new JLabel("0", SwingConstants.CENTER);
    public JLabel counterLine = new JLabel("0", SwingConstants.CENTER);
    public JLabel over = new JLabel("Game Over",SwingConstants.CENTER);

    public TetrisPanel(TetrisLogic logic, int width, int height, JFrame frame) {
        this.logic = logic;
        canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Font();

        setLayout(null);
        JButton button = new JButton("New Game");
        button.setBounds(10, 10, 100, 30);
        add(button);
        button.setFocusPainted(false);
        button.addActionListener(e -> {
            this.logic.newGame();
            frame.requestFocus();
        });

        counter.setBounds(380, 10,80,30);
        counter.setBackground(Color.LIGHT_GRAY);
        counter.setOpaque(true);
        counter.setFont(new Font("TimesRoman", Font.BOLD, 20));
        add(counter);
        counterLine.setBounds(380, 60, 80,30);
        counterLine.setBackground(Color.LIGHT_GRAY);
        counterLine.setOpaque(true);
        counterLine.setFont(new Font("TimesRoman", Font.BOLD, 20));
        add(counterLine);

        over.setBounds(138, 130, 200,40);
        over.setFont(new Font("TimesRoman", Font.BOLD, 34));
        over.setForeground(Color.WHITE);
        over.setVisible(false);
        add(over);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2 = (Graphics2D) g;
        g2.drawImage(canvas, null, null);

        paintField();
        if(flashLine!=-1)paintLine(flashLine);
        else if(flash)paintFlash();

        paintRect(logic.block[0][0],logic.block[0][1], Color.BLUE);
        paintRect(logic.block[1][0],logic.block[1][1], Color.BLUE);
        paintRect(logic.block[2][0],logic.block[2][1], Color.BLUE);
        paintRect(logic.block[3][0],logic.block[3][1], Color.BLUE);
    }

    private void paintField() {
        /*for(int i=0;i<10*blockSize; i++) {
            for(int j=0;j<16*blockSize;j++) {
                canvas.setRGB(blockSize*5+i,j,Color.BLACK.getRGB());
            }
        }*/
        g2.setColor(Color.BLACK);
        g2.fillRect(blockSize*5,0,blockSize*10,blockSize*16);
        for(int i=1;i<17;i++){
            //paintPlane(blockSize*5,blockSize*i-2,blockSize*15,blockSize*i,Color.DARK_GRAY);
            g2.setColor(Color.darkGray);
            g2.fillRect(blockSize*5,blockSize*i-2,blockSize*10,2);
        }
        for(int i=1;i<11;i++){
            //paintPlane(blockSize*5+blockSize*i-2,0,blockSize*5+blockSize*i,blockSize*16,Color.DARK_GRAY);
            g2.setColor(Color.darkGray);
            g2.fillRect(blockSize*5+blockSize*i-2,0,2,blockSize*16);
        }
    }

    public void paintRect(int x, int y, Color color) {
        /*for(int i=0;i<blockSize;i++) {
            for(int j=0;j<blockSize;j++) {
                if(x==0) break;
                canvas.setRGB(blockSize*4 + blockSize*x + i,blockSize*y + j, color.getRGB());
            }
        }*/
        g2.setColor(color);
        g2.fillRect(blockSize*x+blockSize*5,blockSize*y,blockSize,blockSize);
    }

    public void paintFlash() {
        paintField();
        System.out.println("Flash:");
        for(int i=0;i<4;i++) {
            for(int j=0;j<4;j++) {
                if(logic.flash[i][j][0]!=-1) paintRect(logic.flash[i][j][0], logic.flash[i][j][1], Color.WHITE);
                if(logic.flash[i][j][0]!=-1)System.out.println(logic.flash[i][j][0] + " "+logic.flash[i][j][1]);
            }
        }

        paintRect(logic.block[0][0],logic.block[0][1], Color.WHITE);
        paintRect(logic.block[1][0],logic.block[1][1], Color.WHITE);
        paintRect(logic.block[2][0],logic.block[2][1], Color.WHITE);
        paintRect(logic.block[3][0],logic.block[3][1], Color.WHITE);
        flash=false;
    }

    public void paintLine(int y) {
        /*for (int i=0;i<10*blockSize;i++) {
            for (int j=0;j<blockSize;j++) {
                canvas.setRGB(blockSize*5 + i,blockSize*y + j, Color.CYAN.getRGB());
            }
        }*/
        g2.setColor(Color.CYAN);
        g2.fillRect(blockSize*5,blockSize*(y-1),blockSize*10,blockSize*y);
        flashLine=-1;
        flash = false;
        paintField();
        paintMatrix();
    }

    private void paintMatrix() {
        for(int i=0;i<16;i++){
            for(int j=0;j<10;j++){
                if(logic.matrix[j][i]) paintRect(j,i, Color.CYAN);
            }
        }
    }

    private void paintPlane(int startX, int startY, int endX, int endY, Color color) {
        int width = endX-startX;
        int height = endY-startY;
        for(int i=0;i<width;i++){
            for(int j=0;j<height;j++){
                canvas.setRGB(startX+i,startY+j, color.getRGB());
            }
        }
    }

    private void Font() {
        paintPlane(0,0,5*blockSize,17*blockSize,Color.GRAY);
        paintPlane(15*blockSize,0,20*blockSize,17*blockSize,Color.GRAY);
        paintPlane(5*blockSize,16*blockSize-1,15*blockSize,17*blockSize,Color.GRAY);
    }

    public void counterBlock() {
        c++;
        SwingUtilities.invokeLater(() -> counter.setText(String.valueOf(c)));
    }

    public void counterLine() {
        cl++;
        SwingUtilities.invokeLater(() -> counterLine.setText(String.valueOf(cl)));
    }
}