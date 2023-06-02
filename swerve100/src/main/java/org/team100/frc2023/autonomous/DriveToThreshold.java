package org.team100.frc2023.autonomous;

import org.team100.frc2023.subsystems.SwerveDriveSubsystem;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class DriveToThreshold extends CommandBase {
  /** Creates a new DriveToThreshold. */
  SwerveDriveSubsystem m_robotDrive;
  boolean done;
  public DriveToThreshold(SwerveDriveSubsystem robotDrive) {
    // Use addRequirements() here to declare subsystem dependencies.

    m_robotDrive = robotDrive;

    addRequirements(m_robotDrive);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(m_robotDrive.getPose().getX() > 4.1){
        m_robotDrive.drive(-0.4, 0, 0, true);
    } else {
      m_robotDrive.drive(0, 0, 0, true);
      done = true;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return done;
  }
}