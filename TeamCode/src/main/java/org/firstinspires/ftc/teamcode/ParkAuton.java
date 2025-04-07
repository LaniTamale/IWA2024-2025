package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@Autonomous(name="ParkAutonRR", group="LinearOpMode")
public class ParkAuton extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        // Initialize robot hardware
        Robot robot = new Robot(hardwareMap);

        Pose2d initialPose = new Pose2d(12, -55, Math.toRadians(180));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        // Wait for start
        telemetry.addData("Status", "Waiting for start");
        telemetry.update();
        waitForStart();
        runtime.reset();

        // Park in observation zone
        telemetry.addData("Step", "Parking");
        telemetry.update();

        TrajectoryActionBuilder moveToDropOff = drive.actionBuilder(initialPose)
                .strafeTo(new Vector2d(38, -55));

        // Execute the trajectory
        Actions.runBlocking(moveToDropOff.build());

        telemetry.addData("Status", "Autonomous Complete");
        telemetry.update();
    }
}
