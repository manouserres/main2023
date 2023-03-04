package team100.control;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.autonomous.DriveToAprilTag;
import frc.robot.autonomous.DriveToWaypoint2;
import frc.robot.commands.ArmHigh;
import frc.robot.commands.DriveRotation;
import frc.robot.commands.GoalOffset;
import frc.robot.commands.ResetPose;
import frc.robot.commands.ResetRotation;
import frc.robot.subsystems.SwerveDriveSubsystem;


/**
 * see
 * https://docs.google.com/document/d/1M89x_IiguQdY0VhQlOjqADMa6SYVp202TTuXZ1Ps280/edit#
 */
public class DualXboxControl implements Control, Sendable {
    // TODO: express these limits in m/s.
    // private static final int ySlewRateLimit = 3;
    // private static final int xSlewRateLimit = 3;
    
    private final CommandXboxController controller0;
    private final CommandXboxController controller1;

    // private final SlewRateLimiter m_xspeedLimiter = new SlewRateLimiter(xSlewRateLimit);
    // private final SlewRateLimiter m_yspeedLimiter = new SlewRateLimiter(ySlewRateLimit);

    public DualXboxControl() {
        controller0 = new CommandXboxController(0);
        System.out.printf("Controller0: %s\n", controller0.getHID().getName());
        controller1 = new CommandXboxController(1);
        System.out.printf("Controller1: %s\n", controller1.getHID().getName());
        SmartDashboard.putData("Robot Container", this);
    }

    @Override
    public void trajtoApril(SwerveDriveSubsystem m_robotDrive, int ID){
        // controler0.b().whileTrue(MoveToAprilTag.newMoveToAprilTag(m_robotDrive, () -> m_robotDrive.getPose(), 1));
    };


    // @Override
    // public void resetRotation(ResetRotation command) {
    //     // TODO: choose one
    //     controller0.leftBumper().onTrue(command);
    //     // controller0.a().onTrue(command);
    // }

    public void driveSlow(SwerveDriveSubsystem m_robotDrive){
        // controller0.rightBumper().onTrue(m_robotDrive.driveSl)
    }

    @Override
    public void driveToAprilTag(DriveToAprilTag command) {
        // controller0.x().whileTrue(command);
    }

    @Override
    public void driveToLeftGrid(DriveToWaypoint2 command){
        controller0.x().whileTrue(command);
    };

    public void driveToCenterGrid(DriveToWaypoint2 command){
        controller0.a().whileTrue(command);
    };

    public void driveToRightGrid(DriveToWaypoint2 command){
        controller0.b().whileTrue(command);
    };

    public void driveToSubstation(DriveToWaypoint2 command){
        controller0.y().whileTrue(command);
    };


    // TODO: decide what "Y" should do.

    @Override 
    public void driveToAprilTag2(DriveToAprilTag command) {
        // controller0.y().whileTrue(command);
    }

    @Override
    public void resetRotation(ResetRotation command) {
        controller0.rightBumper().onTrue(command);
    }

    @Override
    public void autoLevel(frc.robot.commands.autoLevel command) {
        // controller0.y().whileTrue(command);
    }

    // @Override
    // public void sanjanAuto(SanjanAutonomous command) {
    // controller0.y().onTrue(command);
    // }

    @Override
    public void armHigh(ArmHigh command) {
        controller1.b().onTrue(command);
    }

    // @Override
    // public void driveWithHeading0(DriveWithHeading command){
    //     controller0.povUp().whileTrue(command);
    // }

    // @Override
    // public void driveWithHeading90(DriveWithHeading command){
    //     controller0.povLeft().whileTrue(command);
    // }

    // @Override
    // public void driveWithHeading180(DriveWithHeading command){
    //     controller0.povDown().whileTrue(command);
    // }

    // @Override
    // public void driveWithHeading270(DriveWithHeading command){
    //     controller0.povRight().whileTrue(command);
    // }

    @Override
    public void driveRotation(DriveRotation command){
        controller0.rightBumper().whileTrue(command);
    }


    @Override
    public double xSpeed() {
        return -1.0 * controller0.getRightY();
    }

    @Override
    public double ySpeed() {
        return -1.0 * controller0.getRightX();
    }

    @Override
    public double rotSpeed() {
        return -1.0 * controller0.getLeftX();
    }

    @Override
    public double throttle() {
        return 1.0;
    }

    @Override
    public double openSpeed() {
        return controller1.getRightTriggerAxis();
    }

    @Override
    public double closeSpeed() {
        return controller1.getLeftTriggerAxis();
    }

    @Override
    public double lowerSpeed() {
        return controller1.getRightX();
    }

    @Override
    public double upperSpeed() {
        return controller1.getLeftY();
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("xbox control");
        builder.addDoubleProperty("right y", () -> controller0.getRightY(), null);
        builder.addDoubleProperty("right x", () -> controller0.getRightX(), null);
        builder.addDoubleProperty("left x", () -> controller0.getLeftX(), null);
    }

    @Override
    public void resetPose(ResetPose command) {
        controller0.leftBumper().onTrue(command);
    }

    Rotation2d previousRotation = new Rotation2d(0);


    @Override
    public Rotation2d desiredRotation() {
        // return new Rotation2d(-controller0.getLeftX(), -controller0.getLeftY());
        double foo = -controller0.getHID().getPOV();
        if (foo > 0) {
            return previousRotation;
        } 
        previousRotation = Rotation2d.fromDegrees(foo);
        return previousRotation;
        
        
    }

    @Override
    public GoalOffset goalOffset() {
        double left = controller0.getLeftTriggerAxis();
        double right = controller0.getRightTriggerAxis();
        double kThreshold = .5;
        if (left > kThreshold) {
            if (right > kThreshold) {
                return GoalOffset.center;
            }
            return GoalOffset.left;
        }
        if (right > kThreshold) {
            return GoalOffset.right;
        }
        return GoalOffset.center;
    }
}
