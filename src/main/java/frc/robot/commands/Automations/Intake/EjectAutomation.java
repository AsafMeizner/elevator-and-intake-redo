package frc.robot.commands.Automations.Intake;

import com.ma5951.utils.commands.MotorCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.intake.IntakeConstants;



public class EjectAutomation extends SequentialCommandGroup{
    private static double getPower() {
        return Intake.getInstance().isConeDetected() ? IntakeConstants.coneEjectPower: IntakeConstants.cubeEjectPower;
    }

    public EjectAutomation() {
        addCommands(
            new MotorCommand(Intake.getInstance(), EjectAutomation::getPower , 0)
        );
    }
}
