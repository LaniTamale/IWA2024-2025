package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "PreLoadDoubleAuton", group = "LinearOpMode")
// Autonomous code to hang a specimen on the tall rung and push samples.
public class PreLoadDoubleAuton extends LinearOpMode {

    @Override
    public void runOpMode() {
        // Initialize robot hardware
        Robot robot = new Robot(hardwareMap);

        // Wait for the start signal
        telemetry.addData("Status", "Waiting for start");
        telemetry.update();
        robot.miniClawServo.setPosition(robot.miniClawClosePos);

        waitForStart();

        // Hang first preload
        logAndExecute("Raising front slide and toggling claw",
                () -> robot.frontSlideToPosition(17, 1.0, true));

        logAndExecute("Driving right",
                () -> robot.driveToPosition(34, 0, 0.8, true));

        logAndExecute("Hanging specimen", () -> {
            robot.frontSlideToPosition(12, 0.8, true);
            robot.miniClawServo.setPosition(robot.miniClawOpenPos);
        });

        logAndExecute("Reversing and turning 180 degrees", () -> {
            robot.driveToPosition(-18, 0, 0.8, true);
            robot.frontSlideToPosition(0, 1.0, false);
            robot.rotate(180, 0.5, true);
        });

        // Drive to wall
        logAndExecute("drive to wall", () -> {
            robot.driveToPosition(0, 42, 0.7, true);
            robot.driveToPosition(21, 0, 0.7, true);
        });

        // close claw
        logAndExecute("close claw", () -> {
            robot.miniClawServo.setPosition(robot.miniClawClosePos);
            sleep(250); // Wait for 0.25 second
            robot.frontSlideToPosition(17, 1.0, true);
        });

        // return and spin
        logAndExecute("return and spin", () -> {
            robot.driveToPosition(-15, 0, 0.8, true);
            robot.driveToPosition(0, -41, 0.8, true);
            robot.rotate(180, 0.5,true);
        });

        // Drive to submersible
        logAndExecute("drive to submersible", () -> {
            robot.driveToPosition(24, 0, 0.9, true);
            robot.frontSlideToPosition(12, 1.0, true);
            robot.miniClawServo.setPosition(robot.miniClawOpenPos);

            // Completion message
            telemetry.addData("Step", "Autonomous Complete");
            telemetry.update();
        });
    }
    public void logAndExecute(String step, Runnable action) {
        telemetry.addData("Step", step);
        telemetry.update();
        action.run();
    }
    public void turn180(Robot robot) {
    }
    public void doubleSpecimen(Robot robot) {
    }
}
