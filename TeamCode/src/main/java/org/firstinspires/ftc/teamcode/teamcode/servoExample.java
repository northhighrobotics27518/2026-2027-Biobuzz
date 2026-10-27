/*
Copyright 2026 FIRST Tech Challenge Team 27518

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
associated documentation files (the "Software"), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge, publish, distribute,
sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial
portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package org.firstinspires.ftc.teamcode.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * This file contains a minimal example of a Linear "OpMode". An OpMode is a 'program' that runs
 * in either the autonomous or the TeleOp period of an FTC match. The names of OpModes appear on
 * the menu of the FTC Driver Station. When an selection is made from the menu, the corresponding
 * OpMode class is instantiated on the Robot Controller and executed.
 *
 * Remove the @Disabled annotation on the next line or two (if present) to add this OpMode to the
 * Driver Station OpMode list, or add a @Disabled annotation to prevent this OpMode from being
 * added to the Driver Station.
 */
@TeleOp(name = "servoExample", group = "StarterBot")
@Disabled
public class servoExample extends OpMode {
    private Servo intakeServo = null;
    private static final double SERVO_BACKWARD = 0.25;
    private static final double SERVO_FORWARD = 0.0;
    private static final double SERVO_STOP = 0.175;

    private enum IntakeState { STOP, FORWARD, BACKWARD }
    private IntakeState intakeState = IntakeState.STOP;
    @Override
    public void init() {
        intakeServo = hardwareMap.get(Servo.class, "intakeServo");

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        // Wait for the game to start (driver presses PLAY)
    }
    @Override
    public void init_loop() {
    }
    @Override
    public void start() {
    }
    @Override
    public void loop() {

        if (gamepad2.leftBumperWasPressed()) {
            // Toggle backward: if already backward, turn off; otherwise switch to backward
            intakeState = (intakeState == IntakeState.BACKWARD) ? IntakeState.FORWARD: IntakeState.BACKWARD;
        }

        switch (intakeState) {
            case FORWARD:
                intakeServo.setPosition(SERVO_FORWARD);
                break;
            case BACKWARD:
                intakeServo.setPosition(SERVO_BACKWARD);
                break;
            case STOP:
            default:
                intakeServo.setPosition(SERVO_STOP);
                break;
        }
        // run until the end of the match (driver presses STOP)
    }
}