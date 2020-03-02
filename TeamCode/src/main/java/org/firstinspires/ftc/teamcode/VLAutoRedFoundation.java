/* Copyright (c) 2019 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.ArrayList;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
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
 * --November 1, 2019
 * began implementing TensorFlow
 * to do: use coordinates to fine-tune movement and attempt block pickup
 *
 * --November 4, 2019
 * began defining turn commands in order to attempt block pickup
 *
 * --November 5, 2019
 * began trial and error on picking up blocks
 *
 * --November 6, 2019
 * fixed error with C_RAISE command of claw
 *
 * */

/**
 * This 2019-2020 OpMode illustrates the basics of using the TensorFlow Object Detection API to
 * determine the position of the Skystone game elements.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list.
 *
 * IMPORTANT: In order to use this OpMode, you need to obtain your own Vuforia license key as
 * is explained below.
 */
@Autonomous(name = "VLAuto: Red Side Foundation Move", group = "Concept")
//@Disabled
public class VLAutoRedFoundation extends LinearOpMode {
    private static final String D_FORWARD = "forward";
    private static final String D_BACKWARD = "backward";
    private static final String D_STOP = "stop";
    private static final String C_OPEN = "claw open";
    private static final String C_GRAB = "claw grab";
    private static final String C_TIGHT = "claw tight";
    private static final String C_RAISE = "raise claw";
    private static final String C_LOWER = "lower claw";
    private static final String C_REST = "stop lifting claw";
    private static final String P_LEFT = "pan left";
    private static final String P_RIGHT = "pan right";
    private static final String P_STOP = "pan stop";
    private static final String T_LEFT = "turn left";
    private static final String T_RIGHT = "turn right";
    private static final String T_STOP = "turn stop";


    private long loopCount = 0;
    private long initTime;
    private long finalTime;


    public double driveDist;
    public double inPerLoop = 0.19;

    public boolean spotted = false;
    public boolean grabbing = false;
    public boolean grabbed = false;

    public int tightening = 0;


    //control hub 1
    private DcMotor frontLeftMotor = null;
    private DcMotor frontRightMotor = null;
    private DcMotor backLeftMotor = null;
    private DcMotor backRightMotor = null;

    //control hub 2
    private DcMotor clawMotor = null;
    public CRServo clawServo;

    //claw limits
    int clawLimit = 300;
    int clawMotorZero;



    public void drive (String fb)
    {
        if (fb.equals(D_FORWARD))
        {
            frontLeftMotor.setPower(0.3);
            frontRightMotor.setPower(0.3);
            backLeftMotor.setPower(0.3);
            backRightMotor.setPower(0.3);
        }
        if (fb.equals(D_BACKWARD))
        {
            frontLeftMotor.setPower(-0.3);
            frontRightMotor.setPower(-0.3);
            backLeftMotor.setPower(-0.3);
            backRightMotor.setPower(-0.3);
        }
        if (fb.equals(D_STOP))
        {
            frontLeftMotor.setPower(0);
            frontRightMotor.setPower(0);
            backLeftMotor.setPower(0);
            backRightMotor.setPower(0);
        }
    }


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

    public void turn (String lr)
    {
        if(lr.equals(T_RIGHT))
        {
            frontLeftMotor.setPower(-0.15);
            frontRightMotor.setPower(0.15);
            backLeftMotor.setPower(0.15);
            backRightMotor.setPower(-0.15);
        }
        if(lr.equals(T_LEFT))
        {
            frontLeftMotor.setPower(0.15);
            frontRightMotor.setPower(-0.15);
            backLeftMotor.setPower(-0.15);
            backRightMotor.setPower(0.15);
        }
        if(lr.equals(T_STOP))
        {
            frontLeftMotor.setPower(0);
            frontRightMotor.setPower(0);
            backLeftMotor.setPower(0);
            backRightMotor.setPower(0);
        }
    }

    public void pan (String lr)
    {
        if(lr.equals(P_RIGHT))
        {
            frontLeftMotor.setPower(-0.3);
            frontRightMotor.setPower(0.3);
            backLeftMotor.setPower(-0.3);
            backRightMotor.setPower(0.3);
        }
        if(lr.equals(P_LEFT))
        {
            frontLeftMotor.setPower(0.3);
            frontRightMotor.setPower(-0.3);
            backLeftMotor.setPower(0.3);
            backRightMotor.setPower(-0.3);
        }
        if(lr.equals(P_STOP))
        {
            frontLeftMotor.setPower(0);
            frontRightMotor.setPower(0);
            backLeftMotor.setPower(0);
            backRightMotor.setPower(0);
        }
    }

    public void claw (String clamp)
    {
        if(clamp.equals(C_GRAB))
        {
            clawServo.setPower(-0.125);
        }
        if(clamp.equals(C_OPEN))
        {
            clawServo.setPower(0.15);
        }
        if(clamp.equals(C_TIGHT))
        {
            clawServo.setPower(-0.5);
        }
        if(clamp.equals(C_RAISE))
        {
            if (clawMotor.getCurrentPosition() < clawMotorZero + clawLimit)
            {
                clawMotor.setPower(0.5);
            } else
            {
                clawMotor.setPower(0.0);
            }

        }
        if(clamp.equals(C_LOWER))
        {
            if (clawMotor.getCurrentPosition() > clawMotorZero)
            {
                // should be less than up motion
                clawMotor.setPower(-0.1);
            } else
            {
                clawMotor.setPower(0.0);
            }
        }
        if(clamp.equals(C_REST))
        {
            clawMotor.setPower(0.0);
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
    private static final String TFOD_MODEL_ASSET = "Skystone.tflite";
    private static final String LABEL_STONE = "Stone";
    private static final String LABEL_SKYSTONE = "Skystone";

    /*
     * IMPORTANT: You need to obtain your own license key to use Vuforia. The string below with which
     * 'parameters.vuforiaLicenseKey' is initialized is for illustration only, and will not function.
     * A Vuforia 'Development' license key, can be obtained free of charge from the Vuforia developer
     * web site at https://developer.vuforia.com/license-manager.
     *
     * Vuforia license keys are always 380 characters long, and look as if they contain mostly
     * random data. As an example, here is a example of a fragment of a valid key:
     *      ... yIgIzTqZ4mWjk9wd3cZO9T1axEqzuhxoGlfOOI2dRzKS4T0hQ8kT ...
     * Once you've obtained a license key, copy the string from the Vuforia web site
     * and paste it in to your code on the next line, between the double quotes.
     */
    private static final String VUFORIA_KEY =
            "AX54Lyj/////AAABmSsIALipi0y4oiZBAoZS4o4Jppp+qbLTWgVQVVuyveVi7sLhVC8XAwvTGDzKpxm1tiMRMLgYEV3Y5YXvqKMiA7R7TUZQcZeyL9MMGoqcq7rIeFMX01KOuZUmfs754hgbnsINn38JjhLLAH3g2GuKF9QZBF/CJqw/UFKKzR8bDlv4TkkTP8AyxvF9Vyv9G9gQhK2HoOWuSCWQHzIWl+op5LEPLXU7RmdrWzxDm1zEY3DZoax5pYLMRR349NoNzpUFBzwNu+nmEzT3eXQqtppz/vE/gHA0LRys9MAktPmeXQfvaS2YUi4UdE4PcFxfCUPuWe6L9xOQmUBE7hB39jTRkYxGADmTxILyBZB6fD3qyFHv";

    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    private VuforiaLocalizer vuforia;

    /**
     * {@link #tfod} is the variable we will use to store our instance of the TensorFlow Object
     * Detection engine.
     */
    private TFObjectDetector tfod;


    @Override
    public void runOpMode() {
        ///////////////////////////////////////////////////////////////////////////////////////start
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

        clawMotorZero = clawMotor.getCurrentPosition();

        ////////////////////////////////////////////////////////////////////////////////////////////

        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.
        initVuforia();

        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }

        /**
         * Activate TensorFlow Object Detection before we wait for the start command.
         * Do it here so that the Camera Stream window will have the TensorFlow annotations visible.
         **/
        if (tfod != null) {
            tfod.activate();
        }

        /** Wait for the game to begin */
        telemetry.update();
        waitForStart();

        double middle_x, middle_y;

        if (opModeIsActive()) {
            initTime = System.currentTimeMillis();
            while (opModeIsActive()) {
                if (tfod != null) {
                    // getUpdatedRecognitions() will return null if no new information is available since
                    // the last time that call was made.
                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                    List<String> labels = new ArrayList<String>();
                    if (updatedRecognitions != null) {
                        telemetry.addData("# Object Detected", updatedRecognitions.size());

                        // step through the list of recognitions and display boundary info.
                        int i = 0;
                        for (Recognition recognition : updatedRecognitions) {
                            telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                            telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                                    recognition.getLeft(), recognition.getTop());
                            telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                                    recognition.getRight(), recognition.getBottom());
                            labels.add(recognition.getLabel());
                        }
                        telemetry.update();


                        middle_x = 400;
                        middle_y = 600;

                        loopCount += 1;


                        // to do: use coordinates to fine-tune movement and attempt block pickup

                        finalTime = System.currentTimeMillis() - initTime;

                        telemetry.addData("Loop count:", +loopCount);
                        telemetry.addData("Time is:", finalTime);
                        telemetry.addData("Ms/loop", finalTime / loopCount);
                        telemetry.addData(">", "Version ID: 20200302-1");
                        telemetry.addData(">", "Latest Commit: 2020-03-02 16:42");
                        /* if (autonomous == true)
                        {
                            do.autonomous;
                        }
                         */
                        if (finalTime > 0 && finalTime < 3500) {
                            pan(P_RIGHT);
                            claw(C_GRAB);
                        }
                        if (finalTime > 3500 && finalTime < 6500) {
                            pan(P_STOP);
                            drive(D_FORWARD);
                            claw(C_RAISE);
                        }
                        if (finalTime > 6500 && finalTime < 8500) {
                            drive(D_STOP);
                            claw(C_LOWER);
                        }
                        if (finalTime > 8500 && finalTime < 12500) {
                            claw(C_REST);
                            drive(D_BACKWARD);
                        }
                        if (finalTime > 12500 && finalTime < 14500) {
                            drive(D_STOP);
                            claw(C_RAISE);
                        }
                        if (finalTime > 14500 && finalTime < 16500) {
                            claw(C_REST);
                            pan(P_LEFT);
                        }
                        if (finalTime > 16500 && finalTime < 18500) {
                            claw(C_LOWER);
                        }
                        if (finalTime > 18500 && finalTime < 19500) {
                            claw(C_REST);
                            pan(P_LEFT);
                        }
                        //temporary stop code
                        if (finalTime > 19500)
                        {
                            drive(D_STOP);
                            pan(P_STOP);
                            claw(C_REST);
                        }
                    }
                }
            }
        }

        if (tfod != null) {
            tfod.shutdown();
        }
    }

    /**
     * Initialize the Vuforia localization engine.
     */
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = CameraDirection.BACK;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }

    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minimumConfidence = 0.8;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_STONE, LABEL_SKYSTONE);
    }
}

//Time 1: 174.6 ms/loop
//Time 2: 162.1 ms/loop
//Time 3: 150.9 ms/loop
