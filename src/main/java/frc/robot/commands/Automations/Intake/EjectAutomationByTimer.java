package frc.robot.commands.Automations.Intake;


import com.ma5951.utils.commands.MotorCommand;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.intake.IntakeConstants;

public class EjectAutomationByTimer extends SequentialCommandGroup{
    
    public EjectAutomationByTimer() {
        addCommands(
            new ParallelDeadlineGroup(
                new WaitCommand(IntakeConstants.ejectTime),
                new MotorCommand(Intake.getInstance(), IntakeConstants.coneEjectPower , 0)),
            new InstantCommand(Intake.getInstance()::clearGamePieces)
        );
    }
}
