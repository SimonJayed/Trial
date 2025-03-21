package main;

import entity.Entity;
import entity.Player;
import misc.AssetSetter;
import misc.CollisionChecker;
import misc.EventHandler;
import screen.BattleScreen;
import tile.Map;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class GamePanel extends JPanel implements Runnable {

    final int originalTileSize = 16;
    final int scale = 3;

    public final int tileSize = originalTileSize * scale;
    public final int maxscreenCol = 18;
    public final int maxscreenRow = 14;

    public final int screenWidth = tileSize * maxscreenCol;
    public final int screenHeight = tileSize * maxscreenRow;

    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int maxMap = 10;
    public int currentMap = 0;


    public double FPS = 60;

    int failCTR = 0;

    public TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);
    public MouseHandler mouseH = new MouseHandler(this);
    public Sound sound = new Sound();
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    public EventHandler eHandler = new EventHandler(this);
    public Map map = new Map(this);
    public Thread gameThread;

    public Player player = new Player(this, keyH, mouseH);
    public Entity objectEntity[][] = new Entity[maxMap][10];
    public Entity livingEntity[][] = new Entity[maxMap][20];
    public ArrayList <Entity> entityList = new ArrayList<>();

    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int inventoryState = 2;
    public final int dialogueState = 3;
    public final int newGameState = 4;
    public final int loadGameState = 5;
    public final int settingsState = 6;
    public final int characterPickState = 7;
    public final int battleState = 8;
    public final int mapState = 9;

    public BattleScreen battleScreen = new BattleScreen(this);

    public Font customFont;

    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.addMouseListener(mouseH);
        this.setFocusable(true);
    }

    public void setupGame(){
        aSetter.setObjectEntity();
        aSetter.setLivingEntity();
        gameState = playState;
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void run(){
        double drawInterval;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (gameThread != null){
            drawInterval = 1_000_000_000.0 / FPS;
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1){
                try{
                    update();
                    repaint();
                    delta-=1;
                    drawCount++;
                } catch (ArrayIndexOutOfBoundsException e){
                    failCTR++;
                    JOptionPane.showMessageDialog(null, "Someone got erased from existence", "Dead Lol", JOptionPane.ERROR_MESSAGE);
                    if (failCTR == 3){
                        JOptionPane.showMessageDialog(null, "Game doesn't want you, bro. Peace Out.", "Death Loop", JOptionPane.INFORMATION_MESSAGE);
                        System.exit(0);
                    }
                }

            }

            if (timer >= 1000000000){
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void update(){
        if (gameState == playState) {
            player.update();
        }
        if (gameState == playState){
            for (int i = 0; i < livingEntity[1].length; i++){
                if (livingEntity[currentMap][i] != null){
                    livingEntity[currentMap][i].update();
                    if (!livingEntity[currentMap][i].isAlive){
                        livingEntity[i] = null;
                    }
                }
            }
        }
    }

    public void paintComponent(Graphics g){

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;


        try {
            InputStream fontStream = getClass().getResourceAsStream("/fonts/font1.ttf");
            assert fontStream != null;
            customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(20f);
        } catch (Exception e) {
            e.printStackTrace();
            customFont = new Font("Arial", Font.PLAIN, 40); // Fallback font
        }

        g2.setFont(customFont);

        long drawStart = 0;
        if (keyH.showDebugTest){
            drawStart = System.nanoTime();
        }
        if(gameState == titleState){
            ui.draw(g2);
        }
        else if(gameState == newGameState){
            ui.draw(g2);
        }
        else if(gameState == loadGameState){
            ui.draw(g2);
        }
        else if(gameState == settingsState){
            ui.draw(g2);
        }
        else if(gameState == characterPickState){
            ui.draw(g2);
        }
        else{
            //PLAY STATE
            tileM.draw(g2);

            entityList.clear();
            entityList.add(player);

            for(int i = 0; i < livingEntity[1].length; i++){
                if(livingEntity[currentMap][i] != null){
                    entityList.add(livingEntity[currentMap][i]);
                }
            }
            for(int i = 0; i < objectEntity[1].length; i++){
                if(objectEntity[currentMap][i] != null){
                    entityList.add(objectEntity[currentMap][i]);
                }
            }

//            for (Entity[][] entities : new Entity[][][] {livingEntity, objectEntity}) {
//                for (Entity[] e : entities) {
//                    if (e != null) {
//                        entityList.add(e[i]);
//                    }
//                }
//            }


            entityList.sort(new Comparator<>() {
                @Override
                public int compare(Entity e1, Entity e2) {
                    return Integer.compare(e1.worldY, e2.worldY);
                }
            });

            for (Entity entity : entityList) {
                entity.draw(g2);
            }

            entityList.clear();

            map.drawMiniMap(g2);

            ui.draw(g2);

//            ui.drawForegroundFoliage();


            if (keyH.showDebugTest){
                long drawEnd = System.nanoTime();
                long passed = drawEnd - drawStart;

                g2.setFont(g2.getFont().deriveFont(23f));
                g2.setColor(Color.white);
                int x = 10;
                int y = 400;
                int lineHeight = 20;

                g2.drawString("WorldX" + player.worldX, x, y);y += lineHeight;
                g2.drawString("WorldY" + player.worldY, x, y);y += lineHeight;
                g2.drawString("Col" + (player.worldX+ player.solidArea.x)/tileSize, x, y);y += lineHeight;
                g2.drawString("Row" + (player.worldY+ player.solidArea.y)/tileSize, x, y);y += lineHeight;

                g2.drawString("Draw Time: " + passed , x, y );
            }
        }



        g2.dispose();
    }

    public int randomize(int min, int max) {
        Random num = new Random();
        if (min > max) {
            throw new IllegalArgumentException("min should be less than or equal to max");
        }
        return num.nextInt((max - min) + 1) + min;
    }

    public void playMusic(int i){
        sound.setFile(i);
        sound.play();
        sound.loop(i);
    }
    public void stopMusic(){
        sound.stop();
    }
    public void playSoundEffect(int i){
        sound.setFile(i);
        sound.play();
    }

}
