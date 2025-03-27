package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@Autonomous(name="TestAuton2RR", group="LinearOpMode")
public class TestAuton2RR extends LinearOpMode {

    @Override
    public void runOpMode() {
        // Initialize robot hardware
        Robot robot = new Robot(hardwareMap);

        Pose2d initialPose = new Pose2d(12, -60, Math.toRadians(180));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        // Ensure servo is properly mapped
        if (robot.miniClawServo != null) {
            robot.miniClawServo.setPosition(robot.miniClawClosePos);
        } else {
            telemetry.addData("Error", "miniClawServo not found!");
            telemetry.update();
        }

        waitForStart();

        // Define trajectory actions
        TrajectoryActionBuilder moveToDropOff = drive.actionBuilder(initialPose)
                .strafeTo(new Vector2d(2, -48));

        TrajectoryActionBuilder moveToPickup = drive.actionBuilder(new Pose2d(2, -48, Math.toRadians(180)))
                .strafeTo(new Vector2d(23, -48))
                .strafeToSplineHeading(new Vector2d(45, -12), Math.toRadians(0))
                .strafeToSplineHeading(new Vector2d(45, -53), Math.toRadians(0))
                .strafeToSplineHeading(new Vector2d(49, -12), Math.toRadians(0));

        TrajectoryActionBuilder returnToDropOff = drive.actionBuilder(new Pose2d(35, -60, Math.toRadians(0)))
                .strafeToSplineHeading(new Vector2d(5, -32), Math.toRadians(180));

        logAndExecute("Deposit first pre-load", () -> {
            robot.vertSlideToPosition(17, 1.0, false);
            Actions.runBlocking(new SequentialAction(moveToDropOff.build()));
            robot.vertSlideToPosition(12, 1.0, true);
            robot.miniClawServo.setPosition(robot.miniClawOpenPos);
        });

        logAndExecute("Push blocks", () ->
                Actions.runBlocking(new SequentialAction(moveToPickup.build()))
        );

        logAndExecute("Score deposit 1", () -> {
            robot.miniClawServo.setPosition(robot.miniClawClosePos);
            robot.vertSlideToPosition(17, 1.0, false);
            sleep(1000);
            Actions.runBlocking(new SequentialAction(returnToDropOff.build()));
            robot.vertSlideToPosition(12, 1.0, false);
            robot.miniClawServo.setPosition(robot.miniClawOpenPos);
        });

        telemetry.addData("Status", "Autonomous Complete");
        telemetry.update();
    }

    private void logAndExecute(String step, Runnable action) {
        telemetry.addData("Step", step);
        telemetry.update();
        action.run();
        telemetry.addData("Completed", step);
        telemetry.update();
    }
}
