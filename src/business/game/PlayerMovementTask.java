package business.game;

/**
 * Created by Robert on 02.04.2017.
 */
public class PlayerMovementTask implements Runnable {
    private Player player;

    private boolean active = true;


    private boolean upPressed = false;
    private boolean downPressed = false;
    private boolean rightPressed = false;
    private boolean leftPressed = false;

    public PlayerMovementTask(Player player) {
        this.player = player;
        move(player.getCoords().getX(), player.getCoords().getY());
    }

    @Override
    public void run() {
        long currTime;
        double delta_t;
        long lastTime = System.nanoTime();
        while (!Thread.interrupted()) {

            currTime = System.nanoTime();
            /*
                set speed and rotation
             */
            if (active) {
                delta_t = (double)(currTime - lastTime)/(1000000000.0);

                if (upPressed && player.getSpeed() < Game.MAX_SPEED) {
                    player.setSpeed(player.getSpeed() + (Game.ACCELERATION - Math.signum(player.getSpeed()) * Game.PASSIVE_ACCELERATION) * delta_t);
                }
                else if (downPressed && player.getSpeed() > -Game.MAX_SPEED) {
                    player.setSpeed(player.getSpeed() - (Game.ACCELERATION + Math.signum(player.getSpeed()) * Game.PASSIVE_ACCELERATION) * delta_t);
                }
                else if (player.getSpeed() != 0) {
                    player.setSpeed(player.getSpeed() - Math.signum(player.getSpeed()) * Game.PASSIVE_ACCELERATION * delta_t);
                }

                if (leftPressed) {
                    player.setRotation((player.getRotation() - Game.ROTATION_SPEED * delta_t));
                }
                else if (rightPressed) {
                    player.setRotation((player.getRotation() + Game.ROTATION_SPEED * delta_t));
                }

                /*
                    perform movement
                 */
                move(player.getCoords().getX()+Math.cos(Math.toRadians(player.getRotation())) * player.getSpeed() * delta_t,
                        player.getCoords().getY()+Math.sin(Math.toRadians(player.getRotation())) * player.getSpeed() * delta_t);

            }

            lastTime = currTime;
        }

        upPressed = false;
        downPressed = false;
        leftPressed = false;
        rightPressed = false;
    }

    private boolean correctCoords(double x, double y) {
        return (Math.sqrt(x*x + y*y) < (Game.BOARD_RADIUS- Game.PLAYER_RADIUS));
    }

    private void move(double x, double y) {
        if (correctCoords(x, y)) {
            player.setCoords(x, y);
        }
        else {
            player.setSpeed(0.0);
        }
    }

    public Player getPlayer() {
        return player;
    }


    public void setUpPressed(boolean upPressed) {
        this.upPressed = upPressed;
    }

    public void setDownPressed(boolean downPressed) {
        this.downPressed = downPressed;
    }

    public void setRightPressed(boolean rightPressed) {
        this.rightPressed = rightPressed;
    }

    public void setLeftPressed(boolean leftPressed) {
        this.leftPressed = leftPressed;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
