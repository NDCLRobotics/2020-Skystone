package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.Range;

/*
* --Spooktober 29, 2019
* initial commit
* define variable definitions for motors
*
* --Spooktober 30, 2019
* simplified drive, pan, and turn functions
* building a "drive X distance" function
* troubleshooted velocity
*
* */

@Autonomous(name="VittoLogisticsAuto", group="Iterative Opmode")

public class VittoLogisticsAuto extends OpMode
{

    private long loopCount;
    private long initTime;
    private long finalTime;


    public double driveDist;
    public double inPerLoop = 0.19;


    //control hub 1
    private DcMotor frontLeftMotor = null;
    private DcMotor frontRightMotor = null;
    private DcMotor backLeftMotor = null;
    private DcMotor backRightMotor = null;

    //control hub 2
    private DcMotor clawMotor = null;
    public CRServo clawServo;

    //functions for actions of the robot
    //distance is measured in inches
    public double drive (double distance)
    {
        /*frontLeftMotor.setPower(distance);
        frontRightMotor.setPower(distance);
        backLeftMotor.setPower(distance);
        backRightMotor.setPower(distance);*/

        if (distance == 0)
        {
            frontLeftMotor.setPower(0.0);
            frontRightMotor.setPower(0.0);
            backLeftMotor.setPower(0.0);
            backRightMotor.setPower(0.0);
        }
        else if (distance > 0)
        {
            frontLeftMotor.setPower(0.6);
            frontRightMotor.setPower(0.6);
            backLeftMotor.setPower(0.6);
            backRightMotor.setPower(0.6);

            distance -= inPerLoop;

            if (distance < 0) { distance = 0; }
        }
        else if (distance < 0)
        {
            frontLeftMotor.setPower(-0.6);
            frontRightMotor.setPower(-0.6);
            backLeftMotor.setPower(-0.6);
            backRightMotor.setPower(-0.6);

            distance += inPerLoop;

            if (distance > 0) { distance = 0; }
        }


        //telemetry.addData("say:", "driveForward ended");

        return distance;
    }

    public void turn (double angle)
    {

    }

    public void pan (double distance)
    {

    }


    @Override
    public void init ()
    {
        telemetry.addData("say:", "Working. Last Updated: 10/30/19 4:43 PM");

        loopCount = 0;

        //control hub 1
        frontLeftMotor = hardwareMap.dcMotor.get("frontLeftMotor");
        frontRightMotor = hardwareMap.dcMotor.get("frontRightMotor");
        backLeftMotor = hardwareMap.dcMotor.get("backLeftMotor");
        backRightMotor = hardwareMap.dcMotor.get("backRightMotor");

        //control hub 2
        clawMotor = hardwareMap.dcMotor.get("clawMotor");
        clawServo = hardwareMap.crservo.get("clawServo");

        //setting the direction for each motor
        frontLeftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        clawMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        clawServo.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    @Override
    public void loop ()
    {
        loopCount++;

        if (loopCount == 1)
        {
            driveDist = 75; // move 75 inches
            initTime = System.currentTimeMillis();
        }



        // move until driveDist is reached
        if (driveDist > 0)
        {
            telemetry.addData("say:", "Drive Distance: " + driveDist);
            driveDist = drive(driveDist);
        }
        else
        {
            telemetry.addData("say:", "Finished moving.");
            driveDist = drive(driveDist);
        }
    }

    @Override
    public void stop ()
    {
        // please do kind sir
    }

}

//Approx. Milliseconds per Loop: 6.54
//Average Speed is Approx.: 32.458 Inches/Second
//0.2122 Inches/Loop