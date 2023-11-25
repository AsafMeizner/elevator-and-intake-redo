package frc.robot.commands.Automations.Intake;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.intake.Intake;

public class RunIntakeAutomation extends CommandBase{
    private Command intakeAutomation;

    public RunIntakeAutomation(double power) {
        intakeAutomation = new IntakeAutomation(power);
    }

    @Override
    public void initialize() {
        intakeAutomation.initialize();
    }

    @Override
    public void execute() {
        intakeAutomation.execute();
    }

    @Override
    public void end(boolean interrupted) {
        Intake.getInstance().setPower(0);
    }

    @Override
    public boolean isFinished() {
        return Intake.getInstance().isGamePieceDetected();
    }
    
}
