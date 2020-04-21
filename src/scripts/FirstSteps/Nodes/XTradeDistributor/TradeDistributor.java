package scripts.FirstSteps.Nodes.XTradeDistributor;

import org.tribot.api.General;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.Player;
import org.tribot.api2007.Players;
import org.tribot.api2007.Trading;
import org.tribot.api2007.types.RSPlayer;
import scripts.API.GlobalConstants;
import scripts.FirstSteps.API.Node;
import scripts.FirstSteps.FirstSteps;
import scripts.FirstSteps.Utils.Constants;

import java.util.Arrays;

public class TradeDistributor extends Node {
    @Override
    public void printStatus() {
        General.println("TRADING MULE FOR BOND + CASH");
    }

    @Override
    public boolean validate() {
        return Arrays.stream(Constants.DISTRIBUTOR_STATES).anyMatch(stage -> stage == FirstSteps.gameState);
    }

    @Override
    public void execute() {
        General.println("TRADE OP INITIATED");
        RSPlayer[] players = Players.find(Constants.DISTRIBUTOR_NAME);
        RSPlayer distributor = players[0];

        RSPlayer bot = Player.getRSPlayer(); //maybe Player instead of RSPlayer
        General.println("BOT NAME: " + bot.getName());
        if (distributor != null) {
            General.println("FOUND DISTRIBUTOR: " + distributor.getName());
            if (bot.getPosition().distanceTo(distributor) <= 1) {
                General.println("BOT IN POSITION TO TRADE");
                /*XXX bot is trying to do something here, but trade is not going through.
                      look into Trading
                 */
                Mouse.move(Constants.DISTRIBUTOR_LOCATION[0], Constants.DISTRIBUTOR_LOCATION[1]);
                Mouse.click(GlobalConstants.RIGHT_CLICK);
                String click_text = "Trade with " + distributor.getName();
                General.sleep(1000);
                //bot.click(click_text);
                if (bot.click("Trade with " + Constants.DISTRIBUTOR_NAME)) {
                    General.println("Trading with " + Constants.DISTRIBUTOR_NAME);
                    General.sleep(2000);
                    Trading.offer(1, Constants.BREAD_ID);
                    Trading.offer(1, Constants.SHORTBOW_ID);
                }
            }
        }
    }
}
