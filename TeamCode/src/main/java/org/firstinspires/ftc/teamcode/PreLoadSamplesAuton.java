package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "PreLoadSamplesAuton", group = "LinearOpMode")
// Autonomous code to hang a specimen on the tall rung and push samples.
public class PreLoadSamplesAuton extends LinearOpMode {
    // Timer for tracking runtime

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

        // Execute autonomous steps
        logAndExecute("Raising front slide and toggling claw",
                () -> robot.frontSlideToPosition(17, 1.0, true));

        logAndExecute("Driving right",
                () -> robot.driveToPosition(34, 0, 0.7, true));

        logAndExecute("Hanging specimen", () -> {
            robot.frontSlideToPosition(12, 1.0, true);
            robot.miniClawServo.setPosition(robot.miniClawOpenPos);
        });

        logAndExecute("Reversing",
                () -> robot.driveToPosition(-15, 0, 0.5, true));

        // Push samples
        pushSamples(robot);

        // Completion message
        telemetry.addData("Step", "Autonomous Complete");
        telemetry.update();
    }
    public void logAndExecute(String step, Runnable action) {
        telemetry.addData("Step", step);
        telemetry.update();
        action.run();
    }
    public void pushSamples(Robot robot) {
        // Push Sample #1
        logAndExecute("Push Sample #1", () -> {
            robot.driveToPosition(0, -33, 0.5, true);
            robot.driveToPosition(34, 0, 0.5, true);
            robot.driveToPosition(0, -12, 0.5, true);
            robot.driveToPosition(-38, 0, 0.5, true);
        });

        // Push Sample #2
        logAndExecute("Push Sample #2", () -> {
            robot.driveToPosition(36, 0, 0.5, true);
            robot.driveToPosition(0, -10, 0.5, true);
            robot.driveToPosition(-36, 0, 0.5, true);
        });

        // Push Sample #3
        logAndExecute("Push Sample #3", () -> {
            robot.driveToPosition(36, 0, 0.5, true);
            robot.driveToPosition(0, -5, 0.5, true);
            robot.driveToPosition(-36, 0, 0.5, true);
        });
    }
}
