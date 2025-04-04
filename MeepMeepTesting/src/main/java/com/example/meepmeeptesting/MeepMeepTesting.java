package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();
                Vector2d[] pushPositions = {
                new Vector2d(38, -12), new Vector2d(38, -59),
                new Vector2d(54, -12), new Vector2d(54, -59),
                new Vector2d(60, -12), new Vector2d(60, -59),
        };

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(12, -60, Math.toRadians(180)))
                //Score PreLoaded
                .strafeTo(new Vector2d(0, -28))

                //back to pick up Obseravtion specimen
                .strafeToSplineHeading(new Vector2d(40, -60),Math.toRadians(0))

                //Scoring Obseravtion specimen
                .strafeToSplineHeading(new Vector2d(0,-28), Math.toRadians(180))

                //Travel around sub to Samples 1
                .strafeToSplineHeading(new Vector2d(25, -40), Math.toRadians(0))

                //Travel behind Samples 1
                .strafeToSplineHeading(new Vector2d(41, 0), Math.toRadians(0))

                //Push Sample 1 to observation
                .strafeToSplineHeading(new Vector2d(41, -60), Math.toRadians(0))

                //Travel behind samples 2
                .strafeToSplineHeading(new Vector2d(50, 0), Math.toRadians(0))

                //Push Sample 2 to observation
                .strafeToSplineHeading(new Vector2d(50, -60), Math.toRadians(0))

                //Scoring specimen sample 2
                .strafeToSplineHeading(new Vector2d(0,-28), Math.toRadians(180))

                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();

    }
}