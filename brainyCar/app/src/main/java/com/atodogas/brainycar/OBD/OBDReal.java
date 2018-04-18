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
    private ThrottlePositionCommand throttlePositionCommand;
    private AmbientAirTemperatureCommand ambientAirTemperatureCommand;
    private EngineCoolantTemperatureCommand engineCoolantTemperatureCommand;
    private AirIntakeTemperatureCommand airIntakeTemperatureCommand;
    private FindFuelTypeCommand findFuelTypeCommand;
    private TroubleCodesCommand troubleCodesCommand;
    private ConsumptionRateCommand consumptionRateCommand;
    private MassAirFlowCommand massAirFlowCommand;
    private IntakeManifoldPressureCommand intakeManifoldPressureCommand;


    public OBDReal(InputStream input, OutputStream output) throws IOException, InterruptedException {
        this.input = input;
        this.output = output;

        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        HashMap<String, String> devices = new HashMap<>();

        for (BluetoothDevice device : btAdapter.getBondedDevices()){
            devices.put(device.getName(), device.getAddress());
        }

        String address = devices.get("OBDII");

        BluetoothDevice device = btAdapter.getRemoteDevice(address);
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

        BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuid);
        socket.connect();

        this.input = socket.getInputStream();
        this.output = socket.getOutputStream();

        this.echoOffCommand = new EchoOffCommand();
        this.timeoutCommand = new TimeoutCommand(30);
        this.lineFeedOffCommand = new LineFeedOffCommand();
        this.selectProtocolCommand = new SelectProtocolCommand(ObdProtocols.AUTO);
        this.speedCommand = new SpeedCommand();
        this.rpmCommand = new RPMCommand();
        this.fuelLevelCommand = new FuelLevelCommand();
        this.moduleVoltageCommand = new ModuleVoltageCommand();
        this.throttlePositionCommand = new ThrottlePositionCommand();
        this.ambientAirTemperatureCommand = new AmbientAirTemperatureCommand();
        this.engineCoolantTemperatureCommand = new EngineCoolantTemperatureCommand();
        this.airIntakeTemperatureCommand = new AirIntakeTemperatureCommand();
        this.findFuelTypeCommand = new FindFuelTypeCommand();
        this.troubleCodesCommand = new TroubleCodesCommand();
        this.consumptionRateCommand = new ConsumptionRateCommand();
        this.massAirFlowCommand = new MassAirFlowCommand();
        this.intakeManifoldPressureCommand = new IntakeManifoldPressureCommand();

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
            throttlePositionCommand.run(input, output);
            dto.throttlePos = throttlePositionCommand.getPercentage();
        }
        catch (ResponseException e){
            e.printStackTrace();
        }

        try {
            ambientAirTemperatureCommand.run(input, output);
            dto.ambientTemp = ambientAirTemperatureCommand.getTemperature();
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
            airIntakeTemperatureCommand.run(input, output);
            dto.airIntakeTemp = airIntakeTemperatureCommand.getTemperature();
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

        try {
            intakeManifoldPressureCommand.run(input, output);
            dto.intakeManifoldPresure = intakeManifoldPressureCommand.getMetricUnit();
        }
        catch (ResponseException e){
            e.printStackTrace();
        }

        return dto;
    }

    @Override
    public String getFuelType() throws IOException, InterruptedException {
        String fuelType = "-";
        try {
            findFuelTypeCommand.run(input, output);
            fuelType = findFuelTypeCommand.getFormattedResult();
        }
        catch (ResponseException e){
            e.printStackTrace();
        }

        return fuelType;
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
