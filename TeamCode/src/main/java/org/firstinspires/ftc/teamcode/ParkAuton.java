package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="ParkAuton", group="LinearOpMode")
// new autonomous code to hang a specimen on the tall rung.
public class ParkAuton extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        // Initialize robot hardware
        Robot robot = new Robot(hardwareMap);

        // Wait for the start signal
        telemetry.addData("Status", "Waiting for start");
        telemetry.update();
        waitForStart();
        runtime.reset();

        // raise slide and lock miniClaw
        telemetry.addData("Step", "Parking");
        telemetry.update();
        //robot.driveToPosition(2, 0, 0.5, true);
        //robot.driveToPosition(0, -12, 0.5, true);
    }
}
