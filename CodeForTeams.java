package org.firstinspires.ftc.robotcontroller;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="CodeForTeams", group="Basic: CristoReyTeamsCode")
public class CodeForTeams extends OpMode{
    //I don't know if my code even works... assume each motor is facing the correct way, idk
    double ticks = 537.7;
    double floorPos; //I assume this will  be where your servo arm starts off on, the  ground oc
    double boardPos; //You can set up where you want the arm to go, but I can't figure that out without hardware testing
    /* PLEASE CONFIGURE CORRECTLY, HELL MAKE A NEW CONFIGURATION IDK*/
    DcMotor leftFront;  //Position 0 Control Hub
    DcMotor leftRear;   //Position 1 Control Hub
    DcMotor rightFront; //Position 2 Control Hub
    DcMotor rightRear;  //Position 3 Control Hub
    DcMotor pickupRobotArm; //Position 4 Expansion Hub
    DcMotor pickupServoArm; //Position 5 (Need's Expansion Hub)
    Servo leftClaw;     //Position 0 Control Hub
    Servo rightClaw;    //Position 1 Control Hub


    @Override
    public void init() {

        /*MOTORS SETUP START*/
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        leftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        leftRear = hardwareMap.get(DcMotor.class, "leftRear");
        leftRear.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        rightRear = hardwareMap.get(DcMotor.class, "rightRear");
        rightRear.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        pickupRobotArm = hardwareMap.get(DcMotor.class, "pickupRobotArm");
        pickupRobotArm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        pickupServoArm = hardwareMap.get(DcMotor.class, "pickupServoArm");
        pickupServoArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        floorPos = pickupServoArm.getCurrentPosition();
        /*MOTORS SETUP END*/

        /*SERVO SETUP START*/
        leftClaw = hardwareMap.get(Servo.class, "leftClaw");
        leftClaw.setPosition(0.5);

        rightClaw = hardwareMap.get(Servo.class, "rightClaw");
        rightClaw.setPosition(0.5);
        /*SERVO SETUP END*/

    }


    public void loop() {
        moveRobot();
        moveCertainArm();
        claw();
    }

    public void moveRobot(){
        if(gamepad1.left_stick_y > 0 || gamepad1.left_stick_y < 0){
            leftFront.setPower(gamepad1.left_stick_y);
            leftRear.setPower(gamepad1.left_stick_y);
            rightFront.setPower(gamepad1.left_stick_y);
            rightRear.setPower(gamepad1.left_stick_y);
        }
        else if(gamepad1.left_stick_x > 0 || gamepad1.left_stick_x < 0){
            leftFront.setPower(-gamepad1.left_stick_x);
            leftRear.setPower(gamepad1.left_stick_x);
            rightFront.setPower(gamepad1.left_stick_x);
            rightRear.setPower(-gamepad1.left_stick_x);
        }
        else if(gamepad1.right_stick_x > 0 || gamepad1.right_stick_x < 0){
            leftFront.setPower(gamepad1.right_stick_x);
            leftRear.setPower(gamepad1.right_stick_x);
            rightFront.setPower(-gamepad1.right_stick_x);
            rightRear.setPower(-gamepad1.right_stick_x);
        }
        else{
            leftFront.setPower(0);
            leftRear.setPower(0);
            rightFront.setPower(0);
            rightRear.setPower(0);
        }
    }
    public void moveCertainArm(){
        if((gamepad1.right_stick_y > 0 || gamepad1.right_stick_y < 0) && gamepad1.a){
            pickupRobotArm.setPower(gamepad1.right_stick_y);
        }
        if(gamepad1.y){
            pickupServoArm.setTargetPosition((int)(ticks/4.2));
            pickupServoArm.setPower(0.4);
            pickupServoArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
        if(gamepad1.x){
            pickupServoArm.setTargetPosition((int)floorPos); //Assuming the servo arm starts on the floor
            pickupServoArm.setPower(0.4);
            pickupServoArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
    }


    public void claw(){
        if(gamepad1.left_bumper && leftClaw.getPosition() < 0){
            leftClaw.setPosition(leftClaw.getPosition() - 0.01);
            rightClaw.setPosition(rightClaw.getPosition() + 0.01);
        }
        if(gamepad1.right_bumper && rightClaw.getPosition() > 1){
            leftClaw.setPosition(leftClaw.getPosition() + 0.01);
            rightClaw.setPosition(rightClaw.getPosition() - 0.01);
        }
    }



}
