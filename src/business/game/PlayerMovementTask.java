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
        double delta_v;
        double delta_angle;
        long lastTime = System.nanoTime();
        while (!Thread.interrupted()) {

            currTime = System.nanoTime();
            /*
                set speed and rotation
             */
            if (active) {
                delta_t = (double)(currTime - lastTime)/(1000000000.0);
                if (upPressed && player.getSpeedValue() < Game.MAX_SPEED) {
                    delta_v = (Game.ACCELERATION - Game.PASSIVE_ACCELERATION) * delta_t;
                    player.getSpeedXY().add(Math.cos(Math.toRadians(player.getRotation()))*delta_v, Math.sin(Math.toRadians(player.getRotation()))*delta_v);
                    player.setSpeed(player.getSpeed() + (Game.ACCELERATION - Math.signum(player.getSpeed()) * Game.PASSIVE_ACCELERATION) * delta_t);
                }
                else if (downPressed && player.getSpeedValue() < Game.MAX_SPEED) {
                    delta_v = -(Game.ACCELERATION - Game.PASSIVE_ACCELERATION) * delta_t;
                    player.getSpeedXY().add(Math.cos(Math.toRadians(player.getRotation()))*delta_v, Math.sin(Math.toRadians(player.getRotation()))*delta_v);
                    player.setSpeed(player.getSpeed() - (Game.ACCELERATION + Math.signum(player.getSpeed()) * Game.PASSIVE_ACCELERATION) * delta_t);
                }
                else if (player.getSpeedValue() != 0) {
                    delta_v = - Game.PASSIVE_ACCELERATION * delta_t;
                    if (player.getSpeedValue() > delta_v) {
                        player.getSpeedXY().add(delta_v);
                        player.setSpeed(player.getSpeed() - Math.signum(player.getSpeed()) * Game.PASSIVE_ACCELERATION * delta_t);
                    }
                    else {
                        player.getSpeedXY().setLocation(0.0, 0.0);
                    }
                }

                if (leftPressed) {
                    delta_angle = -Game.ROTATION_SPEED * delta_t;
                    player.rotate(Math.toRadians(delta_angle));
                    player.setRotation((player.getRotation() + delta_angle));
                }
                else if (rightPressed) {
                    delta_angle = Game.ROTATION_SPEED * delta_t;
                    player.rotate(Math.toRadians(delta_angle));
                    player.setRotation((player.getRotation() + delta_angle));
                }

                /*
                    perform movement
                 */
//                move(player.getCoords().getX()+Math.cos(Math.toRadians(player.getRotation())) * player.getSpeed() * delta_t,
//                        player.getCoords().getY()+Math.sin(Math.toRadians(player.getRotation())) * player.getSpeed() * delta_t);
                move(player.getCoords().getX()+player.getSpeedXY().getX()*delta_t,
                        player.getCoords().getY()+player.getSpeedXY().getY()*delta_t);

            }

            lastTime = currTime;
        }

        upPressed = false;
        downPressed = false;
        leftPressed = false;
        rightPressed = false;
    }

    private boolean correctCoords(double x, double y) {
        return (Math.sqrt(x*x + y*y) < (Game.BOARD_RADIUS - Game.PLAYER_RADIUS));
    }

    private void move(double x, double y) {
        if (correctCoords(x, y)) {
            player.setCoords(x, y);
        }
        else {
            player.getSpeedXY().setLocation(0.0, 0.0);
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
