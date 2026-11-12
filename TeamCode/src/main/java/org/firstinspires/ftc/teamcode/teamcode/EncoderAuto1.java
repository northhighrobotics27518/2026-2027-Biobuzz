package org.firstinspires.ftc.teamcode.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "EncoderAuto1", group = "StarterBot")
public class EncoderAuto1 extends LinearOpMode {

    // ---------------- Hardware ----------------
    private DcMotor leftFrontDrive, rightFrontDrive, leftBackDrive, rightBackDrive;
    private DcMotor intakeMotor, launcher;
    private Servo intakeServo;

    private final ElapsedTime runtime = new ElapsedTime();

    // ---------------- Encoder calibration (EDIT THESE FOR YOUR ROBOT) ----------------
    // Defaults assume goBILDA 5203 312 RPM motors (537.7 ticks/rev), 1:1 gearing, 96 mm mecanum wheels.
    static final double COUNTS_PER_MOTOR_REV = 537.7;
    static final double DRIVE_GEAR_REDUCTION = 1.0;      // external gearing (wheel rpm / motor rpm)
    static final double WHEEL_DIAMETER_INCHES = 4.09449;    // 96 mm
    static final double COUNTS_PER_INCH =
            (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * Math.PI);

    // Mecanum strafing is less efficient than driving straight; tune until strafe distances are accurate.
    //Strafe Correction needs to be tested and identified 11/10/26
    static final double STRAFE_CORRECTION = 1.1;

    // Turning: tune by commanding a 90 degree turn and adjusting until it's accurate.
    // Start with the diagonal distance between opposite wheels (inches).
    //Turn Diameter needs to be tested and identified 11/10/26
    static final double TURN_DIAMETER_INCHES = 17.0;
    static final double COUNTS_PER_DEGREE =
            (Math.PI * TURN_DIAMETER_INCHES / 360.0) * COUNTS_PER_INCH;

    static final double DRIVE_SPEED = 0.5;
    static final double TURN_SPEED = 0.4;

    // ---------------- Mechanism values (from Proto1 TeleOp) ----------------
    static final double LAUNCHER_POWER = 0.67;
    static final double SERVO_FORWARD = 0.3;
    static final double SERVO_BACKWARD = 0.6;

    String test = "no";

    @Override
    public void runOpMode() {
        // Hardware map (names must match robot configuration)
        leftFrontDrive = hardwareMap.get(DcMotor.class, "left_front_drive");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "right_front_drive");
        leftBackDrive = hardwareMap.get(DcMotor.class, "left_back_drive");
        rightBackDrive = hardwareMap.get(DcMotor.class, "right_back_drive");
        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
        launcher = hardwareMap.get(DcMotor.class, "launcher");
        intakeServo = hardwareMap.get(Servo.class, "intakeServo");

        // Same directions as TeleOp: positive power drives every wheel forward
        leftFrontDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        rightFrontDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        rightBackDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        intakeMotor.setDirection(DcMotor.Direction.FORWARD);
        launcher.setDirection(DcMotor.Direction.REVERSE);

        for (DcMotor m : new DcMotor[]{leftFrontDrive, rightFrontDrive, leftBackDrive, rightBackDrive}) {
            m.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            m.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            m.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        launcher.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intakeServo.setPosition(SERVO_FORWARD);

        telemetry.addData("Status", "Initialized - encoders reset");
        telemetry.addData("Start counts", "%7d %7d %7d %7d",
                leftFrontDrive.getCurrentPosition(), rightFrontDrive.getCurrentPosition(),
                leftBackDrive.getCurrentPosition(), rightBackDrive.getCurrentPosition());
        telemetry.addData("Motors", test);
        telemetry.update();

        waitForStart();

        // ---------------- AUTONOMOUS SEQUENCE (EDIT ME) ----------------
        // Each move: (inches or degrees, speed, timeout seconds)
        driveInches(0, DRIVE_SPEED, 5.0);// this is the reason as for why it turns
        /*sleep(1000);// forward 36"
        sleep(1000);
        turnDegrees(90, TURN_SPEED, 4.0);
        sleep(1000);// turn right 45 degrees (negative = left)
        driveInches(50, DRIVE_SPEED, 5.0);
        sleep(1000);
        sleep(1000);
        *\
         */
        //launchSequence();                      // spin up launcher and feed

        //turnDegrees(-90, TURN_SPEED, 4.0);     // turn back left
        //strafeInches(12, DRIVE_SPEED, 4.0);    // strafe right 12" (negative = left)
        //driveInches(-12, DRIVE_SPEED, 4.0);    // back up 12"

        telemetry.addData("Path", "Complete");
        telemetry.update();
        sleep(1000);
    }

    // ---------------- Motion helpers ----------------

    /** Drive forward (positive) or backward (negative) by a number of inches. */
    private void driveInches(double inches, double speed, double timeoutS) {
        int counts = (int) Math.round(inches * COUNTS_PER_INCH);
        moveByCounts(counts, counts, counts, counts, speed, timeoutS, "Drive");
    }

    /** Strafe right (positive) or left (negative) by a number of inches. */
    private void strafeInches(double inches, double speed, double timeoutS) {
        int counts = (int) Math.round(inches * COUNTS_PER_INCH * STRAFE_CORRECTION);
        // Strafe right: LF +, RF -, LB -, RB +
        moveByCounts(counts, -counts, -counts, counts, speed, timeoutS, "Strafe");
    }

    /** Turn clockwise/right (positive) or counterclockwise/left (negative) by degrees. */
    private void turnDegrees(double degrees, double speed, double timeoutS) {
        int counts = (int) Math.round(degrees * COUNTS_PER_DEGREE);
        // Turn right: LF +, RF -, LB +, RB -
        moveByCounts(counts, -counts, counts, -counts, speed, timeoutS, "Turn");
        test = "pikmin 87";
    }

    /**
     * Core RUN_TO_POSITION move. Each argument is the number of encoder counts that
     * wheel should travel relative to its current position (sign = direction).
     */
    private void moveByCounts(int lf, int rf, int lb, int rb,
                              double speed, double timeoutS, String label) {
        if (!opModeIsActive()) return;

        leftFrontDrive.setTargetPosition(leftFrontDrive.getCurrentPosition() + lf);
        rightFrontDrive.setTargetPosition(rightFrontDrive.getCurrentPosition() + rf);
        leftBackDrive.setTargetPosition(leftBackDrive.getCurrentPosition() + lb);
        rightBackDrive.setTargetPosition(rightBackDrive.getCurrentPosition() + rb);

        setDriveMode(DcMotor.RunMode.RUN_TO_POSITION);

        runtime.reset();
        setDrivePower(Math.abs(speed));

        // Keep going while any wheel is still moving, until the timeout
        while (opModeIsActive()
                && runtime.seconds() < timeoutS
                && (leftFrontDrive.isBusy() || rightFrontDrive.isBusy()
                || leftBackDrive.isBusy() || rightBackDrive.isBusy())) {

            telemetry.addData(label, "Running (%.1fs)", runtime.seconds());
            telemetry.addData("Target", "%7d %7d %7d %7d",
                    leftFrontDrive.getTargetPosition(), rightFrontDrive.getTargetPosition(),
                    leftBackDrive.getTargetPosition(), rightBackDrive.getTargetPosition());
            telemetry.addData("Actual", "%7d %7d %7d %7d",
                    leftFrontDrive.getCurrentPosition(), rightFrontDrive.getCurrentPosition(),
                    leftBackDrive.getCurrentPosition(), rightBackDrive.getCurrentPosition());
            telemetry.update();
        }

        setDrivePower(0);
        setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);
        sleep(150); // brief pause between moves
    }

    private void setDriveMode(DcMotor.RunMode mode) {
        leftFrontDrive.setMode(mode);
        rightFrontDrive.setMode(mode);
        leftBackDrive.setMode(mode);
        rightBackDrive.setMode(mode);
    }

    private void setDrivePower(double p) {
        leftFrontDrive.setPower(p);
        rightFrontDrive.setPower(p);
        leftBackDrive.setPower(p);
        rightBackDrive.setPower(p);
    }

    // ---------------- Mechanism sequence ----------------

    /** Spin up the launcher, then run the intake to feed. Tune timings for your robot. */
    private void launchSequence() {
        if (!opModeIsActive()) return;

        launcher.setPower(LAUNCHER_POWER);
        sleep(2000);                          // spin-up time

        intakeServo.setPosition(SERVO_BACKWARD);
        intakeMotor.setPower(1.0);            // feed ball(s) in
        sleep(3000);

        intakeMotor.setPower(0);
        launcher.setPower(0);
        intakeServo.setPosition(SERVO_FORWARD);
    }
}