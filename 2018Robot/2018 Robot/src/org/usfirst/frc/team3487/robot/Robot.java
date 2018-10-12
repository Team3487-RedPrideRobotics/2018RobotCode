package org.usfirst.frc.team3487.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Timer;

///////////////IOSetup//////////////
//	Drive Motors				  //
//	PWM 0 = Drive Motor Right 1	  //
//	PWM 1 = Drive Motor Right 2	  //
//	PWM 2 = Drive Motor Left 1	  //
//	PWM 3 = Drive Motor Left 2    //
//								  //	
//	Other Motors				  //
//	PWM 4 = DropOff Motor		  //
//	PWM 5 = Lift Motor			  //
//	PWM 6 = Intake Motor 1		  //
//	PWM 7 = Intake Motor 2		  //
//								  //
//	DIO 0-2 = Auto Switch 0		  //
//	DIO 3-5 = Auto Switch 1		  //
//	DIO 6 = LED Bump Switch		  //
////////////////////////////////////

///////////Button Setup/////////////
//	R_Joystick Moves Right	  	  //
//	L_Joystick Moves Left	  	  //
//	R_Button 2 Is Intake	  	  //
//	L_Button 2 Is Output	  	  //
//	R_Button 7 is Full Down		  //
//	R_Button 8 is Full Up		  //
//	R_Button 9 is Move Up 15	  //
//	R_Button 10 is Move Down 15	  //
////////////////////////////////////

public class Robot extends IterativeRobot {
	
	//Drive motors
	public Spark driveMotor1;
	public Spark driveMotor2;
	public Spark driveMotor3;
	public Spark driveMotor4;
	
	//Other Motors
	public Spark liftMotor;
	public Spark dropOffMotor;
	public Spark intakeMotor1;
	public Spark intakeMotor2;
	
	//Joysticks
	public Joystick leftStick;
	public Joystick rightStick;
	
	//Timer
	public Timer moveTime = new Timer();
	public Timer reeTime = new Timer();
	
	//Lift Motor Variables
	boolean shouldMove15 = false;
	boolean shouldMoveN15 = false;
	boolean fullMoveUp = false;
	boolean fullMoveDown = false;
	boolean intakeMotor = false;
	
	//Bump Switches
	DigitalInput zeroSwitch0;
	DigitalInput zeroSwitch2;
	DigitalInput zeroSwitch3;
	DigitalInput zeroSwitch5;
	DigitalInput switchLED;
	
	//Serial Port
	SerialPort port01;
	
	//Driver Station
	boolean redAlliance = false;
	
	//Bump Switch Booleans
	boolean armMoveUp = false;
	boolean armMoveDown = false;
	
	boolean intakeOn = true;
	
	//Autonomous Modes
	public boolean rightStart = false;
	public boolean leftStart = false;
	public boolean middleStart = false;
	public boolean leftAltStart = false;
	public boolean rightAltStart = false;
	public boolean middleAltStart = false;
	
	//Autonomous Private
	private String gameData;
	boolean drive = false;
	
	//Autonomous Movement
	float autoMoveTime = 6;
	float moveForward = 3.4f;
	float turnTime = 1.6f;
	float correctionTime = 2;
	float spitTime = 1.5f;
	
	@Override
	public void robotInit() {
		
		//Drive Motors
		driveMotor1 = new Spark(0);
		driveMotor2 = new Spark(1);
		driveMotor3 = new Spark(2);
		driveMotor4 = new Spark(3);
		
		//Other Sparks
		dropOffMotor = new Spark(4);
		liftMotor = new Spark(5);
		intakeMotor1 = new Spark(6);
		intakeMotor2 = new Spark(7);
		
		zeroSwitch0 = new DigitalInput(2);
		zeroSwitch2 = new DigitalInput(0);
		zeroSwitch3 = new DigitalInput(3);
		zeroSwitch5 = new DigitalInput(5);
		switchLED = new DigitalInput(8);
		
		//Serial Port
		port01 = new SerialPort(9600, Port.kMXP);
		
		//Joysticks
		leftStick = new Joystick(0);
		rightStick = new Joystick(1);
	}
	
	public void autonomousInit() {
		
		if(zeroSwitch0.get() == false) {
				
			System.out.println("Left");
			leftStart = true;
			rightStart = false;
			middleStart = false;
				
		}
		else if(zeroSwitch2.get() == false) {
				
			System.out.println("Right");
			rightStart = true;
			leftStart = false;
			middleStart = false;
				
		}
		else {
				
			System.out.println("Middle");
			middleStart = true;
			rightStart = false;
			leftStart = false;
				
		}
		if(zeroSwitch3.get() == false) {
				
			rightAltStart = true;
			leftAltStart = false;
			System.out.println("Zero 3");
				
		}
		else if (zeroSwitch5.get() == false) {
				
			leftAltStart = true;
			rightAltStart = false;
			System.out.println("Zero 5");
				
		}
		else {
				
			leftAltStart = false;
			rightAltStart = false;
			System.out.println("Hey this is good we made it :D");
				
		}
		
			moveTime.start();
			moveTime.reset();
		
			gameData = DriverStation.getInstance().getGameSpecificMessage();
			
	}
	
	public void autonomousPeriodic() {
		
		if(leftAltStart == false && rightAltStart == false) {
			
			/////////////
			//Left Side//
			/////////////
			
			if(leftStart == true && rightStart == false) {
				
				System.out.println("Left Start");
				
				if(gameData.length() > 0) {
					
					//If Start Left Then Drive And Drop
					if(gameData.charAt(0) == 'L') {
						if(moveTime.get() <= autoMoveTime) {
							
							driveMotor1.set(0.3);
							driveMotor2.set(0.3);
							
							driveMotor3.set(-0.3);
							driveMotor4.set(-0.3);
							
						}
						else {
						
							//System.out.println("Done!");
							driveMotor1.set(0);
							driveMotor2.set(0);
							
							driveMotor3.set(0);
							driveMotor4.set(0);
							
							if(moveTime.get() <= spitTime + autoMoveTime) {
								
								dropOffMotor.set(1);
								
							}
							else {
								
								dropOffMotor.set(0);
								
							}
						}
					}
					else if(moveTime.get() <= 6){
						
						//Drive Forward If Right Game On Left Side
						driveMotor1.set(0.3);
						driveMotor2.set(0.3);
						
						driveMotor3.set(-0.3);
						driveMotor4.set(-0.3);
						
					}
					else {
						
						driveMotor1.set(0);
						driveMotor2.set(0);
						
						driveMotor3.set(-0);
						driveMotor4.set(-0);
						
					}
				}
			}
				
			//////////////
			//Right Side//
			//////////////
					
			if(rightStart == true && leftStart == false) {
						
				System.out.println("Right Start");
						
				if(gameData.length() > 0) {
							
					//If Start Left Then Drive And Drop
					if(gameData.charAt(0) == 'R') {
						if(moveTime.get() <= autoMoveTime) {
									
							driveMotor1.set(0.3);
							driveMotor2.set(0.3);
									
							driveMotor3.set(-0.3);
							driveMotor4.set(-0.3);
									
						}
						else {
								
							//System.out.println("Done!");
							driveMotor1.set(0);
							driveMotor2.set(0);
									
							driveMotor3.set(0);
							driveMotor4.set(0);
									
							if(moveTime.get() <= spitTime + autoMoveTime) {
										
								dropOffMotor.set(1);
										
							}
							else {
										
								dropOffMotor.set(0);
										
							}
						}
					}
					else if(moveTime.get() <= 6) {
						
						//Drive Forward If Left Game On Right Side
						driveMotor1.set(0.3);
						driveMotor2.set(0.3);
								
						driveMotor3.set(-0.3);
						driveMotor4.set(-0.3);
								
					}
					else {
						
						driveMotor1.set(0);
						driveMotor2.set(0);
								
						driveMotor3.set(0);
						driveMotor4.set(0);
						
					}
				}
			}
				
			////////////////
			//Middle Start//
			////////////////
				
			if(middleStart == true && rightStart == false && leftStart == false) {
					
				if(gameData.length() > 0) {
						
					if(gameData.charAt(0) == 'L') {
						if(moveTime.get() <= 1.25) {
						
							driveMotor1.set(0);
							driveMotor2.set(0);
						
							driveMotor3.set(-.4);
							driveMotor4.set(-.4);
						
						} else if(moveTime.get() >= 1.25 && moveTime.get() <= 2.5) {
							
							driveMotor1.set(.4);
							driveMotor2.set(.4);
							
							driveMotor3.set(0);
							driveMotor4.set(0);
							
						} else if(moveTime.get() >= 2.5 && moveTime.get() <= 6) {
							
							driveMotor1.set(.35);
							driveMotor2.set(.35);
							
							driveMotor3.set(-.35);
							driveMotor4.set(-.35);
						
						} else if(moveTime.get() >= 6 && moveTime.get() <= 8) {
						
							dropOffMotor.set(1);
						
						} else if(moveTime.get() >= 8) {
							
							driveMotor1.set(0);
							driveMotor2.set(0);
							
							driveMotor3.set(0);
							driveMotor4.set(0);
							
							dropOffMotor.set(0);
							
						}
					} else if(gameData.charAt(0) == 'R') {
						if(moveTime.get() <= 2) {
							
							driveMotor1.set(.43);
							driveMotor2.set(.43);
							
							driveMotor3.set(-.3);
							driveMotor4.set(-.3);
							
						} else if(moveTime.get() >= 2 && moveTime.get() <= 4) {
							
							driveMotor1.set(.3);
							driveMotor2.set(.3);
							
							driveMotor3.set(-.4);
							driveMotor4.set(-.4);
							
						} else if(moveTime.get() >= 4 && moveTime.get() <= 5) {
							
							driveMotor1.set(0);
							driveMotor2.set(0);
							
							driveMotor3.set(0);
							driveMotor4.set(0);
							
							dropOffMotor.set(1);
							
						} else if(moveTime.get() >= 5) {
							
							driveMotor1.set(0);
							driveMotor2.set(0);
							
							driveMotor3.set(0);
							driveMotor4.set(0);
							
							dropOffMotor.set(0);
							
						}
					}
				}
			}
		}
		else if (rightAltStart == true) {
			
			if(moveTime.get() <= moveForward) {
				
				driveMotor1.set(0.4);
				driveMotor2.set(0.4);
									
				driveMotor3.set(-0.4);
				driveMotor4.set(-0.4);
				
			}
			else if(moveTime.get() <= moveForward + turnTime) {
				
				driveMotor1.set(-0.3);
				driveMotor2.set(-0.3);
									
				driveMotor3.set(-0.3);
				driveMotor4.set(-0.3);
				
			}
			else if(moveTime.get() <= moveForward + turnTime + correctionTime) {
				
				driveMotor1.set(0.35);
				driveMotor2.set(0.35);
									
				driveMotor3.set(-0.35);
				driveMotor4.set(-0.35);
				
			}
			else if(moveTime.get() <= moveForward + turnTime + correctionTime + spitTime) {
				
				driveMotor1.set(0.0);
				driveMotor2.set(0.0);
									
				driveMotor3.set(-0.0);
				driveMotor4.set(-0.0);
				
				if(gameData.charAt(0) == 'R') {
					dropOffMotor.set(1);
				}
			}
			else {
				
				dropOffMotor.set(0);
				
			}
		}
		else if(leftAltStart == true) {
			
			if(moveTime.get() <= moveForward) {
				
				driveMotor1.set(0.4);
				driveMotor2.set(0.4);
									
				driveMotor3.set(-0.4);
				driveMotor4.set(-0.4);
				
			}
			else if(moveTime.get() <= moveForward + turnTime) {
				
				driveMotor1.set(0.3);
				driveMotor2.set(0.3);
									
				driveMotor3.set(0.3);
				driveMotor4.set(0.3);
				
			}
			else if(moveTime.get() <= moveForward + turnTime + correctionTime) {
				
				driveMotor1.set(0.35);
				driveMotor2.set(0.35);
									
				driveMotor3.set(-0.35);
				driveMotor4.set(-0.35);
				
			}
			else if(moveTime.get() <= moveForward + turnTime + correctionTime + spitTime) {
				
				driveMotor1.set(0.0);
				driveMotor2.set(0.0);
									
				driveMotor3.set(-0.0);
				driveMotor4.set(-0.0);
				
				if(gameData.charAt(0) == 'L') {
					dropOffMotor.set(1);
				}
			}
			else {
				
				dropOffMotor.set(0);
				
			}
		}
	}

	
	public void teleopInit() {
		
		if(DriverStation.getInstance().getAlliance() == DriverStation.Alliance.Red){
			
			redAlliance = true;
			System.out.println("Red");
		
		}
		else {
		
			redAlliance = false;
			System.out.print("Blue");
		
		}
		
		moveTime.start();
		moveTime.reset();

	}
	
	@Override
	public void teleopPeriodic() {
		
		///////////////
		//LED Control//
		///////////////
		
		//Getting Bump Switch
		if(switchLED.get() == false) {
			
			port01.writeString("c");
			
		}
		else {
			CheckAlliance();
		}
		
		////////////////
		//Drive System//
		////////////////
		
		//Right
		driveMotor1.set(rightStick.getY()/2);
		driveMotor2.set(rightStick.getY()/2);
		
		//Left
		driveMotor3.set(-leftStick.getY()/2);
		driveMotor4.set(-leftStick.getY()/2);
		
		/////////////////
		//Output System//
		/////////////////
		
		//Move cube on top if pressed else stop
		if(leftStick.getRawButton(5)) {
			
			dropOffMotor.set(-1);
			
		}
		else if(rightStick.getRawButton(6) == false) {
			
			dropOffMotor.set(0);
			
		}
		
		//Move cube on top backwards if pressed else stop
		if(rightStick.getRawButton(6)) {
			
			dropOffMotor.set(1);
			
		}
		else if(leftStick.getRawButton(5) == false) {
			
			dropOffMotor.set(0);
			
		}
		
		////////////////////////
		//Cube Maneuver System//
		////////////////////////
		
		//Intake
		if(leftStick.getRawButton(2) == true) {
			
			inputOutput("output");
			
		}
		
		//Output
		if(rightStick.getRawButton(2) == true) {
			
			inputOutput("input");
			
		}
		
		////////////
		//Arm Move//
		////////////
		
		//Up
		if(rightStick.getRawButton(4) == true) {
			
			moveArm(15);
			
		}
		else if (leftStick.getRawButton(3) == false) {
			
			liftMotor.set(0);
			
		}
		
		//Down
		if(leftStick.getRawButton(3) == true) {
			
			moveArm(-15);
			
		}
		else if (rightStick.getRawButton(4) == false) {
			
			liftMotor.set(0);
			
		}
		 
		
		if(rightStick.getRawButton(2) == false && leftStick.getRawButton(2) == false) {
			
			intakeMotor1.set(0);
			intakeMotor2.set(0);
			
		}
	}
	
	//Move the arm in intervals
	public void moveArm(int angle) {
		
		//Up
		if (angle == 15) {
			liftMotor.set(.6);
		}	
		
		//Down
		if (angle == -15) {
			liftMotor.set(-.6);
		}
	}
	
	void inputOutput(String type) {
		
		if(type == "input") {
			
			intakeMotor = true;
			intakeMotor1.set(-1);
			intakeMotor2.set(1);
			
		}
		else if (type == "output") {
			
			intakeMotor = true;
			intakeMotor1.set(1);
			intakeMotor2.set(-1);
			
		}
	} 
	
	void CheckAlliance() {

		if(redAlliance == true) {
			System.out.println("Red");
			port01.writeString("a");
		}
		else {
			System.out.println("Blue");
			port01.writeString("b");
		}
	}
}