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
                new Vector2d(38, -14), new Vector2d(38, -59),
                new Vector2d(54, -14), new Vector2d(54, -59),
                new Vector2d(60, -14), new Vector2d(60, -59)
        };


        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(12, -60, Math.toRadians(180)))
                .strafeTo(new Vector2d(0, -32))
                .strafeTo(new Vector2d(23, -32))

                //Samples 1
                .strafeToSplineHeading(new Vector2d(45, -12), Math.toRadians(0))
                .strafeToSplineHeading(new Vector2d(45, -53), Math.toRadians(0))
                //Samples 2
                .strafeToSplineHeading(new Vector2d(49, -12), Math.toRadians(0))
                .strafeToSplineHeading(new Vector2d(51, -12), Math.toRadians(0))
                .strafeToSplineHeading(new Vector2d(51, -53), Math.toRadians(0))
                //Samples 3
                .strafeToSplineHeading(new Vector2d(58, -12), Math.toRadians(0))
                .strafeToSplineHeading(new Vector2d(61, -12), Math.toRadians(0))
                .strafeToSplineHeading(new Vector2d(61, -53), Math.toRadians(0))

                //back for specimen
                .strafeToSplineHeading(new Vector2d(35, -60), Math.toRadians(0))
                .strafeToSplineHeading(new Vector2d(5,-32), Math.toRadians(180))

                //specimen score 1
                .strafeToSplineHeading(new Vector2d(35, -60), Math.toRadians(0))
                .strafeToSplineHeading(new Vector2d(5,-32), Math.toRadians(180))

                //specimen score 2
                .strafeToSplineHeading(new Vector2d(35, -60), Math.toRadians(0))
                .strafeToSplineHeading(new Vector2d(5,-32), Math.toRadians(180))







                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}