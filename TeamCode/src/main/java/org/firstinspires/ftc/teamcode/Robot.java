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
    final public double width = 16.0; // in
    final public double diameter = 4.125; // in
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
    public boolean leftBumperPrev = false;

    // motors
    public DcMotor leftFrontDrive;
    public DcMotor rightFrontDrive;
    public DcMotor leftBackDrive;
    public DcMotor rightBackDrive;
    public DcMotor arm;
    public DcMotor armSlide;
    public DcMotor frontSlide;

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
        armSlide = hardwareMap.get(DcMotor.class, "armslide");
        frontSlide = hardwareMap.get(DcMotor.class, "frontslide");

        armClawServo = hardwareMap.get(Servo.class, "armclaw");
        miniClawServo = hardwareMap.get(Servo.class, "miniclaw");

        // configure
        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);

        arm.setDirection(DcMotor.Direction.FORWARD);
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        armSlide.setDirection(DcMotor.Direction.FORWARD);
        armSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        frontSlide.setDirection(DcMotor.Direction.REVERSE);
        frontSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
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

        // normalize encoders at a reference position (probably 0)
        leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // encoder target position determines travel distance
        // values are rounded to nearest integer
        // multiplier corrects movement length
        double clicks = Math.sqrt(y*y + x*x) / wheelCirc * cpr * drivetrainMultiplier;
        leftFrontDrive.setTargetPosition((int)(clicks * ADRatio + 0.5));
        rightFrontDrive.setTargetPosition((int)(clicks * BCRatio + 0.5));
        leftBackDrive.setTargetPosition((int)(clicks * BCRatio + 0.5));
        rightBackDrive.setTargetPosition((int)(clicks * ADRatio + 0.5));

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

        // conditionally block return until movement is complete
        if (!blockReturn) return;
        while (
                leftFrontDrive.isBusy() ||
                rightFrontDrive.isBusy() ||
                leftBackDrive.isBusy() ||
                rightBackDrive.isBusy()
        ) {
            // wait for completion
            try { sleep(50); }
            catch(InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    // movement is relative; power is a float in the range [0.0, 1.0]
    // optionally block until movement completion
    public boolean frontSlideToPosition(double in, double power, boolean blockReturn) {
        if (in > frontSLideMaxLen) return false;
        double movementClicks = in / frontSlideWheelCirc * cpr;

        frontSlide.setTargetPosition((int)(movementClicks + 0.5));
        frontSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontSlide.setPower(power);

        if (!blockReturn) return true;
        while (frontSlide.isBusy()) {
            try {
                sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        return true;
    }
}
