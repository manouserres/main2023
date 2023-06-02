package org.team100.frc2023.autonomous;

import java.util.List;

import org.team100.frc2023.subsystems.AHRSClass;
import org.team100.frc2023.subsystems.SwerveDriveSubsystem;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;

public class Dodge extends TrajectoryCommand {

  public Dodge(SwerveDriveSubsystem m_robotDrive, Trajectory trajectory, AHRSClass gyro) {
    super(m_robotDrive, trajectory, gyro);
  }

  static Dodge newDodge(SwerveDriveSubsystem m_robotDrive, double y, AHRSClass gyro) {
    Pose2d currentRobotPose = m_robotDrive.getPose();
      double xRobot = currentRobotPose.getX();
      double yRobot = currentRobotPose.getY();
      Rotation2d rotRobot = currentRobotPose.getRotation();
      if (y < 0) {
          rotRobot = new Rotation2d(rotRobot.getRadians()-Math.PI);
      }

      
      TrajectoryConfig trajectoryConfig = new TrajectoryConfig(
              4,
              3)
              // Add kinematics to ensure max speed is actually obeyed
              .setKinematics(SwerveDriveSubsystem.kDriveKinematics);

      Trajectory exampleTrajectory = TrajectoryGenerator.generateTrajectory(
              // Start at the origin facing the +X direction
              new Pose2d(xRobot, yRobot, rotRobot),
              List.of(),
              new Pose2d(xRobot, yRobot + y, rotRobot),
              trajectoryConfig);

        return new Dodge(m_robotDrive, exampleTrajectory, gyro);

    }
}