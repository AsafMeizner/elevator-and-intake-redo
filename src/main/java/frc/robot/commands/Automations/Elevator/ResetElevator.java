// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Automations.Elevator;

import com.ma5951.utils.commands.MotorCommand;

import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.elevator.Elevator;
import frc.robot.subsystems.elevator.ElevatorConstance;

public class ResetElevator extends SequentialCommandGroup {
  public ResetElevator() {
    addCommands(
      new ParallelDeadlineGroup(
        new WaitCommand(0.2), 
        new MotorCommand(
          Elevator.getInstance(),
          ElevatorConstance.lowerPower, ElevatorConstance.lowerPower)),
      new RunElevatorByPower()
    );
  }
}
