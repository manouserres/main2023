package org.team100.frc2023.commands;

import java.util.function.Supplier;

import org.team100.lib.commands.DriveUtil;
import org.team100.lib.motion.drivetrain.SwerveDriveSubsystem;

import edu.wpi.first.math.geometry.Twist2d;
import edu.wpi.first.wpilibj2.command.CommandBase;

/** Accepts [-1,1] input and scales it to the specified maximum speeds. */
public class DriveWithSetpointGenerator extends CommandBase {
    private final Supplier<Twist2d> twistSupplier;
    private final SwerveDriveSubsystem m_robotDrive;
    private final double kMaxSpeed;
    private final double kMaxRot;

    public DriveWithSetpointGenerator(
            Supplier<Twist2d> twistSupplier,
            SwerveDriveSubsystem robotDrive,
            double maxSpeedM_S,
            double maxRotSpeedRad_S) {
        this.twistSupplier = twistSupplier;
        m_robotDrive = robotDrive;
        kMaxSpeed = maxSpeedM_S;
        kMaxRot = maxRotSpeedRad_S;
        addRequirements(m_robotDrive);
    }

    @Override
    public void execute() {
        Twist2d twistM_S = DriveUtil.scale(twistSupplier.get(), kMaxSpeed, kMaxRot);
        m_robotDrive.driveInFieldCoords(twistM_S);
    }
}