package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="VittoLogisticsOriginal", group="Interactive Opmode")



public class robot1 extends OpMode {
    private DcMotor leftMotor = null;
    private DcMotor rightMotor = null;

    @Override
    public void init () {
        leftMotor = hardwareMap.dcMotor.get("MotorLeft");
        rightMotor = hardwareMap.dcMotor.get("MotorRight");
        leftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        rightMotor.setDirection(DcMotorSimple.Direction.FORWARD);


        telemetry.addData("say: ", "Working unlike Little Mac's recovery");
    }

    @Override
    public void init_loop() {

    }

    @Override
    public void loop() {
        double leftPower, rightPower;

        double drive = gamepad1.left_stick_y;
        double turn = gamepad1.right_stick_x;


        leftPower = Range.clip(drive + turn, -1.0, 1.0);
        rightPower = Range.clip(drive - turn, -1.0, 1.0);
        leftMotor.setPower(leftPower);
        rightMotor.setPower(rightPower);

    }

    @Override
    public void stop() {

    }
}