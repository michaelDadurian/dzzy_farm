package scripts.FirstSteps.Nodes.AGielinorGuide;

import org.tribot.api.General;
import org.tribot.api.input.Keyboard;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.types.RSInterfaceMaster;
import scripts.API.InterfaceHandler;
import scripts.FirstSteps.API.Node;
import scripts.FirstSteps.FirstSteps;

public class CreateCharacter extends Node {
    @Override
    public void printStatus() {
        General.println("TUTTHEISLAND: CREATING CHARACTER");
    }

    @Override
    public boolean validate() {
        return FirstSteps.gameState == 1;
    }

    @Override
    public void execute() {
        RSInterfaceMaster masterInterface = Interfaces.get(558);

        if (InterfaceHandler.interfaceContainsText(Interfaces.get(263, 1, 0), "Choosing a Display Name") && Interfaces.isInterfaceSubstantiated(masterInterface)) {
            InterfaceHandler.clickInterface(Interfaces.get(558, 17, 9));
        }


        // Being asked to enter name
        if (Interfaces.isInterfaceSubstantiated(162,44)) {
            Keyboard.typeString("SirEatsAlot");
            Keyboard.pressEnter();
            General.sleep(General.random(500,1000),General.random(2000,3000));
        }

        // Being asked to select a recommended name text is available, then randomly choose one of the 3 suggested names
        if (Interfaces.isInterfaceSubstantiated(masterInterface) && InterfaceHandler.interfaceContainsText(masterInterface.getChild(12), "Sorry, this display name is <col=ff0000>not available</col>")) {
            switch (General.random(0, 2)) {
                case 0:
                    InterfaceHandler.clickInterface(masterInterface.getChild(14));
                    break;
                case 1:
                    InterfaceHandler.clickInterface(masterInterface.getChild(15));
                    break;
                case 2:
                    InterfaceHandler.clickInterface(masterInterface.getChild(16));
                    break;
            }
        }

        // If display name is available, click the set name button
        if (Interfaces.isInterfaceSubstantiated(masterInterface) && InterfaceHandler.interfaceContainsText(masterInterface.getChild(12), "Great! This display name is")) {
            InterfaceHandler.clickInterface(masterInterface.getChild(18));
        }

        // Selecting character screen and then press accept
        if (InterfaceHandler.interfaceContainsText(Interfaces.get(263, 1,0), "Setting your appearance")) {
            final int[] RIGHT_ARROWS_CHILD_IDS = {113, 114, 115, 116, 117, 118, 119, 121, 127, 129, 130, 131};
            for (int i = 0; i < RIGHT_ARROWS_CHILD_IDS.length; i++) {
                for (int j = 0; j < General.random(1, 10); j++) {
                    InterfaceHandler.clickInterface(Interfaces.get(269, RIGHT_ARROWS_CHILD_IDS[i]));
                }
            }

            // Clicking the accept button
            InterfaceHandler.clickInterface(Interfaces.get(269,100));
        }
    }
}
