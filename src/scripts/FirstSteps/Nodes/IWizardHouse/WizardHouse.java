package scripts.FirstSteps.Nodes.IWizardHouse;

import org.tribot.api.General;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSNPC;
import scripts.API.GlobalConstants;
import scripts.API.InterfaceHandler;
import scripts.API.NPCHandler;
import scripts.API.TabsHandler;
import scripts.FirstSteps.API.Node;
import scripts.FirstSteps.FirstSteps;
import scripts.FirstSteps.Utils.Constants;

import java.util.Arrays;

public class WizardHouse extends Node {
    public void printStatus(){
        General.println("AT THE WIZARD HOUSE AREA STAGE");
    }
    public boolean validate() {
        return Arrays.stream(Constants.WIZARD_HOUSE_STATES).anyMatch(stage -> stage == FirstSteps.gameState);
    }
    public void execute() {
        RSInterface chatInterface = Interfaces.get(263,1,0);
        RSNPC[] magicInstructor = NPCs.findNearest(Constants.MAGIC_INSTRUCTOR);
        String[] options = NPCChat.getOptions();

        if (InterfaceHandler.interfaceContainsText(chatInterface, "up the magic interface by clicking on the flashing icon.")) {
            TabsHandler.openTab(GameTab.TABS.MAGIC);
            return;
        }

        if (InterfaceHandler.interfaceContainsText(chatInterface, "Look for the Wind Strike spell in your magic interface.")) {
            RSNPC[] chicken = NPCs.findNearest("Chicken");
            if(Magic.selectSpell("Wind Strike")) {
                NPCHandler.interactWithNPC(chicken, GlobalConstants.CAST + " Wind Strike ->", false);
            }
            return;
        }

        if (InterfaceHandler.interfaceContainsText(Interfaces.get(219,1,0), "Do you want to go to the mainland?")) {
            InterfaceHandler.clickInterface(Interfaces.get(219,1,1));
            return;
        }

        if (options != null && options.length > 0 && options[2] != null  && options[2].contains("No, I'm not planning to do that.")) {
            NPCChat.selectOption(NPCChat.getOptions()[2], true);
            return;
        }

        NPCHandler.talkToNPC(magicInstructor);
    }
}
