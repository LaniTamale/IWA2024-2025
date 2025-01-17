package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name="TestAuton", group="LinearOpMode")
// test autonomous code
public class TestAuton extends LinearOpMode {
    public void runOpMode() {
        Robot robot = new Robot(hardwareMap);

        waitForStart();

        robot.frontSlideToPosition(8, 0.5, true);
        boolean ret = robot.frontSlideToPosition(8, 0.5, true);
        telemetry.addData("return", String.valueOf(ret));
        telemetry.update();
        sleep(99999999);
    }
}
