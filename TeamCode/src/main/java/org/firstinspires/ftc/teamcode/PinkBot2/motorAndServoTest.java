package org.firstinspires.ftc.teamcode.PinkBot2;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


@TeleOp(name="motorTestServo", group="pink-bot2")
public class motorAndServoTest extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();

    private DcMotor frontLeftDrive = null;

    @Override
    public void runOpMode() {
        frontLeftDrive = hardwareMap.get(DcMotor.class, "frontLeft");
        frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        Servo sickServo = hardwareMap.get(Servo.class, "servo");

        waitForStart();
        runtime.reset();

        if (opModeIsActive()) {
            while (opModeIsActive()) {
                if (gamepad1.b){
                    frontLeftDrive.setPower(.5);
                }
                if (gamepad1.bWasReleased()) { //making the motor stop when the button is released
                    frontLeftDrive.setPower(0); //test comment
                }
                if (gamepad1.x){
                    sickServo.setPosition(1);
                }
                if(gamepad1.y){
                    sickServo.setPosition(0);
                }
            }
        }
    }
}
