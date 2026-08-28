package org.firstinspires.ftc.teamcode.PinkBot2;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;


@TeleOp(name="motorTest", group="pink-bot2")
public class motorTest extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();

    private DcMotor frontLeftDrive = null;

    @Override
    public void runOpMode() {
        frontLeftDrive = hardwareMap.get(DcMotor.class, "frontLeft");
        frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);

        waitForStart();
        runtime.reset();

        if (opModeIsActive()) {
            while (opModeIsActive()) {
                if (gamepad1.b){
                    frontLeftDrive.setPower(.5);
                }
                if(gamepad1.a){
                    frontLeftDrive.setPower(0);
                }
            }
            }
    }
}
