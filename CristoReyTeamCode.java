package org.firstinspires.ftc.robotcontroller;

import static java.lang.Thread.sleep;

import java.lang.Math;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="CristoReyTeamCode", group="Basic: CristoReyTeamCode")
//@Disabled

public class CristoReyTeamCode extends OpMode {

    boolean manualServoDrive = false;
    double motorTicks = 1780; // Replace with the motors ticks, on manufactures website

    DcMotor leftFront;  //Position 0 Control Hub
    DcMotor leftRear;   //Position 1 Control Hub
    DcMotor rightFront; //Position 2 Control Hub
    DcMotor rightRear;  //Position 3 Control Hub

    DcMotor pickupArm; //Position 4 (Need's Expansion Hub)

    Servo leftClaw;     //Position 0 Control Hub
    Servo rightClaw;    //Position 1 Control Hub
    Servo handAdjust;   //Position 2 Control Hub



    @Override
    public void init() {
        /*This maps/tells the microcontroller what hardware we are using
        (Physical electronics parts). We'll need to map them now        */

        /*MOTORS SETUP START*/
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        leftRear = hardwareMap.get(DcMotor.class, "leftRear");
        leftRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        rightRear = hardwareMap.get(DcMotor.class, "rightRear");
        rightRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        pickupArm = hardwareMap.get(DcMotor.class, "pickupArm");
        pickupArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        /*MOTORS SETUP END*/

        /*SERVO SETUP START*/
        leftClaw = hardwareMap.get(Servo.class, "leftClaw");
        leftClaw.setPosition(0.5);

        rightClaw = hardwareMap.get(Servo.class, "rightClaw");
        rightClaw.setPosition(0.5);

        handAdjust = hardwareMap.get(Servo.class, "handAdjust");
        handAdjust.setPosition(0.5);
        /*SERVO SETUP END*/


    }

    //@Override
    public void loop() {
        movement();
        moveClaw();
        changeServoControl();

    }

    public void moveClaw(){ //Manual Control over the Servo Hand or Claw
        if(manualServoDrive){
            if(gamepad1.left_bumper && leftClaw.getPosition() < 0){
                leftClaw.setPosition(leftClaw.getPosition() - 0.01);
                rightClaw.setPosition(rightClaw.getPosition() + 0.01);
            }
            if(gamepad1.right_bumper && rightClaw.getPosition() > 1){
                leftClaw.setPosition(leftClaw.getPosition() + 0.01);
                rightClaw.setPosition(rightClaw.getPosition() - 0.01);
            }
        }
        else{
            setClaw();
        }
    }
    public void setClaw(){  //Fast or Automatic Claw or Hand setPosition for quick-grabs
        if(gamepad1.left_bumper){ //This OPENS claw or hand
            leftClaw.setPosition(0);
            rightClaw.setPosition(1);
        }
        if(gamepad1.right_bumper){ //This CLOSES claw or hand
            leftClaw.setPosition(0.5);  //We can calibrate based on claw design
            rightClaw.setPosition(0.5); //We can also calibrate based on claw design
        }
    }

    public void changeServoControl(){
        if(gamepad1.a){
            manualServoDrive = !manualServoDrive;
            try {
                sleep(200);
            } catch (InterruptedException e) {
                manualServoDrive = manualServoDrive;
            }
        }
    }
    public double returnTheta(){ /* This gets Theta(the angle) of were we want to go with joystick*/
        return Math.atan2(-gamepad1.right_stick_y, gamepad1.right_stick_x);
    }

    public void movement(){
        double x = gamepad1.right_stick_x;
        double y = -gamepad1.right_stick_y;
        double turn = gamepad1.right_stick_x;

        double power = Math.hypot(x, y);

        double sin = Math.sin(returnTheta() - Math.PI/4);
        double cos = Math.sin(returnTheta() - Math.PI/4);
        double max = Math.max(Math.abs(sin), Math.abs(cos));

        leftFront.setPower(power * cos/max + turn);
        rightFront.setPower(power * sin/max - turn);
        leftRear.setPower(power * sin/max + turn);
        rightRear.setPower(power * cos/max - turn);

        if((power + Math.abs(turn)) > 1) {
            leftFront.setPower(leftFront.getPower()/power + Math.abs(turn));
            rightFront.setPower(rightFront.getPower()/power + Math.abs(turn));
            leftRear.setPower(leftRear.getPower()/power + Math.abs(turn));
            rightRear.setPower(rightRear.getPower()/power + Math.abs(turn));
        }
    }





}
