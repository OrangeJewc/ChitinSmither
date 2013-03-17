import org.powerbot.core.event.listeners.PaintListener;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.LoopTask;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.job.state.Tree;
import org.powerbot.core.script.methods.Players;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.SceneObject;

import java.awt.*;

@Manifest(name = "Chitin Smither", authors = "OrangeJuice", description = "Smiths chitin to your heart's content!", version = 1.0)
public class Main extends ActiveScript implements PaintListener {

    private int startXP = Skills.getExperience(Skills.SMITHING);
    private long startTime;
    private final Color color1 = new Color(239, 50, 50, 114);
    private final Color color2 = new Color(0, 0, 0);
    public static Tile bTile = new Tile(3189, 3435, 0);
    public static Tile aTile = new Tile(3187, 3426, 0);
    public static SceneObject bank;
    private final Font font1 = new Font("Arial", 0, 15);
    private Tree script = new Tree(new Node[] {
            new Banker(),
            new Smither()
    });

    public void onStart() {
        startTime = System.currentTimeMillis();
    }

    @Override
    public int loop() {
        final Node stateNode = script.state();
        if(stateNode != null && Game.isLoggedIn()) {
            script.set(stateNode);
            final Node setNode = script.get();
            if(setNode != null) {
                getContainer().submit(setNode);
                setNode.join();
            }
        }
        return 0;
    }

    @Override
    public void onRepaint(Graphics g1) {
        int xpGained = Skills.getExperience(Skills.SMITHING)-startXP;
        Graphics2D g = (Graphics2D)g1;
        g.setColor(color1);
        g.fillRoundRect(546, 260, 199, 256, 16, 16);
        g.setFont(font1);
        g.setColor(color2);

        long millis = System.currentTimeMillis() - startTime;
        long hours = millis / (1000 * 60 * 60);
        millis -= hours * (1000 * 60 * 60);
        long minutes = millis / (1000 * 60);
        millis -= minutes * (1000 * 60);
        long seconds = millis / 1000;

        g.drawString("Chitin/Hr: "+(xpGained/10), 550, 339);
        g.drawString("Time: "+ hours + ":" + minutes + ":" + seconds, 550, 295);
        g.drawString("XP/Hr: " + getPerHour(xpGained), 550, 382);
        g.drawString("XP Gained: " + xpGained, 550, 423);
    }

    private int getPerHour(final long value) {
        return (int) ((value) * 3600000D / (System.currentTimeMillis() - startTime));
    }

    public class Banker extends Node {

        @Override
        public boolean activate() {
            return Inventory.getCount(26577) == 0;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void execute() {
            bank = SceneEntities.getNearest(782);
            Walking.findPath(bTile).traverse();
            if(bank.isOnScreen() && Inventory.getCount(26577) == 0) {
                bank.interact("Bank");
                Task.sleep(1000,1500);
                Bank.depositInventory();
                Bank.withdraw(26577, 28);
            }
        }
    }

    public class Smither extends Node {

        @Override
        public boolean activate() {
            return Inventory.getCount(26577) >= 1;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void execute() {
            if(!SceneEntities.getNearest(2783).isOnScreen()) {
                Walking.walk(aTile);
            }
            if(SceneEntities.getNearest(2783).isOnScreen()) {
                if(Players.getLocal().getAnimation()==-1) {
                    SceneEntities.getNearest(2783).click(true);
                    Task.sleep(2500,3100);
                }
            }
        }
    }

}