//The Wildstang config file
com.wildstangs.logger.Logger.logIp=10.1.11.22
com.wildstangs.logger.Logger.port=17654
com.wildstangs.logger.Logger.logToServer=false
com.wildstangs.logger.Logger.logToStdout=true
com.wildstangs.logger.Logger.logLevel="DEBUG"
com.wildstangs.inputfacade.inputs.WsAnalogueInput.startState=0.
com.wildstangs.inputfacade.inputs.WsDigitalInput.startState=false

//2012 Robot Selection
com.wildstangs.outputfacade.base.WsOutputFacade.2012_Robot=false

//Digital Outputs: Replace 'NAME' with whatever name you send in
//com.wildstangs.outputfacade.outputs.WsDigitalOutput.NAME.startState=false

//Solenoids: Replace 'NAME' with whatever ame you send in
//com.wildstangs.outputfacade.outputs.WsSolenoid.NAME.shouldStartTrue=false

//Subsystems
com.wildstangs.subsystems.WsHopper.forwardCycles=15
com.wildstangs.subsystems.WsHopper.backwardCycles=15
com.wildstangs.subsystems.WsIntake.FingerDelayFromAccumulatorSwitch=15.0
com.wildstangs.subsystems.WsIntake.UseTimeDelay=false

//Distance PID
com.wildstangs.pid.controller.base.WsPidController.WsDriveBaseDistancePid.p=0.01
com.wildstangs.pid.controller.base.WsPidController.WsDriveBaseDistancePid.i=0.002
com.wildstangs.pid.controller.base.WsPidController.WsDriveBaseDistancePid.d=0.05
com.wildstangs.pid.controller.base.WsPidController.WsDriveBaseDistancePid.errorIncrement=1.0
com.wildstangs.pid.controller.base.WsPidController.WsDriveBaseDistancePid.errorEpsilon=1.0
com.wildstangs.pid.controller.base.WsPidController.WsDriveBaseDistancePid.staticEpsilon=0.0
com.wildstangs.pid.controller.base.WsPidController.WsDriveBaseDistancePid.maxIntegral=1.0
com.wildstangs.pid.controller.base.WsPidController.WsDriveBaseDistancePid.integralErrorThresh=-1.0
com.wildstangs.pid.controller.base.WsPidController.WsDriveBaseDistancePid.differentiatorBandLimit=1.0
com.wildstangs.pid.controller.base.WsPidController.WsDriveBaseDistancePid.maxOutput=1.0
com.wildstangs.pid.controller.base.WsPidController.WsDriveBaseDistancePid.minOutput=-1.0
com.wildstangs.pid.controller.base.WsPidController.WsDriveBaseDistancePid.maxInput=1000
com.wildstangs.pid.controller.base.WsPidController.WsDriveBaseDistancePid.minInput=-1000
com.wildstangs.pid.controller.base.WsPidController.WsDriveBaseDistancePid.minOnTargetTime=0.1

//Heading PID
com.wildstangs.pid.controller.base.WsPidController.WsDriveBaseHeadingPid.p=0.01
com.wildstangs.pid.controller.base.WsPidController.WsDriveBaseHeadingPid.i=0.002
com.wildstangs.pid.controller.base.WsPidController.WsDriveBaseHeadingPid.d=0.05
com.wildstangs.pid.controller.base.WsPidController.WsDriveBaseHeadingPid.errorIncrement=1.0
com.wildstangs.pid.controller.base.WsPidController.WsDriveBaseHeadingPid.errorEpsilon=1.0
com.wildstangs.pid.controller.base.WsPidController.WsDriveBaseHeadingPid.staticEpsilon=0.0
com.wildstangs.pid.controller.base.WsPidController.WsDriveBaseHeadingPid.maxIntegral=1.0
com.wildstangs.pid.controller.base.WsPidController.WsDriveBaseHeadingPid.integralErrorThresh=-1.0
com.wildstangs.pid.controller.base.WsPidController.WsDriveBaseHeadingPid.differentiatorBandLimit=1.0
com.wildstangs.pid.controller.base.WsPidController.WsDriveBaseHeadingPid.maxOutput=1.0
com.wildstangs.pid.controller.base.WsPidController.WsDriveBaseHeadingPid.minOutput=-1.0
com.wildstangs.pid.controller.base.WsPidController.WsDriveBaseHeadingPid.maxInput=1000
com.wildstangs.pid.controller.base.WsPidController.WsDriveBaseHeadingPid.minInput=-1000
com.wildstangs.pid.controller.base.WsPidController.WsDriveBaseHeadingPid.minOnTargetTime=0.1

//Speed PID
com.wildstangs.pid.controller.base.WsSpeedPidController.WsDriveBaseSpeedPid.p=0.00
com.wildstangs.pid.controller.base.WsSpeedPidController.WsDriveBaseSpeedPid.i=0.0
com.wildstangs.pid.controller.base.WsSpeedPidController.WsDriveBaseSpeedPid.d=0.00
com.wildstangs.pid.controller.base.WsSpeedPidController.WsDriveBaseSpeedPid.errorIncrement=1.0
com.wildstangs.pid.controller.base.WsSpeedPidController.WsDriveBaseSpeedPid.errorEpsilon=1.0
com.wildstangs.pid.controller.base.WsSpeedPidController.WsDriveBaseSpeedPid.staticEpsilon=0.0
com.wildstangs.pid.controller.base.WsSpeedPidController.WsDriveBaseSpeedPid.maxIntegral=1.0
com.wildstangs.pid.controller.base.WsSpeedPidController.WsDriveBaseSpeedPid.integralErrorThresh=-1.0
com.wildstangs.pid.controller.base.WsSpeedPidController.WsDriveBaseSpeedPid.differentiatorBandLimit=1.0
com.wildstangs.pid.controller.base.WsSpeedPidController.WsDriveBaseSpeedPid.maxOutput=0.5
com.wildstangs.pid.controller.base.WsSpeedPidController.WsDriveBaseSpeedPid.minOutput=-0.5
com.wildstangs.pid.controller.base.WsSpeedPidController.WsDriveBaseSpeedPid.maxInput=1000
com.wildstangs.pid.controller.base.WsSpeedPidController.WsDriveBaseSpeedPid.minInput=-1000
com.wildstangs.pid.controller.base.WsSpeedPidController.WsDriveBaseSpeedPid.minOnTargetTime=0.1


//Drivebase 
com.wildstangs.subsystems.WsDriveBase.wheel_diameter=6.210
com.wildstangs.subsystems.WsDriveBase.ticks_per_rotation=360.0
com.wildstangs.subsystems.WsDriveBase.throttle_low_gear_accel_factor=0.250
com.wildstangs.subsystems.WsDriveBase.heading_low_gear_accel_factor=0.500
com.wildstangs.subsystems.WsDriveBase.throttle_high_gear_accel_factor=0.750
com.wildstangs.subsystems.WsDriveBase.heading_high_gear_accel_factor=0.250
com.wildstangs.subsystems.WsDriveBase.max_high_gear_percent=0.800
com.wildstangs.inputfacade.inputs.driverstation.WsDSAnalogInput.startState=0.0
com.wildstangs.inputfacade.inputs.driverstation.WsDSDigitalInput.startState=0
com.wildstangs.outputfacade.outputs.WsSolenoid.FrisbieControl.shouldStartTrue=false
com.wildstangs.outputfacade.outputs.WsSolenoid.Kicker.shouldStartTrue=false
com.wildstangs.outputfacade.outputs.WsSolenoid.AccumulatorSolenoid.shouldStartTrue=false
com.wildstangs.subsystems.WsFloorPickup.maxAccumulatorSpeed=1.0
com.wildstangs.subsystems.WsDriveBase.encoder_gear_ratio=6.0
com.wildstangs.subsystems.WsDriveBase.slow_turn_forward_speed=0.30
com.wildstangs.subsystems.WsDriveBase.slow_turn_backward_speed=-0.26
com.wildstangs.subsystems.WsDriveBase.deadband=0.10
com.wildstangs.subsystems.WsDriveBase.feed_forward_velocity_constant=1.0
//com.wildstangs.subsystems.WsDriveBase.feed_forward_acceleration_constant=0.00216
com.wildstangs.subsystems.WsDriveBase.feed_forward_acceleration_constant=0.0
com.wildstangs.subsystems.WsDriveBase.max_acceleration_drive_profile=200.0
com.wildstangs.subsystems.WsDriveBase.max_speed_inches_lowgear=94.0
//Velocity is in ft/sec
com.wildstangs.subsystems.WsDriveBase.deceleration_velocity_threshold=48.0
com.wildstangs.subsystems.WsDriveBase.deceleration_motor_speed=0.3
com.wildstangs.subsystems.WsDriveBase.stopping_distance_at_max_speed_lowgear=12.0

//Shooter
com.wildstangs.subsystems.WsShooter.LowerWheelSpeed=100
com.wildstangs.subsystems.WsShooter.LowerVictorSpeed=0.5
com.wildstangs.subsystems.WsShooter.LowerWheelEnterTestSpeed=0.0
com.wildstangs.subsystems.WsShooter.LowerWheelExitTestSpeed=0.0
com.wildstangs.subsystems.WsShooter.UpperWheelEnterTestSpeed=9000
com.wildstangs.subsystems.WsShooter.UpperWheelExitTestSpeed=9000
com.wildstangs.subsystems.WsShooter.AtSpeedTolerance=0.05
com.wildstangs.subsystems.WsShooter.enter_gear_ratio=3.0
com.wildstangs.subsystems.WsShooter.exit_gear_ratio=3.2


//Shooter Presets
com.wildstangs.subsystems.WsShooter.PresetTowerShooterEnterSpeed=2200
com.wildstangs.subsystems.WsShooter.PresetTowerShooterExitSpeed=2150
com.wildstangs.subsystems.WsShooter.PresetTowerShooterAngle=true
com.wildstangs.subsystems.WsShooter.PresetLongLowEnterSpeed=3200
com.wildstangs.subsystems.WsShooter.PresetLongLowExitSpeed=3150
com.wildstangs.subsystems.WsShooter.PresetLongLowAngle=false
com.wildstangs.subsystems.WsShooter.PresetShortHighEnterSpeed=2700
com.wildstangs.subsystems.WsShooter.PresetShortHighExitSpeed=2650
com.wildstangs.subsystems.WsShooter.PresetShortHighAngle=true

//Loading Ramp
com.wildstangs.subsystems.WsLoadingRamp.AngleUp=76.5
com.wildstangs.subsystems.WsLoadingRamp.AngleDown=170.0

//   XX
//    X              X
//    X              X
//   X X   XX  XX   XXXX    XXXXX  XX XX
//   X X    X   X    X     X     X  XX  X
//  X   X   X   X    X     X     X  X   X
//  XXXXX   X   X    X     X     X  X   X
//  X   X   X  XX    X  X  X     X  X   X
// XXX XXX   XX XX    XX    XXXXX  XXX XXX

//ShootSeven
com.wildstangs.autonomous.programs.WsAutonomousProgramShootSeven.ForceStopAtStep=0

com.wildstangs.autonomous.programs.WsAutonomousProgramShootSeven.insidePyramidBackCenter.StartDrive=0.0
com.wildstangs.autonomous.programs.WsAutonomousProgramShootSeven.insidePyramidBackCenter.AngleTurn=0.0
com.wildstangs.autonomous.programs.WsAutonomousProgramShootSeven.insidePyramidBackCenter.SecondDrive=0.0
com.wildstangs.autonomous.programs.WsAutonomousProgramShootSeven.insidePyramidBackCenter.ThirdDrive=36.0
com.wildstangs.autonomous.programs.WsAutonomousProgramShootSeven.insidePyramidBackCenter.FourthDrive=100.0
com.wildstangs.autonomous.programs.WsAutonomousProgramShootSeven.insidePyramidBackCenter.FifthDrive=-46.0

com.wildstangs.autonomous.programs.WsAutonomousProgramShootSeven.insidePyramidBackCenter.ThirdFrisbeeDelay=100

com.wildstangs.autonomous.programs.WsAutonomousProgramShootSeven.insidePyramidBackCenter.FirstEnterWheelSetPoint=3200
com.wildstangs.autonomous.programs.WsAutonomousProgramShootSeven.insidePyramidBackCenter.FirstExitWheelSetPoint=3150
com.wildstangs.autonomous.programs.WsAutonomousProgramShootSeven.insidePyramidBackCenter.FirstShooterAngle=false

com.wildstangs.autonomous.programs.WsAutonomousProgramShootSeven.insidePyramidBackCenter.SecondEnterWheelSetPoint=2700
com.wildstangs.autonomous.programs.WsAutonomousProgramShootSeven.insidePyramidBackCenter.SecondExitWheelSetPoint=2650
com.wildstangs.autonomous.programs.WsAutonomousProgramShootSeven.insidePyramidBackCenter.SecondShooterAngle=true

com.wildstangs.autonomous.programs.WsAutonomousProgramShootSeven.insidePyramidBackCenter.FunnelatorLoadDelay=3000
com.wildstangs.autonomous.programs.WsAutonomousProgramShootSeven.insidePyramidBackCenter.RaiseAccumulatorDelay=3000
com.wildstangs.autonomous.programs.WsAutonomousProgramShootSeven.insidePyramidBackCenter.LowerAccumulatorDelay=0

//ShootFive
com.wildstangs.autonomous.programs.WsAutonomousProgramShootFive.ForceStopAtStep=0

com.wildstangs.autonomous.programs.WsAutonomousProgramShootFive.insidePyramidBackCenter.FirstDrive=36.0
com.wildstangs.autonomous.programs.WsAutonomousProgramShootFive.insidePyramidBackCenter.SecondDrive=-36.0

com.wildstangs.autonomous.programs.WsAutonomousProgramShootFive.insidePyramidBackCenter.ThirdFrisbeeDelay=100

com.wildstangs.autonomous.programs.WsAutonomousProgramShootFive.insidePyramidBackCenter.FirstEnterWheelSetPoint=3200
com.wildstangs.autonomous.programs.WsAutonomousProgramShootFive.insidePyramidBackCenter.FirstExitWheelSetPoint=3150
com.wildstangs.autonomous.programs.WsAutonomousProgramShootFive.insidePyramidBackCenter.FirstShooterAngle=false

com.wildstangs.autonomous.programs.WsAutonomousProgramShootFive.insidePyramidBackCenter.SecondEnterWheelSetPoint=3200
com.wildstangs.autonomous.programs.WsAutonomousProgramShootFive.insidePyramidBackCenter.SecondExitWheelSetPoint=3150
com.wildstangs.autonomous.programs.WsAutonomousProgramShootFive.insidePyramidBackCenter.SecondShooterAngle=false

com.wildstangs.autonomous.programs.WsAutonomousProgramShootFive.insidePyramidBackCenter.FunnelatorLoadDelay=7000
com.wildstangs.autonomous.programs.WsAutonomousProgramShootFive.insidePyramidBackCenter.RaiseAccumulatorDelay=3000
com.wildstangs.autonomous.programs.WsAutonomousProgramShootFive.insidePyramidBackCenter.LowerAccumulatorDelay=1500

//Drive distance
//Distance is in inches
com.wildstangs.autonomous.programs.WsAutonomousProgramDriveDistance.distance=100

//Drive Distance Motion Profile
com.wildstangs.autonomous.programs.WsAutonomousProgramDriveDistanceMotionProfile.ForceStopAtStep=0
com.wildstangs.autonomous.programs.WsAutonomousProgramDriveDistanceMotionProfile.heading=0.8
com.wildstangs.autonomous.programs.WsAutonomousProgramDriveDistanceMotionProfile.distance=-46

//Drive heading
//Heading is in degrees
com.wildstangs.autonomous.programs.WsAutonomousProgramDriveHeading.angle=-30
