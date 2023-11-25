// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Automations.Elevator;

import com.ma5951.utils.commands.MotorCommand;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.elevator.Elevator;
import frc.robot.subsystems.elevator.ElevatorConstance;

public class RunElevatorByPower extends CommandBase {
  private final Command command;

  public RunElevatorByPower() {
    command = new MotorCommand(
      Elevator.getInstance(), ElevatorConstance.lowerPower, 0);
  }

  @Override
  public void initialize() {
    command.initialize();
  }

  @Override
  public void execute() {
    command.execute();
  }

  @Override
  public void end(boolean interrupted) {
    Elevator.getInstance().resetEncoderPose(0);
    Elevator.getInstance().setSetPoint(ElevatorConstance.minPose);
    command.end(interrupted);
  }

  @Override
  public boolean isFinished() {
    return ElevatorConstance.currentAmpThreshold < Elevator.getInstance().getCurrent();
  }
}
