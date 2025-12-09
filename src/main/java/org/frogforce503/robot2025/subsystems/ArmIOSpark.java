package org.frogforce503.robot2025.subsystems;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

public class ArmIOSpark implements ArmIO{
    RelativeEncoder encoder;
    SparkMax motor;
    SparkMaxConfig config;

    public ArmIOSpark(int CANID, MotorType MotorType){
        motor = new SparkMax(CANID, MotorType);
        SparkMaxConfig motorconfig = new SparkMaxConfig();
        motor.configure(motorconfig, null, null);
        encoder = motor.getEncoder();
    }

    public void updateInputs(ArmIOData inputs){
      inputs = new ArmIOData(
        encoder.getPosition(),
        encoder.getVelocity(),
        motor.isMotorConnected(),
        motor.getMotorTemperature(),
        motor.getBusVoltage(),
        motor.getOutputCurrent()
      );
    }

    public double getPosition(){
        return encoder.getPosition();
    }

    public double getVelocity(){
        return encoder.getVelocity();
    }

    public void setPosition(double setPoint){
        encoder.setPosition(setPoint);
    }

    public void setPID(double p, double i, double d) {
        config.closedLoop.p(p);
        config.closedLoop.i(i);
        config.closedLoop.d(d);
        motor.configure(config, null, null);
    }

    public void setOperatingMode(){
        config.idleMode(IdleMode.kBrake);
    }

    public void stop(){
       motor.stopMotor();
    }

    public void reset(){
        encoder.setPosition(0);
    }
}