package frc.robot.commands.Automations.Elevator;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.elevator.Elevator;

public class setDesiredPosition extends SequentialCommandGroup {
    public setDesiredPosition(double setPoint) {
        addCommands(
            new InstantCommand(() -> Elevator.getInstance().setDesiredPos(setPoint))
        );
    }
}
