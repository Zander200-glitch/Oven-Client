package oven;

import engine.Window;
import static org.lwjgl.glfw.GLFW.*;

public class OvenApi {
    public static void updateClientTitle(String newTitle){
        OvenOpenData.windowTitle = newTitle;
    }
    public static void togglevSync(boolean on){
        Window.setVSync(on);
    }
    public static void toggleWindowVisibility(boolean on){
        long windowHandle = Window.getWindowHandle();
        if(on){
            glfwShowWindow(windowHandle);
        }
        else{
            glfwHideWindow(windowHandle);
        }
    }
}
