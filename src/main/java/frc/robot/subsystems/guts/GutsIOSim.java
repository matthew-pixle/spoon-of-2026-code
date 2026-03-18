package frc.robot.subsystems.guts;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;

public class GutsIOSim implements GutsIO {
  private final DCMotor gearbox = DCMotor.getNEO(1); // makes a neo gearbox fo the guts
  private final DCMotorSim sim;

  // private final PIDController pid = new PIDController(1, 0, 0, Constants.kLoopPeriodSeconds);

  private double appliedVolts = 0.0;
  //creates new sim for guts
  public GutsIOSim() {
    sim =
        new DCMotorSim(
            LinearSystemId.createDCMotorSystem(gearbox, 0.025, GutsConstants.kGutMotorGearRatio),
            gearbox);
  }

  @Override
  public void updateInputs(GutsIOInputs inputs) {
    // sets max values for the volts
    appliedVolts = MathUtil.clamp(appliedVolts, -12.0, 12.0);
    //applies volts to the sim
    sim.setInputVoltage(appliedVolts);
    sim.update(0.02);

    inputs.velocityRadPerSec = sim.getAngularVelocityRPM();
  }

  @Override
  public void setOpenLoop(double speed) {
    appliedVolts = 12 * speed; // sets the applied volts based off the target speed
  }
}
