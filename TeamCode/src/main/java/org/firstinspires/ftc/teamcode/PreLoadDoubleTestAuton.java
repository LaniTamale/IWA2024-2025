package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
/*
@Autonomous(name = "PreLoadDoubleTestAuton", group = "LinearOpMode")
// Autonomous code to hang a specimen on the tall rung and push samples.
public class PreLoadDoubleTestAuton extends LinearOpMode {

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

        // Hang first preload
        logAndExecute("Raising front slide and toggling claw",
                () -> robot.vertSlideToPosition(17, 0.7, true));

        logAndExecute("Driving right",
                () -> robot.driveToPositionREL(31, 0, 1.0, true));

        logAndExecute("Hanging specimen", () -> {
            robot.vertSlideToPosition(12, 0.6, true);
            robot.miniClawServo.setPosition(robot.miniClawOpenPos);
        });

        logAndExecute("Reversing and turning 180 degrees", () -> {
            robot.driveToPositionREL(-15, 0, 0.9, true);
            robot.vertSlideToPosition(0, 0.7, false);
            robot.rotate(180, 0.5, true);
        });

        // Drive to wall
        logAndExecute("drive to wall", () -> {
            robot.driveToPositionREL(0, 40, 1.0, true);
            robot.driveToPositionREL(12, 0, 1.0, true);
        });

        // close claw
        logAndExecute("close claw", () -> {
            robot.miniClawServo.setPosition(robot.miniClawClosePos);
            sleep(2000); // Wait for 2 seconds
            robot.vertSlideToPosition(17, 0.7, true);
        });

        // return and spin
        logAndExecute("return and spin", () -> {
            robot.driveToPositionREL(-13, 0, 1.0, true);
            robot.driveToPositionREL(0, -38, 1.0, true);
            robot.rotate(180, 0.5,true);
        });

        // Drive to submersible
        logAndExecute("drive to submersible", () -> {
            robot.driveToPositionREL(21, 0, 0.8, true);
            robot.vertSlideToPosition(12, 0.7, true);
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
*/
