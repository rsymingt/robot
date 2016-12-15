package org.usfirst.frc.team5631.robot;

public class Regulator {
	
	double speedZ, speedY, throttle;
	
	public Regulator(){
		
		speedZ = speedY = throttle = 0;
		
	}
	
	public double regulateLeftSpeed(){
		double speedLeft = 0;
		
		speedLeft = -speedY;
		
		if(speedZ > 0 && speedY <= 0){
			speedLeft += speedZ;
		}
		if(speedZ < 0 && speedY <= 0){
			speedLeft += speedZ;
		}
		
		if(speedZ > 0 && speedY > 0){
			speedLeft += speedZ;
		}
		if(speedZ < 0 && speedY > 0){
			speedLeft += speedZ;
		}
		
		return speedLeft * throttle;
	}
	
	public double regulateRightSpeed(){
		double speedRight = 0;
		
		speedRight = -speedY;
		
		if(speedZ < 0 && speedY <= 0){
			speedRight -= speedZ;
		}
		if(speedZ > 0 && speedY <= 0){
			speedRight -= speedZ;
		}
		
		if(speedZ < 0 && speedY > 0){
			speedRight -= speedZ;
		}
		if(speedZ > 0 && speedY > 0){
			speedRight -= speedZ;
		}
		
		return speedRight * throttle;
	}
	
	public void setThrottle(double throttle){
		this.throttle = -throttle;
	}
	
	public void setSpeed(double speed, int i){
		if(i == 1)speedY = speed;
		if(i == 2)speedZ = speed/2;
	}
	
}
