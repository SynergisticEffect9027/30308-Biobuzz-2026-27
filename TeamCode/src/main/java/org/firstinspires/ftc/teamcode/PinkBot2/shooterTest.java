package org.firstinspires.ftc.teamcode.PinkBot2;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="shooterTest-pinkBot2", group="pink-bot2")
public class shooterTest extends LinearOpMode{
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontLeftDrive = null;
    private DcMotor backLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backRightDrive = null;
    private DcMotorEx shooterA;
    private DcMotorEx shooterB;

    private DcMotor passThrough = null;

    double step = 0;
    public ElapsedTime lancherTimer;

    @Override
    public void runOpMode() {

        frontLeftDrive = hardwareMap.get(DcMotor.class, "frontLeft");
        backLeftDrive = hardwareMap.get(DcMotor.class, "backLeft");
        frontRightDrive = hardwareMap.get(DcMotor.class, "frontRight");
        backRightDrive = hardwareMap.get(DcMotor.class, "backRight");

        frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        backLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
        backRightDrive.setDirection(DcMotor.Direction.FORWARD);

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
                double max;

                // POV Mode uses left joystick to go forward & strafe, and right joystick to rotate.
                double axial   = -gamepad1.left_stick_y;  // Note: pushing stick forward gives negative value
                double lateral =  gamepad1.left_stick_x;
                double yaw     =  gamepad1.right_stick_x;

                // Combine the joystick requests for each axis-motion to determine each wheel's power.
                // Set up a variable for each drive wheel to save the power level for telemetry.
                double frontLeftPower  = axial + lateral + yaw;
                double frontRightPower = axial - lateral - yaw;
                double backLeftPower   = axial - lateral + yaw;
                double backRightPower  = axial + lateral - yaw;

                // Normalize the values so no wheel power exceeds 100%
                // This ensures that the robot maintains the desired motion.
                max = Math.max(Math.abs(frontLeftPower), Math.abs(frontRightPower));
                max = Math.max(max, Math.abs(backLeftPower));
                max = Math.max(max, Math.abs(backRightPower));

                if (max > 1.0) {
                    frontLeftPower  /= max;
                    frontRightPower /= max;
                    backLeftPower   /= max;
                    backRightPower  /= max;
                }

                frontLeftDrive.setPower(frontLeftPower);
                frontRightDrive.setPower(frontRightPower);
                backLeftDrive.setPower(backLeftPower);
                backRightDrive.setPower(backRightPower);

                PIDFCoefficients ava = new PIDFCoefficients(96, 0, 0, 12.227);

                shooterA.setPIDFCoefficients(DcMotorEx.RunMode.RUN_USING_ENCODER, ava);
                shooterB.setPIDFCoefficients(DcMotorEx.RunMode.RUN_USING_ENCODER, ava);

                double targetRPM = 0; //shooter
                double ticksPerRev = 28;
                double dis = 0;
                targetRPM = 6000;
                dis = (targetRPM * ticksPerRev) / 60;
                if (gamepad2.b){
                    lancherTimer.reset();
                    shooterB.setVelocity(dis);
                    shooterA.setVelocity(dis);
                    step = 1;
                }
                while (step == 1){
                    telemetry.addData("set",dis);
                    telemetry.addData("time",lancherTimer.seconds());
                    telemetry.addData("velocity",shooterA.getVelocity());
                    telemetry.addData("velocity",shooterB.getVelocity());
                    telemetry.update();
                    if (lancherTimer.seconds() > 4){
                        passThrough.setPower(1);
                        step = 2;
                    }
                }
                while (step == 2){
                    if (lancherTimer.seconds() > 6){
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
