/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.destroflyer.birds;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author Carl
 */
public class MainFrame extends JFrame {

    public MainFrame() {
        initComponents();
        FrameUtil.centerFrame(this);
        setIconImage(Util.getImage("./assets/images/icon.png"));
        addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent evt) {
                super.keyReleased(evt);
                switch(evt.getKeyCode()) {
                    case KeyEvent.VK_S:
                        currentSkinIndex++;
                        if (currentSkinIndex >= SKINS.length) {
                            currentSkinIndex = 0;
                        }
                        panMap.setSkin(SKINS[currentSkinIndex]);
                        break;
                }
            }
        });
        startNewGame();
        new Thread(() -> {
            long lastFrameStartTimestamp;
            float lastTimePerFrame = 0;
            while (true) {
                lastFrameStartTimestamp = System.currentTimeMillis();
                map.onNextFrameCalculation(lastTimePerFrame);
                panMap.updateUI();
                try {
                    Thread.sleep(1000 / FPS);
                }  catch (Exception ex) {
                }
                lastTimePerFrame = ((System.currentTimeMillis() - lastFrameStartTimestamp) / 1000f);
            }
        }).start();
    }
    private static final Skin[] SKINS = new Skin[]{
        new Skin("default", new Color[]{new Color(10, 160, 20), new Color(170, 230, 30)}, new Color[]{new Color(0, 255, 255), new Color(230, 150, 255)}),
        new Skin("christmas", new Color[]{new Color(255, 255, 255), new Color(210, 210, 210)}, new Color[]{new Color(0, 0, 50), new Color(0, 150, 255)}),
        new Skin("halloween", new Color[]{new Color(100, 50, 0), new Color(20, 10, 0)}, new Color[]{new Color(0, 0, 0), new Color(255, 180, 255)})
    };
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static final int FPS = 60;
    private Map map;
    private PanMap panMap;
    private int currentSkinIndex;

    private void startNewGame() {
        map = new Map();
        panMap = new PanMap(map, SKINS[currentSkinIndex]);
        panMap.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent evt) {
                super.mousePressed(evt);
                map.setPlayerAccelerating(true);
                if (map.isGameOver()) {
                    startNewGame();
                }
            }

            @Override
            public void mouseReleased(MouseEvent evt) {
                super.mouseReleased(evt);
                map.setPlayerAccelerating(false);
            }
        });
        panContainer.removeAll();
        panContainer.add(panMap);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        panContainer = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Birds [Version 0.1]");
        setResizable(false);

        panContainer.setLayout(new java.awt.GridLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panContainer, javax.swing.GroupLayout.DEFAULT_SIZE, WIDTH, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panContainer, javax.swing.GroupLayout.DEFAULT_SIZE, HEIGHT, Short.MAX_VALUE)
        );

        pack();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new MainFrame().setVisible(true));
    }
    private javax.swing.JPanel panContainer;
}
