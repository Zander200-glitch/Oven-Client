package engine.graph;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import static engine.ItemEntity.*;
import static engine.TNTEntity.getTNTPosition;
import static engine.TNTEntity.getTNTScale;

public class Transformation {

    private final Matrix4f projectionMatrix;

    private final Matrix4f modelViewMatrix;

    private final Matrix4f viewMatrix;

    public Transformation(){
        projectionMatrix = new Matrix4f();
        modelViewMatrix = new Matrix4f();
        viewMatrix = new Matrix4f();
    }

    public Matrix4f getViewMatrix(Camera camera){
        Vector3f cameraPos = camera.getPosition();
        Vector3f rotation = camera.getRotation();
        viewMatrix.identity();
        //first do the rotation so the camera rotates over it's position
        viewMatrix.rotate((float)Math.toRadians(rotation.x), new Vector3f(1,0,0)).rotate((float)Math.toRadians(rotation.y), new Vector3f(0,1,0));
        //then do the translation
        viewMatrix.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        return viewMatrix;
    }

    public final Matrix4f getProjectionMatrix(float fov, float width, float height, float zNear, float zFar){
        float aspectRatio = width / height;
        projectionMatrix.identity();
        projectionMatrix.perspective(fov, aspectRatio, zNear, zFar);
        return projectionMatrix;
    }

    public Matrix4f getEntityModelViewMatrix(int ID, Matrix4f viewMatrix){
        Vector3f rotation = getRotation(ID);
        modelViewMatrix.identity().translate(getPositionWithHover(ID)).
                rotateX((float)Math.toRadians(-rotation.x)).
                rotateY((float)Math.toRadians(-rotation.y)).
                rotateZ((float)Math.toRadians(-rotation.z)).
                scale(getScale(ID));
        Matrix4f viewCurr = new Matrix4f(viewMatrix);
        return viewCurr.mul(modelViewMatrix);
    }

    public Matrix4f getTNTModelViewMatrix(int ID, Matrix4f viewMatrix){
        modelViewMatrix.identity().translate(getTNTPosition(ID)).
                rotateX((float)Math.toRadians(0)).
                rotateY((float)Math.toRadians(0)).
                rotateZ((float)Math.toRadians(0)).
                scale(getTNTScale(ID));
        Matrix4f viewCurr = new Matrix4f(viewMatrix);
        return viewCurr.mul(modelViewMatrix);
    }


    public Matrix4f getModelViewMatrix(Matrix4f viewMatrix){
        Vector3f rotation = new Vector3f(0,0,0);
        modelViewMatrix.identity().translate(new Vector3f(0,0,0)).
                rotateX((float)Math.toRadians(-rotation.x)).
                rotateY((float)Math.toRadians(-rotation.y)).
                rotateZ((float)Math.toRadians(-rotation.z)).
                scale(1f);
        Matrix4f viewCurr = new Matrix4f(viewMatrix);
        return viewCurr.mul(modelViewMatrix);
    }

    public static  Matrix4f updateGenericViewMatrix(Vector3f position, Vector3f rotation, Matrix4f matrix) {
        // First do the rotation so camera rotates over its position
        return matrix.rotationX((float)Math.toRadians(rotation.x))
                .rotateY((float)Math.toRadians(rotation.y))
                .translate(-position.x, -position.y, -position.z);
    }
}
