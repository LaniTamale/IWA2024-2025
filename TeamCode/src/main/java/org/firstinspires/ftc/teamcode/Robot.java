package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import java.lang.Math;
import static java.lang.Thread.sleep;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/*
Contains robot build, state, and transformation functions.
Used to init hardware when an OpMode is run and to control hardware and track state during a run.
hardwareMap names come from the robot configuration step on the DS or DC.
 */

public class Robot {
    // dimensions
    final public double width = 16.0; // in
    final public double diameter = 4.125; // in
    public boolean isArmClawOpen;
    public boolean isMiniClawOpen;
    double cpr = 537.7; // clicks
    double wheelCirc = 11.9; // in
    static final double frontSlideWheelCirc = Math.PI * 1.5;

    // limits
    final public double ArmClawOpenPos = 0.3;
    final public double ArmClawClosePos = 0.0;

    final public double miniClawOpenPos = 0.3;
    final public double miniClawClosePos = 0.0;

    final public double frontSLideMaxLen = 19; // in


    final public double wristOpenPos = 0;
    final public double wristClosePos = 1;

    // state
    public boolean isClawOpen = false;
    public boolean rightBumperPrev = false;
    public boolean rightTriggerPrev = false;
    public boolean leftBumperPrev = false;

    //public boolean isWristOpen = true;

    // motors
    public DcMotor leftFrontDrive;
    public DcMotor rightFrontDrive;
    public DcMotor leftBackDrive;
    public DcMotor rightBackDrive;
    public DcMotor arm;
    public DcMotor armslide;
    public DcMotor frontslide;

    //servos
    public Servo armClawServo;
    public Servo miniClawServo;

    //public Servo wristServo;

    // Creates a PIDFController with gains kP, kI, kD, and kF
    //PIDFController pidf;

    public Robot(HardwareMap hardwareMap) {
        //pidf = new PIDFController(kP, kI, kD, kF);
        // init hardware
        leftFrontDrive = hardwareMap.get(DcMotor.class, "frontLeft");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "frontRight");
        leftBackDrive = hardwareMap.get(DcMotor.class, "backLeft");
        rightBackDrive = hardwareMap.get(DcMotor.class, "backRight");

        arm = hardwareMap.get(DcMotor.class, "arm");
        armslide = hardwareMap.get(DcMotor.class, "armslide");
        frontslide = hardwareMap.get(DcMotor.class, "frontslide");

        armClawServo = hardwareMap.get(Servo.class, "armclaw");
        miniClawServo = hardwareMap.get(Servo.class, "miniclaw");


        //wristServo = hardwareMap.get(Servo.class, "wrist");

        // configure
        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);

        arm.setDirection(DcMotor.Direction.FORWARD);
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        armslide.setDirection(DcMotor.Direction.FORWARD);
        armslide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        frontslide.setDirection(DcMotor.Direction.FORWARD);
        frontslide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    // Move linearly from the current position to the specified relative point.
    // Function blocks until movement completion.
    // x and y are in inches; power is a float in the range [0.0, 1.0]
    //If x is 0, use specific angles based on the value of y.
    //Use Math.atan2(y, x) instead of Math.atan(y / x) because atan2 automatically handles cases where x is 0
    public void driveToPosition(float x, float y, double power, Telemetry telemetry) {
        // Handle special case where x is 0 to avoid division by zero
        double theta;
        //y > 0: The robot should move directly upwards, so theta = 90° = π/2.
        //y < 0: The robot should move directly downwards, so theta = -90° = -π/2 (or 3π/2).
        //x = 0 and y = 0: No movement is required, so an error message is logged, and the method exits with return.
        if (x == 0) {
            if (y > 0) {
                theta = Math.PI / 2; // 90 degrees for positive y
            } else if (y < 0) {
                theta = -Math.PI / 2; // -90 degrees for negative y
            } else {
                telemetry.addData("Error", "x and y both zero; no movement specified.");
                telemetry.update();
                return; // No movement needed
            }
        } else {
            theta = Math.atan2(y, x); // atan2 handles quadrants and x=0 safely
            //(x > 0, y > 0): First quadrant
            //(x < 0, y > 0): Second quadrant
            //(x < 0, y < 0): Third quadrant
            //(x > 0, y < 0): Fourth quadrant
        }
        // Compute motor power ratios
        double ADRatio = Math.cos(theta - Math.PI / 4);
        double BCRatio = Math.sin(theta - Math.PI / 4);

        //Adjusts the angle theta by 45° (π/4 radians) because the wheels are oriented at 45° to the robot's frame.
        double maxRatio = Math.max(Math.abs(ADRatio), Math.abs(BCRatio));
        if (maxRatio > 1.0) {
            ADRatio /= maxRatio;
            BCRatio /= maxRatio;
        }

        // normalize encoders at a reference position (probably 0)
        leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // encoder target position determines travel distance
        // values are rounded to nearest integer
        double clicks = Math.sqrt(y*y + x*x) / wheelCirc * cpr;
        telemetry.addData("Target Clicks", clicks);
        leftFrontDrive.setTargetPosition((int)(clicks * ADRatio/maxRatio + 0.5));
        rightFrontDrive.setTargetPosition((int)(clicks * BCRatio/maxRatio + 0.5));
        leftBackDrive.setTargetPosition((int)(clicks * BCRatio/maxRatio + 0.5));
        rightBackDrive.setTargetPosition((int)(clicks * ADRatio/maxRatio + 0.5));

        // enable distance based movement
        leftFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // begin movement; direction is based on motor power
        leftFrontDrive.setPower(ADRatio/maxRatio * power);
        rightFrontDrive.setPower(BCRatio/maxRatio * power);
        leftBackDrive.setPower(BCRatio/maxRatio * power);
        rightBackDrive.setPower(ADRatio/maxRatio * power);

        // block return until movement is complete
        while(
                leftFrontDrive.isBusy() ||
                rightFrontDrive.isBusy() ||
                leftBackDrive.isBusy() ||
                rightBackDrive.isBusy()
        ) {
            telemetry.addData("Left Front", leftFrontDrive.getCurrentPosition());
            telemetry.addData("Right Front", rightFrontDrive.getCurrentPosition());
            telemetry.addData("Left Back", leftBackDrive.getCurrentPosition());
            telemetry.addData("Right Back", rightBackDrive.getCurrentPosition());
            telemetry.update();

            try {
                sleep(50);}
            catch(InterruptedException e) {
                Thread.currentThread().interrupt();
                break;

            }
        }
    }

    // movement is relative; power is a float in the range [0.0, 1.0]
    // optionally block until movement completion
    public slideToPosition(DcMotor motor, double in, double power, boolean blockReturn) {
        double currentPos = motor.getCurrentPosition() / cpi;
    }
}
