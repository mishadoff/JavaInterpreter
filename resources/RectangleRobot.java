package mishadoff;

import java.awt.Color;

import robocode.Robot;
import robocode.ScannedRobotEvent;

/**
 * Good against <code>Corners</code> robot
 * @author mishadoff
 */
public class RectangleRobot extends Robot{
	
	public void run() {
		wearUniform();
		avoiding();
	}
	
	public void onScannedRobot(ScannedRobotEvent event) {
		if ((event.getDistance() > 200) && (event.getDistance() < 400)) {
			fireMachineGun();
		}
		else if (event.getDistance() > 400){
			moveUp(event);
		}
		else {
			moveDown(event);
		}
	}
	
	/** Wear RED uniform to enrage enemy */
	public void wearUniform(){
		setBodyColor(Color.RED);
		setGunColor(Color.RED);
		setRadarColor(Color.RED);
		setBulletColor(Color.RED);
		setScanColor(Color.RED);
	}
	
	/** Move close to enemy */
	public void moveUp(ScannedRobotEvent event){
		turnLeft(event.getBearing());
		ahead(event.getDistance()/2);
	}
	
	/** Move far from enemy */
	public void moveDown(ScannedRobotEvent event){
		turnLeft(event.getBearing());
		back(event.getDistance()/2);
	}
	
	/** Crazy MachineGun */
	public void fireMachineGun(){
		fire(3);
	}
	
	/** Move rectangle */
	public void avoiding(){
		while (true){
			ahead(100);	
			turnLeft(90);
		}
	}
	
}
