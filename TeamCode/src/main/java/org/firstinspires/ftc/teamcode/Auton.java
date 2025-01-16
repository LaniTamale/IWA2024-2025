package org.firstinspires.ftc.teamcode;

import static java.lang.Thread.sleep;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="Auton", group="LinearOpMode")
// new autonomous code to hang a specimen on the tall rung.
public class Auton extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();

    // Method to strafe right autonomously "original emergency park"
    public void strafeRightAuto(Robot robot){
        robot.driveToPosition(48, 0, 0.2, telemetry);
    }
    // Method to score the preload object
    public void scorePreload(Robot robot){
        robot.driveToPosition(0, 0, 0.2, telemetry);
    }
    // Method to add a pause with telemetry feedback
    private void sleepWithTelemetry(long millis, String message) {
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < millis && opModeIsActive()) {
            telemetry.addData("Pause", message);
            telemetry.update();
        }
    }

    @Override
    public void runOpMode() {
        // Initialize robot hardware
        Robot robot = new Robot(hardwareMap);

        // Wait for the start signal
        telemetry.addData("Status", "Waiting for start");
        telemetry.update();
        waitForStart();
        runtime.reset();

        if (opModeIsActive()) {

        }
            // Step 1: Initialize mini claw closed for pre load
            robot.miniClawServo.setPosition(robot.miniClawClosePos);

            // Step 2: Raise the front slide to max height and operate mini claw
            telemetry.addData("Step", "Raising front slide and toggling claw");
            telemetry.update();

            telemetry.addData("Step", "front slide reaching high position");
            telemetry.update();
            robot.slideToPosition(robot.frontslide, robot.frontSlidMaxLen, 4, 0.5, true);

            // Step 3: Drive left (-33 inches)
            telemetry.addData("Step", "Driving left");
            telemetry.update();
            robot.driveToPosition(-33, 0, 0.4, telemetry);

            // Step 4:Score the preload object
            telemetry.addData("Step", "Hanging specimen");
            telemetry.update();
            //encoder target position
            sleepWithTelemetry(2000, "Hanging on tall rung");
            robot.miniClawServo.setPosition(robot.miniClawOpenPos);

            // Completion message
            telemetry.addData("Step", "Autonomous Complete");
            telemetry.update();
        }
    }

/*
    // Method to add a pause with telemetry feedback
    @Override
    public void runOpMode() {
        // Initialize robot hardware
        Robot robot = new Robot(hardwareMap);

        // Wait for the start signal
        waitForStart();
        runtime.reset();

        if (opModeIsActive()) {
            // Test case 1: Drive right (48 inches)
            telemetry.addData("Step", "Driving right");
            telemetry.update();

            scorePreload(robot);

            // drive forward
            // drive diagonally left backwards
            // robot.driveToPosition(-10, -10, 0.2, telemetry);

            /* Optional: Add a small pause between movements for clarity
            sleepWithTelemetry(1000, "Pause after step 1");

            // Test case 2: Drive forward (8 inches)
            telemetry.addData("Step", "Driving forward");
            telemetry.update();
            robot.driveToPosition(0, 8, 0.2, telemetry);

            sleepWithTelemetry(1000, "Pause after step 2");

            // Test case 3: Drive diagonally left backwards (-10, -10 inches)
            telemetry.addData("Step", "Driving diagonally left backwards");
            telemetry.update();
            robot.driveToPosition(-10, -10, 0.2, telemetry);

            sleepWithTelemetry(1000, "Pause after step 3");

            // Test case 4: Drive diagonally right forwards (10, 10 inches)
            telemetry.addData("Step", "Driving diagonally right forwards");
            telemetry.update();
            robot.driveToPosition(10, 10, 0.2, telemetry);

            telemetry.addData("Status", "Autonomous Complete");
            telemetry.update();*/

    /*
     * Pauses the program for a specified duration while providing telemetry feedback.
     * @param millis Duration of the pause in milliseconds.
     * @param message Message to display during the pause.

    private void sleepWithTelemetry(long millis, String message) {
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < millis && opModeIsActive()) {
            telemetry.addData("Pause", message);
            telemetry.update();
        }
    }
}  */