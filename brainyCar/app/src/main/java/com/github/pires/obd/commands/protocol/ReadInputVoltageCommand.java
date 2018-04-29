package com.github.pires.obd.commands.protocol;

public class ReadInputVoltageCommand extends ObdProtocolCommand {

    public ReadInputVoltageCommand() {
        super("AT RV");
    }

    @Override
    public String getFormattedResult() {
        return getResult();
    }

    @Override
    public String getName() {
        return "Input Voltage";
    }

    public float getVoltage(){
        String voltage = getResult();

        return  Float.parseFloat(voltage.substring(0, voltage.length() - 2));
    }
}
