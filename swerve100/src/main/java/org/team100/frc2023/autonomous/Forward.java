package org.team100.frc2023.autonomous;

import java.util.List;

import org.team100.frc2023.subsystems.AHRSClass;
import org.team100.frc2023.subsystems.SwerveDriveSubsystem;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;

/** Add your docs here. */
public class Forward extends TrajectoryCommand {

    public Forward(SwerveDriveSubsystem m_robotDrive, Trajectory trajectory, AHRSClass gyro) {
        super(m_robotDrive, trajectory, gyro);
        
    }
    static Forward newForward(SwerveDriveSubsystem m_robotDrive, double x, AHRSClass gyro) {
        Pose2d currentRobotPose = m_robotDrive.getPose();
            double xRobot = currentRobotPose.getX();
            double yRobot = currentRobotPose.getY();
            Rotation2d rotRobot = currentRobotPose.getRotation();
            if (x < 0) {
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
                    new Pose2d(xRobot + x, yRobot, rotRobot),
                    trajectoryConfig);
    
            return new Forward(m_robotDrive, exampleTrajectory, gyro);
       } 
}