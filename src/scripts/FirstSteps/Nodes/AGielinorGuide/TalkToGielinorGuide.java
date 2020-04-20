package scripts.FirstSteps.Nodes.AGielinorGuide;

import org.tribot.api.General;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSNPC;
import scripts.API.InterfaceHandler;
import scripts.API.NPCHandler;
import scripts.API.TabsHandler;
import scripts.FirstSteps.API.Node;
import scripts.FirstSteps.FirstSteps;
import scripts.FirstSteps.Utils.Constants;

import java.util.Arrays;

public class TalkToGielinorGuide extends Node {
    public void printStatus() {
        General.println("TALKING TO GIELINOR GUIDE");
    }
    public boolean validate() {
        return Arrays.stream(Constants.GIELINOR_GUIDE_GAME_STATES).anyMatch(stage -> stage == FirstSteps.gameState);
    }
    public void execute() {
        RSNPC[] gielinorGuide = NPCs.findNearest(Constants.GIELINOR_GUIDE);
        // Click on the options menu
        RSInterface clickOptionInterface = Interfaces.get(263,1,0);
        boolean shouldClickOptionsMenu = InterfaceHandler.interfaceContainsText(clickOptionInterface, "This will display your options menu.");

        if (shouldClickOptionsMenu) {
            TabsHandler.openTab(GameTab.TABS.OPTIONS);
            return;
        }

        // Choose Experience
        RSInterface[] experienceWithRSInterfaces = {Interfaces.get(219,1,1), Interfaces.get(219,1,2), Interfaces.get(219,1,3)};
        for (RSInterface experiencePlayerInterface: experienceWithRSInterfaces) {
            if (InterfaceHandler.interfaceContainsText(experiencePlayerInterface, "I am an experienced player.")) {
                if (InterfaceHandler.clickInterface(experiencePlayerInterface)) {
                    InterfaceHandler.clickHereToContinue();
                    return;
                }
            }
        }

        NPCHandler.talkToNPC(gielinorGuide);
    }
}