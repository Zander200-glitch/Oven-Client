package engine;

import static engine.Window.updateWindowTitle;
import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class Timer {
    private static double lastLoopTime = System.nanoTime();

    public static double timerGetTime(){
        return System.nanoTime();
    }

    public static double timerGetElapsedTime(){
        double time = timerGetTime();
        double elapsedTime = time - lastLoopTime;
        lastLoopTime = time;
        return elapsedTime;
    }

    public static double timerGetLastLoopTime(){
        return lastLoopTime;
    }


    private static double lastFPSTime = System.nanoTime();
    private static double elapsedTime = 0;
    private static int framesPerSecond = 0;

    public static void countFPS() {
        double time = timerGetTime();
        double currentElapsedTime = time - lastFPSTime;
        lastFPSTime = time;
        elapsedTime += currentElapsedTime;
        framesPerSecond++;
        if (elapsedTime >= 1_000_000_000) {
//            System.out.println("framerate :" +  framesPerSecond);
            updateWindowTitle("Crafter Pre-Alpha 0.01 | FPS: " + framesPerSecond);
            framesPerSecond = 0;
            elapsedTime = 0;
        }
    }
}
