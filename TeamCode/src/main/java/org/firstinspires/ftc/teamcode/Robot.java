package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import java.lang.Math;

import static java.lang.Thread.sleep;

/*
Contains robot build, state, and transformation functions.
Used to init hardware when an OpMode is run and to control hardware and track state during a run.
hardwareMap names come from the robot configuration step on the DS or DC.
 */

public class Robot {
    // dimensions
    final public double drivetrainDiagonal = 19.5; // in
    public boolean isArmClawOpen;
    public boolean isMiniClawOpen;
    double cpr = 537.7; // clicks
    double wheelCirc = 11.9; // in
    static final double frontSlideWheelCirc = Math.PI * 1.5;
    // to correct movement lengths
    static final double drivetrainMultiplier = 1.65;

    // limits
    final public double ArmClawOpenPos = 0.3;
    final public double ArmClawClosePos = 0.0;

    final public double miniClawOpenPos = 0.3;
    final public double miniClawClosePos = 0.0;

    final public double frontSLideMaxLen = 18; // in

    // state
    public boolean isClawOpen = false;
    public boolean rightBumperPrev = false;
    public boolean rightTriggerPrev = false;

    // motors
    public DcMotor leftFrontDrive;
    public DcMotor rightFrontDrive;
    public DcMotor leftBackDrive;
    public DcMotor rightBackDrive;
    public DcMotor arm;
    public DcMotor armSlide;
    public DcMotor vertSlide;

    //servos
    public Servo armClawServo;
    public Servo miniClawServo;

    public Robot(HardwareMap hardwareMap) {
        // init hardware
        leftFrontDrive = hardwareMap.get(DcMotor.class, "frontLeft");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "frontRight");
        leftBackDrive = hardwareMap.get(DcMotor.class, "backLeft");
        rightBackDrive = hardwareMap.get(DcMotor.class, "backRight");

        arm = hardwareMap.get(DcMotor.class, "arm");
        armSlide = hardwareMap.get(DcMotor.class, "armSlide");
        vertSlide = hardwareMap.get(DcMotor.class, "vertSlide");

        armClawServo = hardwareMap.get(Servo.class, "armClaw");
        miniClawServo = hardwareMap.get(Servo.class, "miniClaw");

        // configure drive motors
        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);

        // Configure encoders
        drivetrainSetRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        drivetrainSetRunMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Configure slides
        arm.setDirection(DcMotor.Direction.REVERSE);
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        armSlide.setDirection(DcMotor.Direction.FORWARD);
        armSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        vertSlide.setDirection(DcMotor.Direction.REVERSE);
        vertSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        vertSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void drivetrainSetRunMode(DcMotor.RunMode mode) {
        leftFrontDrive.setMode(mode);
        rightFrontDrive.setMode(mode);
        leftBackDrive.setMode(mode);
        rightBackDrive.setMode(mode);
    }

    // Move linearly from the current position to the specified relative point.
    // Function conditionally blocks until movement completion.
    // x and y are in inches; power is a float in the range [0.0, 1.0]
    public void driveToPosition(float x, float y, double power, boolean blockReturn) {
        // no movement required
        if (x == 0 && y == 0) return;
        // atan2 handles quadrants and x == 0 safely
        double theta = Math.atan2(y, x);

        // Compute motor power ratios
        double ADRatio = Math.cos(theta - Math.PI / 4);
        double BCRatio = Math.sin(theta - Math.PI / 4);

        // max ratio for power scaling
        double maxRatio = Math.max(Math.abs(ADRatio), Math.abs(BCRatio));

        // encoder target position determines travel distance
        // values are rounded to nearest integer
        // multiplier corrects movement length
        double clicks = Math.sqrt(y*y + x*x) / wheelCirc * cpr * drivetrainMultiplier;
        leftFrontDrive.setTargetPosition(leftFrontDrive.getCurrentPosition() + (int)(clicks * ADRatio + 0.5));
        rightFrontDrive.setTargetPosition(rightFrontDrive.getCurrentPosition() + (int)(clicks * BCRatio + 0.5));
        leftBackDrive.setTargetPosition(leftBackDrive.getCurrentPosition() + (int)(clicks * BCRatio + 0.5));
        rightBackDrive.setTargetPosition(rightBackDrive.getCurrentPosition() + (int)(clicks * ADRatio + 0.5));

        // enable distance based movement
        drivetrainSetRunMode(DcMotor.RunMode.RUN_TO_POSITION);

        // begin movement; direction is based on motor power
        leftFrontDrive.setPower(ADRatio/maxRatio * power);
        rightFrontDrive.setPower(BCRatio/maxRatio * power);
        leftBackDrive.setPower(BCRatio/maxRatio * power);
        rightBackDrive.setPower(ADRatio/maxRatio * power);

        // conditionally block return until movement is complete
        if (!blockReturn) return; // Exit if non-blocking mode

        while (leftFrontDrive.isBusy() || rightFrontDrive.isBusy() ||
                        leftBackDrive.isBusy() || rightBackDrive.isBusy()) { // Check if motors are moving
            try {
                Thread.sleep(50); // Wait briefly before checking again
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore interrupt status
                break; // Exit loop on interruption
            }
        }
    }

    public void driveToPositionREL(float x, float y, double power, boolean blockReturn) {
        // no movement required
        if (x == 0 && y == 0) return;
        // atan2 handles quadrants and x == 0 safely
        double theta = Math.atan2(y, x);

        // Compute motor power ratios
        double ADRatio = Math.cos(theta - Math.PI / 4);
        double BCRatio = Math.sin(theta - Math.PI / 4);

        // max ratio for power scaling
        double maxRatio = Math.max(Math.abs(ADRatio), Math.abs(BCRatio));

        // Reset encoders so movement is done from a reference position
        drivetrainSetRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // encoder target position determines travel distance
        // values are rounded to nearest integer
        // multiplier corrects movement length
        double clicks = Math.sqrt(y*y + x*x) / wheelCirc * cpr * drivetrainMultiplier;
        leftFrontDrive.setTargetPosition((int)(clicks * ADRatio + 0.5));
        rightFrontDrive.setTargetPosition((int)(clicks * BCRatio + 0.5));
        leftBackDrive.setTargetPosition((int)(clicks * BCRatio + 0.5));
        rightBackDrive.setTargetPosition((int)(clicks * ADRatio + 0.5));

        // enable distance based movement
        drivetrainSetRunMode(DcMotor.RunMode.RUN_TO_POSITION);

        // begin movement; direction is based on motor power
        leftFrontDrive.setPower(ADRatio/maxRatio * power);
        rightFrontDrive.setPower(BCRatio/maxRatio * power);
        leftBackDrive.setPower(BCRatio/maxRatio * power);
        rightBackDrive.setPower(ADRatio/maxRatio * power);

        // conditionally block return until movement is complete
        if (!blockReturn) return; // Exit if non-blocking mode

        while (leftFrontDrive.isBusy() || rightFrontDrive.isBusy() ||
                leftBackDrive.isBusy() || rightBackDrive.isBusy()) { // Check if motors are moving
            try {
                Thread.sleep(50); // Wait briefly before checking again
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore interrupt status
                break; // Exit loop on interruption
            }
        }
    }

    // Moves the front slide to a specified position (in inches).
    // If blockReturn is true, the method will wait until movement is complete.
    // movement is relative; power is a float in the range [0.0, 1.0]
    // optionally block until movement completion
    public boolean frontSlideToPosition(double in, double power, boolean blockReturn) {
        if (in > frontSLideMaxLen) return false;
        double movementClicks = in / frontSlideWheelCirc * cpr;

        vertSlide.setTargetPosition((int)(movementClicks + 0.5));
        vertSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        vertSlide.setPower(power);

        if (!blockReturn) return true;
        while (vertSlide.isBusy()) {
            try {
                sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        return true;
    }



    // degrees is used for familiarity
    // positive power for clockwise, negative for counterclockwise
    public void rotate(double degrees, double power, boolean blockReturn) {
        // Reset encoders to ensure accurate movement
        drivetrainSetRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Determine encoder target position for turn
        // Turning mecanum drive is normal tank tread movement
        int turnClicks = (int) (Math.PI * drivetrainDiagonal / wheelCirc * cpr * degrees / 360 * drivetrainMultiplier);

        // Set target positions for a 180-degree turn
        //Instead of moving motors in a linear direction (x, y), function makes the left side move forward and the right side move backward
        leftFrontDrive.setTargetPosition(turnClicks);
        leftBackDrive.setTargetPosition(turnClicks);
        rightFrontDrive.setTargetPosition(-turnClicks);
        rightBackDrive.setTargetPosition(-turnClicks);

        // Enable encoder distance-based movement
        drivetrainSetRunMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Set motor power for rotation
        //ensures that both sides rotate at the same speed
        leftFrontDrive.setPower(power);
        leftBackDrive.setPower(power);
        rightFrontDrive.setPower(power);
        rightBackDrive.setPower(power);

        // Block until movement is complete if required
        if (!blockReturn) return;

        //ensures the robot fully completes the turn before continuing
        while (leftFrontDrive.isBusy() || rightFrontDrive.isBusy() ||
                        leftBackDrive.isBusy() || rightBackDrive.isBusy()) {
            try {
                Thread.sleep(50); // Wait briefly before checking again
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
