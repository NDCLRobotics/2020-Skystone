package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.Range;

/*
* --September 24, 2019
* Added power scale
* Set power scale to 0.8
*
* --Spooktober 1, 2019
* Added clawMotor and clawServo
* (currently lines 118-136)
*
* --Spooky Month 7, 2019
*  changed clawServo to a CRServo
*  tested claw setup, yet to adjust parameters
*
* --Spooktober 12, '19
* added limit to clawMotor (claw height)
* added clawDebug mode, which disables limits and sets to a new zero when deactivated
* tweaked clawServo parameters for expansion/contraction
* changed clawServo to setPower in increments over time *UNTESTED*
*
* --Spooktober 16, 2019
* current issue: CR servo treating both positivce and negative the same?
* clawServo needs much fixing, we lack understanding
*
*
* --Spooktober 22, 2019
* clawServo works in a hacky way, hardcoded values for each position
* IF THE CLAW IS DISMANTLED THE CODE NEEDS TO CHANGE (values for clawServo.setPower())
* LB for open claw, RB for wide stone grip, B + RB for tight stone grip
*
* --Spooktober 23, 2019
* adding adjustable powerScale by d-pad
* moved all text output lines to the beginning of the loop
* added a limit to the powerScale
* changed panPower to be dependant on powerScale
*
* --Spooktober 28, 2019
* increased the default claw limit
*
* --Spooktober 29, 2019
* reverted increased claw limit
* added text output for debug mode
*
* --Spooktober 30, 2019
* fixed text output for debug mode
*
* --November 13, 2019
* Adjusted clawLimit for extended claw neck
*
* --December 10, 2019
* added method for inversing drive controls
*
* --February 25, 2020
* added tape measure controls
*
* */



@TeleOp(name="VittoLogisticsTele", group="Interactive Opmode")

public class yeet extends OpMode
{
    // control hub 1
    private DcMotor frontLeftMotor = null;
    private DcMotor frontRightMotor = null;
    private DcMotor backLeftMotor = null;
    private DcMotor backRightMotor = null;

    // control hub 2
    private DcMotor clawMotor = null;
    public CRServo clawServo;
    private DcMotor tapeMotor = null;

    private int clawMotorZero;
    private int tapeMotorZero;

    // variables for debug mode in setting claw height
    private boolean clawDebug = false;
    // switching is a workaround to allow only one frame input
    private boolean switching = false;
    private boolean powerSwitching = false;
    // when activated, the two control sticks will be inverted
    private boolean inverseControls = false;

    private int check = 0;

    // scale down power from max for driving
    private double powerScale = 0.75;



    @Override
    public void init () {
        // control hub 1
        frontLeftMotor = hardwareMap.dcMotor.get("frontLeftMotor");
        frontRightMotor = hardwareMap.dcMotor.get("frontRightMotor");
        backLeftMotor = hardwareMap.dcMotor.get("backLeftMotor");
        backRightMotor = hardwareMap.dcMotor.get("backRightMotor");

        // control hub 2
        clawMotor = hardwareMap.dcMotor.get("clawMotor");
        clawServo = hardwareMap.crservo.get("clawServo");
        tapeMotor = hardwareMap.dcMotor.get("tapeMotor");


        //setting the direction for each motor
        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        backLeftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        clawMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        clawMotorZero = clawMotor.getCurrentPosition();

        tapeMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        tapeMotorZero = tapeMotor.getCurrentPosition();

        clawServo.setDirection(DcMotorSimple.Direction.FORWARD);

        // zero servo
        //clawServoPower = clawServo.getPower();


        telemetry.addData("say: ", "Working, latest updated: 12/11/19 4:43 PM");
    }

    @Override
    public void init_loop() {

    }

    @Override
    public void loop() {

        double frontLeftPower, frontRightPower, backLeftPower, backRightPower;
        double frontLeftPan, frontRightPan, backLeftPan, backRightPan;

        // scale down power from max for panning
        double panPower = powerScale + 0.2;

        // messages displayed on the phone while running
        telemetry.addData("say:", "Working. Last Updated: 02/25/20 4:58 PM");
        telemetry.addData("say:", "Power Scale equals: " + powerScale);
        telemetry.addData("say:", "Servo Power equals: " + clawServo.getPower());
        telemetry.addData("say:", "Current Claw Position: " + clawMotor.getCurrentPosition());
        if (clawDebug == false)
            { telemetry.addData("say:", "Debug mode is off"); } else
            { telemetry.addData("say:", "Debug mode is ON!!!"); }
        if (inverseControls == false)
            { telemetry.addData("say:", "Controls are regular"); } else
            { telemetry.addData("say:", "BEWARE - Controls are REVERSED!!!"); }
        telemetry.addData("say:", "Current Tape Measure Position: " + tapeMotor.getCurrentPosition());

        // increase or decrease powerScale
        if (gamepad1.dpad_up && !powerSwitching)
        {
            powerSwitching = true;
            powerScale += 0.25;
        }
        if (gamepad1.dpad_down && !powerSwitching)
        {
            powerSwitching = true;
            powerScale -= 0.25;
        }
        if (!gamepad1.dpad_down && !gamepad1.dpad_up && powerSwitching)
        {
            powerSwitching = false;
        }


        // clamp powerScale and panPower to [0.25, 1.0]
        if (powerScale > 1.0)
        {
            powerScale = 1.0;
    } else if (powerScale < 0.25)
        {
            powerScale = 0.25;
        }

        if (panPower > 1.0)
        {
            panPower = 1.0;
        } else if (panPower < 0.25)
        {
            panPower = 0.25;
        }


        if (gamepad1.y)
        {
            inverseControls = !inverseControls;
        }

        // limit on how high claw and how far tape measure goes
        int clawLimit = 20000;
        int tapeLimit = 20000;

        //This is where we set the code to the location on the controller
        double drive = gamepad1.left_stick_y;
        double turn = gamepad1.right_stick_x;
        double pan = gamepad1.left_stick_x;
        if (gamepad1.dpad_right && gamepad1.left_stick_x == 0)
        {
            pan = 1;
        }
        if (gamepad1.dpad_left && gamepad1.left_stick_x == 0)
        {
            pan = -1;
        }
        if (!gamepad1.dpad_left && !gamepad1.dpad_right && gamepad1.left_stick_x == 0)
        {
            pan = 0;
        }


        // panning, based on original code for two motor turning, capped at 1 and -1
        if (inverseControls == false)
        {
            frontLeftPan = Range.clip(drive + pan, -1.0, 1.0);
            frontRightPan = Range.clip(drive - pan, -1.0, 1.0);
            backLeftPan = Range.clip(drive + pan, -1.0, 1.0);
            backRightPan = Range.clip(drive - pan, -1.0, 1.0);
            frontLeftMotor.setPower(panPower * frontLeftPan);
            frontRightMotor.setPower(panPower * frontRightPan);
            backLeftMotor.setPower(panPower * backLeftPan);
            backRightMotor.setPower(panPower * backRightPan);

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
        if (inverseControls == true)
        {
            frontLeftPan = Range.clip(-drive - pan, -1.0, 1.0);
            frontRightPan = Range.clip(-drive + pan, -1.0, 1.0);
            backLeftPan = Range.clip(-drive - pan, -1.0, 1.0);
            backRightPan = Range.clip(-drive + pan, -1.0, 1.0);
            frontLeftMotor.setPower(panPower * frontLeftPan);
            frontRightMotor.setPower(panPower * frontRightPan);
            backLeftMotor.setPower(panPower * backLeftPan);
            backRightMotor.setPower(panPower * backRightPan);

            // turning, capped at 1 and -1
            frontLeftPower = Range.clip(-drive + turn, -1.0, 1.0);
            frontRightPower = Range.clip(-drive - turn, -1.0, 1.0);
            backLeftPower = Range.clip(-drive - turn, -1.0, 1.0);
            backRightPower = Range.clip(-drive + turn, -1.0, 1.0);
            frontLeftMotor.setPower(powerScale * frontLeftPower);
            frontRightMotor.setPower(powerScale * frontRightPower);
            backLeftMotor.setPower(powerScale * backLeftPower);
            backRightMotor.setPower(powerScale * backRightPower);
        }





        // claw debug mode
        if (gamepad1.a && !switching)
        {
            switching = true;

            if (clawDebug)
            {
                clawMotorZero = clawMotor.getCurrentPosition();
                //telemetry.addData("say:", "Debug mode is deactivated " + check++);
            }
            else
            {
                //telemetry.addData("say:", "Debug mode is activated " + check++);
            }

            clawDebug = !clawDebug;
        }
        else if (!gamepad1.a && switching)
        {
            switching = false;
        }

        // claw motion up-down
        if (gamepad1.right_trigger > 0 && gamepad1.left_trigger == 0 &&
                /*not fully extended*/ (clawDebug || clawMotor.getCurrentPosition() < (clawMotorZero + clawLimit)))
        {
            clawMotor.setPower(0.5);
        } else
        {
            if (gamepad1.left_trigger > 0 && gamepad1.right_trigger == 0 &&
                    /*not fully collapsed*/ (clawDebug || clawMotor.getCurrentPosition() > clawMotorZero))
            {
                // should be less than up motion
                clawMotor.setPower(-0.1);
            } else
            {
                clawMotor.setPower(0.0);
            }
        }

        // tape measure controls
        if (gamepad1.start)
        {
            if ((tapeMotor.getCurrentPosition() < (tapeMotorZero + tapeLimit)) && (tapeMotor.getCurrentPosition() >= tapeMotorZero))
            {
                tapeMotor.setPower(0.5);
            }
            else
            {
                tapeMotor.setPower(0.0);
            }
        }
        else
        {
            tapeMotor.setPower(0.0);
        }

        // claw grasp
        // setPower for a CR motor is basically setPosition?
        // caps at +/- 1.0

        // open: 0.0
        // wide block: -0.1
        // narrow block: -0.45

        //0.2 = 2.125"

        // right bumper: grab, left bumper: release
        if (gamepad1.left_bumper)
        {
            //clawServo.setDirection(DcMotorSimple.Direction.REVERSE);
            clawServo.setPower(0.15);
            //telemetry.addData("say:", "power +");

        } else if (gamepad1.right_bumper)
        {
            //clawServo.setDirection(DcMotorSimple.Direction.FORWARD);

            if (gamepad1.b)
            {
                // grab stone narrow or capstone
                //telemetry.addData("say:", "flag X");
                clawServo.setPower(-0.55);

            }
            else
            {
                // grab stone wide
                clawServo.setPower(-0.125);
                //telemetry.addData("say:", "flag Y");

            }

            //telemetry.addData("say:", clawServo.getPower());
        } else {
            //clawServo.setPower(0.5);
        }


        //telemetry.addData("say:", "Servo \"power\" set to: " + clawServo.getPower());




    }

    @Override
    public void stop()
    {
        // pls do
    }

}













































//Randomly say hi to me if you see this (from NATHAN)