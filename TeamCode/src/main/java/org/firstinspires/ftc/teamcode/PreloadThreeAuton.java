package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@Autonomous(name="PreloadThreeAuton", group="LinearOpMode")

public class PreloadThreeAuton extends LinearOpMode {
    public void runOpMode() {
        Robot robot = new Robot(hardwareMap);
        Pose2d initialPose = new Pose2d(12, -60, Math.toRadians(180));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);
        robot.miniClawServo.setPosition(robot.miniClawClosePos);

        waitForStart();
        // Move to score pre loaded first specimen
        TrajectoryActionBuilder moveToDropOff = drive.actionBuilder(initialPose)
                .strafeTo(new Vector2d(-1, -28));


        // Move to observation for first human
        // First move to the pickup point
        TrajectoryActionBuilder moveToPickup = moveToDropOff.endTrajectory().fresh()
                .strafeToSplineHeading(new Vector2d(40, -61), Math.toRadians(0));

        //Scoring Obseravtion specimen
        TrajectoryActionBuilder returnToSub0 = moveToPickup.endTrajectory().fresh()
		// add or subtract small rotation to force a direction
                .strafeToSplineHeading(new Vector2d(2, -28), Math.toRadians(180));
        //Travel around sub to Samples 1
        TrajectoryActionBuilder returnToSub1 = returnToSub0.endTrajectory().fresh()
                .strafeToSplineHeading(new Vector2d(29   ,-40), Math.toRadians(0));
        //Travel behind Samples 1
        TrajectoryActionBuilder returnToSub2 = returnToSub1.endTrajectory().fresh()
                .strafeToSplineHeading(new Vector2d(43, 0), Math.toRadians(0));
        //Stop for human to prep sample
        TrajectoryActionBuilder returnToSub3 = returnToSub2.endTrajectory().fresh()
                .strafeToSplineHeading(new Vector2d(43,-52), Math.toRadians(0));
        //Retreat Sample 1 to observation
        TrajectoryActionBuilder returnToSub4 = returnToSub3.endTrajectory().fresh()
                .strafeToSplineHeading(new Vector2d(43,-48), Math.toRadians(0));
        //Sample 1 to observation for human
        TrajectoryActionBuilder returnToSub5 = returnToSub4.endTrajectory().fresh()
                .strafeToSplineHeading(new Vector2d(43,-61), Math.toRadians(0));
        //Scoring specimen sample 1
        TrajectoryActionBuilder returnToSub6 = returnToSub5.endTrajectory().fresh()
                .strafeToSplineHeading(new Vector2d(3,-28), Math.toRadians(180));
        //Parking
        TrajectoryActionBuilder returnToSub7 = returnToSub6.endTrajectory().fresh()
                .strafeToSplineHeading(new Vector2d(43,-60), Math.toRadians(0));


        logAndExecute("Score Preload", () -> {
            robot.vertSlideToPosition(17, 1.0, false);
            Actions.runBlocking(new SequentialAction(moveToDropOff.build()));
            robot.vertSlideToPosition(12, 1.0, true);
            robot.miniClawServo.setPosition(robot.miniClawOpenPos);
            Actions.runBlocking(new SequentialAction(moveToPickup.build()));
            robot.vertSlideToPosition(0, 1.0, true);
            sleep(1000);
            robot.miniClawServo.setPosition(robot.miniClawClosePos);
        });

        logAndExecute("Score Observation Specimen", () -> {
            robot.vertSlideToPosition(17, 1.0, false);
            Actions.runBlocking(new SequentialAction(returnToSub0.build()));
            robot.vertSlideToPosition(12, 1.0, false);
            sleep(500);
            robot.miniClawServo.setPosition(robot.miniClawOpenPos);
        });

        logAndExecute("Pushing Sample 1 and Pause/Retreat", () -> {
            Actions.runBlocking(new SequentialAction(returnToSub1.build()));
            Actions.runBlocking(new SequentialAction(returnToSub2.build()));
            Actions.runBlocking(new SequentialAction(returnToSub3.build()));
            Actions.runBlocking(new SequentialAction(returnToSub4.build()));
            sleep(2500);
            Actions.runBlocking(new SequentialAction(returnToSub5.build()));
            robot.vertSlideToPosition(0, 1.0, false);
            sleep(1000);
            robot.miniClawServo.setPosition(robot.miniClawClosePos);
        });

        logAndExecute("Score Sample 1", () -> {
            robot.vertSlideToPosition(17, 1.0, false);
            Actions.runBlocking(new SequentialAction(returnToSub6.build()));
            robot.vertSlideToPosition(12, 1.0, false);
            sleep(500);
            robot.miniClawServo.setPosition(robot.miniClawOpenPos);
        });

        logAndExecute("Parking", () -> {
            Actions.runBlocking(new SequentialAction(returnToSub7.build()));
        });

        telemetry.addData("Status", "Autonomous Complete");
        telemetry.update();
    }

public void logAndExecute(String step, Runnable action) {
    telemetry.addData("Step", step);
    telemetry.update();
    action.run();
}
}
