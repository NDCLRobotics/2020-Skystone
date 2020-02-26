package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

/*
 * Today's Changes (September 25, 2019) [DRIFT CODE, NOT OFFICIAL]
 * Messed around with code to add panning
 * Set power scale to 0.1 to test
 * Changed that back to 0.7 because by gosh it was slow
 *
 * */



@TeleOp(name="VittoLogisticsDrift", group="Interactive Opmode")

public class drift extends OpMode
{
    private DcMotor frontLeftMotor = null;
    private DcMotor frontRightMotor = null;
    private DcMotor backLeftMotor = null;
    private DcMotor backRightMotor = null;

    @Override
    public void init () {
        frontLeftMotor = hardwareMap.dcMotor.get("frontLeftMotor");
        frontRightMotor = hardwareMap.dcMotor.get("frontRightMotor");
        backLeftMotor = hardwareMap.dcMotor.get("backLeftMotor");
        backRightMotor = hardwareMap.dcMotor.get("backRightMotor");

        //setting the direction for each motor.JBJJHGT
        frontLeftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        // when we test robot, look at this and switch direction of back motors yeet



        telemetry.addData("say: ", "Working, latest change: scalePower");
    }

    @Override
    public void init_loop() {

    }

    @Override
    public void loop() {
        double frontLeftPower, frontRightPower, backLeftPower, backRightPower;
        double frontLeftPan, frontRightPan, backLeftPan, backRightPan;

        // scale down power from max
        double powerScale = 1.0;

        //This is where we set the code to the location on the controller
        double drive = gamepad1.left_stick_y;
        double pan = gamepad1.left_stick_x;
        double turn = gamepad1.right_stick_x;


        // panning, based on original code for two motor turning, capped at 1 and -1
        frontLeftPan = Range.clip(drive + turn, -1.0, 1.0);
        frontRightPan = Range.clip(drive - turn, -1.0, 1.0);
        backLeftPan = Range.clip(drive + turn, -1.0, 1.0);
        backRightPan = Range.clip(drive - turn, -1.0, 1.0);
        frontLeftMotor.setPower(powerScale * frontLeftPan);
        frontRightMotor.setPower(powerScale * frontRightPan);
        backLeftMotor.setPower(powerScale * backLeftPan);
        backRightMotor.setPower(powerScale * backRightPan);

        // turning, capped at 1 and -1
        frontLeftPower = Range.clip(drive - pan, -1.0, 1.0);
        frontRightPower = Range.clip(drive + pan, -1.0, 1.0);
        backLeftPower = Range.clip(drive + pan, -1.0, 1.0);
        backRightPower = Range.clip(drive - pan, -1.0, 1.0);
        frontLeftMotor.setPower(powerScale * frontLeftPower);
        frontRightMotor.setPower(powerScale * frontRightPower);
        backLeftMotor.setPower(powerScale * backLeftPower);
        backRightMotor.setPower(powerScale * backRightPower);

    }

    @Override
    public void stop() {

    }

}
