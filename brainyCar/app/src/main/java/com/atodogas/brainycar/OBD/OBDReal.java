package com.atodogas.brainycar.OBD;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import com.github.pires.obd.commands.SpeedCommand;
import com.github.pires.obd.commands.control.ModuleVoltageCommand;
import com.github.pires.obd.commands.control.TroubleCodesCommand;
import com.github.pires.obd.commands.engine.MassAirFlowCommand;
import com.github.pires.obd.commands.engine.RPMCommand;
import com.github.pires.obd.commands.engine.ThrottlePositionCommand;
import com.github.pires.obd.commands.fuel.ConsumptionRateCommand;
import com.github.pires.obd.commands.fuel.FindFuelTypeCommand;
import com.github.pires.obd.commands.fuel.FuelLevelCommand;
import com.github.pires.obd.commands.pressure.IntakeManifoldPressureCommand;
import com.github.pires.obd.commands.protocol.AvailablePidsCommand;
import com.github.pires.obd.commands.protocol.AvailablePidsCommand_01_20;
import com.github.pires.obd.commands.protocol.AvailablePidsCommand_21_40;
import com.github.pires.obd.commands.protocol.AvailablePidsCommand_41_60;
import com.github.pires.obd.commands.protocol.EchoOffCommand;
import com.github.pires.obd.commands.protocol.LineFeedOffCommand;
import com.github.pires.obd.commands.protocol.ReadInputVoltageCommand;
import com.github.pires.obd.commands.protocol.SelectProtocolCommand;
import com.github.pires.obd.commands.protocol.TimeoutCommand;
import com.github.pires.obd.commands.temperature.AirIntakeTemperatureCommand;
import com.github.pires.obd.commands.temperature.AmbientAirTemperatureCommand;
import com.github.pires.obd.commands.temperature.EngineCoolantTemperatureCommand;
import com.github.pires.obd.enums.ObdProtocols;
import com.github.pires.obd.exceptions.ResponseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;


public class OBDReal extends OBDAdapter {
    private InputStream input;
    private OutputStream output;
    private EchoOffCommand echoOffCommand;
    private LineFeedOffCommand lineFeedOffCommand;
    private TimeoutCommand timeoutCommand;
    private SelectProtocolCommand selectProtocolCommand;
    private SpeedCommand speedCommand;
    private RPMCommand rpmCommand;
    private FuelLevelCommand fuelLevelCommand;
    private ModuleVoltageCommand moduleVoltageCommand;
    private EngineCoolantTemperatureCommand engineCoolantTemperatureCommand;
    private TroubleCodesCommand troubleCodesCommand;
    private MassAirFlowCommand massAirFlowCommand;
    private ReadInputVoltageCommand readInputVoltageCommand;


    public OBDReal(InputStream input, OutputStream output) throws IOException, InterruptedException {
        this.input = input;
        this.output = output;

        this.echoOffCommand = new EchoOffCommand();
        this.timeoutCommand = new TimeoutCommand(30);
        this.lineFeedOffCommand = new LineFeedOffCommand();
        this.selectProtocolCommand = new SelectProtocolCommand(ObdProtocols.AUTO);
        this.speedCommand = new SpeedCommand();
        this.rpmCommand = new RPMCommand();
        this.fuelLevelCommand = new FuelLevelCommand();
        this.moduleVoltageCommand = new ModuleVoltageCommand();
        this.engineCoolantTemperatureCommand = new EngineCoolantTemperatureCommand();
        this.troubleCodesCommand = new TroubleCodesCommand();
        this.massAirFlowCommand = new MassAirFlowCommand();
        this.readInputVoltageCommand = new ReadInputVoltageCommand();

        this.echoOffCommand.run(this.input, this.output);
        this.lineFeedOffCommand.run(this.input, this.output);
        this.timeoutCommand.run(this.input, this.output);
        this.selectProtocolCommand.run(this.input, this.output);
    }

    @Override
    public OBDDTO getRealTimeData () throws IOException, InterruptedException {
        OBDDTO dto = new OBDDTO();

        try {
            speedCommand.run(input, output);
            dto.speed = speedCommand.getMetricSpeed();
        }
        catch (ResponseException e){
            e.printStackTrace();
        }

        try {
            rpmCommand.run(input, output);
            dto.rpm = rpmCommand.getRPM();
        }
        catch (ResponseException e){
            e.printStackTrace();
        }

        try {
            fuelLevelCommand.run(input, output);
            dto.fuelTankLevel = fuelLevelCommand.getFuelLevel();
        }
        catch (ResponseException e){
            e.printStackTrace();
        }

        try {
            moduleVoltageCommand.run(input, output);
            dto.moduleVoltage = (float) moduleVoltageCommand.getVoltage();
        }
        catch (ResponseException e){
            e.printStackTrace();
        }

        try {
            engineCoolantTemperatureCommand.run(input, output);
            dto.engineCoolantTemp = engineCoolantTemperatureCommand.getTemperature();
        }
        catch (ResponseException e){
            e.printStackTrace();
        }

        try {
            massAirFlowCommand.run(input, output);
            dto.massAirFlow = (float) massAirFlowCommand.getMAF();
        }
        catch (ResponseException e){
            e.printStackTrace();
        }

        return dto;
    }

    @Override
    public boolean isConnected(){
        try {
            readInputVoltageCommand.run(input, output);
            float voltage = readInputVoltageCommand.getVoltage();

            if(voltage > 0){
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public ArrayList<String> getTroubleCodes() throws IOException, InterruptedException {
        try{
            troubleCodesCommand.run(input, output);
            String troubleCodesString = troubleCodesCommand.getFormattedResult();

            return new ArrayList<>(Arrays.asList(troubleCodesString.split("\n")));
        }
        catch (ResponseException ex) {
            return new ArrayList<String>();
        }
    }

    public ArrayList<String> getPIDsSupported() {
        ArrayList<String> supported = new ArrayList<>();
        try {
            AvailablePidsCommand av = new AvailablePidsCommand_01_20();
            av.run(input, output);
            supported.add(av.getCalculatedResult());

            av = new AvailablePidsCommand_21_40();
            av.run(input, output);
            supported.add(av.getCalculatedResult());

            av = new AvailablePidsCommand_41_60();
            av.run(input, output);
            supported.add(av.getCalculatedResult());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return supported;
    }
}
