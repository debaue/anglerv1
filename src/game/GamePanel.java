package game;

import javax.swing.*;
import java.awt.*;

import game.renderer.*;
import util.InputHandler;

public class GamePanel extends JPanel {

    public static final int WIDTH  = 960;
    public static final int HEIGHT = 576;

    private final Game game;
    private final Timer timer;

    private final TitleRenderer     titleRenderer;
    private final WorldRenderer     worldRenderer;
    private final HudRenderer       hudRenderer;
    private final FishingRenderer   fishingRenderer;
    private final InventoryRenderer inventoryRenderer;
    private final ShopRenderer      shopRenderer;
    private final FishBookRenderer  fishBookRenderer;
    private final PauseRenderer     pauseRenderer;

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);

        InputHandler input = new InputHandler();
        addKeyListener(input);
        addMouseListener(input);

        game = new Game(input);

        titleRenderer     = new TitleRenderer(game);
        worldRenderer     = new WorldRenderer(game);
        hudRenderer       = new HudRenderer(game);
        fishingRenderer   = new FishingRenderer(game);
        inventoryRenderer = new InventoryRenderer(game);
        shopRenderer      = new ShopRenderer(game);
        fishBookRenderer  = new FishBookRenderer(game);
        pauseRenderer     = new PauseRenderer(game);

        timer = new Timer(16, e -> {
            game.update(0.016f);
            repaint();
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.scale((double) getWidth() / WIDTH, (double) getHeight() / HEIGHT);

        if (game.getState() == Game.GameState.TITLE) {
            titleRenderer.draw(g2);
            return;
        }

        worldRenderer.draw(g2);
        hudRenderer.draw(g2);

        switch (game.getState()) {
            case FISHING_MENU -> fishingRenderer.draw(g2);
            case INVENTORY    -> inventoryRenderer.draw(g2);
            case SHOP         -> shopRenderer.draw(g2);
            case FISH_BOOK    -> fishBookRenderer.draw(g2);
            case PAUSE        -> pauseRenderer.draw(g2);
        }
    }
}
