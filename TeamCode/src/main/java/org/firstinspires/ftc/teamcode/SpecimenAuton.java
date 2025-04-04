package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@Autonomous(name="SpecimenAuton", group="LinearOpMode")
// test autonomous code
public class SpecimenAuton extends LinearOpMode {
    public void runOpMode() {
        Robot robot = new Robot(hardwareMap);
        Pose2d initialPose = new Pose2d(12, -60, Math.toRadians(180));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);
        robot.miniClawServo.setPosition(robot.miniClawClosePos);

        waitForStart();
        // Move to score first specimen
        TrajectoryActionBuilder moveToDropOff = drive.actionBuilder(initialPose)
                .strafeTo(new Vector2d(0, -28));
        // Push Block to drop off
        TrajectoryActionBuilder moveToPickup = moveToDropOff.endTrajectory().fresh()
                .strafeTo(new Vector2d(23, -42))
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
                .strafeToSplineHeading(new Vector2d(35, -60), Math.toRadians(0));
        // Return to drop-off
        TrajectoryActionBuilder returnToDropOff = moveToPickup.endTrajectory().fresh()
                .strafeToSplineHeading(new Vector2d(5,-32), Math.toRadians(180));
        //back for human specimen score
        TrajectoryActionBuilder returnToSub0 = returnToDropOff.endTrajectory().fresh()
                .strafeToSplineHeading(new Vector2d(35, -60), Math.toRadians(0));
        TrajectoryActionBuilder returnToSub1 = returnToSub0.endTrajectory().fresh()
                .strafeToSplineHeading(new Vector2d(5,-32), Math.toRadians(180));
        //specimen score 1
        TrajectoryActionBuilder returnToSub2 = returnToSub1.endTrajectory().fresh()
                .strafeToSplineHeading(new Vector2d(35, -60), Math.toRadians(0));
        TrajectoryActionBuilder returnToSub3 = returnToSub2.endTrajectory().fresh()
                .strafeToSplineHeading(new Vector2d(5,-32), Math.toRadians(180));

        //specimen score 2
        TrajectoryActionBuilder returnToSub4 = returnToSub3.endTrajectory().fresh()
                .strafeToSplineHeading(new Vector2d(35, -60), Math.toRadians(0));
        TrajectoryActionBuilder returnToSub5 = returnToSub4.endTrajectory().fresh()
                .strafeToSplineHeading(new Vector2d(5,-32), Math.toRadians(180));


        logAndExecute("Deposit first pre load", () -> {
            robot.vertSlideToPosition(17, 1.0, false);
            Actions.runBlocking(new SequentialAction(moveToDropOff.build()));
            robot.vertSlideToPosition(12, 1.0, true);
            robot.miniClawServo.setPosition(robot.miniClawOpenPos);
        });

        logAndExecute("Push blocks", () -> Actions.runBlocking(new SequentialAction(moveToPickup.build())));

        logAndExecute("score deposit 1", () -> {
            robot.miniClawServo.setPosition(robot.miniClawClosePos);
            robot.vertSlideToPosition(17, 1.0, false);
            sleep(1000);
            Actions.runBlocking(new SequentialAction(returnToDropOff.build()));
            robot.vertSlideToPosition(12, 1.0, false);
            robot.miniClawServo.setPosition(robot.miniClawOpenPos);
        });

        logAndExecute("score deposit 2", () -> {
            Actions.runBlocking(new SequentialAction(returnToSub0.build()));
            robot.miniClawServo.setPosition(robot.miniClawClosePos);
            robot.vertSlideToPosition(17, 1.0, false);
            sleep(1000);
            Actions.runBlocking(new SequentialAction(returnToSub1.build()));
            robot.vertSlideToPosition(12, 1.0, false);
            robot.miniClawServo.setPosition(robot.miniClawOpenPos);
        });

        logAndExecute("score deposit 3", () -> {
            Actions.runBlocking(new SequentialAction(returnToSub2.build()));
            robot.miniClawServo.setPosition(robot.miniClawClosePos);
            robot.vertSlideToPosition(17, 1.0, false);
            sleep(1000);
            Actions.runBlocking(new SequentialAction(returnToSub3.build()));
            robot.vertSlideToPosition(12, 1.0, false);
            robot.miniClawServo.setPosition(robot.miniClawOpenPos);
        });

        logAndExecute("score deposit 4", () -> {
            Actions.runBlocking(new SequentialAction(returnToSub4.build()));
            robot.miniClawServo.setPosition(robot.miniClawClosePos);
            robot.vertSlideToPosition(17, 1.0, false);
            sleep(1000);
            Actions.runBlocking(new SequentialAction(returnToSub5.build()));
            robot.vertSlideToPosition(12, 1.0, false);
            robot.miniClawServo.setPosition(robot.miniClawOpenPos);
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
