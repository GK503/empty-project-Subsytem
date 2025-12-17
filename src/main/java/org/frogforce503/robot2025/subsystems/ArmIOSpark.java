package org.frogforce503.robot2025.subsystems;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.REVLibError;
import com.revrobotics.spark.ClosedLoopSlot;
import com.revrobotics.spark.SparkAbsoluteEncoder;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.wpilibj.CAN;

import com.revrobotics.spark.config.SparkMaxConfig;

public class ArmIOSpark implements ArmIO{
   private SparkAbsoluteEncoder encoder;
   private SparkMax motor;
   private SparkMaxConfig motorconfig;
   private ClosedLoopSlot closedLoopSlot;

   public ArmIOSpark(int CANID, MotorType MotorType){
       motor = new SparkMax(CANID, MotorType);
       encoder = motor.getAbsoluteEncoder();
       motorconfig = new SparkMaxConfig();
       motorconfig.closedLoop.pid(0.1, 0.0, 0.2, ClosedLoopSlot.kSlot0);
       motorconfig.closedLoop.pid(0.2, 0.0, 0.1, ClosedLoopSlot.kSlot1);
       motorconfig.idleMode(IdleMode.kBrake);
       motorconfig.absoluteEncoder.zeroOffset(0.0);
       motorconfig.inverted(true);
       motorconfig.smartCurrentLimit(40);
       motorconfig.voltageCompensation(12);
       motorconfig.absoluteEncoder
              .zeroOffset(0)
              .positionConversionFactor(2 * Math.PI) // Rotations to Radians
              .velocityConversionFactor(2 * Math.PI / 60);
       motor.configure(motorconfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
   }

   @Override
   public void updateInputs(ArmIOInputs inputs){
   inputs.data = new ArmIOData(
       encoder.getPosition(),
       encoder.getVelocity(),
       motor.getLastError() == REVLibError.kOk,
       motor.getMotorTemperature(),
       motor.getBusVoltage() + motor.getAppliedOutput(),
       motor.getOutputCurrent()
       );
   }

   public double getPosition(){
       return encoder.getPosition();
   }

   public double getVelocity(){
       return encoder.getVelocity();
   }

   @Override
   public void setPosition(double setPointRadians){
       motor.getClosedLoopController().setReference(setPointRadians, ControlType.kPosition, closedLoopSlot);
   }

   @Override
   public void setPID(double p, double i, double d, ClosedLoopSlot slot) {
       motorconfig.closedLoop.pid(p, i, d, slot);
       motor.configure(motorconfig, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);
   }

   @Override
   public void setIdleMode(boolean isBrake){
       if (isBrake == true){
           motorconfig.idleMode(IdleMode.kBrake);
       } else {
           motorconfig.idleMode(IdleMode.kCoast);
       }
       motor.configure(motorconfig, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);
   }

   @Override
   public void stop(){
      motor.stopMotor();
   }

   @Override
   public void reset(int CANID, MotorType MotorType){
        motor.stopMotor();
        motor = new SparkMax(CANID, MotorType);
        encoder = motor.getAbsoluteEncoder();
        motorconfig = new SparkMaxConfig();
        motorconfig.closedLoop.pid(0.1, 0.0, 0.2, ClosedLoopSlot.kSlot0);
        motorconfig.closedLoop.pid(0.2, 0.0, 0.1, ClosedLoopSlot.kSlot1);
        motorconfig.idleMode(IdleMode.kBrake);
        motorconfig.absoluteEncoder.zeroOffset(0.0);
        motorconfig.inverted(true);
        motorconfig.smartCurrentLimit(40);
        motorconfig.voltageCompensation(12);
        motorconfig.absoluteEncoder
               .zeroOffset(0)
               .positionConversionFactor(2 * Math.PI) // Rotations to Radians
               .velocityConversionFactor(2 * Math.PI / 60);
        motor.configure(motorconfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
   }

   public double getTemp(){
       return motor.getMotorTemperature();
   }

   public double getOutputCurrent(){
       return motor.getOutputCurrent();
   }

   @Override
   public boolean isMotorConnected(){
       double voltage = motor.getBusVoltage();
       return voltage > 0.0;
    }

    public void setVoltage(double voltage) {
        motor.setVoltage(voltage);
    }

    public double getVoltage() {
        return motor.getBusVoltage();
    }

    @Override
    public void setPIDSlot(int slot) {
       switch (slot) {
        case 0:
            closedLoopSlot = ClosedLoopSlot.kSlot0;
            break;
        case 1:
            closedLoopSlot = ClosedLoopSlot.kSlot1;
            break;
        case 2:
            closedLoopSlot = ClosedLoopSlot.kSlot2;
            break;
        case 3:
            closedLoopSlot = ClosedLoopSlot.kSlot3;
            break;
        default:
            closedLoopSlot = ClosedLoopSlot.kSlot0;
            break;
       }
    }
}