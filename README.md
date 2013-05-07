# Team 111's 2013 Robot Code

This repository contains the Team 111 2013 FRC codebase. This code is released under the BSD 2-clause license. A copy of this license is included in COPYING.

Similar to Team 254, we also decided to switch to Java from C++ this year. Java is easier to teach, already had some instruction in school, and the students can grasp the concepts faster. Also the students and mentors could work on both Windows or Mac computers. 

After the initial heartburn from Sqwauk's and Java ME's limitations, we feel the change was a success and will continue using Java in future years. 

## Intro

The repository contains two Netbeans projects; crioTarget and WsSimulation. The crioTarget is what is run on the robot. The WsSimulation links against the crioTarget and replaces many of WPI classes with its own stub or simulation implementation. 

## crioTarget

The robot framework was designed using the Subject/Observer object oriented design pattern and inputs and outputs can notify any registered object when they change. To keep things simple and synchronous, all updates occured during the 20 ms periodic loop.     

### com.wildstangs.autonomous
Contains the autonomous framework, all autonomous programs and steps. 
  
### com.wildstangs.config/configfacade
Contains the base objects and singleton for managing configuration parameters from a file

### com.wildstangs.logger
Contains the singleton for handling logging. This needs to be used sparingly as printOut's are very slow

### com.wildstangs.inputfacade
Contains the subjects and implementations for inputs

### com.wildstangs.outputfacade
Contains the subjects and implementations for outputs

### com.wildstangs.subjects.base
Contains the base classes for subjects that are used throughout the framework

### com.wildstangs.subsystems
Contains all of the subsystems that control driving, shooting, and hanging

## WsSimulation
This allows the software to test all of its logic before loading it on the hardware. It simulated a variety of hardware including the drive encoders, flywheel encoders, and limit switches to test proper control schemes. It allowed the use of two USB or onscreen Joysticks for inputs.    