package scripts.FirstSteps;
import org.tribot.api.General;
import org.tribot.api2007.Game;
import org.tribot.api2007.Login;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;

import org.tribot.script.interfaces.MessageListening07;
import scripts.FirstSteps.API.Node;
import scripts.FirstSteps.Nodes.AGielinorGuide.CreateCharacter;
import scripts.FirstSteps.Nodes.AGielinorGuide.TalkToGielinorGuide;
import scripts.FirstSteps.Nodes.BSurvivalExpert.SurvivalExpert;
import scripts.FirstSteps.Nodes.CMasterChef.MasterChef;
import scripts.FirstSteps.Nodes.DQuestGuide.QuestGuide;
import scripts.FirstSteps.Nodes.EMiningInstructor.MiningInstructor;
import scripts.FirstSteps.Nodes.FCombatArea.CombatArea;
import scripts.FirstSteps.Nodes.GAccountGuide.AccountGuide;
import scripts.FirstSteps.Nodes.HBrotherBrace.BrotherBrace;
import scripts.FirstSteps.Nodes.IWizardHouse.WizardHouse;
import scripts.FirstSteps.Nodes.XTradeDistributor.TradeDistributor;
import scripts.FirstSteps.Nodes.LoginUser;
import scripts.FirstSteps.Nodes.Traversal;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.dax_api.api_lib.models.DaxCredentials;
import scripts.dax_api.api_lib.models.DaxCredentialsProvider;

import java.util.ArrayList;
import java.util.Collections;

@ScriptManifest(
        authors = {"dzzl"},
        category = "Quests",
        name = "First Steps V0.1",
        gameMode = 1,
        description = "Extension of Tutorial Island Bot by ThatWebDevKid. Trades a mule for a bond after account creation.",
        version = (1.1)
)

public class FirstSteps extends Script {
    public static ArrayList<Node> Nodes = new ArrayList<>();
    public static int gameState = Game.getSetting(281);
    //public static boolean tutorialIslandComplete = false;
    //public static int gameState = 1;

    @Override
    public void run() {
        DaxWalker.setCredentials(new DaxCredentialsProvider() {
            @Override
            public DaxCredentials getDaxCredentials() {
                return new DaxCredentials("sub_DPjXXzL5DeSiPf", "PUBLIC-KEY");
            }
        });

        General.println("RUN GAMESTATE: " + gameState);
        Collections.addAll(
                Nodes,
                /*
                new LoginUser(),
                new CreateCharacter(),
                new Traversal(),
                new TalkToGielinorGuide(),
                new SurvivalExpert(),
                new MasterChef(),
                new QuestGuide(),
                new MiningInstructor(),
                new CombatArea(),
                new AccountGuide(),
                new BrotherBrace(),
                new WizardHouse(),

                 */
                new Traversal(),
                new TradeDistributor() //Get bond + gp, activate bond
                //Switch to P2P, traverse to GE, purchase items to make money
                //Begin money making
        );

        //gameState = 1000;

        while (true) {
            loop();
            if (gameState == 20000) {
                General.println("FINISHED! LOGGING YOU OUT NOW :)");
                Login.logout();
                break;
            }
        }
    }

    private void loop() {
        //gameState = 1000; //init at 1000 and see where it goes

        for (final Node node: Nodes) {
            General.println("CHECKIGN NODE: ");
            node.printStatus();

            /* hard code as 1000 for now to test out TradeMule node.
                Constants.MULE_STATES = {1000}

                Traversal to mule initiates at game state 1000

                try incrementing gameState manually for testing
             */
            gameState = Game.getSetting(281); //need documentation
             //seems to be when you get off tutorial island?
            General.println("GAME STATE: " + gameState);
            if (node.validate()) {
                node.printStatus();
                node.execute();
                General.sleep(General.random(2500, 3000));
            }
        }
    }
}
