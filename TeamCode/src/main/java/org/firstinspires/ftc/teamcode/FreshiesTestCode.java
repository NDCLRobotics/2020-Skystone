package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="2020 Freshies Test Code", group="Interactive Opmode")

public class FreshiesTestCode extends OpMode
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

        //setting the direction for each motor
        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        backLeftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        // when we test robot, look at this and switch direction of back motors



        telemetry.addData("say: ", "Working");
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
        frontLeftPan = Range.clip(drive + pan, -1.0, 1.0);
        frontRightPan = Range.clip(drive - pan, -1.0, 1.0);
        backLeftPan = Range.clip(drive + pan, -1.0, 1.0);
        backRightPan = Range.clip(drive - pan, -1.0, 1.0);
        frontLeftMotor.setPower(powerScale * frontLeftPan);
        frontRightMotor.setPower(powerScale * frontRightPan);
        backLeftMotor.setPower(powerScale * backLeftPan);
        backRightMotor.setPower(powerScale * backRightPan);

        // turning, capped at 1 and -1
        frontLeftPower = Range.clip(drive + turn, -1.0, 1.0);
        frontRightPower = Range.clip(drive - turn, -1.0, 1.0);
        backLeftPower = Range.clip(drive - turn, -1.0, 1.0);
        backRightPower = Range.clip(drive + turn, -1.0, 1.0);
        frontLeftMotor.setPower(powerScale * frontLeftPower);
        frontRightMotor.setPower(powerScale * frontRightPower);
        backLeftMotor.setPower(powerScale * backLeftPower);
        backRightMotor.setPower(powerScale * backRightPower);

    }

    @Override
    public void stop() {

    }

}
