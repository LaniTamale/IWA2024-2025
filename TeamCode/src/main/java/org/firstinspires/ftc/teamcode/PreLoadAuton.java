package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="PreLoadAuton", group="LinearOpMode")
// new autonomous code to hang a specimen on the tall rung.
public class PreLoadAuton extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        // Initialize robot hardware
        Robot robot = new Robot(hardwareMap);

        // Wait for the start signal
        telemetry.addData("Status", "Waiting for start");
        telemetry.update();
        robot.miniClawServo.setPosition(robot.miniClawClosePos);

        waitForStart();
        runtime.reset();

        // raise slide and lock miniClaw
        telemetry.addData("Step", "Raising front slide and toggling claw");
        telemetry.update();
        robot.frontSlideToPosition(18, 0.5, true);

        // drive left
        telemetry.addData("Step", "Driving left");
        telemetry.update();
        robot.driveToPosition(-31, 0, 0.5, true);

        // Score the preload object
        telemetry.addData("Step", "Hanging specimen");
        telemetry.update();
        robot.frontSlideToPosition(12, 0.5, true);
        robot.miniClawServo.setPosition(robot.miniClawOpenPos);

        // park
        telemetry.addData("Step", "Parking");
        telemetry.update();
        robot.driveToPosition(28, 0, 0.5, true);
        robot.driveToPosition(0, 44, 0.5, true);

        // Completion message
        telemetry.addData("Step", "Autonomous Complete");
        telemetry.update();
    }
}
