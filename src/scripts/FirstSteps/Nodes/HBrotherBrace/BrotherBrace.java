package scripts.FirstSteps.Nodes.HBrotherBrace;

import org.tribot.api.General;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSNPC;
import scripts.API.InterfaceHandler;
import scripts.API.NPCHandler;
import scripts.API.TabsHandler;
import scripts.FirstSteps.API.Node;
import scripts.FirstSteps.FirstSteps;
import scripts.FirstSteps.Utils.Constants;

import java.util.Arrays;

public class BrotherBrace extends Node {
    public void printStatus(){
        General.println("AT THE BROTHER BRACE AREA STAGE");
    }
    public boolean validate() {
        return Arrays.stream(Constants.BROTHER_BRACE_STATES).anyMatch(stage -> stage == FirstSteps.gameState);
    }
    public void execute() {
        RSInterface chatInterface = Interfaces.get(263,1,0);
        RSNPC[] brotherBrace = NPCs.findNearest(Constants.BROTHER_BRACE);

        if (InterfaceHandler.interfaceContainsText(chatInterface, "Click on the flashing icon to open the Prayer menu.")) {
            TabsHandler.openTab(GameTab.TABS.PRAYERS);
            return;
        }

        if (InterfaceHandler.interfaceContainsText(chatInterface, "on the flashing face icon to open your friends and ignore lists.")) {
            TabsHandler.openTab(GameTab.TABS.FRIENDS);
            return;
        }

        NPCHandler.talkToNPC(brotherBrace);
    }
}
