package org.firstinspires.ftc.teamcode.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
@TeleOp(name = "launcherExample", group = "StarterBot")
@Disabled
public class launcherExample extends OpMode {

    // Declare OpMode members.

    private DcMotor launcher = null;
    double launcherPower = 0.0;

    @Override
    public void init() {

        /*
         * Initialize the hardware variables. Note that the strings used here as parameters
         * to 'get' must correspond to the names assigned during the robot configuration
         * step.
         */

        launcher = hardwareMap.get(DcMotor.class, "launcher");

        // Drive directions set from the wheel test so positive power drives every wheel forward

        launcher.setDirection(DcMotor.Direction.FORWARD);

        /*
         * Setting zeroPowerBehavior to BRAKE enables a "brake mode". This causes the motor to
         * slow down much faster when it is coasting.
         */
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

        // Launcher (gamepad2): right bumper toggles on/off
        if (gamepad2.rightBumperWasPressed()) {
            launcherPower = (launcherPower == 0.0) ? 1.0 : 0.0;
        }

        launcher.setPower(launcherPower);
    }

    @Override
    public void stop() {
    }
}