package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
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
                         .strafeTo(new Vector2d(-1, -28))
                //Scoring Obseravtion specimen
                // Move to observation for first human
                // First move to the pickup point
                        .strafeToSplineHeading(new Vector2d(40, -61), Math.toRadians(0))
                        // add or subtract small rotation to force a direction
                        .strafeToSplineHeading(new Vector2d(2, -28), Math.toRadians(180))
                //Travel around sub to Samples 1
                        .strafeToSplineHeading(new Vector2d(29   ,-40), Math.toRadians(0))
                //Travel behind Samples 1
                        .strafeToSplineHeading(new Vector2d(45, 0), Math.toRadians(0))
                //Stop for human to prep sample
                        .strafeToSplineHeading(new Vector2d(45,-52), Math.toRadians(0))
                //Sample 1 to observation for human load
                        .strafeToSplineHeading(new Vector2d(45, -61), Math.toRadians(0))
                //Scoring specimen sample 1
                        .strafeToSplineHeading(new Vector2d(4,-28), Math.toRadians(180))
                //Parking
                        .strafeToSplineHeading(new Vector2d(43,-60), Math.toRadians(0))
                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();

    }
}