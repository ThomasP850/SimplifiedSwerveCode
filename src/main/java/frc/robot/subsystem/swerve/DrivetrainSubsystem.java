package frc.robot.subsystem.swerve;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;

import frc.robot.subsystem.PortMan;
import frc.robot.subsystem.swerve.commands.DriveCommand;
import frc.robot.subsystem.telemetry.Pigeon;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.kinematics.SwerveDriveKinematics;
import edu.wpi.first.wpilibj.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frcteam2910.common.drivers.SwerveModule;
import org.frcteam2910.common.math.Vector2;
import org.frcteam2910.common.robot.drivers.Mk2SwerveModuleBuilder;

public class DrivetrainSubsystem extends SubsystemBase {
    private static final double TRACKWIDTH = 17.5;
    private static final double WHEELBASE = 17.5;

    //Need to calibrate
    private static final double FRONT_LEFT_ANGLE_OFFSET = -Math.toRadians(152.1);
    private static final double FRONT_RIGHT_ANGLE_OFFSET = -Math.toRadians(71.1);
    private static final double BACK_LEFT_ANGLE_OFFSET = -Math.toRadians(214.5);
    private static final double BACK_RIGHT_ANGLE_OFFSET = -Math.toRadians(357.0);

    private SwerveModule frontLeftModule ;
    private SwerveModule frontRightModule ;
    private SwerveModule backLeftModule ;
    private SwerveModule backRightModule ;

    private final SwerveDriveKinematics kinematics = new SwerveDriveKinematics(
            new Translation2d(TRACKWIDTH / 2.0, WHEELBASE / 2.0),
            new Translation2d(TRACKWIDTH / 2.0, -WHEELBASE / 2.0),
            new Translation2d(-TRACKWIDTH / 2.0, WHEELBASE / 2.0),
            new Translation2d(-TRACKWIDTH / 2.0, -WHEELBASE / 2.0)
    );

    //private final Gyroscope gyroscope = new NavX(SPI.Port.kMXP);
    private Pigeon pigeon;

    public DrivetrainSubsystem() {
    }

    public void init(PortMan portMan) throws Exception {

        //private final Gyroscope gyroscope = new NavX(SPI.Port.kMXP);
        pigeon      = new Pigeon(portMan.acquirePort(portMan.can_21_label, "Pigeon"));

        pigeon.calibrate();
        pigeon.setInverted(true); // You might not need to invert the gyro

        //Need to update encoder ports and motor ports
        frontLeftModule = new Mk2SwerveModuleBuilder(
            new Vector2(TRACKWIDTH / 2.0, WHEELBASE / 2.0))
            .angleEncoder(new AnalogInput(portMan.acquirePort(PortMan.analog1_label, "FL.Swerve.Encoder")), FRONT_LEFT_ANGLE_OFFSET)
            .angleMotor(new CANSparkMax(portMan.acquirePort(PortMan.can_15_label, "FL.Swerve.angle"), CANSparkMaxLowLevel.MotorType.kBrushless),
                    Mk2SwerveModuleBuilder.MotorType.NEO)
            .driveMotor(new CANSparkMax(portMan.acquirePort(PortMan.can_10_label, "FL.Swerve.drive"), CANSparkMaxLowLevel.MotorType.kBrushless),
                    Mk2SwerveModuleBuilder.MotorType.NEO)
            .build();

        frontRightModule = new Mk2SwerveModuleBuilder(
            new Vector2(TRACKWIDTH / 2.0, -WHEELBASE / 2.0))
            .angleEncoder(new AnalogInput(portMan.acquirePort(PortMan.analog0_label, "FR.Swerve.Encoder")), FRONT_RIGHT_ANGLE_OFFSET)
            .angleMotor(new CANSparkMax(portMan.acquirePort(PortMan.can_17_label, "FR.Swerve.angle"), CANSparkMaxLowLevel.MotorType.kBrushless),
                    Mk2SwerveModuleBuilder.MotorType.NEO)
            .driveMotor(new CANSparkMax(portMan.acquirePort(PortMan.can_06_label, "FR.Swerve.drive"), CANSparkMaxLowLevel.MotorType.kBrushless),
                    Mk2SwerveModuleBuilder.MotorType.NEO)
            .build();
            
        backLeftModule = new Mk2SwerveModuleBuilder(
            new Vector2(-TRACKWIDTH / 2.0, WHEELBASE / 2.0))
            .angleEncoder(new AnalogInput(portMan.acquirePort(PortMan.analog2_label, "BL.Swerve.Encoder")), BACK_LEFT_ANGLE_OFFSET)
            .angleMotor(new CANSparkMax(portMan.acquirePort(PortMan.can_59_label, "BL.Swerve.angle"), CANSparkMaxLowLevel.MotorType.kBrushless),
                    Mk2SwerveModuleBuilder.MotorType.NEO)
            .driveMotor(new CANSparkMax(portMan.acquirePort(PortMan.can_60_label, "BL.Swerve.drive"), CANSparkMaxLowLevel.MotorType.kBrushless),
                    Mk2SwerveModuleBuilder.MotorType.NEO)
            .build();

        backRightModule = new Mk2SwerveModuleBuilder(
            new Vector2(-TRACKWIDTH / 2.0, -WHEELBASE / 2.0))
            .angleEncoder(new AnalogInput(portMan.acquirePort(PortMan.analog3_label, "BR.Swerve.Encoder")), BACK_RIGHT_ANGLE_OFFSET)
            .angleMotor(new CANSparkMax(portMan.acquirePort(PortMan.can_14_label, "BR.Swerve.angle"), CANSparkMaxLowLevel.MotorType.kBrushless),
                    Mk2SwerveModuleBuilder.MotorType.NEO)
            .driveMotor(new CANSparkMax(portMan.acquirePort(PortMan.can_09_label, "BR.Swerve.drive"), CANSparkMaxLowLevel.MotorType.kBrushless),
                    Mk2SwerveModuleBuilder.MotorType.NEO)
            .build();

        

        frontLeftModule.setName("Front Left");
        frontRightModule.setName("Front Right");
        backLeftModule.setName("Back Left");
        backRightModule.setName("Back Right");

        setDefaultCommand(new DriveCommand(this));
    }


    @Override
    public void periodic() {
        frontLeftModule.updateSensors();
        frontRightModule.updateSensors();
        backLeftModule.updateSensors();
        backRightModule.updateSensors();

        SmartDashboard.putNumber("Front Left Module Angle", Math.toDegrees(frontLeftModule.getCurrentAngle()));
        SmartDashboard.putNumber("Front Right Module Angle", Math.toDegrees(frontRightModule.getCurrentAngle()));
        SmartDashboard.putNumber("Back Left Module Angle", Math.toDegrees(backLeftModule.getCurrentAngle()));
        SmartDashboard.putNumber("Back Right Module Angle", Math.toDegrees(backRightModule.getCurrentAngle()));

        SmartDashboard.putNumber("Gyroscope Angle", pigeon.getAngle().toDegrees());

        frontLeftModule.updateState(TimedRobot.kDefaultPeriod);
        frontRightModule.updateState(TimedRobot.kDefaultPeriod);
        backLeftModule.updateState(TimedRobot.kDefaultPeriod);
        backRightModule.updateState(TimedRobot.kDefaultPeriod);
    }

    public void drive(Translation2d translation, double rotation, boolean fieldOriented) {
        rotation *= 2.0 / Math.hypot(WHEELBASE, TRACKWIDTH);
        ChassisSpeeds speeds;
        if (fieldOriented) {
            speeds = ChassisSpeeds.fromFieldRelativeSpeeds(translation.getX(), translation.getY(), rotation,
                    Rotation2d.fromDegrees(pigeon.getAngle().toDegrees()));
        } else {
            speeds = new ChassisSpeeds(translation.getX(), translation.getY(), rotation);
        }

        SwerveModuleState[] states = kinematics.toSwerveModuleStates(speeds);
        frontLeftModule.setTargetVelocity(states[0].speedMetersPerSecond, states[0].angle.getRadians());
        frontRightModule.setTargetVelocity(states[1].speedMetersPerSecond, states[1].angle.getRadians());
        backLeftModule.setTargetVelocity(states[2].speedMetersPerSecond, states[2].angle.getRadians());
        backRightModule.setTargetVelocity(states[3].speedMetersPerSecond, states[3].angle.getRadians());
    }

    public void resetGyroscope() {
        pigeon.setAdjustmentAngle(pigeon.getUnadjustedAngle());
    }
}
