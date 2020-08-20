package game;

import engine.GameItem;
import engine.IGameLogic;
import engine.MouseInput;
import engine.Window;
import engine.graph.Camera;
import engine.graph.Mesh;
import engine.graph.Texture;
import game.ChunkHandling.Chunk;
import game.ChunkHandling.ChunkData;
import game.ChunkHandling.ChunkMesh;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.system.CallbackI;

import static org.lwjgl.glfw.GLFW.*;

public class Crafter implements IGameLogic {

    private static final float MOUSE_SENSITIVITY = 0.1f;

    private final Vector3f cameraInc;

    private final Renderer renderer;

    private final Camera camera;

    private GameItem[] gameItems;

    private static final float CAMERA_POS_STEP = 0.15f;

    public Crafter(){
        renderer = new Renderer();
        camera = new Camera();
        cameraInc = new Vector3f();
    }

    @Override
    public void init(Window window) throws Exception{
        renderer.init(window);

        Chunk chunk = new Chunk(0,0);

        ChunkData.storeChunk(0,0, chunk);

        Mesh mesh = new ChunkMesh(chunk).getMesh();

        GameItem gameItem = new GameItem(mesh);

        gameItems = new GameItem[] {gameItem};
    }

    @Override
    public void input(Window window, MouseInput input){
        cameraInc.set(0,0,0);
        if (window.isKeyPressed(GLFW_KEY_W)){
            cameraInc.z = -1;
        } else if (window.isKeyPressed(GLFW_KEY_S)){
            cameraInc.z = 1;
        }

        if (window.isKeyPressed(GLFW_KEY_A)){
            cameraInc.x = -1;
        } else if (window.isKeyPressed(GLFW_KEY_D)){
            cameraInc.x = 1;
        }

        if (window.isKeyPressed(GLFW_KEY_LEFT_SHIFT)){
            cameraInc.y = -1;
        } else if (window.isKeyPressed(GLFW_KEY_SPACE)){
            cameraInc.y = 1;
        }
    }

    @Override
    public void update(float interval, MouseInput mouseInput){

        //update camera position
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP,
                cameraInc.y * CAMERA_POS_STEP,
                cameraInc.z * CAMERA_POS_STEP);

        //update camera based on mouse
//        if (mouseInput.isRightButtonPressed()){
            Vector2f rotVec = mouseInput.getDisplVec();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
//        }

        for (GameItem gameItem : gameItems){
            //update position
//            Vector3f itemPos = gameItem.getPosition();
//            float posx = itemPos.x + displxInc * 0.01f;
//            float posy = itemPos.y + displyInc * 0.01f;
//            float posz = itemPos.z + displzInc * 0.01f;

//            gameItem.setPosition(posx, posy, posz);

            //Update scale
//            float scale = gameItem.getScale();
//            scale += scaleInc * 0.05f;
//            if (scale < 0) {
//                scale = 0;
//            }
//            gameItem.setScale(scale);

            //gameItem.setPosition((float)Math.random()-0.5f,(float)Math.random()-0.5f,-2f);

            //update rotation angle
//            float rotation = gameItem.getRotation().y - 1.5f;
//
//
//            if (rotation > 360) {
//                rotation -= 360;
//            }
//
//            gameItem.setRotation(rotation, rotation, rotation);
        }
    }

    @Override
    public void render(Window window){
        renderer.render(window, camera, gameItems);
    }

    @Override
    public void cleanup(){
        renderer.cleanup();
        for (GameItem gameItem : gameItems){
            gameItem.getMesh().cleanUp();
        }
    }
}
