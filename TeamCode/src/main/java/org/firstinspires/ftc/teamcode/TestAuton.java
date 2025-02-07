package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@Autonomous(name="TestAuton", group="LinearOpMode")
// test autonomous code
public class TestAuton extends LinearOpMode {
    public void runOpMode() {
        Robot robot = new Robot(hardwareMap);
        Pose2d initialPose = new Pose2d(11.8, 61.7, Math.toRadians(90));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        Action trajectoryActionForward = drive.actionBuilder(initialPose)
                .lineToX(24)
                .build();

        waitForStart();

        Actions.runBlocking(
                new SequentialAction(
                        trajectoryActionForward
                )
        );

        robot.frontSlideToPosition(8, 0.5, true);
        boolean ret = robot.frontSlideToPosition(8, 0.5, true);
        telemetry.addData("return", String.valueOf(ret));
        telemetry.update();
        sleep(99999999);
    }
}
