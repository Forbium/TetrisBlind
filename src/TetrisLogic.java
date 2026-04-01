import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class TetrisLogic implements Runnable {
    private final TetrisPanel panel;
    public boolean[][] matrix = new boolean[10][16]; // поле тетриса
    public int[][] block = new int[4][2];
    public int[][][] flash = new int[4][4][2];
    private int blockForm;
    private volatile boolean game;

    public TetrisLogic(TetrisPanel panel) {
        this.panel = panel;
    }

    public void run() {
        for(int i=0; i<4; i++)
            for(int j=0; j<4; j++)
                flash[i][j][0] = -1;
        generateBlock();
        game = true;
        while (true) {
            // тик логики
            if(game) {
                blockFall();
                if(panel.flashLine==-1) panel.repaint();
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    private boolean blockFall() {
        boolean flag = false;
        for(int i=0;i<4;i++) {
            if(block[i][1]==15 || matrix[block[i][0]][block[i][1]+1]){
                flag = true;
                break;
            }
        }
        if(!flag) {
            for(int i=0;i<4;i++){
                block[i][1]+=1;
            }
            return true;
        }
        else {
            for(int i=0;i<4;i++){
                if(block[i][1]==0) {
                    game=false;
                    panel.over.setVisible(true);
                }
            }
            addBlockToMatrix();
            calculateFlash();
            generateBlock();
            return false;
        }
    }

    private void addBlockToMatrix() {
        int x,y;
        for(int i=0;i<4;i++){
            x=block[i][0];
            y=block[i][1];
            matrix[x][y] = true;
        }
        panel.counterBlock();
        deleteLine();
    }

    private void generateBlock() {
        int form = (int)(Math.random()*7);
        blockForm = form;
        switch (form) {
            case 0://square
                setBlockPos(0,4,0);
                setBlockPos(1,5,0);
                setBlockPos(2,4,1);
                setBlockPos(3,5,1);
                break;
            case 1://line
                setBlockPos(0,3,0);
                setBlockPos(1,4,0);
                setBlockPos(2,5,0);
                setBlockPos(3,6,0);
                break;
            case 2://g
                setBlockPos(0,3,0);
                setBlockPos(1,4,0);
                setBlockPos(2,5,0);
                setBlockPos(3,5,1);
                break;
            case 3:
                setBlockPos(0,3,0);
                setBlockPos(1,4,0);
                setBlockPos(2,5,0);
                setBlockPos(3,3,1);
                break;
            case 4://22
                setBlockPos(0,3,0);
                setBlockPos(1,4,0);
                setBlockPos(2,4,1);
                setBlockPos(3,5,1);
                break;
            case 5://22-
                setBlockPos(0,3,1);
                setBlockPos(1,4,1);
                setBlockPos(2,4,0);
                setBlockPos(3,5,0);
                break;
            case 6://31/T
                setBlockPos(0,3,0);
                setBlockPos(1,4,0);
                setBlockPos(2,5,0);
                setBlockPos(3,4,1);
                break;
        }
    }

    private void setBlockPos(int index, int x, int y) {
        block[index][0] = x;
        block[index][1] = y;
    }

    private void deleteLine() {
        boolean flag=false;
        int g=0;
        for(int i=0;i<16;i++){
            for(int j=1;j<10;j++){
                if(!matrix[j][i])break;
                if(j==9) {flag=true; g=i;panel.counterLine();}
            }
            if(flag){
                for(int j=0;j<10;j++){
                    matrix[j][i] =false;
                }
                break;
            }
        }
        if(flag) {
            System.out.println(g+"deleted");
            for(int i=g-1;i>=0;i--){
                for(int j=0;j<10;j++){
                    if(matrix[j][i]){
                        matrix[j][i] = false;
                        matrix[j][i+1] = true;
                    } else {
                        matrix[j][i+1] = false;
                    }
                }
            }
            panel.flashLine=g;
        }
    }
    
    private void calculateFlash() {
        for(int i=0;i<4;i++) {
            if(block[i][1]!=15) {
                if(matrix[block[i][0]][block[i][1]+1]){
                    flash[i][0][0] = block[i][0];
                    flash[i][0][1] = block[i][1]+1;
                } else flash[i][0][0] = -1;
            } else flash[i][0][0] = -1;
            if(block[i][0]>0) {
                if(matrix[block[i][0]-1][block[i][1]]){
                    flash[i][1][0] = block[i][0]-1;
                    flash[i][1][1] = block[i][1];
                } else flash[i][1][0] = -1;
            } else flash[i][1][0] = -1;
            if(block[i][0]<9) {
                if(matrix[block[i][0]+1][block[i][1]]){
                    flash[i][2][0] = block[i][0]+1;
                    flash[i][2][1] = block[i][1];
                } else flash[i][2][0] = -1;
            } else flash[i][2][0] = -1;
            if(block[i][1]!=0) {
                if(matrix[block[i][0]][block[i][1]-1]){
                    flash[i][3][0] = block[i][0];
                    flash[i][3][1] = block[i][1]-1;
                } else flash[i][3][0]=-1;
            } else flash[i][3][0]=-1;
        }
        panel.flash=true;
        panel.repaint();
    }

    public void moveLeft() {
        boolean flag = false;
        for(int i=0;i<4;i++) {
            if(block[i][0]==0 || matrix[block[i][0]-1][block[i][1]]){
                flag = true;
                break;
            }
        }
        if(!flag) {
            for(int i=0; i<4;i++) {
                block[i][0] -=1;
            }
        }
        panel.repaint();
    }

    public void moveRight() {
        boolean flag = false;
        for(int i=0;i<4;i++) {
            if(block[i][0]==9 || matrix[block[i][0]+1][block[i][1]]){
                flag = true;
                break;
            }
        }
        if(!flag) {
            for(int i=0; i<4;i++) {
                block[i][0] +=1;
            }
        }
        panel.repaint();
    }

    public void rotate() {
        boolean flag = false;
        int[] x= new int[4];
        int[] y= new int[4];
        for(int i=0;i<4;i++) {
            x[i] = block[1][0] + (block[i][1]-block[1][1]);
            y[i] = block[1][1] - (block[i][0]-block[1][0]);
            if(x[i]<0||x[i]>9||y[i]<0||y[i]>15||blockForm==0) flag=true;
        }
        if(!flag){
            for(int i=0;i<4;i++) {
                block[i][0]=x[i];
                block[i][1]=y[i];
            }
            panel.repaint();
        }
    }

    public void fallDown() {
        boolean flag = false;
        while (!flag) {
            flag = blockFall();
            panel.repaint();
        }
    }

    public void newGame() {
        game = false;
        try { Thread.sleep(600); } catch (InterruptedException e) {}
        System.out.println("Start New Game");
        for(boolean[] row : matrix) Arrays.fill(row, false);
        panel.c=0;
        panel.cl=0;
        SwingUtilities.invokeLater(() -> {
            panel.counter.setText("0");
            panel.counterLine.setText("0");
            panel.over.setVisible(false);
        });
        generateBlock();
        game = true;
    }
}