package engine;

import engine.graph.Importer;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import javax.swing.*;
import java.awt.*;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.stbi_load;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private final String title;

    private int width;

    private int height;

    private long windowHandle;

    private boolean resized;

    private boolean vSync;

    public Window( String title, int width, int height, boolean vSync){
        this.title = title;
        this.width = width;
        this.height = height;
        this.vSync = vSync;
        this.resized = false;
    }

    public void init() throws Exception {
        // setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit()){
            throw new IllegalStateException("Unable to initialize GLFW!");
        }

        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE); //the window will be resizable
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

        // create the window
        windowHandle = glfwCreateWindow(width, height, title, NULL, NULL);
        if (windowHandle == NULL){
            throw new RuntimeException("Failed to create the GLFW window!");
        }

        // setup resize callback
        glfwSetFramebufferSizeCallback(windowHandle, (window, width, height) -> {
            this.width = width;
            this.height = height;
            this.setResized(true);
        });

        // setup a key callback. it will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE){
                glfwSetWindowShouldClose(window, true); //we will detect this in the rendering loop
            }
        });

        // make the OpenGL context current
        glfwMakeContextCurrent(windowHandle);

        if (isvSync()){
            //enable v-sync
            glfwSwapInterval(1);
        }

        //center window
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension d = tk.getScreenSize();
        glfwSetWindowPos(windowHandle, (d.width - this.width) / 2, (d.height - this.height) / 2);

        //make the window visible
        glfwShowWindow(windowHandle);

        GL.createCapabilities();

        //set the clear color
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        //set depth testing
        glEnable(GL_DEPTH_TEST);

        //enable backface culling
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);

        // Support for transparencies
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        //hide cursor
        glfwSetInputMode(this.windowHandle, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);

        //load icon todo: needs to be it's own class
        MemoryStack stack = MemoryStack.stackPush();
        IntBuffer w = stack.mallocInt(1);
        IntBuffer h = stack.mallocInt(1);
        IntBuffer channels = stack.mallocInt(1);
        ByteBuffer buf = stbi_load("textures/icon.png", w, h, channels, 4);
        GLFWImage image = GLFWImage.malloc();
        image.set(32,32, buf);
        GLFWImage.Buffer images = GLFWImage.malloc(1);
        images.put(0, image);

        //set icon
        glfwSetWindowIcon(windowHandle, images);

    }

    public long getWindowHandle(){
        return windowHandle;
    }

    public void setClearColor(float r, float g, float b, float alpha){
        glClearColor(r, g, b, alpha);
    }

    public boolean isKeyPressed(int keyCode){
        return glfwGetKey(windowHandle, keyCode) == GLFW_PRESS;
    }

    public boolean windowShouldClose(){
        return glfwWindowShouldClose(windowHandle);
    }

    public String getTitle(){
        return title;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public boolean isResized(){
        return resized;
    }

    public void setResized(boolean resized){
        this.resized = resized;
    }

    public boolean isvSync(){
        return vSync;
    }

    public void setvSync(boolean vSync){
        this.vSync = vSync;
    }

    public void update(){
        glfwSwapBuffers(windowHandle);
        glfwPollEvents();
    }

}
