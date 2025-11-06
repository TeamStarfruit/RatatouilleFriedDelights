package org.starfruit.ratatouillefrieddelights.ponder;

import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Items;
import org.starfruit.ratatouillefrieddelights.entry.RFDItems;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.Random;

public class ConcreteNumberFortyTwoScene {
    public static void concrete(SceneBuilder builder, SceneBuildingUtil util) {
        BlockPos mainCharacterPos = util.grid().at(5, 3, 4);
        BlockPos secondCharacterPos = util.grid().at(1, 3, 2);
        BlockPos firstCharacterPos = util.grid().at(2, 3, 4);
        Random random = new Random();

        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.scaleSceneView(.75f);

        scene.title("compost_tower", "Pasta should be mixed with No. 42 concrete");
        scene.configureBasePlate(0, 0, 9);

        scene.world().showSection(util.select().everywhere(), Direction.UP);

        scene.overlay().showText(50)
                .text("Let me first ask you a question.")
                .placeNearTarget()
                .attachKeyFrame()
                .pointAt(util.vector().topOf(mainCharacterPos));
        scene.idle(60);
        scene.overlay().showText(50)
                .text("When is the last time you showered?")
                .placeNearTarget()
                .pointAt(util.vector().topOf(mainCharacterPos));
        scene.idle(60);

        scene.rotateCameraY(150);

        scene.overlay().showText(50)
                .text("Bikini, beautiful not beautiful?")
                .placeNearTarget()
                .pointAt(util.vector().topOf(firstCharacterPos));
        scene.idle(60);
        scene.rotateCameraY(-150);
        scene.overlay().showText(50)
                .text("Answer correct.")
                .placeNearTarget()
                .pointAt(util.vector().topOf(mainCharacterPos));
        scene.idle(60);
        scene.overlay().showText(50)
                .text("Is there aliens in Kalgan?")
                .placeNearTarget()
                .attachKeyFrame()
                .pointAt(util.vector().topOf(mainCharacterPos));
        scene.idle(60);
        scene.rotateCameraY(180);
        scene.overlay().showText(50)
                .text("Smoking is bad for your health")
                .placeNearTarget()
                .pointAt(util.vector().topOf(firstCharacterPos));
        scene.idle(60);
        scene.rotateCameraY(-180);
        scene.overlay().showText(50)
                .text("Answer correct.")
                .placeNearTarget()
                .pointAt(util.vector().topOf(mainCharacterPos));
        scene.idle(60);

        scene.rotateCameraY(150);
        scene.overlay().showText(50)
                .text("If these answers get him cigarettes I'm not filming this anymore")
                .placeNearTarget()
                .attachKeyFrame()
                .pointAt(util.vector().topOf(secondCharacterPos));
        scene.idle(60);
        scene.overlay().showText(50)
                .text("I'm not filming this anymore, you two go film")
                .placeNearTarget()
                .pointAt(util.vector().topOf(secondCharacterPos));
        scene.idle(60);

        scene.overlay().showText(50)
                .text("For his opinion, I beg to differ")
                .placeNearTarget()
                .attachKeyFrame()
                .pointAt(util.vector().topOf(secondCharacterPos));
        scene.idle(60);

        scene.overlay().showText(50)
                .text("Then you say")
                .placeNearTarget()
                .pointAt(util.vector().topOf(mainCharacterPos));
        scene.idle(60);

        scene.rotateCameraY(random.nextInt(360) - 180);

        scene.overlay().showText(50)
                .text("I personally believe, this, pasta, need to be mixed with Number 42 concrete")
                .placeNearTarget()
                .attachKeyFrame()
                .pointAt(util.vector().topOf(secondCharacterPos));
        scene.overlay().showControls(util.vector().topOf(secondCharacterPos), Pointing.DOWN, 70).rightClick()
                .withItem(RFDItems.NO42_CONCRETE_MIXING_PASTA.asStack());
        scene.idle(60);

        scene.rotateCameraY(random.nextInt(360) - 180);

        scene.overlay().showText(50)
                .text("For the reason being that the length of this screw ")
                .placeNearTarget()
                .pointAt(util.vector().topOf(secondCharacterPos));
        scene.idle(60);

        scene.rotateCameraY(random.nextInt(360) - 180);

        scene.overlay().showText(50)
                .text("can easily directly impact the torque of this excavator.")
                .placeNearTarget()
                .pointAt(util.vector().topOf(secondCharacterPos));
        scene.overlay().showControls(util.vector().topOf(mainCharacterPos), Pointing.DOWN, 70).rightClick()
                .withItem(RFDItems.NO42_CONCRETE_MIXING_PASTA.asStack());
        scene.idle(60);

        scene.rotateCameraY(random.nextInt(360) - 180);


        scene.overlay().showText(50)
                .text("You know")
                .placeNearTarget()
                .pointAt(util.vector().topOf(secondCharacterPos));
        scene.idle(60);

        scene.rotateCameraY(random.nextInt(360) - 180);

        scene.overlay().showText(50)
                .text("When the excavator punch it in,")
                .placeNearTarget()
                .pointAt(util.vector().topOf(secondCharacterPos));
        scene.idle(60);

        scene.rotateCameraY(random.nextInt(360) - 180);

        scene.overlay().showText(50)
                .text("At that exact moment...")
                .placeNearTarget()
                .pointAt(util.vector().topOf(secondCharacterPos));
        scene.overlay().showControls(util.vector().topOf(firstCharacterPos), Pointing.DOWN, 70).rightClick()
                .withItem(RFDItems.NO42_CONCRETE_MIXING_PASTA.asStack());
        scene.idle(60);

        scene.rotateCameraY(random.nextInt(360) - 180);

        scene.overlay().showText(50)
                .text("It will generate, a large amount of...")
                .placeNearTarget()
                .pointAt(util.vector().topOf(secondCharacterPos));
        scene.overlay().showControls(util.vector().topOf(secondCharacterPos), Pointing.DOWN, 80).rightClick()
                .withItem(ModItems.RAW_PASTA.get().getDefaultInstance());
        scene.idle(60);

        scene.rotateCameraY(random.nextInt(360) - 180);

        scene.overlay().showText(50)
                .text("Dynein! Or most commonly called?")
                .placeNearTarget()
                .pointAt(util.vector().topOf(secondCharacterPos));
        scene.idle(60);

        scene.rotateCameraY(random.nextInt(360) - 180);

        scene.overlay().showText(50)
                .text("UFO... This will greatly affect the progression of the economy")
                .placeNearTarget()
                .pointAt(util.vector().topOf(secondCharacterPos));
        scene.overlay().showControls(util.vector().topOf(mainCharacterPos), Pointing.DOWN, 80).rightClick()
                .withItem(Items.CYAN_CONCRETE.getDefaultInstance());
        scene.idle(60);

        scene.rotateCameraY(random.nextInt(360) - 180);

        scene.overlay().showText(50)
                .text("It will even cause a surprising amount of this...")
                .placeNearTarget()
                .pointAt(util.vector().topOf(secondCharacterPos));
        scene.idle(60);

        scene.rotateCameraY(random.nextInt(360) - 180);

        scene.overlay().showText(50)
                .text("nuclear contamination for the entire pacific ocean and electric chargers")
                .placeNearTarget()
                .pointAt(util.vector().topOf(secondCharacterPos));
        scene.idle(60);

        scene.rotateCameraY(random.nextInt(360) - 180);

        scene.overlay().showText(50)
                .text("You know...")
                .placeNearTarget()
                .pointAt(util.vector().topOf(secondCharacterPos));
        scene.idle(60);

        scene.rotateCameraY(random.nextInt(360) - 180);

        scene.overlay().showText(50)
                .text("Other than that, according to this...")
                .placeNearTarget()
                .attachKeyFrame()
                .pointAt(util.vector().topOf(secondCharacterPos));
        scene.idle(60);

        scene.rotateCameraY(random.nextInt(360) - 180);

        scene.overlay().showText(50)
                .text("Pythagorean theorem, you can easily infer that...")
                .placeNearTarget()
                .pointAt(util.vector().topOf(secondCharacterPos));
        scene.idle(60);

        scene.rotateCameraY(random.nextInt(360) - 180);

        scene.overlay().showText(50)
                .text("This captive bred Hi\"ducky\" Tōjō... ")
                .placeNearTarget()
                .pointAt(util.vector().topOf(secondCharacterPos));
        scene.overlay().showControls(util.vector().topOf(secondCharacterPos), Pointing.DOWN, 80).rightClick()
                .withItem(Items.YELLOW_CONCRETE.getDefaultInstance());
        scene.idle(60);

        scene.rotateCameraY(random.nextInt(360) - 180);

        scene.overlay().showText(50)
                .text("will be able to capture natural...")
                .placeNearTarget()
                .pointAt(util.vector().topOf(secondCharacterPos));
        scene.idle(60);

        scene.rotateCameraY(random.nextInt(360) - 180);

        scene.overlay().showText(50)
                .text("Trigonometric Functions, so this-this-th-is-this...")
                .placeNearTarget()
                .pointAt(util.vector().topOf(secondCharacterPos));

        scene.overlay().showControls(util.vector().topOf(mainCharacterPos), Pointing.DOWN, 80).rightClick()
                .withItem(Items.BLUE_CONCRETE.getDefaultInstance());
        scene.idle(60);

        scene.rotateCameraY(random.nextInt(360) - 180);

        scene.overlay().showText(50)
                .text("whether this cross-section of this Emperor Qin is this radioactive,")
                .placeNearTarget()
                .pointAt(util.vector().topOf(secondCharacterPos));
        scene.idle(60);

        scene.rotateCameraY(random.nextInt(360) - 180);

        scene.overlay().showText(50)
                .text("or whether the Donald Trump's Nth exponent have any this precipitate in the beaker,")
                .placeNearTarget()
                .pointAt(util.vector().topOf(secondCharacterPos));
        scene.idle(60);
        scene.overlay().showControls(util.vector().topOf(firstCharacterPos), Pointing.DOWN, 80).rightClick()
                .withItem(ModItems.RAW_PASTA.get().getDefaultInstance());

        scene.rotateCameraY(random.nextInt(360) - 180);

        scene.overlay().showText(50)
                .text("it will not affect the meetup between Walmart and Convair in the-the Antarctica")
                .placeNearTarget()
                .pointAt(util.vector().topOf(secondCharacterPos));
        scene.idle(60);

        scene.rotateCameraY(random.nextInt(360) - 180);

        scene.overlay().showText(50)
                .text("What????")
                .placeNearTarget()
                .attachKeyFrame()
                .pointAt(util.vector().topOf(secondCharacterPos));

        scene.idle(60);
        scene.overlay().showControls(util.vector().topOf(mainCharacterPos), Pointing.DOWN, 50).rightClick()
                .withItem(ModItems.RAW_PASTA.get().getDefaultInstance());
        scene.overlay().showControls(util.vector().topOf(firstCharacterPos), Pointing.DOWN, 40).rightClick()
                .withItem(Items.WHITE_CONCRETE.getDefaultInstance());
        scene.overlay().showControls(util.vector().topOf(secondCharacterPos), Pointing.DOWN, 70).rightClick()
                .withItem(RFDItems.NO42_CONCRETE_MIXING_PASTA.asStack());

        scene.rotateCameraY(2160);


    }
}
