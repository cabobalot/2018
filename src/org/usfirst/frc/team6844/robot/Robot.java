/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team6844.robot;

import org.usfirst.frc.team6844.robot.Arm.Position;
import org.uvstem.borg.joysticks.LogitechGamepadController;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.TimedRobot;

public class Robot extends TimedRobot {

	Drivetrain drivetrain;
	Intake intake;
	Arm arm;
	
	LogitechGamepadController gamepadDriver;
	LogitechGamepadController gamepadOperator;

	@Override
	public void robotInit() {
		CameraServer.getInstance().startAutomaticCapture();

		drivetrain = new Drivetrain();
		intake = new Intake();
		arm = new Arm();

		gamepadDriver = new LogitechGamepadController(1);
		gamepadOperator = new LogitechGamepadController(2);
	}

	@Override
	public void autonomousInit() {
		super.autonomousInit();
		drivetrain.resetGyro();
	}

	@Override
	public void autonomousPeriodic() {
		super.autonomousPeriodic();
	}

	@Override
	public void teleopInit() {}

	@Override
	public void teleopPeriodic() {
		drivetrain.arcadeDrive(gamepadDriver.getRightY(), gamepadDriver.getLeftX(), true);
		
		// Operator Y button, sets position of switch to top
		if (gamepadOperator.getRawButtonPressed(gamepadOperator.Y_BUTTON)) {
			arm.setPosition(Position.TOP);
		}
		
		// Operator X/B button, sets position of switch to middle
		if (gamepadOperator.getRawButtonPressed(gamepadOperator.X_BUTTON) || 
				gamepadOperator.getRawButtonPressed(gamepadOperator.B_BUTTON)) {
			arm.setPosition(Position.MIDDLE);
		}
		
		// Operator A button, sets position of switch to bottom
		if (gamepadOperator.getRawButtonPressed(gamepadOperator.A_BUTTON)) {
			arm.setPosition(Position.BOTTOM);
		}
		
		//Driver start button, nerfs the speed
		if (gamepadDriver.getRawButtonPressed(gamepadDriver.START_BUTTON)) {
			drivetrain.nerfSpeed();
		}

		//Driver A button, switches forwards and backwards
		if (gamepadDriver.getRawButtonPressed(gamepadDriver.A_BUTTON)) {
			drivetrain.reverseDriveDirection();
		}

		//Operator left bumper, intake out
		//Operator right bumper, intake in
		if (Math.abs(gamepadOperator.getLeftX()) > 0) {
			intake.stopIntake();
		} else if (gamepadOperator.getLeftY() < 0){
		    intake.intakeOut();
		} else if (gamepadOperator.getLeftY() > 0){
		    intake.intakeIn();
		}
		
		arm.update();
		intake.update();
	}

	@Override
	public void testInit() {}

	@Override
	public void testPeriodic() {}
}
