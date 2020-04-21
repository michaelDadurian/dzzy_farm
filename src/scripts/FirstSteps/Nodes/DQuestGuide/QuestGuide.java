package scripts.FirstSteps.Nodes.DQuestGuide;

import org.tribot.api.General;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSNPC;
import scripts.FirstSteps.API.Node;
import scripts.API.*;

import java.util.Arrays;

import scripts.FirstSteps.FirstSteps;
import scripts.FirstSteps.Utils.Constants;
public class QuestGuide extends Node {
    public void printStatus(){
        General.println("AT THE QUEST AREA STAGE");
    }
    public boolean validate() {
        return Arrays.stream(Constants.QUEST_GUIDE_STATES).anyMatch(stage -> stage == FirstSteps.gameState);
    }
    public void execute() {
        RSInterface chatInterface = Interfaces.get(263,1,0);
        RSNPC[] questGuide = NPCs.findNearest(Constants.QUEST_GUIDE);

        if (InterfaceHandler.interfaceContainsText(chatInterface, "Click on the flashing icon to the left of your inventory.")) {
            TabsHandler.openTab(GameTab.TABS.QUESTS);
            return;
        }

        NPCHandler.talkToNPC(questGuide);
    }
}
