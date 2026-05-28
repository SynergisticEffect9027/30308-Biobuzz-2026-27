package org.firstinspires.ftc.teamcode.PinkBot2;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="shooterTest-pinkBot2", group="pink-bot2")
public class shooterTest extends LinearOpMode{
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotorEx shooterA;
    private DcMotorEx shooterB;

    private DcMotor passThrough = null;

    double step = 0;
    public ElapsedTime lancherTimer;

    @Override
    public void runOpMode() {
        lancherTimer = new ElapsedTime();
        shooterA = hardwareMap.get(DcMotorEx.class, "shooterA");
        shooterB = hardwareMap.get(DcMotorEx.class, "shooterB");
        passThrough = hardwareMap.get(DcMotorEx.class, "passThrough");

        shooterA.setDirection(DcMotorEx.Direction.FORWARD);
        shooterB.setDirection(DcMotor.Direction.REVERSE);
        passThrough.setDirection(DcMotor.Direction.FORWARD);

        shooterA.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooterB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        passThrough.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterA.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();

        if (opModeIsActive()) {
            while (opModeIsActive()) {
                double targetRPM = 0; //shooter
                double ticksPerRev = 28;
                double dis = 0;
                targetRPM = 4500;
                dis = (targetRPM * ticksPerRev) / 60;
                if (gamepad2.b){
                    shooterB.setVelocity(dis);
                    shooterA.setVelocity(dis);
                    step = 1;
                }
                while (step == 1){
                    telemetry.addData("set",dis);
                    telemetry.addData("velocity",shooterA.getVelocity());
                    telemetry.addData("velocity",shooterB.getVelocity());
                    telemetry.update();
                    if (shooterA.getVelocity() >= (dis) && shooterB.getVelocity() >= (dis)){
                        lancherTimer.reset();
                        passThrough.setPower(.5);
                        step = 2;
                    }
                }
                while (step == 2){
                    if (lancherTimer.seconds() > 4){
                        passThrough.setPower(0);
                        shooterB.setVelocity(0);
                        shooterA.setVelocity(0);
                        step = 0;
                    }
                }
            }
        }
    }
}
