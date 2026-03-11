package frc.robot.commands;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Constants.DriveConstants;
import frc.robot.subsystems.drive.Drive;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

public final class DriveCommands {
  /**
   * command that runs the drive using the joystick. it
   *
   * @param drive drive subsystem
   * @param xSupplier gets the x position of the joystik control stick thing you know what im
   *     talking about
   * @param ySupplier gets the y position of the joystik control stick thing you know what im
   *     talking about
   * @param rotationSupplier gets the rot of the joystik control stick thing you know what im
   *     talking about
   * @param isRobotRelative makes sure that in case the robot relative speed gets desynced it can
   *     reset i think im not sure
   * @return runs the command at the speed based on the joysticks
   * 
   * @author Matthew McGrath
   */
  public static final Command joystickDrive(
      Drive drive,
      DoubleSupplier xSupplier,
      DoubleSupplier ySupplier,
      DoubleSupplier rotationSupplier,
      BooleanSupplier isRobotRelative) {
    return Commands.run(
        () -> {
          // gets x (-1.0 - 1.0)
          double x = xSupplier.getAsDouble();
          // gets y (-1.0 - 1.0)
          double y = ySupplier.getAsDouble();
          // gets rot (-1.0 - 1.0)
          double rot = rotationSupplier.getAsDouble();
          /*makes sure that if the position of the stick is outside the
          deadband the input is reversed because the field is
          weird and if its in the dead band the input is killed*/
          x = Math.abs(x) > DriveConstants.kDeadband ? x * Math.abs(x) : 0.0;
          y = Math.abs(y) > DriveConstants.kDeadband ? y * Math.abs(y) : 0.0;
          rot = Math.abs(rot) > DriveConstants.kDeadband ? rot * Math.abs(rot) : 0.0;
          // takes the percentage of max speed based on how far the stick is pushed
          x *= DriveConstants.kPhysicalMaxSpeed;
          y *= DriveConstants.kPhysicalMaxSpeed;
          rot *= DriveConstants.kMaxTeleAngularSpeed;
          // resets speeds if the speeds are desynced im not really sure
          // NOTE FOR FUTURE
          // COME BACK TO THIS ONE
          // AND VERIFY
          ChassisSpeeds speeds =
              isRobotRelative.getAsBoolean()
                  ? new ChassisSpeeds(x, y, rot)
                  : ChassisSpeeds.fromFieldRelativeSpeeds(x, y, rot, drive.getRawGyroRotation());

          drive.runVelocity(speeds, false);
        },
        drive);
  }
}
