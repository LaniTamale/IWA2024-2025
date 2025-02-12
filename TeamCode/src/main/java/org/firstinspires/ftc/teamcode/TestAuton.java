package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@Autonomous(name="TestAuton", group="LinearOpMode")
// test autonomous code
public class TestAuton extends LinearOpMode {
    public void runOpMode() {
        Robot robot = new Robot(hardwareMap);
        Pose2d initialPose = new Pose2d(12, -60, Math.toRadians(180));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);
        robot.miniClawServo.setPosition(robot.miniClawClosePos);

        waitForStart();

        // Hang first preload Specamen
        logAndExecute("Raising front slide and toggling claw",
                () -> robot.frontSlideToPosition(17, 1.0, false));
        TrajectoryActionBuilder tab1 = drive.actionBuilder(initialPose)
                .strafeTo(new Vector2d(0, -35));
        logAndExecute("Driving right",
                () -> Actions.runBlocking(new SequentialAction(tab1.build())));
        logAndExecute("Hanging specimen", () -> {
            robot.frontSlideToPosition(12, 0.8, true);
            robot.miniClawServo.setPosition(robot.miniClawOpenPos);
        });

        TrajectoryActionBuilder tab2 = tab1.endTrajectory().fresh()
                .strafeToSplineHeading(new Vector2d(56, -59),Math.toRadians(0));
        logAndExecute( "Reversing and turning 180 degrees", () -> {
            robot.frontSlideToPosition(0, 1.0, false);
            Actions.runBlocking(new SequentialAction(tab2.build()));
        });

        // close claw
        logAndExecute("close claw", () -> {
            robot.miniClawServo.setPosition(robot.miniClawClosePos);
            sleep(250); // Wait for 0.25 second
            robot.frontSlideToPosition(17, 1.0, true);
        });

        // return to submersible/ Second Specamen
        TrajectoryActionBuilder tab3 = tab2.endTrajectory().fresh()
                .strafeToSplineHeading(new Vector2d(0, -35),Math.toRadians(180));
        logAndExecute("Drive to sub", () -> {
            Actions.runBlocking(new SequentialAction(tab3.build()));
        });
        logAndExecute("Hanging specimen", () -> {
            robot.frontSlideToPosition(12, 0.8, true);
            robot.miniClawServo.setPosition(robot.miniClawOpenPos);
        });

        /* Drive to submersible
        TrajectoryActionBuilder tab4 = tab3.endTrajectory().fresh()
                .strafeToSplineHeading(new Vector2d(0, -35),Math.toRadians(90));
        logAndExecute("Third Human Load", () -> {

        Actions.runBlocking(new SequentialAction(tab4.build()));

            // Completion message
            telemetry.addData("Step", "Autonomous Complete");
            telemetry.update();
        });

         TrajectoryActionBuilder tab4 = tab3.endTrajectory().fresh()
                .strafeToSplineHeading(new Vector2d(0, -35),Math.toRadians(90));
        logAndExecute("Third Human Load", () -> {

        Actions.runBlocking(new SequentialAction(tab4.build()));

            // Completion message
            telemetry.addData("Step", "Autonomous Complete");
            telemetry.update();
        });

         */
    }
    public void logAndExecute(String step, Runnable action) {
        telemetry.addData("Step", step);
        telemetry.update();
        action.run();
    }
}
