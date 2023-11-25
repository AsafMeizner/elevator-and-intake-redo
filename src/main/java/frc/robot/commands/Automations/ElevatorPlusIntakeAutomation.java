package frc.robot.commands.Automations;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.Automations.Intake.RunIntakeAutomation;
import frc.robot.subsystems.elevator.Elevator;

public class ElevatorPlusIntakeAutomation extends SequentialCommandGroup {
  public ElevatorPlusIntakeAutomation(double power , double hight) {
    addCommands(
      new InstantCommand(() -> Elevator.getInstance().setSetPoint(hight)).andThen(new RunIntakeAutomation(power))
    );
  }
}
