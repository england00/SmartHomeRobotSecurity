<div id="top">

<!-- HEADER STYLE: COMPACT -->
<img src="docs/SmartHomeRobotSecurity.png" width="30%" align="left" style="margin-right: 15px">

# SMART HOME ROBOT SECURITY
<em></em>

<!-- BADGES -->
<!-- local repository, no metadata badges. -->

<br clear="left"/>
</div>

## ☀️ Table of Contents

- [☀ ️ Table of Contents](#-table-of-contents)
- [🌞 Overview](#-overview)
- [🔥 Features](#-features)
- [🌅 Project Structure](#-project-structure)
- [🚀 Getting Started](#-getting-started)
    - [🌟 Prerequisites](#-prerequisites)
    - [⚡ Installation](#-installation)
    - [🔆 Usage](#-usage)
    - [🌠 Testing](#-testing)

---

## 🌞 Overview

A Demo IoT Application Use Case emulating a Smart Home system with multiple:

- IoT Robot Smart Objects, with sensors and actuators;
- Presence Monitoring Smart Objects, only with sensors;
- Charging Stations, only with sensors.

---

## 🔥 Features

|     | Component         | Details                                                                                                                     |
|:----| :----------------| :--------------------------------------------------------------------------------------------------------------------------|
| ⚙️  | **Architecture**  | <ul><li>Monolithic Java app</li><li>MVC patterns likely</li><li>Text-file based config/data</li><li>Maven project</li></ul> |
| 🔩  | **Code Quality**  | <ul><li>Maven structure</li><li>Logging via Logback</li><li>Jackson/Gson for JSON</li><li>Strong dependency mgmt</li></ul>  |
|  🔌 | **Integrations**  | <ul><li>Californium CoAP (IoT comms)</li><li>Logback (logging)</li><li>Gson/Jackson (serialization)</li></ul>               |
| 🧩  | **Modularity**    | <ul><li>Maven modules</li><li>POJO model separation</li><li>External data: <code>*.txt</code> files</li></ul>               |
| ⚡️  | **Performance**   | <ul><li>Lightweight deps</li><li>Text-file IO (potential bottleneck)</li><li>Embedded communication</li></ul>               |
| 📦  | **Dependencies**  | <ul><li>californium-core</li><li>logback-classic</li><li>gson</li><li>jackson-databind</li></ul>                            |
| 🚀  | **Scalability**   | <ul><li>Single-host design</li><li>Not containerized</li><li>Scaling limited by file-based storage</li></ul>                |
```

---

## 🌅 Project Structure

```sh
└── SmartHomeRobotSecurity/
    ├── Californium.properties
    ├── docs
    │   ├── SmartHomeRobotSecurity - Presentation.pdf
    │   └── SmartHomeRobotSecurity - Project Specifications.pdf
    ├── pom.xml
    ├── README.md
    ├── src
    │   └── main
    │       ├── java
    │       │   └── it
    │       │       └── unimore
    │       │           └── fum
    │       │               └── iot
    │       │                   ├── client
    │       │                   │   ├── alarm.txt
    │       │                   │   └── DataManager.java
    │       │                   ├── exception
    │       │                   │   ├── ManagerConflict.java
    │       │                   │   └── ManagerException.java
    │       │                   ├── model
    │       │                   │   ├── descriptor
    │       │                   │   │   ├── AddressDescriptor.java
    │       │                   │   │   ├── AlarmDataDescriptor.java
    │       │                   │   │   ├── AlarmStatusDescriptor.java
    │       │                   │   │   ├── ChargingStationDescriptor.java
    │       │                   │   │   ├── PresenceMonitoringObjectDescriptor.java
    │       │                   │   │   ├── RobotDescriptor.java
    │       │                   │   │   └── RoomDescriptor.java
    │       │                   │   ├── general
    │       │                   │   │   ├── GeneralDataListener.java
    │       │                   │   │   └── GeneralDescriptor.java
    │       │                   │   └── raw
    │       │                   │       ├── BatteryLevelRawSensor.java
    │       │                   │       ├── EnergyConsumptionRawSensor.java
    │       │                   │       ├── IndoorPositionRawSensor.java
    │       │                   │       ├── ModeRawActuator.java
    │       │                   │       ├── PresenceRawSensor.java
    │       │                   │       ├── ReturnHomeRawActuator.java
    │       │                   │       └── SwitchRawActuator.java
    │       │                   ├── persistence
    │       │                   │   ├── charging_stations.txt
    │       │                   │   ├── IManager.java
    │       │                   │   ├── IRoomsManager.java
    │       │                   │   ├── objects
    │       │                   │   │   ├── ChargingStationsManager.java
    │       │                   │   │   ├── PresenceMonitoringObjectsManager.java
    │       │                   │   │   ├── RobotsManager.java
    │       │                   │   │   └── RoomsManager.java
    │       │                   │   ├── presence_monitoring_objects.txt
    │       │                   │   ├── robots.txt
    │       │                   │   └── rooms.txt
    │       │                   ├── request
    │       │                   │   ├── MakeCameraSwitchRequest.java
    │       │                   │   ├── MakeModeRequest.java
    │       │                   │   └── MakeReturnHomeRequest.java
    │       │                   ├── resource
    │       │                   │   ├── charger
    │       │                   │   │   ├── ChargingStationResource.java
    │       │                   │   │   ├── EnergyConsumptionSensorResource.java
    │       │                   │   │   ├── RobotBatteryLevelSensorResource.java
    │       │                   │   │   └── RobotPresenceSensorResource.java
    │       │                   │   ├── presence
    │       │                   │   │   ├── PassiveInfraRedSensorResource.java
    │       │                   │   │   └── PresenceMonitoringObjectResource.java
    │       │                   │   └── robot
    │       │                   │       ├── BatteryLevelSensorResource.java
    │       │                   │       ├── CameraSwitchActuatorResource.java
    │       │                   │       ├── IndoorPositionSensorResource.java
    │       │                   │       ├── ModeActuatorResource.java
    │       │                   │       ├── PresenceInCameraStreamSensorResource.java
    │       │                   │       ├── ReturnHomeActuatorResource.java
    │       │                   │       └── RobotResource.java
    │       │                   ├── server
    │       │                   │   ├── ChargingStationCoapProcess.java
    │       │                   │   ├── PresenceMonitoringObjectCoapProcess.java
    │       │                   │   └── RobotCoapProcess.java
    │       │                   ├── test
    │       │                   │   ├── charger
    │       │                   │   │   ├── EnergyConsumptionSensorDescriptorTester.java
    │       │                   │   │   └── RobotBatteryLevelDescriptorTester.java
    │       │                   │   ├── client
    │       │                   │   │   ├── DataManagerGetClientProcess.java
    │       │                   │   │   ├── DataManagerObservingClientProcess.java
    │       │                   │   │   ├── DataManagerPostClientProcess.java
    │       │                   │   │   ├── DataManagerPutClientProcess.java
    │       │                   │   │   └── DataManagerResourceDiscoveryClientProcess.java
    │       │                   │   ├── hashmap
    │       │                   │   │   ├── FileToHashMap.java
    │       │                   │   │   └── HashMapToFile.java
    │       │                   │   ├── model
    │       │                   │   │   ├── IBatteryLevelSensorDescriptor.java
    │       │                   │   │   ├── ICameraSwitchActuatorDescriptor.java
    │       │                   │   │   ├── IEnergyConsumptionSensorDescriptor.java
    │       │                   │   │   ├── IIndoorPositionSensorDescriptor.java
    │       │                   │   │   ├── IModeActuatorDescriptor.java
    │       │                   │   │   ├── IPassiveInfraRedSensorDescriptor.java
    │       │                   │   │   ├── IPresenceInCameraStreamSensorDescriptor.java
    │       │                   │   │   ├── IReturnHomeActuatorDescriptor.java
    │       │                   │   │   ├── IRobotBatteryLevelSensorDescriptor.java
    │       │                   │   │   ├── IRobotPresenceSensorDescriptor.java
    │       │                   │   │   └── raw
    │       │                   │   ├── persistence
    │       │                   │   │   ├── DefaultSmartObjectsInventoryManager.java
    │       │                   │   │   ├── ISmartObjectsInventoryManager.java
    │       │                   │   │   ├── robotId.txt
    │       │                   │   │   └── RobotManager.java
    │       │                   │   ├── presence
    │       │                   │   │   └── PassiveInfraRedSensorDescriptorTester.java
    │       │                   │   └── robot
    │       │                   │       ├── BatteryLevelSensorDescriptorTester.java
    │       │                   │       ├── CameraSwitchActuatorDescriptorTester.java
    │       │                   │       ├── IndoorPositionSensorDescriptorTester.java
    │       │                   │       └── PresenceInCameraStreamSensorDescriptorTester.java
    │       │                   └── utils
    │       │                       ├── CoreInterfaces.java
    │       │                       ├── SenMLPack.java
    │       │                       └── SenMLRecord.java
    │       └── resources
    │           ├── log4j.properties
    │           └── logback.xml
    └── target
        └── classes
            ├── log4j.properties
            └── logback.xml
```

---

## 🚀 Getting Started

### 🌟 Prerequisites

This project requires the following dependencies:

- **Programming Language:** Java
- **Package Manager:** Maven

### ⚡ Installation

Build SmartHomeRobotSecurity from the source and intsall dependencies:

1. **Clone the repository:**

    ```sh
    ❯ git clone ../SmartHomeRobotSecurity
    ```

2. **Navigate to the project directory:**

    ```sh
    ❯ cd SmartHomeRobotSecurity
    ```

3. **Install the dependencies:**


	<!-- [![maven][maven-shield]][maven-link] -->
	<!-- REFERENCE LINKS -->
	<!-- [maven-shield]: https://img.shields.io/badge/Maven-C71A36.svg?style={badge_style}&logo=apache-maven&logoColor=white -->
	<!-- [maven-link]: https://maven.apache.org/ -->

	**Using [maven](https://maven.apache.org/):**

	❯ mvn install
	

### 🔆 Usage

Run the project with:

**Using [maven](https://maven.apache.org/):**
```sh
mvn exec:java
```

### 🌠 Testing

Smarthomerobotsecurity uses the {__test_framework__} test framework. Run the test suite with:

**Using [maven](https://maven.apache.org/):**
```sh
mvn test
```