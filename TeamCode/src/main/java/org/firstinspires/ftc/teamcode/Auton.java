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

    // Method to strafe right autonomously
    public void strafeRightAuto(Robot robot){
        robot.driveToPosition(48, 0, 0.2, telemetry);
    }
    // Method to score the preload object
    public void scorePreload(Robot robot){
        robot.driveToPosition(0, 8, 0.2, telemetry);
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
        waitForStart();
        runtime.reset();

        if (opModeIsActive()) {
            // Test case 1: Drive right (48 inches)
            telemetry.addData("Step", "Driving right");
            telemetry.update();
            strafeRightAuto(robot);

            // Test case 2: Score preload object
            telemetry.addData("Step", "Scoring preload");
            telemetry.update();
            scorePreload(robot);

            // Optional: Pause to simulate hanging the specimen
            telemetry.addData("Step", "Hanging specimen");
            telemetry.update();
            sleepWithTelemetry(2000, "Hanging on tall rung");

            telemetry.addData("Step", "Autonomous Complete");
            telemetry.update();
        }
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
}