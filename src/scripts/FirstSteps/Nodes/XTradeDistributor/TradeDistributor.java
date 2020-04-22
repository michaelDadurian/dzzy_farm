package scripts.FirstSteps.Nodes.XTradeDistributor;

import com.sun.xml.internal.ws.wsdl.writer.document.Message;
import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSPlayer;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.interfaces.MessageListening07;
import scripts.API.GlobalConstants;
import scripts.FirstSteps.API.Node;
import scripts.FirstSteps.FirstSteps;
import scripts.FirstSteps.Utils.Constants;

import java.util.Arrays;

public class TradeDistributor extends Node implements MessageListening07 {
    @Override
    public void printStatus() {
        General.println("TRADING MULE FOR BOND + CASH");
    }

    @Override
    public boolean validate() {
        RSPlayer bot = Player.getRSPlayer();
        RSTile position = bot.getPosition();
        boolean nearMule = Constants.DISTRIBUTOR_AREA.contains(position);
        General.println("EXECUTE TRADE? GAME STATE = " + nearMule);
        return nearMule;
        //return Arrays.stream(Constants.TRADE_DISTRIBUTOR_STATES).anyMatch(stage -> stage == FirstSteps.gameState);
    }

    @Override
    public void execute() {
        /* Use RSArea Generator tool
        * area.contains(distributor)
        * trade distributor
        * https://www.youtube.com/watch?v=P0mSWEI7ixY
        * Walking.blindWalkTo(tile)
        * */
        General.println("TRADE OP INITIATED");
        RSArea distributorArea = Constants.DISTRIBUTOR_AREA;
        RSPlayer[] players = Players.find(Constants.DISTRIBUTOR_NAME);
        RSPlayer distributor = players[0];
        //General.println("FOUND DISTRIBUTOR: " + distributor.getName());
        RSPlayer bot = Player.getRSPlayer();
        General.println("BOT NAME: " + bot.getName());

        if (distributor != null) {
            General.println("FOUND DISTRIBUTOR: " + distributor.getName());
            General.println("CLICKING ON: (" + distributor.getPosition().getX() + ", " + distributor.getPosition().getY() + ")" );
            //Mouse.click(distributor.getPosition().getX(), distributor.getPosition().getY(), GlobalConstants.RIGHT_CLICK);
            //DynamicClicking.clickRSTile(distributor.getPosition(), GlobalConstants.RIGHT_CLICK);
            General.sleep(1000);

            Mouse.click(Projection.tileToScreen(distributor.getPosition(), 0), GlobalConstants.RIGHT_CLICK);
            //General.sleep(1000);
            String click_text = "Trade with " + distributor.getName();
            //General.sleep(1000);
            if (bot.click(click_text)) {
                General.println("Trading with " + Constants.DISTRIBUTOR_NAME);
                General.sleep(5000);
                if (Trading.hasAccepted(true)) {
                    Trading.offer(1, Constants.BREAD_ID);
                    Trading.offer(1, Constants.SHORTBOW_ID);
                }


            }

        }
    }
}
