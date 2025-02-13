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
                .strafeTo(new Vector2d(0, -28));
        logAndExecute("Driving right",
                () -> Actions.runBlocking(new SequentialAction(tab1.build())));
        logAndExecute("Hanging specimen", () -> {
            robot.frontSlideToPosition(12, 1.0, true);
            robot.miniClawServo.setPosition(robot.miniClawOpenPos);
        });
        TrajectoryActionBuilder tab2 = tab1.endTrajectory().fresh()
                .strafeToSplineHeading(new Vector2d(56, -60),Math.toRadians(0));
        logAndExecute( "Reversing and turning 180 degrees", () -> {
            robot.frontSlideToPosition(0, 1.0, false);
            Actions.runBlocking(new SequentialAction(tab2.build()));
        });

        // close claw
        logAndExecute("close claw", () -> {
            robot.miniClawServo.setPosition(robot.miniClawClosePos);
            sleep(1000); // Wait for 1.0 seconds
            robot.frontSlideToPosition(17, 1.0, true);
        });

        // return to submersible/ Second Specimen
        TrajectoryActionBuilder tab3 = tab2.endTrajectory().fresh()
                .strafeToSplineHeading(new Vector2d(0, -28),Math.toRadians(180));
        logAndExecute("Drive to sub", () -> {
            Actions.runBlocking(new SequentialAction(tab3.build()));
        });
        logAndExecute("Hanging specimen", () -> {
            robot.frontSlideToPosition(12, 1.0, true);
            robot.miniClawServo.setPosition(robot.miniClawOpenPos);
        });

        // pushing alliance samples
        TrajectoryActionBuilder tab4 = tab3.endTrajectory().fresh()
                .strafeToSplineHeading(new Vector2d(56, -60),Math.toRadians(0));
        logAndExecute( "Reversing and turning 180 degrees", () -> {
            robot.frontSlideToPosition(0, 1.0, false);
            Actions.runBlocking(new SequentialAction(tab4.build()));
        });
         TrajectoryActionBuilder tab5 = tab4.endTrajectory().fresh()
                .strafeToSplineHeading(new Vector2d(38, -14),Math.toRadians(90));
        logAndExecute("pushing alliance sample 1", () -> {
        Actions.runBlocking(new SequentialAction(tab5.build()));
        });
        TrajectoryActionBuilder tab6 = tab5.endTrajectory().fresh()
                .strafeToSplineHeading(new Vector2d(38, -59),Math.toRadians(90));
        logAndExecute("pushing alliance sample 1", () -> {
            Actions.runBlocking(new SequentialAction(tab6.build()));
        });
        TrajectoryActionBuilder tab7 = tab6.endTrajectory().fresh()
                .strafeToSplineHeading(new Vector2d(54, -14),Math.toRadians(90));
        logAndExecute("pushing alliance sample 2", () -> {
            Actions.runBlocking(new SequentialAction(tab7.build()));
        });
        TrajectoryActionBuilder tab8 = tab7.endTrajectory().fresh()
                .strafeToSplineHeading(new Vector2d(54, -59),Math.toRadians(90));
        logAndExecute("pushing alliance sample 2", () -> {
            Actions.runBlocking(new SequentialAction(tab8.build()));
        });
        TrajectoryActionBuilder tab9 = tab8.endTrajectory().fresh()
                .strafeToSplineHeading(new Vector2d(60,-14),Math.toRadians(90));
        logAndExecute("pushing alliance sample 3", () -> {
            Actions.runBlocking(new SequentialAction(tab9.build()));
        });
        TrajectoryActionBuilder tab10 = tab9.endTrajectory().fresh()
                .strafeToSplineHeading(new Vector2d(60, -59),Math.toRadians(90));
        logAndExecute("pushing alliance sample 3", () -> {
            Actions.runBlocking(new SequentialAction(tab10.build()));
        });

        // close claw for human
        logAndExecute("close claw", () -> {
            robot.miniClawServo.setPosition(robot.miniClawClosePos);
            sleep(3000); // Wait for 3.0 seconds
            robot.frontSlideToPosition(17, 1.0, true);
        });

        // return to submersible/ third Specimen
        TrajectoryActionBuilder tab11 = tab10.endTrajectory().fresh()
                .strafeToSplineHeading(new Vector2d(14, -28),Math.toRadians(0));
        logAndExecute( "Reversing and turning 180 degrees", () -> {
            robot.frontSlideToPosition(0, 1.0, false);
            Actions.runBlocking(new SequentialAction(tab11.build()));
        });
        TrajectoryActionBuilder tab12 = tab11.endTrajectory().fresh()
                .strafeTo(new Vector2d(0, -28));
        logAndExecute("Driving right",
                () -> Actions.runBlocking(new SequentialAction(tab12.build())));
        logAndExecute("Hanging specimen", () -> {
            robot.frontSlideToPosition(17, 1.0, false);
            robot.frontSlideToPosition(12, 1.0, true);
            robot.miniClawServo.setPosition(robot.miniClawOpenPos);
        });

        // Completion message
            telemetry.addData("Step", "Autonomous Complete");
            telemetry.update();
        }
    public void logAndExecute(String step, Runnable action) {
        telemetry.addData("Step", step);
        telemetry.update();
        action.run();
    }
}
