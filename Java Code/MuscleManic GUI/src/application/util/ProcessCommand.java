package application.util;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class ProcessCommand {

	private static Robot mRobot;
    // Constant that indicate command from devices
    private static final int EXIT_CMD = 10;
    private static final int MOUSE_MOVE_UP = 0;
    private static final int MOUSE_MOVE_DOWN = 1;
    private static final int MOUSE_MOVE_RIGHT = 2;
    private static final int MOUSE_MOVE_LEFT = 3;
    private static final int MOUSE_LEFT_CLICK = 4;
    private static final int MOUSE_RIGHT_CLICK = 5;
    private static final int KEY_RIGHT = 6;
    private static final int KEY_LEFT = 7;
    private static final int KEY_SPACE = 8;
    private static final int KEY_ESC = 9;

	public ProcessCommand(){
		try {
			mRobot = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void doCommand(int command){
		Point point = MouseInfo.getPointerInfo().getLocation(); //Get current mouse position
		float nowx = point.x;
		float nowy = point.y;
        switch (command) {
        case EXIT_CMD:
	        System.out.println("Exit");
	        System.exit(0);
	        break;
    	case MOUSE_MOVE_UP:
			mRobot.mouseMove((int)(nowx),(int)(nowy-50));
			System.out.println("MOUSE_MOVE_UP");
    		break;
    	case MOUSE_MOVE_DOWN:
			mRobot.mouseMove((int)(nowx),(int)(nowy+50));
			System.out.println("MOUSE_MOVE_DOWN");
    		break;
    	case MOUSE_MOVE_RIGHT:
			mRobot.mouseMove((int)(nowx+50),(int)(nowy));
			System.out.println("MOUSE_MOVE_RIGHT");
    		break;
    	case MOUSE_MOVE_LEFT:
			mRobot.mouseMove((int)(nowx-50),(int)(nowy));
			System.out.println("MOUSE_MOVE_LEFT");
    		break;
        case MOUSE_LEFT_CLICK:
			mouseClick(InputEvent.BUTTON1_DOWN_MASK);
			System.out.println("MOUSE_LEFT_CLICK");
            break;
        case MOUSE_RIGHT_CLICK:
			mouseClick(InputEvent.BUTTON2_DOWN_MASK);
			System.out.println("MOUSE_RIGHT_CLICK");
        case KEY_RIGHT:
            keyClick(KeyEvent.VK_RIGHT);
            System.out.println("KEY_RIGHT");
            break;
        case KEY_LEFT:
            keyClick(KeyEvent.VK_LEFT);
            System.out.println("KEY_LEFT");
            break;
        case KEY_SPACE:
            keyClick(KeyEvent.VK_SPACE);
            System.out.println("KEY_SPACE");
            break;
        case KEY_ESC:
        	keyClick(KeyEvent.VK_ESCAPE);
            System.out.println("KEY_ESC");
            break;
        }
	}

    private void keyClick(int keyEvent){
        mRobot.keyPress(keyEvent);
        mRobot.delay(100); // hold for a tenth of a second, adjustable
        mRobot.keyRelease(keyEvent);
    }

    private void mouseClick(int inputEvent){
		mRobot.mousePress(inputEvent);
        mRobot.delay(100); // hold for a tenth of a second, adjustable
		mRobot.mouseRelease(inputEvent);
    }
}
