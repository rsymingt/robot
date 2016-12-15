package org.usfirst.frc.team5631.robot;

import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;

public class DriveTrain {

	MotorEncoderSystem leftSide, rightSide, elevator;
	Controller[] controller;
	Regulator regulator;
	double maxSpeed, calibratingTimer;

	public DriveTrain() {
	
		//Sets the left side to store the left side motors and encoder
		//vice versa for the right side
		//also sets the elevator motor and encoder pin locations
		leftSide = new MotorEncoderSystem(new Talon(0), new Talon(1),
				new Encoder(2, 3, true, CounterBase.EncodingType.k1X));
		rightSide = new MotorEncoderSystem(new Talon(2), new Talon(3),
				new Encoder(0, 1, true, CounterBase.EncodingType.k1X));
		elevator = new MotorEncoderSystem(new Talon(4), new Encoder(5, 6,
				false, CounterBase.EncodingType.k1X));
		controller = new Controller[2];
		
		//Creates a new controller -- controller 0 needs to be joystick and controller 1 needs to be xbox controller
		controller[0] = new Controller(new Joystick(0));
		controller[1] = new Controller(new Joystick(1));
		
		//creates the speed regulator
		regulator = new Regulator();
		
		//sets max speed determined by person
		maxSpeed = 50;
		calibratingTimer = 0;

	}

	public void runRobot() {
		
		//checks inputs from two controllers
		checkInputs();
		
		//checks for emergency brake
		if (!checkEmergBrake()) {
			//pulls needed speeds from regulator and passes them to wheel system
			setWheelSystemSpeeds(regulator.regulateLeftSpeed(),
					regulator.regulateRightSpeed());
		} else {
			//sets wheel systems power and speed to 0
			setWheelSystemPowers(0, 0);
			setWheelSystemSpeeds(0, 0);
		}
		
		//runs the robots wheel systems
		runSystems();
		
		//not necessary
		System.out.println(elevator.distance);

	}

	public void resetDistance() {
		//resets both sides distances
		leftSide.resetDist();
		rightSide.resetDist();
	}

	public void raiseElevator(double level) {
		
		//Set speed, distance from level before slowing down and 
		double t = 2;
		double speed = 10;
		
		//not sure what this does lols
		boolean added = false;
		
		if (level < elevator.getLevel())
			speed *= -1;
		
			if (elevator.getLevel() < level) {
				elevator.setSpeedElev(speed);
			}
			
			if (elevator.getLevel() > level) {
				elevator.setSpeedElev(speed);
			}
			
			if(level == elevator.getLevel()){
				Robot.i++;
				elevator.setSpeedElev(0);
			}

	}

	public void drive(double distance1, double distance2) {

		int t = 10;
		double speed1 = maxSpeed / 2;
		double speed2 = -maxSpeed / 2;

		if (distance1 < 0)
			speed1 *= -1;
		if (distance2 < 0)
			speed2 *= -1;

		distance1 = Math.abs(distance1);
		distance2 = Math.abs(distance2);

		double leftDist = Math.abs(leftSide.distance);
		double rightDist = Math.abs(rightSide.distance);

		if ((leftDist) < distance1 - t) {
			leftSide.setSpeed(speed1);
		} else if (leftDist > distance1 - t && leftDist < distance1) {
			leftSide.setSpeed(speed1 / 3);
		} else {
			leftSide.setPower(0);
		}

		if ((rightDist) < distance2 - t) {
			rightSide.setSpeed(speed2);
		} else if (rightDist > distance2 - t && rightDist < distance2) {
			rightSide.setSpeed(speed2 / 4);
		} else {
			rightSide.setPower(0);
		}

		if (Math.abs(leftSide.distance) >= Math.abs(distance1)
				&& Math.abs(rightSide.distance) >= Math.abs(distance2)) {
			Robot.i++;
			resetDistance();
			setWheelSystemSpeeds(0, 0);
		}

	}

	public void setWheelSystemPowers(double leftSidePower, double rightSidePower) {

		leftSide.setPower(leftSidePower);
		rightSide.setPower(-rightSidePower);

	}

	public void setWheelSystemSpeeds(double leftSideSpeed, double rightSideSpeed) {

		leftSide.setSpeed(leftSideSpeed);
		rightSide.setSpeed(-rightSideSpeed);

	}

	public void runSystems() {

		leftSide.runMotors();
		rightSide.runMotors();
		elevator.runMotor();

	}

	public void calibrateElevator() {
		elevator.setSpeedElev(-10);
		runSystems();
		if (calibratingTimer > 5) {
			if (elevator.encoder.getRate() > -0.5
					&& elevator.encoder.getRate() <= 0) {
				// if(elevator.encoder.getRate() == 0){
				Robot.calibrating = false;
				elevator.setPower(0);
				calibratingTimer = 0;
				elevator.encoder.reset();
			}
		}
		calibratingTimer++;
	}

	public void checkInputs() {

		for (int i = 1; i < 3; i++) {

			double input = controller[0].getAxisValue(i);
			double para = 0.2;

			if (Methods.checkParameters(input, para)) {
				regulator.setSpeed(input, i);
			} else {
				regulator.setSpeed(0, i);
			}

			if (controller[1] != null) {
				double input2 = -controller[1].getAxisValue(1);
				if (Methods.checkParameters(input2, 0.2)) {
					elevator.setSpeedElev(input2 * 20);
				} else {
					elevator.setSpeedElev(0);
				}
			}

		}

		regulator.setThrottle(((controller[0].getAxisValue(3) - 1) / 2)
				* maxSpeed);

	}

	public boolean checkEmergBrake() {

		if (controller[0].getButtonState(1)) {
			return true;
		} else {
			return false;
		}

	}

}
