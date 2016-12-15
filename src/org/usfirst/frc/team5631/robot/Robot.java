//Ryan

package org.usfirst.frc.team5631.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.CameraServer;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	DriveTrain driveTrain;
	CameraServer server;
	double[][] commands;
	static int i = 0;

	public static boolean calibrating = true;

	public void robotInit() {

		server = CameraServer.getInstance();
		server.setQuality(1);
		server.startAutomaticCapture("cam1");

		driveTrain = new DriveTrain();

	}

	public void autonomousInit() {
		//Determines whether it is calibrating -- always true if autonomous is turned off then on
		//also resets the distance that the elevator and 2 motor sides have gone
		i = 0;
		calibrating = true;
		driveTrain.resetDistance();
		
		//turning distance
		double t = 23.1;//found through experimentation with physical robot
	
		//Sets the size of the commands array, needs to be [n = numberOfCommands][3]
		commands = new double[13][3];
		
		//Last number tells you whether its the elevator or motors running 1 = motors, 2 = elevator
		//First number tells the motors on the left side distance forwards or the elevators level that it needs to go
		//second number tells right side motors to travel a certain distance forwards
		commands[0] = convert(2, 0, 2);
		commands[1] = convert(117, 117, 1);
		commands[2] = convert(0, 0, 2);
		commands[3] = convert(-10, -10, 1);
		commands[4] = convert(-t, t, 1);
		commands[5] = convert(87, 87, 1);
		commands[6] = convert(-t, t, 1);
		commands[7] = convert(107, 107, 1);
		commands[8] = convert(2, 0, 2);
		commands[9] = convert(-123, -123, 1);
		commands[10] = convert(-t, t, 1);
		commands[11] = convert(87, 87, 1);
		commands[12] = convert(1, 0, 2);
	}

	public double[] convert(double num1, double num2, double n) {
		double[] number = new double[3];
		number[0] = num1;
		number[1] = num2;
		number[2] = n;
		return number;
	}

	public void autonomousPeriodic() {
		
		//if the robot is not calibrating then it will run the set of commands
		if (!calibrating) {
			if (i < commands.length) {
				if (commands[i][2] == 1) {
					driveTrain.drive(commands[i][0], commands[i][1]);
				} else if (commands[i][2] == 2) {
					driveTrain.raiseElevator(commands[i][0]);
				}
				driveTrain.runSystems();
			} else {
				driveTrain.setWheelSystemPowers(0, 0);
				driveTrain.elevator.setSpeed(0);
				driveTrain.runSystems();
			}
		}else{
			//calibrates the robot
			driveTrain.calibrateElevator();
		}
	}

	public void teleopPeriodicInit() {
		//refer to autonomousInit
		driveTrain.resetDistance();
		calibrating = true;
	}

	public void teleopPeriodic() {
		//refer to autonomousPeriodic
		if (!calibrating) {
			//Runs the robot (refer to DriveTrain class)
			driveTrain.runRobot();
		} else {
			driveTrain.calibrateElevator();
		}
	}

	public void testPeriodic() {
	}

}
