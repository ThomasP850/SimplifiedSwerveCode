package frc.robot.subsystem.swerve.commands;

import frc.robot.OI;
import frc.robot.subsystem.swerve.DrivetrainSubsystem;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.geometry.Translation2d;

public class DriveCommand extends CommandBase {

    private DrivetrainSubsystem driveTrain;

    public DriveCommand(DrivetrainSubsystem dt) {
        driveTrain = dt;
        addRequirements(driveTrain);
    }

    @Override
    public void execute() {
        double forward;
        if(OI.getInstance().getLeftXboxYValue() != 0) {
            forward = - OI.getInstance().getLeftXboxYValue();
        } else if(OI.getInstance().getAuxJoystickYValue() != 0) {
            forward = - OI.getInstance().getAuxJoystickYValue();
        } else {
            forward = - OI.getInstance().getLeftJoystickYValue();
        }
        // Square the forward stick
        forward = Math.copySign(Math.pow(forward, 2.0), forward);

        double strafe;
        if(OI.getInstance().getLeftXboxXValue() != 0) {
            strafe = - OI.getInstance().getLeftXboxXValue();
        } else if(OI.getInstance().getAuxJoystickXValue() != 0) {
            strafe = - OI.getInstance().getAuxJoystickXValue();
        } else {
            strafe = - OI.getInstance().getLeftJoystickXValue();
        }
        // Square the strafe stick
        strafe = Math.copySign(Math.pow(strafe, 2.0), strafe);

        double rotation;
        if(OI.getInstance().getRightXboxXValue() != 0) {
            rotation = - OI.getInstance().getRightXboxXValue();
        } else if(OI.getInstance().getAuxJoystickZValue() != 0) {
            rotation = - OI.getInstance().getAuxJoystickZValue();
        } else {
            rotation = - OI.getInstance().getRightJoystickXValue();
        }
        // Square the rotation stick
        rotation = Math.copySign(Math.pow(rotation, 2.0), rotation);

        driveTrain.drive(new Translation2d(forward, strafe), rotation, true);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
