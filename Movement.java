package org.firstinspires.ftc.robotcontroller;

import static java.lang.Thread.sleep;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="Movement", group="Basic: CristoReyTeamCode")
public class Movement extends OpMode {


    boolean manualServoDrive = false;
    double motorTicks = 537.7; // Replace with the motors ticks, on manufactures website
    DcMotor leftFront;  //Position 0 Control Hub
    DcMotor leftRear;   //Position 1 Control Hub
    DcMotor rightFront; //Position 2 Control Hub
    DcMotor rightRear;  //Position 3 Control Hub
    DcMotor pickupArm; //Position 4 (Need's Expansion Hub)
    Servo leftClaw;     //Position 0 Control Hub
    Servo rightClaw;    //Position 1 Control Hub
    Servo handAdjustLeft;   //Position 2 Control Hub

    Servo handAdjustRight;  //Position 3 Control Hub

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

        // These Servos should be speed: leftClaw and rightClaw
        leftClaw = hardwareMap.get(Servo.class, "leftClaw");
        leftClaw.setPosition(0.5);

        rightClaw = hardwareMap.get(Servo.class, "rightClaw");
        rightClaw.setPosition(0.5);

        //These Servos should be Torque: handAdjustLeft and handAdjustLeft
        handAdjustLeft = hardwareMap.get(Servo.class, "handAdjustLeft");
        handAdjustLeft.setPosition(0.5);

        handAdjustRight = hardwareMap.get(Servo.class, "handAdjustRight");
        handAdjustRight.setPosition(0.5);
        /*SERVO SETUP END*/

    }

    //@Override
    public void loop() {
        movement();
        moveServoArm();
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
    public void moveServoArm(){
        if(gamepad1.dpad_up) {
            handAdjustLeft.setPosition(handAdjustLeft.getPosition() + 0.01);
            handAdjustRight.setPosition(handAdjustRight.getPosition() + 0.01);
        }
        if(gamepad1.dpad_down){
            handAdjustLeft.setPosition(handAdjustLeft.getPosition() - 0.01);
            handAdjustRight.setPosition(handAdjustLeft.getPosition() - 0.01);
        }
    }

    public void setClaw(){  //Fast or Automatic Claw or Hand setPosition for quick-grabs
        if(gamepad1.left_bumper){ //This OPENS claw or hand
            leftClaw.setPosition(0);
            rightClaw.setPosition(1);
        }
        if(gamepad1.right_bumper){ //This CLOSES claw or hand
            leftClaw.setPosition(0.5);  //We can calibrate based on claw design
            rightClaw.setPosition(0.5); //We can also calibrate this one based on claw design
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

    public void movement() {
        if(gamepad1.left_stick_x > 0 || gamepad1.left_stick_x < 0){
            leftFront.setPower(gamepad1.left_stick_x);
            leftRear.setPower(gamepad1.left_stick_x);
            rightFront.setPower(gamepad1.left_stick_x);
            rightRear.setPower(gamepad1.left_stick_x);
        }
        else if(gamepad1.left_stick_y > 0 || gamepad1.left_stick_y < 0) {
            leftFront.setPower(gamepad1.left_stick_x);
            leftRear.setPower(gamepad1.left_stick_x);
            rightFront.setPower(gamepad1.left_stick_x);
            rightRear.setPower(gamepad1.left_stick_x);
        }
        else if(gamepad1.right_stick_x != 0){
            leftFront.setPower(-gamepad1.right_stick_x);
            leftRear.setPower(gamepad1.right_stick_x);
            rightFront.setPower(gamepad1.right_stick_x);
            rightRear.setPower(-gamepad1.right_stick_x);
        }
        else{
            leftFront.setPower(0);
            leftRear.setPower(0);
            rightFront.setPower(0);
            rightRear.setPower(0);
        }
    }
    public void encoderMovement(){
        double movement = 1 / motorTicks;
        if(gamepad1.left_stick_x > 0 || gamepad1.left_stick_x > 0){
            leftFront.setPower(movement * gamepad1.left_stick_x);
            leftRear.setPower(movement * gamepad1.left_stick_x);
            rightFront.setPower(movement * gamepad1.left_stick_x);
            rightRear.setPower(movement * gamepad1.left_stick_x);
        }
        else{
            leftFront.setPower(0);
            leftRear.setPower(0);
            rightFront.setPower(0);
            rightRear.setPower(0);
        }


    }


    public void printSystems(){

    }




}
