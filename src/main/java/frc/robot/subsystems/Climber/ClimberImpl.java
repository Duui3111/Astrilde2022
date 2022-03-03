package frc.robot.subsystems.Climber;

// import java.lang.Thread;
import java.util.concurrent.TimeUnit;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
// import edu.wpi.first.wpilibj.DoubleSolenoid;
import frc.robot.RobotMap;

public class ClimberImpl implements Climber {
     
    private long starttime = 0;

    WPI_TalonSRX lifterMotorController;
    //DoubleSolenoid climberSolenoid;

    public ClimberImpl() {
        this.lifterMotorController = new WPI_TalonSRX(RobotMap.LIFTER_TALON_PORT);
        //this.climberSolenoid = new DoubleSolenoid(RobotMap.CLIMBER_SOLENOID_CHANNELS[0], RobotMap.CLIMBER_SOLENOID_CHANNELS[1]);
    }

    @Override
    public void extendOuterClimber() {
        long now = System.nanoTime();
        if(this.starttime < 0) this.starttime = now;
        this.lifterMotorController.set(1); // test value
        if(now > this.starttime + TimeUnit.SECONDS.toNanos(5)) this.lifterMotorController.set(0);
    }

    @Override
    public void retractOuterClimber() {
        long now = System.nanoTime();
        if(this.starttime < 0) this.starttime = now;
        this.lifterMotorController.set(1); // test value
        if(now > this.starttime + TimeUnit.SECONDS.toNanos(5)) this.lifterMotorController.set(-1);
    }

    @Override
    public void angleOuter(double angle) {}

    @Override
    public void attachInner() {}

}
