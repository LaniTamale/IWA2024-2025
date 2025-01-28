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

        // Raise slide and lock miniClaw
        logAndExecute("Raising front slide and toggling claw", () -> {
            robot.frontSlideToPosition(18, 0.5, true);
        });

        // Drive left
        logAndExecute("Driving right", () -> {
            robot.driveToPosition(31, 0, 0.5, true);
        });

        // Score the preload object
        logAndExecute("Hanging specimen", () -> {
            robot.frontSlideToPosition(12, 0.5, true);
            robot.miniClawServo.setPosition(robot.miniClawOpenPos);
        });

        // Park
        logAndExecute("Parking", () -> {
            robot.driveToPosition(-28, 0, 0.5, true);
            robot.driveToPosition(0, -44, 0.5, true);
        });

        // Completion message
        logAndExecute("Autonomous Complete", () -> {});
    }

    // Public helper method to log a step and execute a Runnable
    public void logAndExecute(String step, Runnable action) {
        telemetry.addData("Step", step);
        telemetry.update();
        action.run();
    }
}