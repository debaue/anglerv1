package util;

import java.awt.image.BufferedImage;

public class AnimationController {
    private BufferedImage[][] idleFrames;
    private BufferedImage[][] walkFrames;
    private BufferedImage[][] fishFrames;

    public enum AnimState {
        IDLE,
        WALK,
        FISHING
    }

    private int direction = 0; // 0 1 2 3, links, rechts, hoch runter
    private int frameIndex = 0;
    private float frameTimer = 0f;
    private float frameSpeed = 0.15f;
    private AnimState state;

    public AnimationController() {
        this.idleFrames = SpriteLoader.getIdleFrames();
        this.walkFrames = SpriteLoader.getWalkFrames();
    }

    public void update(float delta, AnimState state, int direction) {
        this.state     = state;
        this.direction = direction;

        frameTimer += delta;

        int maxFrames = switch(state) {
            case IDLE    -> idleFrames[direction].length;
            case WALK    -> walkFrames[direction].length;
            case FISHING -> fishFrames[direction].length;
        };

        if(frameTimer >= frameSpeed) {
            frameTimer = 0f;
            frameIndex++;
            if(frameIndex >= maxFrames) frameIndex = 0;
        }
    }

    public BufferedImage getCurrentFrame() {
        return switch(state) {
            case IDLE    -> idleFrames[direction][frameIndex];
            case WALK    -> walkFrames[direction][frameIndex];
            case FISHING -> fishFrames[direction][frameIndex];
        };
    }
}
