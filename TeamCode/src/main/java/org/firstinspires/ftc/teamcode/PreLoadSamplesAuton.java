package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "PreLoadSamplesAuton", group = "LinearOpMode")
// Autonomous code to hang a specimen on the tall rung and push samples.
public class PreLoadSamplesAuton extends LinearOpMode {
    // Timer for tracking runtime
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

        // Execute autonomous steps
        logAndExecute("Raising front slide and toggling claw",
                () -> robot.frontSlideToPosition(18, 0.5, true));

        logAndExecute("Driving left",
                () -> robot.driveToPosition(31, 0, 0.5, true));

        logAndExecute("Hanging specimen", () -> {
            robot.frontSlideToPosition(12, 0.5, true);
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

    /**
     * Helper method for telemetry logging and executing a specific action.
     *
     * @param step Description of the step being executed.
     * @param action The runnable action to perform for this step.
     */
    public void logAndExecute(String step, Runnable action) {
        telemetry.addData("Step", step);
        telemetry.update();
        action.run();
    }

    /**
     * Executes the steps for pushing all samples.
     *
     * @param robot The robot instance to use for driving and movement.
     */
    public void pushSamples(Robot robot) {
        // Push Sample #1
        logAndExecute("Push Sample #1", () -> {
            robot.driveToPosition(0, -30, 0.5, true);
            robot.driveToPosition(50, 0, 0.5, true);
            robot.driveToPosition(0, -9, 0.5, true);
            robot.driveToPosition(-36, 0, 0.5, true);
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
