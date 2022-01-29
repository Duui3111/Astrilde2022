package frc.robot.subsystems.BallPath;

import java.util.concurrent.TimeUnit;

import ca.team3161.lib.robot.LifecycleEvent;
import ca.team3161.lib.robot.subsystem.RepeatingPooledSubsystem;

import frc.robot.subsystems.BallPath.Intake.Intake;
import frc.robot.subsystems.BallPath.Elevator.Elevator;
import frc.robot.subsystems.BallPath.Shooter.Shooter;
import edu.wpi.first.wpilibj.Ultrasonic;

public class BallPathImpl extends RepeatingPooledSubsystem implements BallPath {

    private final Intake intake;
    private final Elevator elevator;
    private final Ultrasonic elevatorSensor;
    private final Shooter shooter;
    private String action;

    public BallPathImpl(Intake intake, Elevator elevator, Shooter shooter, Ultrasonic elevatorSensor) {
        super(20, TimeUnit.MILLISECONDS);
        this.intake = intake;
        this.elevator = elevator;
        this.elevatorSensor = elevatorSensor;
        this.shooter = shooter;
    }

    @Override
    public void defineResources(){}

    @Override
    public void task() throws InterruptedException{
        if (this.action.equals("START_INTAKE")){
            if (this.intake.checkIntake()){
                this.intake.stop();
            }
            if (this.intake.checkColour() && !this.checkIfPrimed()){
                this.intake.start();
                Thread.sleep(3000);
                this.intake.stop();
            }
        }
    }

    // Declare interface with team
    @Override
    public void startIntake(){
        this.intake.start();
        this.action = "START_INTAKE";
    }

    @Override
    public void reverseIntake(){
        this.intake.reverse();
    }

    @Override
    public void stopIntake(){
        this.intake.stop();
    }

    @Override
    public boolean checkIfPrimed(){
        // if (ballUnderElevator){
        //     return true;
        // }
        if (this.elevatorSensor.getRangeInches() <= 4 ){
            return true;
        }
        return false;
    }

    @Override
    public void startElevator(){
        this.elevator.start();
    }
    
    // can be used to stop a ball from going up the elevator in the event that we cannot shoot the ball
    @Override
    public void reverseElevator(){
        this.elevator.reverse();
    }

    @Override
    public void stopElevator(){
        this.elevator.stop();
    }

    @Override
    public void findAndCenterTarget(){
        this.shooter.findAndCenterTarget();
    }

    @Override
    public boolean readyToShoot(){
        return this.shooter.readyToShoot();
    }

    @Override
    public void startShooter(){
        this.shooter.start();
    }

    @Override
    public void stopShooter(){
        this.shooter.stop();
    }
        
    @Override
    public void lifecycleStatusChanged(LifecycleEvent previous, LifecycleEvent current) {}
   
}
