
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY = 100;
    final int[] x = new int[GAME_UNITS];
    final int[] y = new int[GAME_UNITS];
    int bodyParts = 1;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;
    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.GRAY);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame(){
        newApple();
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        // 为什么这段代码执行了两次
        //System.out.println("ttt");
        if( running ){
            for (int i =0;i<SCREEN_HEIGHT/UNIT_SIZE;i++){
                g.drawLine(i*UNIT_SIZE,0,i*UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0,i*UNIT_SIZE,SCREEN_WIDTH, i*UNIT_SIZE);
            }
            g.setColor(Color.RED);
            g.fillOval(appleX,appleY,UNIT_SIZE,UNIT_SIZE);

            g.setColor(Color.GREEN);
            for (int i = 0;i<bodyParts;i++){
                g.fillRect(x[i],y[i],UNIT_SIZE,UNIT_SIZE);
            }
        }
        else
            //System.out.println("Game Over!");;
            gameOver(g);
    }

    public void newApple(){
        appleX = random.nextInt((int) SCREEN_WIDTH/UNIT_SIZE)*UNIT_SIZE;
        appleY = random.nextInt((int) SCREEN_HEIGHT/UNIT_SIZE)*UNIT_SIZE;
    }

    public void move(){
        for (int i = bodyParts-1;i>0;i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch (direction){
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    /**
     * Why is Apple?
     */
    public void checkApple(){
        if( (x[0] == appleX) && (y[0]==appleY) ){
            bodyParts++;
            applesEaten++;
            timer.setDelay(timer.getDelay()-2);
            newApple();
        }
    }

    public void checkCollisions(){
        // checks if head collides with body
        for (int i =bodyParts-1;i>0;i--){
            if( (x[0] == x[i]) && (y[0] == y[i]) )
                running = false;
        }
        // check if head touches left border
        if( x[0]<0 )
            running = false;
        // check if head touches right border
        if (x[0]>=SCREEN_WIDTH)
            running = false;
        // check if head touches top border
        if( y[0]<0 )
            running = false;
        // check if head touches bottom border
        if( y[0]>=SCREEN_HEIGHT )
            running = false;

        if( !running )
            timer.stop();
    }

    public void gameOver(Graphics g){
        // Game OVer text
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free",Font.BOLD,75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over",( SCREEN_WIDTH - metrics.stringWidth("Game Over") )/2,SCREEN_HEIGHT/2);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        // System.out.println(123);
        if( running ){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if( direction != 'R' ){
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if( direction != 'L' ){
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if( direction != 'D' ){
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if( direction != 'U' ){
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
