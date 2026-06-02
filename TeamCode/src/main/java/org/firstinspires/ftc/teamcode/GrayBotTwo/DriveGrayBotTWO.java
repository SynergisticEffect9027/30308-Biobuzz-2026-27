package org.firstinspires.ftc.teamcode.GrayBotTwo;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import java.util.Timer;
/* Copyright (c) 2025 FIRST. All rights reserved.
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



/*
 * This OpMode illustrates how to program your robot to drive field relative. This means
 * that the robot drives the direction you push the joystick regardless of the current orientation
 * of the robot.
 *
 * This OpMode assumes that you have four mecanum wheels each on its own motor named:
 * front_left_motor, front_right_motor, back_left_motor, back_right_motor
 *
 * and that the left motors are flipped such that when they turn clockwise the wheel moves backwards
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list
 *
 */
@TeleOp(name = "Drive! Gray! Bot! TWO!")
//@Disabled
public class DriveGrayBotTWO extends LinearOpMode {
    // This declares the four motors needed
    private DcMotor frontLeftDrive;
    private DcMotor frontRightDrive;
    private DcMotor backLeftDrive;
    private DcMotor backRightDrive;
    //not drive train
    private DcMotor intake;
    private DcMotor bootKicker;
    private DcMotorEx shootOne;
    private DcMotorEx shootTwo;
    private ElapsedTime shootTimer;

    // This declares the IMU needed to get the current direction the robot is facing
    IMU imu;

    @Override
    public void runOpMode() {

        frontLeftDrive = hardwareMap.get(DcMotor.class, "frontLeftDrive");
        frontRightDrive = hardwareMap.get(DcMotor.class, "frontRightDrive");
        backLeftDrive = hardwareMap.get(DcMotor.class, "backLeftDrive");
        backRightDrive = hardwareMap.get(DcMotor.class, "backRightDrive");
        //not drive
        intake = hardwareMap.get(DcMotor.class, "intake");
        bootKicker = hardwareMap.get(DcMotor.class, "bootKicker");
        shootOne = hardwareMap.get(DcMotorEx.class, "shootOne");
        shootTwo = hardwareMap.get(DcMotorEx.class, "shootTwo");
        //Timer
        shootTimer = new ElapsedTime();

// pushServoL.resetDeviceConfigurationForOpMode();
// pushServoR.resetDeviceConfigurationForOpMode();

        // We set the left motors in reverse which is needed for drive trains where the left
        // motors are opposite to the right ones.
        backRightDrive.setDirection(DcMotor.Direction.FORWARD);
        frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        //not drive
        intake.setDirection(DcMotor.Direction.REVERSE);
        bootKicker.setDirection(DcMotor.Direction.FORWARD);
        shootOne.setDirection(DcMotor.Direction.FORWARD);
        shootTwo.setDirection(DcMotor.Direction.REVERSE);

        // This uses RUN_USING_ENCODER to be more accurate. If you don't have the encoder
        // wires, you should remove these
        //frontLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //frontRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //backLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        // backRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); //Maybe change to not using encoder
        frontRightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); //we did! Arabella + Layla 11/12/25 4:18 p.m.
        backLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        imu = hardwareMap.get(IMU.class, "imu");
        // This needs to be changed to match the orientation on your robot
        RevHubOrientationOnRobot.LogoFacingDirection logoDirection =
                RevHubOrientationOnRobot.LogoFacingDirection.LEFT;
        RevHubOrientationOnRobot.UsbFacingDirection usbDirection =
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD;

        RevHubOrientationOnRobot orientationOnRobot = new
                RevHubOrientationOnRobot(logoDirection, usbDirection);
        imu.initialize(new IMU.Parameters(orientationOnRobot));

        waitForStart();

        while (opModeIsActive()) {


            double targetRPM = 5000;
            double targetRPMSlide = 435; //maybe bad, came from goBilda website
            double ticksPerRev = 28;
            //linearSlideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            // If you press the A button, then you reset the Yaw to be zero from the way
            // the robot is currently pointing
// if (gamepad1.a) {
// imu.resetYaw();
// }
            // If you press the left bumper, you get a drive from the point of view of the robot
            // (much like driving an RC vehicle)
            //intake forward
            if (gamepad1.a){
                intake.setPower(0.7);
                drive(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x);
            }
            //intake backwards
            if (gamepad1.right_bumper){
                intake.setPower(-0.7);
                drive(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x);
            }
            //stop intake
            if (gamepad1.aWasReleased() || gamepad1.rightBumperWasReleased()){
                intake.setPower(0);
                drive(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x);
            }

            //bootkicker forward
            if (gamepad1.b){
                bootKicker.setPower(0.8);
                drive(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x);
            }
            //bootkicker bckwards
            if (gamepad1.left_bumper){
                bootKicker.setPower(-0.8);
                drive(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x);
            }
            //stop bootkicker
            if (gamepad1.leftBumperWasReleased() || gamepad1.bWasReleased()){
                bootKicker.setPower(0);
                drive(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x);
            }

            //shoot forward
            if (gamepad1.x) {
                shootTimer.reset();
                while (shootTimer.seconds() < 10) {
                    shootOne.setVelocity(targetRPM / ticksPerRev);
                    shootTwo.setVelocity(targetRPM / ticksPerRev);
                    if (shootTimer.seconds() >= 4){
                        shootOne.setVelocity(targetRPM / ticksPerRev);
                        shootTwo.setVelocity(targetRPM / ticksPerRev);
                        bootKicker.setPower(0.8);
                        break;
                    }
                }
                drive(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x);
            }
            //shoot backwards
//            if (gamepad1.y) {
//                shootOne.setPower(-0.8);
//                shootTwo.setPower(-0.8);
//                drive(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x);
//            }
            //stop shoot
            if (gamepad1.xWasReleased()) {
                shootOne.setVelocity(0);
                shootTwo.setVelocity(0);
                bootKicker.setPower(0);
                drive(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x);
            }


            drive(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x);


        }
    }


    // This routine drives the robot field relative
    private void driveFieldRelative(double forward, double right, double rotate) {
        // First, convert direction being asked to drive to polar coordinates
        double theta = Math.atan2(forward, right);
        double r = Math.hypot(right, forward);

        // Second, rotate angle by the angle the robot is pointing
        theta = AngleUnit.normalizeRadians(theta -
                imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS));

        // Third, convert back to cartesian
        double newForward = r * Math.sin(theta);
        double newRight = r * Math.cos(theta);

        // Finally, call the drive method with robot relative forward and right amounts
        drive(newForward, newRight, rotate);
    }

    // Thanks to FTC16072 for sharing this code!!
    public void drive(double forward, double right, double rotate) {

        //axial = forward, lateral = right, yaw = rotate
        // if (gamepad1.left_trigger > 0.000) {
        //forward = forward * 0.65;
        //right = right * 0.65;
        //rotate = rotate * 0.65; //comment theses three lines out
        //}
// if (gamepad1.left_trigger > 0.000 && gamepad1.left_trigger < 0.001) {
// forward = forward / 0.55;
// right = right / 0.55;
// rotate = rotate / 0.55;
// }

        // This calculates the power needed for each wheel based on the amount of forward,
        // strafe right, and rotate
        double frontLeftPower = forward + right + rotate;
        double frontRightPower = forward - right - rotate;
        double backRightPower = forward + right - rotate;
        double backLeftPower = forward - right + rotate;

        double maxPower = 0.9;
        double maxSpeed = 1.0; // make this slower for outreaches

        // This is needed to make sure we don't pass > 1.0 to any wheel
        // It allows us to keep all of the motors in proportion to what they should
        // be and not get clipped
        maxPower = Math.max(maxPower, Math.abs(frontLeftPower));
        maxPower = Math.max(maxPower, Math.abs(frontRightPower));
        maxPower = Math.max(maxPower, Math.abs(backRightPower));
        maxPower = Math.max(maxPower, Math.abs(backLeftPower));

        // We multiply by maxSpeed so that it can be set lower for outreaches
        // When a young child is driving the robot, we may not want to allow full
        // speed.

// if (gamepad1.right_bumper) {
// frontLeftDrive.setPower(0.5 * (frontLeftPower / 0.5));
// frontRightDrive.setPower(0.5 * (frontRightPower / 0.5));
// backLeftDrive.setPower(0.5 * (backLeftPower / 0.5));
// backRightDrive.setPower(0.5 * (backRightPower / 0.5));

        frontLeftDrive.setPower(maxSpeed * (frontLeftPower / maxPower));
        frontRightDrive.setPower(maxSpeed * (frontRightPower / maxPower));
        backLeftDrive.setPower(maxSpeed * (backLeftPower / maxPower));
        backRightDrive.setPower(maxSpeed * (backRightPower / maxPower));

// telemetry.addData("linear slide pos to go: ", linearSlideMotor.getTargetPosition());
// telemetry.update();

    }
}

