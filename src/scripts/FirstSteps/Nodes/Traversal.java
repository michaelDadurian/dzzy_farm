package scripts.FirstSteps.Nodes;

import org.tribot.api.General;
import org.tribot.api2007.types.RSTile;
import scripts.FirstSteps.API.Node;
import scripts.FirstSteps.FirstSteps;
import scripts.dax_api.api_lib.DaxWalker;

import java.util.Arrays;
import scripts.FirstSteps.Utils.Constants;
public class Traversal extends Node {
    public void printStatus(){
        General.println("WALKING TO THE NEXT STAGE");
    }
    public boolean validate() {
        return Arrays.stream(Constants.TRAVERSAL_STATES).anyMatch(stage -> stage == FirstSteps.gameState);
    }
    public void execute() {
        General.println("TRAVERSE GAMESTATE: " + FirstSteps.gameState);
        switch (FirstSteps.gameState) {
            case Constants.WALK_TO_SURVIVAL_AREA:
                General.println("WALKING TO SURVIVAL AREA");
                DaxWalker.walkTo(Constants.SURVIVAL_AREA.getRandomTile());
                break;
            case Constants.WALK_TO_KITCHEN_AREA:
                General.println("WALKING TO KITCHEN AREA");
                DaxWalker.walkTo(Constants.KITCHEN_AREA.getRandomTile());
                break;
            case Constants.WALK_TO_KITCHEN_AREA2:
                General.println("WALKING TO KITCHEN_AREA2");
                DaxWalker.walkTo(Constants.KITCHEN_AREA.getRandomTile());
                break;
            case Constants.WALK_TO_QUEST_GUIDE_AREA:
                General.println("WALKING TO QUEST GUIDE");
                DaxWalker.walkTo(Constants.QUEST_GUIDE_HOUSE.getRandomTile());
                break;
            case Constants.WALK_TO_MINING_AREA:
                General.println("WALKING TO MINING");
                DaxWalker.walkTo(Constants.MINING_AREA.getRandomTile());
                break;
            case Constants.WALK_TO_COMBAT_AREA:
                General.println("WALKING TO COBMAT");
                DaxWalker.walkTo(Constants.COMBAT_AREA.getRandomTile());
                break;
            case Constants.WALK_TO_ACCOUNT_GUIDE_AREA:
                General.println("WALKING TO ACCT GUIDE");
                DaxWalker.walkTo(Constants.BANK_AREA.getRandomTile());
                break;
            case Constants.WALK_TO_BROTHER_BRACE_AREA:
                General.println("WALKING TO BROTHER BRACE");
                DaxWalker.walkTo(Constants.PRIEST_GUIDE_HOUSE.getRandomTile());
                break;
            case Constants.WALK_TO_WIZARD_HOUSE_AREA:
                General.println("WALKING TO WIZARD");
                DaxWalker.walkTo(Constants.WIZARD_HOUSE.getRandomTile());
                break;
            case Constants.WALK_TO_MULE_AREA:
                General.println("WALKING TO MULE");
                //DaxWalker.walkTo(Constants.DISTRIBUTOR_AREA.getRandomTile());
                DaxWalker.walkTo(new RSTile(Constants.DISTRIBUTOR_LOCATION[0], Constants.DISTRIBUTOR_LOCATION[1], Constants.DISTRIBUTOR_LOCATION[2]));
                break;
        }

        General.sleep(7000);
    }

}
