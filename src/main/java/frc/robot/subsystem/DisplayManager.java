/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystem;


import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Add your docs here.
 */
public class DisplayManager {

    private static Logger logger = Logger.getLogger(DisplayManager.class.getName());

    private ArrayList<SBInterface> subsystemUpdateList;

    public DisplayManager(){
        subsystemUpdateList = new ArrayList<SBInterface>();
    }
    public void update() {
        for (int j = 0; j < subsystemUpdateList.size(); j ++) {
            subsystemUpdateList.get(j).update();
          }
    }
}
