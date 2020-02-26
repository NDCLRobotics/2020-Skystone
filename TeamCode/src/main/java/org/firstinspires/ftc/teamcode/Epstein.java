package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="Epstein", group="Interactive Opmode")

public class Epstein extends OpMode
{
    public int EpsteinKillHimself = 0;
    private DcMotor GovernmentalMissile = null;

    @Override
    public void init()
    {
        telemetry.addData("say:", "Did Epstein kill himself?");
        telemetry.addData("say:", "Press A for no, B for yes, X for I don't know, and Y for a surprise.");
    }

    @Override
    public void loop()
    {
        if(gamepad1.a && EpsteinKillHimself == 0)
        {
            EpsteinKillHimself = 1;
        }
        if(gamepad1.b && EpsteinKillHimself == 0)
        {
            EpsteinKillHimself = 2;
        }
        if(gamepad1.x && EpsteinKillHimself == 0)
        {
            EpsteinKillHimself = 3;
        }
        if(gamepad1.y && EpsteinKillHimself == 0)
        {
            EpsteinKillHimself = 4;
        }
        if(EpsteinKillHimself == 1)
        {
            telemetry.addData("say:", "Goodbye citizen.");
            GovernmentalMissile.setPower(9000);
        }
        if(EpsteinKillHimself == 2)
        {
            telemetry.addData("say:", "Ah, I see you're a man of culture as well. You may live.");
        }
        if(EpsteinKillHimself == 3)
        {
            telemetry.addData("say:", "Well he did. Learn that if you want to live.");
        }
        if(EpsteinKillHimself == 4)
        {
            telemetry.addData("say:", "He committed toaster bath.");
        }
    }
}
