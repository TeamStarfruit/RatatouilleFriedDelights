package org.starfruit.ratatouillefrieddelights.ponder;

import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class ConcreteNumberFortyTwoScene {
    public static void concrete(SceneBuilder builder, SceneBuildingUtil util) {
        BlockPos mainCharacterPos = util.grid().at(5, 3, 4);
        BlockPos firstCharacterPos = util.grid().at(1, 3, 2);
        BlockPos secondCharacterPos = util.grid().at(2, 3, 4);

        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.scaleSceneView(.5f);

        scene.title("compost_tower", "Turn organic waste into compost");
        scene.configureBasePlate(0, 0, 5);
        scene.rotateCameraY(150);
        scene.world().showSection(util.select().everywhere(), Direction.UP);

        scene.overlay().showText(50)
                .text("Irrigation towers maintain optimal moisture levels for farmland within their range")
                .placeNearTarget()
                .attachKeyFrame()
                .pointAt(util.vector().topOf(mainCharacterPos));
        scene.idle(60);
        scene.addKeyframe();

        scene.overlay().showText(50)
                .text("Irrigation towers maintain optimal moisture levels for farmland within their range")
                .placeNearTarget()
                .attachKeyFrame()
                .pointAt(util.vector().topOf(firstCharacterPos));
        scene.idle(60);
        scene.addKeyframe();

        scene.overlay().showText(50)
                .text("Irrigation towers maintain optimal moisture levels for farmland within their range")
                .placeNearTarget()
                .attachKeyFrame()
                .pointAt(util.vector().topOf(secondCharacterPos));
        scene.idle(60);
        scene.addKeyframe();
    }
}
