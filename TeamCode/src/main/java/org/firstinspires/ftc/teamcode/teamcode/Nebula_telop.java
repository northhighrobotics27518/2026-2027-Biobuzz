package org.firstinspires.ftc.teamcode.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Nebula_telop", group = "StarterBot")
public class Nebula_telop extends OpMode {

    // Declare OpMode members.
    private DcMotor leftFrontDrive = null;
    private DcMotor rightFrontDrive = null;
    private DcMotor leftBackDrive = null;
    private DcMotor rightBackDrive = null;
    private DcMotor intakeMotor = null;
    private DcMotor launcher = null;
    private Servo windmillServo = null;
    private Servo rightServo = null;
    private Servo leftServo = null;
    // Set up a variable for each drive wheel to save power level for telemetry.
    double leftFrontPower;
    double rightFrontPower;
    double leftBackPower;
    double rightBackPower;
    double intakeMotorPower = 0.0;
    double launcherPower = 0.0;

    @Override
    public void init() {

        /*
         * Initialize the hardware variables. Note that the strings used here as parameters
         * to 'get' must correspond to the names assigned during the robot configuration
         * step.
         */
        leftFrontDrive = hardwareMap.get(DcMotor.class, "left_front_drive");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "right_front_drive");
        leftBackDrive = hardwareMap.get(DcMotor.class, "left_back_drive");
        rightBackDrive = hardwareMap.get(DcMotor.class, "right_back_drive");
        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
        launcher = hardwareMap.get(DcMotor.class, "launcher");
        windmillServo = hardwareMap.get(Servo.class, "windmillServo");
        rightServo = hardwareMap.get(Servo.class, "rightServo");
        leftServo = hardwareMap.get(Servo.class, "leftServo");

        // Drive directions set from the wheel test so positive power drives every wheel forward
        leftFrontDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        rightFrontDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        rightBackDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        intakeMotor.setDirection(DcMotor.Direction.FORWARD);
        launcher.setDirection(DcMotor.Direction.FORWARD);
        windmillServo.setDirection(Servo.Direction.FORWARD);
        rightServo.setDirection(Servo.Direction.FORWARD);
        leftServo.setDirection(Servo.Direction.REVERSE);

        /*
         * Setting zeroPowerBehavior to BRAKE enables a "brake mode". This causes the motor to
         * slow down much faster when it is coasting.
         */
        leftFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        launcher.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
    }

    @Override
    public void loop() {
        /*
         * Drive controls (gamepad1):
         *   Left stick up/down    -> forward/backward
         *   Left stick left/right -> strafe left/right
         *   Right stick left/right -> turn left/right
         */
        double forward = gamepad1.left_stick_y;
        double strafe = -gamepad1.left_stick_x;
        double turn = -gamepad1.right_stick_x;

        mecanumDrive(forward, strafe, turn);

        // Intake motor (gamepad2): right trigger = in, left trigger = out
        launcherPower = (gamepad2.right_trigger > 0.5) ? 0.74 : 0.0;
        intakeMotorPower = (gamepad2.left_trigger > 0.5) ? 1.0 : 0.0;

        launcher.setPower(launcherPower);
        if (launcherPower != 0) {
            windmillServo.setPosition(1);
        }
        // Launcher (gamepad2): right bumper toggles on/off
        intakeMotor.setPower(intakeMotorPower);
        if (intakeMotorPower == 1) {
            rightServo.setPosition(intakeMotorPower);
            leftServo.setPosition(intakeMotorPower);
        }

        /*
         * Show motor powers on the Driver Station via telemetry.
         */
        telemetry.addData("Motors", "FL (%.2f), FR (%.2f), BL(%.2f), BR(%.2f)",
                leftFrontPower, rightFrontPower, leftBackPower, rightBackPower);
        telemetry.addData("Intake Motor Power", "%.2f", intakeMotorPower);
    }

    @Override
    public void stop() {
    }

    void mecanumDrive(double forward, double strafe, double rotate) {

        /* The denominator is the largest motor power (absolute value) or 1.
         * This ensures all the powers maintain the same ratio,
         * but only if at least one is out of the range [-1, 1].
         */
        double denominator = Math.max(Math.abs(forward) + Math.abs(strafe) + Math.abs(rotate), 1);

        leftFrontPower = (forward - strafe + rotate) / denominator;
        rightFrontPower = (forward + strafe - rotate) / denominator;
        leftBackPower = (forward + strafe + rotate) / denominator;
        rightBackPower = (forward - strafe - rotate) / denominator;

        leftFrontDrive.setPower(leftFrontPower);
        rightFrontDrive.setPower(rightFrontPower);
        leftBackDrive.setPower(leftBackPower);
        rightBackDrive.setPower(rightBackPower);
    }
}