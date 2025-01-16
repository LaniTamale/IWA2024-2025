package org.firstinspires.ftc.teamcode;

import static java.lang.Thread.sleep;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="TestAuton", group="LinearOpMode")
// test autonomous code
public class TestAuton extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();

    public void runOpMode() {
        Robot robot = new Robot(hardwareMap);

        waitForStart();

        while (opModeIsActive());

    }
}

/*movement test
            robot.driveToPosition(-30, 0, 0.5, true);
            robot.driveToPosition(15, 0, 0.5, true);
            robot.driveToPosition(0, 15, 0.5, true);
            robot.driveToPosition(0, -30, 0.5, true);
            robot.driveToPosition(-15, -15, 0.5, true);
        }
    }
}
*/