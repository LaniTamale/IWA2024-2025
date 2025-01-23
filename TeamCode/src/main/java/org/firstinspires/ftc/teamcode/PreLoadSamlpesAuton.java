package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="PreLoadSamplesAuton", group="LinearOpMode")
// new autonomous code to hang a specimen on the tall rung then driving to push samples.
public class PreLoadSamlpesAuton extends LinearOpMode {
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

        //Step1: raise slide and lock miniClaw
        telemetry.addData("Step1", "Raising front slide and toggling claw");
        telemetry.update();
        robot.frontSlideToPosition(18, 0.5, true);

        //Step2: drive left
        telemetry.addData("Step2", "Driving left");
        telemetry.update();
        robot.driveToPosition(-31, 0, 0.5, true);

        //Step3: Score the preload object
        telemetry.addData("Step3", "Hanging specimen");
        telemetry.update();
        robot.frontSlideToPosition(12, 0.5, true);
        robot.miniClawServo.setPosition(robot.miniClawOpenPos);

        //Step4: Drive Backward
        telemetry.addData("Step4", "Reversing to wall");
        telemetry.update();
        robot.driveToPosition(29, 0, 0.5, true);

        //Step5: drive to Assent Sample #1
        telemetry.addData("Step5", "Push Sample #1");
        telemetry.update();
        robot.driveToPosition(0, 35, 0.5, true);
        robot.driveToPosition(-45, 0, 0.5, true);
        robot.driveToPosition(0, 10, 0.5, true);
        robot.driveToPosition(31, 0, 0.5, true);

        //Step6: drive to sample #2
        telemetry.addData("Step6", "Push Sample #2");
        telemetry.update();
        robot.driveToPosition(-31, 0, 0.5, true);
        robot.driveToPosition(0, 6, 0.5, true);
        robot.driveToPosition(31, 0, 0.5, true);

        //Step7: drive to sample #3
        telemetry.addData("Step", "Push Sample #3");
        telemetry.update();
        robot.driveToPosition(-31, 0, 0.5, true);
        robot.driveToPosition(0, 6, 0.5, true);
        robot.driveToPosition(31, 0, 0.5, true);

        //Step8: Completion message
        telemetry.addData("Step", "Autonomous Complete");
        telemetry.update();
    }
}
