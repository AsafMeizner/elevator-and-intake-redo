package frc.robot.commands.Automations.Elevator;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import frc.robot.subsystems.elevator.Elevator;

public class SetElevatorPosition extends SequentialCommandGroup {
  public SetElevatorPosition(double setPoint) {
    addCommands(
        new InstantCommand(() -> Elevator.getInstance().setSetPoint(setPoint)),
        new ParallelDeadlineGroup(
        new WaitUntilCommand(Elevator.getInstance()::atPoint))
    );
  }
}
