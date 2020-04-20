package scripts.FirstSteps.Nodes.JTradeMule;

import org.tribot.api.General;
import org.tribot.api2007.Player;
import org.tribot.api2007.Players;
import org.tribot.api2007.types.RSPlayer;
import org.tribot.api2007.types.RSTile;
import scripts.FirstSteps.API.Node;
import scripts.FirstSteps.FirstSteps;
import scripts.FirstSteps.Utils.Constants;

import java.util.Arrays;

public class TradeMule extends Node {
    @Override
    public void printStatus() {
        General.println("TRADING MULE FOR BOND + CASH");
    }

    @Override
    public boolean validate() {
        return Arrays.stream(Constants.MULE_STATES).anyMatch(stage -> stage == FirstSteps.gameState);
    }

    @Override
    public void execute() {
        General.println("Executing...");
        RSPlayer[] mules = Players.find(Constants.MULE_USER);
        RSPlayer muleToTrade = mules[0];

        RSPlayer bot = Player.getRSPlayer(); //maybe Player instead of RSPlayer
        if (muleToTrade != null) {
            General.println("FOUND MULE: " + muleToTrade.getName());
            if (bot.getPosition().distanceTo(muleToTrade) <= 1) {
                General.println("BOT IN POSITION TO TRADE");
                /*XXX bot is trying to do something here, but trade is not going through.
                      look into Trading
                 */
                if (bot.click("Trade with " + Constants.MULE_USER)) {
                    General.println("Trading with " + Constants.MULE_USER);
                }
            }
        }
    }
}
