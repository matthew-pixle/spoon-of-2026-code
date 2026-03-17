// Copyright (c) 2021-2026 Littleton Robotics
// http://github.com/Mechanical-Advantage
//
// Use of this source code is governed by a BSD
// license that can be found in the LICENSE file
// at the root directory of this project.

package frc.robot.subsystems.drive;

import edu.wpi.first.math.geometry.Rotation2d;
import org.littletonrobotics.junction.AutoLog;

public interface GyroIO {
  @AutoLog
  public static class GyroIOInputs {
    public boolean connected = false; //determines if gyro is connected
    public Rotation2d yawPosition = Rotation2d.kZero; //records yaw position in degrees
    public double yawVelocityRadPerSec = 0.0; //records yaw velocity in rad/sec
    public double[] odometryYawTimestamps = new double[] {}; //i think it creates timestamps for the odometry relating to yaw
    public Rotation2d[] odometryYawPositions = new Rotation2d[] {}; // records th odometry for the yaw positions
  }

  public default void updateInputs(GyroIOInputs inputs) {}

  public default void setYaw(Rotation2d angle) {}
}
