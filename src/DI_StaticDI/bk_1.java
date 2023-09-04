import Automation.BDaq.*;

...

DeviceInformation devInfo = new DeviceInformation(deviceDescription); 
InstantDiCtrl instantDiCtrl = new InstantDiCtrl();

//Set the selected device.
instantDiCtrl.setSelectedDevice(devInfo);

// Read profile to configure device
ret = instantDiCtrl.LoadProfile(fileDefaultPath);
        
PortDirection[] portDirs = instantDiCtrl.getPortDirection();
        
if (portDirs != null) {
            
    //Set the first two port to output
    DioPortDir dir = DioPortDir.Input;
    portDirs[0].setDirection(dir);
    portDirs[1].setDirection(dir);
            
    //get port direction and print the direction information
    DioPortDir currentDir = portDirs[0].getDirection();
    System.out.println("Current Direction of Port[" + 0 + "] = " + currentDir.toString());
    currentDir = portDirs[1].getDirection();
  System.out.println("Current Direction of Port[" + 1 + "] = " + currentDir.toString());    
} else {
        System.out.println("There is no DIO port of the selected device can set direction!\n");
}

...

// Read DI port
ret = instantDiCtrl.Read(portStart, portCount, portData);

... 
