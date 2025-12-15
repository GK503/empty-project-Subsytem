package org.frogforce503.robot2025.subsystems;

import org.littletonrobotics.junction.AutoLog;

import com.revrobotics.spark.ClosedLoopSlot;

public interface ArmIO { 
    // Defines an interface for the ArmIO subsystem

    @AutoLog 
    class ArmIOInputs{
        // Class to hold input data for the ArmIO, annotated with @AutoLog to automatically log the data
        ArmIOData data = new ArmIOData(0, 0, false, 0, 0, 0);
    }

    record ArmIOData(
        double positionRadians,  // Current position of the arm in radians
        double velocityRadians,  // Current velocity of the arm in radians per second
        boolean isMotorConnected,  // Whether the motor is connected
        double motorTemperature,  // Current temperature of the motor
        double motorVoltage,  // Current voltage supplied to the motor 
        double motorCurrent  // Current current draw of the motor
    ){}

    public void updateInputs(ArmIOInputs inputs);
    // Updates the input data for the ArmIO subsystem

    public double getPosition();
    // Returns the current position of the arm in radians

    public double getVelocity();
    // Returns the current velocity of the arm in radians per second

    public void setVoltage(double voltage);
    // Sets the desired velocity of the arm

    public double getVoltage();

    public void setPosition(double setPointRadans);
    // Sets the desired position of the arm

    public void setPID(double p, double i, double d, ClosedLoopSlot slot);
    // Sets the PID constants for the arm controller

    public void setIdleMode(boolean isBrake);
    // Sets the operating mode of the arm (e.g. position control, velocity control, etc.)

    public void stop();
    // Stops the arm motor

    public void reset();
    // Resets the arm encoder and controller

    public double getTemp();
    // Returns the current temperature of the arm motor

    public double getOutputCurrent();
    // Returns the current output current of the arm motor

    public boolean isMotorConnected();
    // checks weather the motor is connected

    public void setPIDSlot(ClosedLoopSlot slot);
    // sets the PID slot
}