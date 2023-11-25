package frc.robot.subsystems.intake;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import com.ma5951.utils.MAShuffleboard;
import com.ma5951.utils.subsystem.MotorSubsystem;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.PortMap;

public class Intake extends SubsystemBase implements MotorSubsystem {

    private static Intake intake;

    private CANSparkMax intakeMotor;
    private DigitalInput intakeSensor;

    private MAShuffleboard shuffleboard;

    private boolean coneDetected = false;
    private boolean cubeDetected = false;

    private boolean ignoreMotorCurrent = false;
    private boolean ignoreProximitySensor = false;

    private double voltageSpikeStartTime = 0.0;

    private Intake() {
        intakeMotor = new CANSparkMax(PortMap.Intake.motorID, MotorType.kBrushless);
        intakeSensor = new DigitalInput(PortMap.Intake.sensorID);
        shuffleboard = new MAShuffleboard("Intake");

        intakeMotor.setIdleMode(IdleMode.kBrake);
        intakeMotor.setInverted(false);
    }

    public boolean isProximitySensorTriggered() {
        return !intakeSensor.get();
    }

    public double getCurrentDraw() {
        return intakeMotor.getOutputCurrent();
    }

    public boolean isConeDetected() {
        return coneDetected;
    }

    public void setConeDetection(boolean detected) {
        coneDetected = detected;
    }

    public void setCubeDetection(boolean detected) {
        cubeDetected = detected;
    }

    public void clearGamePieces() {
        setConeDetection(false);
        setCubeDetection(false);
        setIgnoreProximitySensor(true);
    }

    public boolean isCubeDetected() {
        return cubeDetected;
    }

    public boolean isGamePieceDetected() {
        return isCubeDetected() || isConeDetected();
    }

    @Override
    public boolean canMove() {
        return true;
    }

    @Override
    public void setVoltage(double voltage) {
        intakeMotor.set(voltage / 12);
    }

    public void setIgnoreCurrent(boolean ignoreCurrent) {
        this.ignoreMotorCurrent = ignoreCurrent;
    }

    public void setIgnoreProximitySensor(boolean ignoreProximitySensor) {
        this.ignoreProximitySensor = ignoreProximitySensor;
    }

    public static Intake getInstance() {
        if (intake == null) {
            intake = new Intake();
        }
        return intake;
    }

    @Override
    public void periodic() {
        shuffleboard.addBoolean("Is Cone Detected", isConeDetected());
        shuffleboard.addBoolean("Is Cube Detected", isCubeDetected());

        if (getCurrentDraw() > IntakeConstants.spikeThreshold && !ignoreMotorCurrent && ignoreProximitySensor) {
            setConeDetection(true);
        }

        if ((isProximitySensorTriggered() || (getCurrentDraw() > IntakeConstants.spikeThreshold && !ignoreMotorCurrent))
                && !ignoreProximitySensor) {
            if (voltageSpikeStartTime == 0.0) {
                voltageSpikeStartTime = Timer.getFPGATimestamp();
            }
            double currentTime = Timer.getFPGATimestamp();
            if (currentTime - voltageSpikeStartTime > IntakeConstants.spikeTimeThreshold) {
                setCubeDetection(true);
            }
        } else {
            voltageSpikeStartTime = 0.0; // Reset the timer if no voltage spike
        }
    }
}
