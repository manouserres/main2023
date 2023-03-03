package team100.localization;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;

public class PoseEstimationHelperTest {
    private static final double kDelta = 0.01;

    @Test
    public void testCameraRotationInFieldCoords() {
        Transform3d cameraInRobotCoords = new Transform3d(
                new Translation3d(0.5, 0, 0.5), // front camera
                new Rotation3d(0, -Math.PI / 4, Math.PI / 4)); // pi/4 tilt up, pi/4 yaw left
        // robot rotation should only ever be yaw
        Rotation3d robotRotationInFieldCoordsFromGyro = new Rotation3d(0, 0, Math.PI / 2);
        Rotation3d cameraRotationInFieldCoords = PoseEstimationHelper.cameraRotationInFieldCoords(
                cameraInRobotCoords,
                robotRotationInFieldCoordsFromGyro);
        assertEquals(0, cameraRotationInFieldCoords.getX(), kDelta); // still no roll
        assertEquals(-Math.PI / 4, cameraRotationInFieldCoords.getY(), kDelta); // same tilt
        assertEquals(3 * Math.PI / 4, cameraRotationInFieldCoords.getZ(), kDelta);
    }

    @Test
    public void testBlipToNWU() {
        // Blip is "z-forward", one meter up, two meters left, three meters ahead
        // rotation doesn't matter
        Blip blip = new Blip(5,
                new double[][] {
                        { 1, 0, 0 },
                        { 0, 1, 0 },
                        { 0, 0, 1 } },
                new double[][] {
                        { -2 },
                        { -1 },
                        { 3 } });

        Translation3d nwuTranslation = PoseEstimationHelper.blipToNWU(blip);

        // NWU values now
        assertEquals(3, nwuTranslation.getX(), kDelta);
        assertEquals(2, nwuTranslation.getY(), kDelta);
        assertEquals(1, nwuTranslation.getZ(), kDelta);
    }

    /**
     * We don't trust the camera's estimate of tag rotation. Instead, since we know
     * the pose of the tag, and we know the rotation of the camera (from the
     * gyro/compass and the camera offset), we calculate what the tag rotation
     * *should* be.
     */
    @Test
    public void testtagRotationInRobotCoordsFromGyro() {
        {
            Rotation3d tagRotationInFieldCoords = new Rotation3d(0, 0, 0); // on the far wall
            Rotation3d cameraRotationInFieldCoords = new Rotation3d(0, 0, Math.PI / 4); // 45 degrees left
            Rotation3d tagRotationInRobotCoordsFromGyro = PoseEstimationHelper.tagRotationInRobotCoordsFromGyro(
                    tagRotationInFieldCoords,
                    cameraRotationInFieldCoords);
            assertEquals(0, tagRotationInRobotCoordsFromGyro.getX(), kDelta);
            assertEquals(0, tagRotationInRobotCoordsFromGyro.getY(), kDelta);
            assertEquals(-Math.PI / 4, tagRotationInRobotCoordsFromGyro.getZ(), kDelta); // 45 degrees right
        }
        {
            Rotation3d tagRotationInFieldCoords = new Rotation3d(0, 0, 0); // on the far wall
            Rotation3d cameraRotationInFieldCoords = new Rotation3d(0, -Math.PI / 4, 0); // 45 degrees up
            Rotation3d tagRotationInRobotCoordsFromGyro = PoseEstimationHelper.tagRotationInRobotCoordsFromGyro(
                    tagRotationInFieldCoords,
                    cameraRotationInFieldCoords);
            assertEquals(0, tagRotationInRobotCoordsFromGyro.getX(), kDelta);
            assertEquals(Math.PI / 4, tagRotationInRobotCoordsFromGyro.getY(), kDelta); // 45 degrees down
            assertEquals(0, tagRotationInRobotCoordsFromGyro.getZ(), kDelta);
        }
        {
            Rotation3d tagRotationInFieldCoords = new Rotation3d(0, 0, 0); // on the far wall
            Rotation3d cameraRotationInFieldCoords = new Rotation3d(0, -Math.PI / 4, Math.PI / 4); // pan/tilt
            Rotation3d tagRotationInRobotCoordsFromGyro = PoseEstimationHelper.tagRotationInRobotCoordsFromGyro(
                    tagRotationInFieldCoords,
                    cameraRotationInFieldCoords);
            // composing and then inverting multiple rotations yields this:
            assertEquals(-0.615, tagRotationInRobotCoordsFromGyro.getX(), kDelta);
            assertEquals(0.523, tagRotationInRobotCoordsFromGyro.getY(), kDelta);
            assertEquals(-0.955, tagRotationInRobotCoordsFromGyro.getZ(), kDelta);
        }
    }

    @Test
    public void testToFieldCoordinates() {
        {
            // opposite corner of 1,1 square
            Transform3d tagInCameraCords = new Transform3d(
                    new Translation3d(Math.sqrt(2), 0, 0),
                    new Rotation3d(0, 0, -Math.PI / 4));
            Pose3d tagInFieldCords = new Pose3d(
                    new Translation3d(1, 1, 0),
                    new Rotation3d());
            Pose3d toFieldCoordinates = PoseEstimationHelper.toFieldCoordinates(
                    tagInCameraCords,
                    tagInFieldCords);
            assertEquals(0, toFieldCoordinates.getX(), kDelta);
            assertEquals(0, toFieldCoordinates.getY(), kDelta);
            assertEquals(0, toFieldCoordinates.getZ(), kDelta);
            assertEquals(0, toFieldCoordinates.getRotation().getX(), kDelta);
            assertEquals(0, toFieldCoordinates.getRotation().getY(), kDelta);
            assertEquals(Math.PI / 4, toFieldCoordinates.getRotation().getZ(), kDelta); // pan45
        }
        {
            // in front, high
            Transform3d tagInCameraCords = new Transform3d(
                    new Translation3d(Math.sqrt(2), 0, 0),
                    new Rotation3d(0, Math.PI / 4, 0));
            Pose3d tagInFieldCords = new Pose3d(
                    new Translation3d(1, 0, 1),
                    new Rotation3d());
            Pose3d toFieldCoordinates = PoseEstimationHelper.toFieldCoordinates(
                    tagInCameraCords,
                    tagInFieldCords);
            assertEquals(0, toFieldCoordinates.getX(), kDelta);
            assertEquals(0, toFieldCoordinates.getY(), kDelta);
            assertEquals(0, toFieldCoordinates.getZ(), kDelta);
            assertEquals(0, toFieldCoordinates.getRotation().getX(), kDelta);
            assertEquals(-Math.PI / 4, toFieldCoordinates.getRotation().getY(), kDelta); // tilt45
            assertEquals(0, toFieldCoordinates.getRotation().getZ(), kDelta);
        }
    }

    @Test
    public void testApplyCameraOffset() {
        // trivial example: if camera offset happens to match the camera global pose
        // then of course the robot global pose is the origin.
        Pose3d cameraInFieldCoords = new Pose3d(
                new Translation3d(1, 1, 1),
                new Rotation3d(0, 0, 0));
        Transform3d cameraInRobotCoords = new Transform3d(
                new Translation3d(1, 1, 1),
                new Rotation3d(0, 0, 0));
        Pose3d robotPoseInFieldCoords = PoseEstimationHelper.applyCameraOffset(
                cameraInFieldCoords,
                cameraInRobotCoords);
        assertEquals(0, robotPoseInFieldCoords.getX(), kDelta);
        assertEquals(0, robotPoseInFieldCoords.getY(), kDelta);
        assertEquals(0, robotPoseInFieldCoords.getZ(), kDelta);
        assertEquals(0, robotPoseInFieldCoords.getRotation().getX(), kDelta);
        assertEquals(0, robotPoseInFieldCoords.getRotation().getY(), kDelta);
        assertEquals(0, robotPoseInFieldCoords.getRotation().getZ(), kDelta);
    }

}
