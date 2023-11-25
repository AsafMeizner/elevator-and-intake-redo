// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.elevator;

import com.ma5951.utils.MAShuffleboard;
import com.ma5951.utils.subsystem.DefaultInternallyControlledSubsystem;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.PortMap;
import frc.robot.subsystems.intake.Intake;

public class Elevator extends SubsystemBase implements DefaultInternallyControlledSubsystem {
  private CANSparkMax master;
  private CANSparkMax slave;
  private RelativeEncoder relativeEncoder;

  private SparkMaxPIDController pidController;
  private MAShuffleboard board;
  private static Elevator instance;

  public double midhight = ElevatorConstance.ConeMidPose;
  public double highHight = ElevatorConstance.highPoseCone;
  public double lowHight = ElevatorConstance.lowPose;
  public double minHight = ElevatorConstance.minPose;
  private double desiredPos = 0;
  private double setPoint = 0;

  private Elevator() {
    master = new CANSparkMax(PortMap.Elevator.mainMotorID, MotorType.kBrushless);
    slave = new CANSparkMax(PortMap.Elevator.followMotorID, MotorType.kBrushless);

    master.setIdleMode(IdleMode.kBrake);
    slave.setIdleMode(IdleMode.kBrake);

    master.setInverted(true);
    slave.follow(master, true);

    relativeEncoder = master.getEncoder();

    relativeEncoder.setPositionConversionFactor(ElevatorConstance.positionConversionFactor);
    relativeEncoder.setVelocityConversionFactor(ElevatorConstance.positionConversionFactor / 60);
    relativeEncoder.setPosition(0);

    master.setClosedLoopRampRate(ElevatorConstance.closedLoopRampRate);
    master.setOpenLoopRampRate(0);

    pidController = master.getPIDController();
    pidController.setFeedbackDevice(relativeEncoder);
    pidController.setP(ElevatorConstance.kP);
    pidController.setI(ElevatorConstance.kI);
    pidController.setD(ElevatorConstance.kD);

    board = new MAShuffleboard("Elevator");
  }

  public void resetEncoderPose(double pose) {
    relativeEncoder.setPosition(pose);
  }

  public double getElevatorExtension() {
    return relativeEncoder.getPosition();
  }

  @Override
  public void calculatePid(double setPoint) {
    pidController.setReference(setPoint, ControlType.kPosition);
  }

  @Override
  public boolean atPoint() {
    return Math.abs(getElevatorExtension() - setPoint) <= ElevatorConstance.tolerance;
  }

  @Override
  public void setVoltage(double voltage) {
    master.set(voltage / 12);
  }

  @Override
  public boolean canMove() {
    return setPoint >= ElevatorConstance.minPose
        && setPoint <= ElevatorConstance.maxPose;
  }

  @Override
  public void setSetPoint(double setPoint) {
    this.setPoint = setPoint;
  }

  @Override
  public double getSetPoint() {
    return setPoint;
  }

  public void setDesiredPos(double pos) {
    desiredPos = pos;
  }

  public double getDesiredPos() {
      return desiredPos;
  }

  public double getCurrent() {
    return master.getOutputCurrent();
  }

  public static Elevator getInstance() {
    if (instance == null) {
      instance = new Elevator();
    }
    return instance;
  }

  @Override
  public void periodic() {
    board.addNum("current position", getElevatorExtension());

    if (Intake.getInstance().isCubeDetected()) {
      midhight = ElevatorConstance.CubeMidPose;
      highHight = ElevatorConstance.highPoseCube;
    } else {
      midhight = ElevatorConstance.ConeMidPose;
      highHight = ElevatorConstance.highPoseCone;
    }
  }
}