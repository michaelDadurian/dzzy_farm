package scripts.Distributor;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import org.tribot.api.General;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.Login;
import org.tribot.api2007.Player;
import org.tribot.api2007.Trading;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSPlayer;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import scripts.FirstSteps.Utils.Constants;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.dax_api.api_lib.models.DaxCredentials;
import scripts.dax_api.api_lib.models.DaxCredentialsProvider;

import java.util.Arrays;

@ScriptManifest(
        authors = {"dzzl"},
        category = "Quests",
        name = "Distributor",
        gameMode = 1,
        description = "Bond/GP Distributor.",
        version = (1.1)
)
public class Distributor extends Script {

    //public static int[] LOCATION = {3221, 3242, 0}; //TODO: config with script args
    public static int[] LOCATION = Constants.DISTRIBUTOR_LOCATION;
    public static int[] UNIQUE_ITEMS = {Constants.SHORTBOW_ID, Constants.BREAD_ID};
    public boolean END_SCRIPT = false;

    @Override
    public void run() {
        DaxWalker.setCredentials(new DaxCredentialsProvider() {
            @Override
            public DaxCredentials getDaxCredentials() {
                return new DaxCredentials("sub_DPjXXzL5DeSiPf", "PUBLIC-KEY");
            }
        });
        //SCRIPT ARGS! Use script args to set location, same arg passed to FirstSteps

        while (!END_SCRIPT) {
            sleep(10000);
            loop();
            /*
            if (some logout condition) {
                General.println("FINISHED! LOGGING YOU OUT NOW :)");
                Login.logout();
                break;
            }*/
        }

        //Login.logout();
    }

    private void loop() {
        //Accept trades from anyone
        //Bot offers some unique combo of items
        //If unique combo is satisfied, offer bond + gp
        //accept trade
        //Move mouse periodically to avoid logout
        boolean isInLocation = false;
        RSTile distributorPosition = Player.getPosition();
        General.println("DISTRIBUTOR POSITION: " + distributorPosition);

        isInLocation = distributorPosition.getX() == LOCATION[0] && distributorPosition.getY() == LOCATION[1];

        RSPlayer distributor = Player.getRSPlayer();
        General.println("IN LOCATION? " + isInLocation);
        if (isInLocation) {
            String click_text = "56 Group 599 wishes to trade with you"; //regex?

            if (distributor.click(click_text)){
                General.sleep(1000);

                boolean validTrade = false;
                RSItem[] offeredItems = Trading.getOfferedItems(true);
                int[] offeredItemIDs = new int[offeredItems.length];
                for (int i = 0; i < offeredItems.length; i++) {
                    offeredItemIDs[i] = offeredItems[i].getID();
                }
                Arrays.sort(offeredItemIDs);
                Arrays.sort(UNIQUE_ITEMS);

                if (offeredItems.length == Constants.NUM_UNIQUE_ITEMS) {
                    validTrade = Arrays.equals(offeredItemIDs, UNIQUE_ITEMS);
                } else {
                    General.println("Declining trade...Not enough items");
                    distributor.click("Decline");
                }
                if (validTrade) {
                    General.println("VALID TRADE - OFFERING ITEMS...");
                    General.sleep(1000);
                    //Trading.offer(1, Constants.BOND_ID);
                    //Trading.offer(50000, Constants.GOLD_ID);
                    Trading.offer(1, 379);

                    //Accept
                }
            }
        }else{
            General.println("DISTRIBUTOR IN WRONG LOCATION");
            END_SCRIPT = true;
        }

    }
}
