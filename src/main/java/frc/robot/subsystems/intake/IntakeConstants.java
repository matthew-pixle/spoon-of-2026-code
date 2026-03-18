package frc.robot.subsystems.intake;

import com.ctre.phoenix6.configs.Slot0Configs;

public final class IntakeConstants {
  public static final double kPivotMotorSpeed = 0.4; //pivot speed of the intake
  public static final double kRollerMotorSpeed = -0.8; //roller speed of the intake
  public static final double kSignificantlyFasterRollerMotorSpeed = -0.75; //jobless

  // TODO: Tune
  public static final double kExtensionPositionRotations = 100.0;

  // Change Gear Ratios later
  public static final double kPivotMotorGearRatio = 1.0;
  public static final double kRollerMotorGearRatio = 1.0;

  public static final Slot0Configs kPivotGains =
      new Slot0Configs().withKP(0.0).withKD(0.0).withKS(0.0).withKV(0.0);
}
