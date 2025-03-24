package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "TeleOp", group = "LinearOpMode")
// TeleOp for claw bot with Mecanum drive
public class Main extends LinearOpMode {
    public void runOpMode() {
        Robot robot = new Robot(hardwareMap);
        ElapsedTime runtime = new ElapsedTime();

        //wait for the game to start (driver presses START)
        telemetry.addData("status", "initialized");
        telemetry.addData("Servo Power", robot.intakeServo1.getPower());
        //telemetry.addData("Servo Position", wristServo.getPosition());
        telemetry.update();
        waitForStart();
        runtime.reset();

        //run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            double max;
            boolean armLocked = false;

            //POV Mode uses left joystick to go forward & strafe, and right joystick to rotate
            double axial = -gamepad1.left_stick_y; //note: pushing stick forward gives negative value
            double lateral = gamepad1.left_stick_x;
            double yaw = gamepad1.right_stick_x;

            //arm1 and arm2 for twerk
            //robot.arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            double arm1Power = 1;
            double arm2Power = 1;
            if (gamepad2.dpad_down) { //was: dpad_up edit: reverted
                robot.arm1.setPower(-arm1Power);
                robot.arm2.setPower(-arm2Power);
                //robot.arm.setTargetPosition(robot.arm.getCurrentPosition()-10);
            } else if (gamepad2.dpad_up) { //was: dpad_down edit: reverted
                robot.arm1.setPower(arm1Power);
                robot.arm2.setPower(arm2Power);
            } else {
                robot.arm1.setPower(0);
                robot.arm2.setPower(0);
            }

            // control back main linear slides
            if (gamepad2.y) {
                 robot.vertSlide.setPower(0.5);
            }
            else if (gamepad2.x) {
                 robot.vertSlide.setPower(-0.5);
            } else {
                 robot.vertSlide.setPower(0.0);
            }

            // control frontward facing side linear slides
            if (gamepad2.a) {
                robot.armSlide.setPower(0.5);
            }
            else if (gamepad2.b) {
                robot.armSlide.setPower(-0.5);
            } else {
                robot.armSlide.setPower(0.0);
            }

            //combine the joystick requests for each axis-motion to determine each wheel's power
            //set up a variable for each drive wheel to save the power level for telemetry
            double leftFrontPower = axial + lateral + yaw;
            double rightFrontPower = axial - lateral - yaw;
            double leftBackPower = axial - lateral + yaw;
            double rightBackPower = axial + lateral - yaw;

            //normalize the values so no wheel power exceeds 100%
            //this ensures that the robot maintains the desired motion
            max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
            max = Math.max(max, Math.abs(leftBackPower));
            max = Math.max(max, Math.abs(rightBackPower));

            if (max > 1.0) {
                leftFrontPower /= max;
                rightFrontPower /= max;
                leftBackPower /= max;
                rightBackPower /= max;
            }

            //send calculated power to wheels
            robot.leftFrontDrive.setPower(leftFrontPower);
            robot.rightFrontDrive.setPower(rightFrontPower);
            robot.leftBackDrive.setPower(leftBackPower);
            robot.rightBackDrive.setPower(rightBackPower);

            // Auto Intake
            // Example: Timed Intake Cycle (opens and closes every X seconds)
            //Timed cycle: Alternates between open and closed states at a set time interval.
            double currentTime = robot.timer.seconds();
            if (currentTime - robot.lastIntakeTime > robot.intakeInterval) {
                robot.isArmClawOpen = !robot.isArmClawOpen;
                robot.lastIntakeTime = currentTime;  // Reset timer
            }

            if (gamepad2.right_trigger > 0) {
                robot.intakeServo1.setPower(gamepad2.right_trigger);
                robot.intakeServo2.setPower(gamepad2.right_trigger);
            } else if (gamepad2.left_trigger > 0){
                robot.intakeServo1.setPower(-gamepad2.left_trigger);
                robot.intakeServo2.setPower(-gamepad2.left_trigger);
            }
            else {
                robot.intakeServo1.setPower(0);
                robot.intakeServo2.setPower(0);
            }

            // Mini claw
            if (gamepad2.right_bumper && !robot.rightBumperPrev) {
                robot.isMiniClawOpen = !robot.isMiniClawOpen;
            }
            robot.rightBumperPrev = gamepad2.right_bumper;
            if (robot.isMiniClawOpen) {
                robot.miniClawServo.setPosition(robot.miniClawOpenPos);
            } else {
                robot.miniClawServo.setPosition(robot.miniClawClosePos);
            }

            // wrist
            if (gamepad2.dpad_right) {
                robot.wristServo.setPosition(1);//135 deg
            }
            else if (gamepad2.dpad_left) {
                robot.wristServo.setPosition(0);//-135 deg
            }
            else {
                robot.wristServo.setPosition(0.5);//0 deg
            }

            //show the elapsed game time and wheel power.
            telemetry.addData("status", "Run Time:" + runtime);
            telemetry.addData("Front left/Right", "%4.2f,%4.2f", leftFrontPower, rightFrontPower);
            telemetry.addData("Back left/Right", "%4.2f,%4.2f", leftBackPower, rightBackPower);
            //telemetry.addData("Wrist Pos", "%4.2f", wristServo.getPosition());
            telemetry.addData("Claw Power", "%4.2f", robot.intakeServo1.getPower());
            telemetry.addData("robot.arm Pos", robot.arm1.getCurrentPosition());
            telemetry.update();
        }
    }
}

/*
            //Hard Stop Odometry wheels
            public class OdometryWheelStop extends LinearOpMode {
                private DcMotor OdometryWheelMotor;

                @Override
                public void init() {
                    // Initialize the motor and gamepad1
                    odometryWheelMotorY = hardwareMap.get(DcMotor.class, "odometryWheelMotorY");
                    gamepad1 = gamepad1; // Assuming gamepad1 is used
                    odometryWheelMotorX = hardwareMap.get(DcMotor.class, "odometryWheelMotorX");
                    gamepad1 = gamepad1; // Assuming gamepad1 is used
                }
                @Override
                public void loop() {
                    // Check if the button (e.g., 'A' button) is pressed
                    if (gamepad1.a) {
                        // Hard stop the odometry wheel motor
                        odometryWheelMotorY.setPower(0);
                        odometryWheelMotorY.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                        odometryWheelMotorX.setPower(0);
                        odometryWheelMotorX.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
             */
            /*
            // Arm claw
            if (gamepad2.right_trigger > 0.5 && !robot.rightTriggerPrev) {
                robot.isArmClawOpen = !robot.isArmClawOpen;
            }
           robot.rightTriggerPrev = gamepad2.right_trigger > 0.5;
            if (robot.isArmClawOpen) {
                robot.armClawServo.setPosition(robot.ArmClawOpenPos);
            } else {
                robot.armClawServo.setPosition(robot.ArmClawClosePos);
            }
             */
            /*
            if (gamepad2.left_bumper) {
                if (!armLocked) {
                    robot.arm1.setTargetPosition(robot.arm1.getCurrentPosition());
                    robot.arm1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    robot.arm1.setPower(1);
                    robot.arm2.setTargetPosition(robot.arm2.getCurrentPosition());
                    robot.arm2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    robot.arm2.setPower(1);
                   armLocked = true;
                } else {
                    robot.arm1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    robot.arm2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    armLocked = false;
                }
            }
            */

