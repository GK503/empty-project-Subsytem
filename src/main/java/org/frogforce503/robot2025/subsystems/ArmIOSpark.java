package org.frogforce503.robot2025.subsystems;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkAbsoluteEncoder;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

public class ArmIOSpark implements ArmIO{
    SparkAbsoluteEncoder encoder;
    SparkMax motor;
    SparkMaxConfig motorconfig;

    public ArmIOSpark(int CANID, MotorType MotorType){
        motor = new SparkMax(CANID, MotorType);
        encoder = motor.getAbsoluteEncoder();
        motorconfig = new SparkMaxConfig();
        motorconfig.closedLoop.pid(0.0, 0.0, 0.0);
        motorconfig.idleMode(IdleMode.kBrake);
        motorconfig.absoluteEncoder.zeroOffset(0.0);
        motor.configure(motorconfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    @Override
    public void updateInputs(ArmIOInputs inputs){
    inputs.data = new ArmIOData(
        encoder.getPosition(),
        encoder.getVelocity(),
        false,
        motor.getMotorTemperature(),
        motor.getBusVoltage(),
        motor.getOutputCurrent()
      );
    }

    @Override
    public double getPosition(){
        return encoder.getPosition();
    }

    @Override
    public double getVelocity(){
        return encoder.getVelocity();
    }

    @Override
    public void setPosition(double setPoint){
        motor.getClosedLoopController().setReference(setPoint, ControlType.kVelocity);
    }

    @Override
    public void setPID(double p, double i, double d) {
        motorconfig.closedLoop.p(p);
        motorconfig.closedLoop.i(i);
        motorconfig.closedLoop.d(d);
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
    public void reset(){
        motor.getClosedLoopController().setReference(0.0, ControlType.kVelocity);
    }
}