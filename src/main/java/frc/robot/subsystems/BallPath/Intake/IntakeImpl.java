package frc.robot.subsystems.BallPath.Intake;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import ca.team3161.lib.robot.LifecycleEvent;
import ca.team3161.lib.robot.subsystem.RepeatingPooledSubsystem;
import edu.wpi.first.wpilibj.Ultrasonic;

public class IntakeImpl extends RepeatingPooledSubsystem implements Intake {

    private static final double MOTOR_SPEED = 0.3;
    private static final double PRIMED_DIST_THRESHOLD = 15;
    private static final int SAMPLE_COUNT = 1;

    private final WPI_TalonSRX intake;
    private final Ultrasonic intakeSensor;

    private IntakeAction action = IntakeAction.NONE;
    private boolean lastPresent = false;
    private final Queue<Double> sensorSamples;

    public IntakeImpl(WPI_TalonSRX intake, Ultrasonic intakeSensor) {
        super(20, TimeUnit.MILLISECONDS);
        this.intake = intake;
        this.intakeSensor = intakeSensor;
        this.sensorSamples = new ArrayDeque<>();
    }

    @Override
    public void defineResources() {
        require(intake);
        require(intakeSensor);
    }

    @Override
    public void setAction(IntakeAction action) {
        this.action = action;
    }

    @Override
    public boolean ballPrimed() {
        return lastPresent;
    }

    @Override
    public void task() throws Exception {
        double sensorReading = this.intakeSensor.getRangeInches();
        this.sensorSamples.add(sensorReading);
        if (sensorSamples.size() > SAMPLE_COUNT) {
            this.sensorSamples.remove();
        }
        double meanReading = 0;
        for (Double d : sensorSamples) {
            meanReading += d / sensorSamples.size();
        }

        boolean ballPresent = meanReading < PRIMED_DIST_THRESHOLD;
        // boolean stateChanged = ballPresent != lastPresent;

        switch (action) {
            case FEED:
                if (ballPresent) {
                    this.intake.stopMotor();
                } else {
                    this.intake.set(MOTOR_SPEED);
                }
                break;
            case PRIME:
                if (ballPresent) {
                    this.intake.set(MOTOR_SPEED);
                } else {
                    this.intake.stopMotor();
                }
                break;
            case REJECT:
                if (ballPresent) {
                    this.intake.set(-MOTOR_SPEED);
                } else {
                    this.intake.stopMotor();
                }
                break;
            case NONE:
            default:
                intake.stopMotor();
                break;
        }

        lastPresent = ballPresent;
    }

    @Override
    public void lifecycleStatusChanged(LifecycleEvent previous, LifecycleEvent current) {
        switch (current) {
            case ON_INIT:
            case ON_AUTO:
            case ON_TELEOP:
            case ON_TEST:
                this.start();
                break;
            case ON_DISABLED:
            case NONE:
            default:
                this.cancel();
                break;
        }
    }
}
