package org.usfirst.frc.team5631.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;

public class MotorEncoderSystem {
	
	public Talon front, rear, main;
	public Encoder encoder;
	
	double power, speed, time;
	
	//PID Variables
	double error, target_speed, net_error, sum_error, diff_error, prev_error;
	double kP, kI, kD;
	public double distance;
	
	public MotorEncoderSystem(Talon front, Talon rear, Encoder encoder){
		
		this.front = front;
		this.rear = rear;
		this.encoder = encoder;
		this.encoder.setDistancePerPulse((4*Math.PI) / 1000);
		
		init();
		
		kP = 0.015;
		kI = 0.000005;
		kD = 0.001;
		
	}
	
	public MotorEncoderSystem(Talon main, Encoder encoder){
		
		this.main = main;
		this.encoder = encoder;
		this.encoder.setDistancePerPulse(0.196/7);
		
		init();

		kP = 0.02;
		kI = 0.000005;
		kD = 0.001;
		
	}
	
	public void init(){
		
		power = speed = time = 0;
		
		//initialize PID Vars
		error = target_speed = net_error = sum_error = prev_error = diff_error = 0;
		
	}
	
	public void runMotors(){
		
		distance = encoder.getDistance() * 4;
		System.out.println("dist: " + distance);
		
		time++;
		
		if(time > 1){
			PID();
			time = 0;
		}
		
		if(power > 1)power = 1;
		if(power < 0.15 && power > -0.15 && speed == 0)power = 0;
		
		front.set(power);
		rear.set(power);
		
	
	}
	
public void runMotor(){
	
	distance = encoder.getDistance()/3.159245;
		
		time++;
		
		if(speed == 0){
			kP = 0.020;
			power = 0;
		}else{
			kP = 0.03;
		}
		
		if(time > 1){
			PID();
			time = 0;
		}
		
		if(power > 1)power = 1;
		
		if(Robot.calibrating == false){
			if(distance <= 1 && power < 0){
				power = 0;
				speed = 0;
				sum_error = 0;
			}
			if(distance >= 25.5 && power > 0){
				power = 0;
				speed = 0;
				sum_error = 0;
			}
		}
		
		main.set(power);
	}
	
	public void resetDist(){
		encoder.reset();
	}

	public void PID(){
		
		double actualSpeed = encoder.getRate();
		target_speed = speed;
		
		error = target_speed - actualSpeed;

        sum_error = sum_error + error;

        diff_error = error - prev_error;
        prev_error = error;
        net_error = kP*error + kI*sum_error; //+ kD*diff_error;
		
        power = power + net_error;
		
	}
	
	public void setSpeed(double speed){
		
		this.speed = speed/4;
		
	}
	
	public double getLevel(){
		if(distance < 2.32){
			return 0;
		}else if(distance > 2.32 && distance < 25){
			return 1;
		}else{
			return 2;
		}
	}
	
	public void setSpeedElev(double speed){
		this.speed = speed*2;
	}
	
	public void setPower(double power){
		
		this.power = power;
		
	}
	
}
