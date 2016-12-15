package org.usfirst.frc.team5631.robot;

import edu.wpi.first.wpilibj.Joystick;

public class Controller {
	
	Joystick joyStick;
	
	public Controller(Joystick joyStick){
		
		this.joyStick = joyStick;
		
	}
	
	public double getAxisValue(int input){
		double axisValue = joyStick.getRawAxis(input);
		
		return axisValue;
	}
	
	public boolean getButtonState(int input){
		boolean state = joyStick.getRawButton(input);
		
		return state;
	}
	
	public double getPovState(int input){
		
		return joyStick.getPOV(input);
		
	}
	
}
