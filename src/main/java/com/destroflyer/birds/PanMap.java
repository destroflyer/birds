/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.destroflyer.birds;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

public class PanMap extends javax.swing.JPanel {

    public PanMap(Map map, Skin skin){
        initComponents();
        this.map = map;
        setSkin(skin);
    }
    private int TERRAIN_CHUNK_WIDTH = MainFrame.WIDTH;
    private Graphics2D graphics;
    private Map map;
    private Skin skin;
    private BufferedImage imageSky;
    private Sprite birdSprite;
    private int currentChunkIndex = 0;
    private BufferedImage imageTerrainLeft;
    private BufferedImage imageTerrainMiddle;
    private BufferedImage imageTerrainRight;
    private BufferedImage imageTerrainFuture;
    private HashMap<Integer, Color[]> islandTerrainColors = new HashMap<>();

    @Override
    protected void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        this.graphics = (Graphics2D) graphics;
        drawTerrain();
        drawFancyText("score " + map.getScore(), 20 , 20);
        if (map.isGameOver()) {
            drawFancyText("game over", 20 , 60);
            drawFancyText("click to restart", 20 , 100);
        } else if (map.getScore() < 1500) {
            drawFancyText("hold mouse for gravity", 20, getHeight() - 52);
        }
    }

    private void drawTerrain(){
        MapObject player = map.getPlayer();
        int cameraX = (int) (player.getX() - 250);
        graphics.drawImage(imageSky, 0, (getHeight() - imageSky.getHeight()), this);
        drawWater(cameraX);
        graphics.translate(-1 * cameraX, 0);
        int chunkIndex = (int) (player.getX() / TERRAIN_CHUNK_WIDTH);
        if(chunkIndex > currentChunkIndex){
            currentChunkIndex = chunkIndex;
            imageTerrainLeft = imageTerrainMiddle;
            imageTerrainMiddle = imageTerrainRight;
            imageTerrainRight = imageTerrainFuture;
            new Thread(() -> {
                imageTerrainFuture = createTerrainImage(currentChunkIndex + 2);
            }).start();
        }
        graphics.drawImage(imageTerrainLeft, ((currentChunkIndex - 1) * TERRAIN_CHUNK_WIDTH), (getHeight() - imageTerrainLeft.getHeight()), this);
        graphics.drawImage(imageTerrainMiddle, (currentChunkIndex * TERRAIN_CHUNK_WIDTH), (getHeight() - imageTerrainMiddle.getHeight()), this);
        graphics.drawImage(imageTerrainRight, ((currentChunkIndex + 1) * TERRAIN_CHUNK_WIDTH), (getHeight() - imageTerrainRight.getHeight()), this);
        ArrayList<MapObject> mapObjects = map.getObjects();
        for (MapObject mapObject : mapObjects) {
            drawMapObject(mapObject);
        }
        if (player.getY() > MainFrame.HEIGHT) {
            BufferedImage imageUp = ImageCache.getImage("./assets/images/up.png");
            graphics.drawImage(imageUp, (int) (player.getX() - (imageUp.getWidth() / 2f)), 70, this);
        }
        graphics.translate(cameraX, 0);
    }

    private void drawWater(int cameraX) {
        BufferedImage imageWater = ImageCache.getImage("./assets/images/water.png");
        double waterXProgress = ((System.currentTimeMillis() % 1000) / 1000.0);
        int waterXOffset = (int) (waterXProgress * imageWater.getWidth());
        double waterHeightProgress = ((Math.sin(System.currentTimeMillis() / 1000.0) + 1) / 2);
        int waterY = ((getHeight() - imageWater.getHeight()) + (int) (waterHeightProgress * 40));
        for (int x = (-1 * ((cameraX - waterXOffset) % imageWater.getWidth())); x < getWidth(); x += imageWater.getWidth()) {
            graphics.drawImage(imageWater, x, waterY, this);
        }
    }

    private void drawMapObject(MapObject mapObject){
        int x = convertMapX(mapObject.getX());
        int y = convertMapY(mapObject.getY());
        graphics.translate(x, y);
        graphics.rotate(mapObject.getAngle());
        Sprite sprite = mapObject.getSprite();
        if(mapObject instanceof Bird){
            sprite = birdSprite;
        }
        if(sprite != null){

            // TODO: Have it dynamic as soon as there are other map objects than birds
            int contentX = 1;
            int contentY = 7;
            int contentWidth = 33;
            int contentHeight = 33;

            BufferedImage image = ImageCache.getImage(sprite.getImageFilePath());
            int offsetX = (-1 * contentX);
            int offsetY = (-1 * (contentY + (contentHeight / 2)));
            graphics.translate(offsetX, offsetY);
            if(sprite.isTiled()){
                SpriteTile currentTile = sprite.getCurrentTile();
                int halfWidth = (currentTile.getWidth() / 2);
                int halfHeight = (currentTile.getHeight() / 2);
                graphics.drawImage(image,
                    -1 * halfWidth, -1 * halfHeight, halfWidth, halfHeight,
                    currentTile.getX(), currentTile.getY(), (currentTile.getX() + currentTile.getWidth()), (currentTile.getY() + currentTile.getHeight()),
                    this
                );
            }
            else{
                int halfWidth = (contentWidth / 2);
                int halfHeight = (contentHeight / 2);
                graphics.drawImage(image, -1 * halfWidth, -1 * halfHeight, this);
            }
            graphics.translate(-1 * offsetX, -1 * offsetY);
        }
        graphics.rotate(-1 * mapObject.getAngle());
        graphics.translate(-1 * x, -1 * y);
    }

    private int convertMapX(float x){
        return (int) x;
    }

    private int convertMapY(float y){
        return (int) (getHeight() - y);
    }

    private void drawFancyText(String text, int x, int y){
        int characterX = x;
        for (char character : text.toCharArray()) {
            if (character == ' ') {
                characterX += 20;
            } else {
                BufferedImage imageCharacter = ImageCache.getImage("./assets/images/letters/" + character + ".png");
                graphics.drawImage(imageCharacter, characterX, y, this);
                characterX += imageCharacter.getWidth();
            }
        }
    }

    public void setSkin(Skin skin){
        this.skin = skin;
        birdSprite = new Sprite("./assets/skins/" + skin.getName() + "/player_normal.png");
        updateImage_Sky();
        islandTerrainColors.put(0, skin.getTerrainColors());
        imageTerrainLeft = createTerrainImage(currentChunkIndex - 1);
        imageTerrainMiddle = createTerrainImage(currentChunkIndex);
        imageTerrainRight = createTerrainImage(currentChunkIndex + 1);
        imageTerrainFuture = createTerrainImage(currentChunkIndex + 2);
    }

    private void updateImage_Sky(){
        imageSky = new BufferedImage(MainFrame.WIDTH, MainFrame.HEIGHT, BufferedImage.TYPE_INT_ARGB);
        int imageWidth = imageSky.getWidth();
        int imageHeight = imageSky.getHeight();
        Graphics2D graphics = (Graphics2D) imageSky.getGraphics();
        Color backgroundColorTop = skin.getSkyColors()[0];
        Color backgroundColorBottom = skin.getSkyColors()[1];
        for(int y=0;y<imageHeight;y++){
            float interpolationProgress = (((float) y) / imageHeight);
            graphics.setColor(getInterpolatedColor(backgroundColorTop, backgroundColorBottom, interpolationProgress));
            graphics.drawLine(0, y, (imageWidth - 1), y);
        }
    }

    private BufferedImage createTerrainImage(int index){
        Terrain terrain = map.getTerrain();
        int terrainOffset = (index * TERRAIN_CHUNK_WIDTH);
        BufferedImage imageTerrain = new BufferedImage(TERRAIN_CHUNK_WIDTH, MainFrame.HEIGHT, BufferedImage.TYPE_INT_ARGB);
        int imageHeight = imageTerrain.getHeight();
        Graphics2D graphics = (Graphics2D) imageTerrain.getGraphics();
        for(int x=0;x<imageTerrain.getWidth();x++){
            int terrainX = (terrainOffset + x);
            int y = (int) (imageHeight - terrain.getHeight(terrainX));
            int island = (terrainX / (Terrain.ISLAND_CONTENT_WIDTH + Terrain.ISLAND_DISTANCE));
            Color[] terrainColors = islandTerrainColors.computeIfAbsent(island, isl -> {
                Color[] randomTerrainColors = new Color[2];
                for (int i = 0; i < randomTerrainColors.length; i++) {
                    randomTerrainColors[i] = new Color((float) Math.random(), (float) Math.random(), (float) Math.random(), 1);
                }
                return randomTerrainColors;
            });
            for(int r=imageHeight;r>y;r-=1){
                float interpolationProgress = (((float) r) / imageHeight);
                graphics.setColor(getInterpolatedColor(terrainColors[0], terrainColors[1], interpolationProgress));
                graphics.drawLine(x, y, x, r + 1);
            }
            graphics.setColor(Color.BLACK);
            graphics.drawLine(x, y - 1, x, y);
        }
        for(int x=0;x<imageTerrain.getWidth();x++){
            int terrainX = (terrainOffset + x);
            float terrainY = terrain.getHeight(terrainX);
            if (terrainY > 0) {
                int y = (int) (imageHeight - terrainY);
                if (Util.getRandomNumberBetween(1, 120) == 1) {
                    BufferedImage vegetationImage = ImageCache.getImage("./assets/skins/" + skin.getName() + "/vegetation_" + Util.getRandomNumberBetween(0, 1) + ".png");
                    int imageSize = vegetationImage.getWidth();
                    if ((x >= imageSize) && (x < (imageTerrain.getWidth() - imageSize))) {
                        float terrainSlope = terrain.getSlope(terrainX);
                        int originX = (imageSize / 2);
                        double angle = ((2 * Math.PI) - Math.atan(terrainSlope));
                        graphics.translate(x, y);
                        graphics.rotate(angle);
                        graphics.drawImage(vegetationImage, -1 * originX, -1 * imageSize, null);
                        graphics.rotate(-1 * angle);
                        graphics.translate(-1 * x, -1 * y);
                    }
                }
            }
        }
        return imageTerrain;
    }

    private static Color getInterpolatedColor(Color color1, Color color2, float progress){
        float[] colorComponents1 = color1.getColorComponents(null);
        float[] colorComponents2 = color2.getColorComponents(null);
        float[] interpolatedColorComponents = new float[3];
        for(int i=0;i<interpolatedColorComponents.length;i++){
            float value  = (colorComponents1[i] + (colorComponents2[i] - colorComponents1[i]) * progress);
            if(value < 0){
                value = 0;
            }
            interpolatedColorComponents[i] = value;
        }
        return new Color(interpolatedColorComponents[0], interpolatedColorComponents[1], interpolatedColorComponents[2], 1);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 600, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 350, Short.MAX_VALUE)
        );
    }
}
