package frc.robot;

import com.ma5951.utils.commands.MotorCommand;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandPS4Controller;
import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.Automations.ElevatorPlusIntakeAutomation;
import frc.robot.commands.Automations.Elevator.ResetElevator;
import frc.robot.commands.Automations.Elevator.SetElevatorPosition;
import frc.robot.commands.Automations.Elevator.setDesiredPosition;
import frc.robot.commands.Automations.Intake.EjectAutomation;
import frc.robot.commands.Automations.Intake.RunIntakeAutomation;
import frc.robot.subsystems.elevator.Elevator;
import frc.robot.subsystems.elevator.ElevatorConstance;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.intake.IntakeConstants;

public class RobotContainer {
        public static final CommandPS4Controller DRIVER_PS4_CONTROLLER = new CommandPS4Controller(
                        OperatorConstants.DRIVER_CONTROLLER_PORT);

        public static final CommandPS4Controller OPERATOR_PS4_CONTROLLER = new CommandPS4Controller(
                        OperatorConstants.OPERATOR_CONTROLLER_PORT);

        public RobotContainer() {
                configureBindings();
        }

        private void configureBindings() {
                // Driver Controller Bindings
                DRIVER_PS4_CONTROLLER.R1().whileTrue(new InstantCommand(() -> Intake.getInstance().setIgnoreProximitySensor(true))
                .andThen(new RunIntakeAutomation(IntakeConstants.coneIntakePower)));

                DRIVER_PS4_CONTROLLER.L1().whileTrue(new InstantCommand(() -> Intake.getInstance().setIgnoreProximitySensor(false))
                        .andThen(new RunIntakeAutomation(IntakeConstants.cubeIntakePower)));

                DRIVER_PS4_CONTROLLER.circle().whileTrue(new EjectAutomation())
                        .whileFalse(new InstantCommand(Intake.getInstance()::clearGamePieces)
                                .andThen(new InstantCommand(() -> Elevator.getInstance().setSetPoint(ElevatorConstance.minPose))));

                DRIVER_PS4_CONTROLLER.povDown().whileTrue(new MotorCommand(Elevator.getInstance(), 0.3, 0));

                DRIVER_PS4_CONTROLLER.povUp().whileTrue(new MotorCommand(Intake.getInstance(), IntakeConstants.coneIntakePower, 0));

                DRIVER_PS4_CONTROLLER.povLeft().whileTrue(new ResetElevator());

                DRIVER_PS4_CONTROLLER.triangle().whileTrue(
                        new SetElevatorPosition((Elevator.getInstance().getDesiredPos())));

                // Operator Controller Bindings
                OPERATOR_PS4_CONTROLLER.circle().whileTrue(new ResetElevator());

                OPERATOR_PS4_CONTROLLER.square().whileTrue(
                        new InstantCommand(() -> Intake.getInstance().setIgnoreProximitySensor(false))
                                .andThen(new ElevatorPlusIntakeAutomation(IntakeConstants.cubeIntakePower,
                                        ElevatorConstance.ShelfPose)))
                        .whileFalse(
                                new InstantCommand(() -> Elevator.getInstance().setSetPoint(ElevatorConstance.minPose)));

                OPERATOR_PS4_CONTROLLER.povUp().whileTrue(
                        new setDesiredPosition(Elevator.getInstance().highHight));

                OPERATOR_PS4_CONTROLLER.povDown().whileTrue(
                        new setDesiredPosition(ElevatorConstance.lowPose));

                OPERATOR_PS4_CONTROLLER.povRight().whileTrue(
                        new setDesiredPosition(Elevator.getInstance().midhight));

                OPERATOR_PS4_CONTROLLER.povLeft().whileTrue(
                        new setDesiredPosition(Elevator.getInstance().minHight));

                OPERATOR_PS4_CONTROLLER.L2().whileTrue(
                        new MotorCommand(
                                Elevator.getInstance(),
                                0.3, 0));
        }

        public Command getAutonomousCommand() {
                return null;
        }
}
