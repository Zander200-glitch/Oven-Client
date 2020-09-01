package engine;

import engine.graph.Mesh;
import engine.graph.Texture;
import game.player.Player;
import org.joml.Vector3f;

import java.util.ArrayList;

import static engine.FancyMath.*;
import static game.blocks.BlockDefinition.*;
import static game.blocks.BlockDefinition.getBottomTexturePoints;
import static game.collision.Collision.applyInertia;

public class ItemEntity {

    private final static float itemSize   = 0.2f;

    public final static int MAX_ID_AMOUNT = 126_000;

    private static int totalObjects       = 0;

    //TODO: pseudo object holder
    private static Mesh[] meshStorage  =     new Mesh[MAX_ID_AMOUNT];

    private static int[] thisMeshID    =      new int[MAX_ID_AMOUNT];

    private static Vector3f[] position = new Vector3f[MAX_ID_AMOUNT];

    private static float[] scale       =    new float[MAX_ID_AMOUNT];

    private static float[] hover       =    new float[MAX_ID_AMOUNT];

    private static boolean[] floatUp =    new boolean[MAX_ID_AMOUNT];

    private static Vector3f[] rotation = new Vector3f[MAX_ID_AMOUNT];

    private static Vector3f[] inertia  = new Vector3f[MAX_ID_AMOUNT];

    public static int getTotalObjects(){
        return totalObjects;
    }

    public static void createItem(int blockID, Vector3f pos){

        thisMeshID[totalObjects] = blockID;
        pos.x+=0.5f;
        pos.y+=0.5f;
        pos.z+=0.5f;
        position[totalObjects] = pos;
        inertia[totalObjects] = new Vector3f(randomForceValue(9f),(float)Math.random()*10f,randomForceValue(9f));
        rotation[totalObjects] = new Vector3f(0,0,0);
        floatUp[totalObjects] = true;
        scale[totalObjects] = 1f;
        totalObjects++;

        System.out.println("total items: " + totalObjects);
    }

    public static void onStep(){
        for (int i = 0; i < totalObjects; i++){

//            System.out.println(Player);

            applyInertia(position[i], inertia[i],true, itemSize, itemSize*2, true);
            rotation[i].y += 0.1f;
            if (floatUp[i]){
                hover[i] += 0.00025f;
                if (hover[i] >= 0.5f){
                    floatUp[i] = false;
                }
            } else {
                hover[i] -= 0.00025f;
                if (hover[i] <= 0.0f){
                    floatUp[i] = true;
                }
            }
        }
    }

    public static Vector3f getPosition(int ID){
        return position[ID];
    }

    public static Vector3f getPositionWithHover(int ID){
        return new Vector3f(position[ID].x, position[ID].y + hover[ID], position[ID].z);
    }

    public static void setPosition(float x, float y, float z, int ID){
        position[ID].x = x;
        position[ID].y = y;
        position[ID].z = z;
    }

    public static float getScale(int ID){
        return scale[ID];
    }

    public static void setScale(float newScale, int ID){
        scale[ID] = newScale;
    }

    public static Vector3f getRotation(int ID){
        return rotation[ID];
    }

    public static void setRotation(float x, float y, float z, int ID){
        rotation[ID].x = x;
        rotation[ID].y = y;
        rotation[ID].z = z;
    }

    public static Mesh getMesh(int ID){
        return meshStorage[thisMeshID[ID]];
    }

    public static void createBlockObjectMesh(int thisBlock) throws Exception {

        int indicesCount = 0;

        ArrayList positions     = new ArrayList();
        ArrayList textureCoord  = new ArrayList();
        ArrayList indices       = new ArrayList();
        ArrayList light         = new ArrayList();

        //create the mesh

        float thisLight = 1f;//(float)Math.pow(Math.pow(15,1.5),1.5);

        //front
        positions.add( itemSize); positions.add( itemSize*2f); positions.add(itemSize);
        positions.add(-itemSize); positions.add( itemSize*2f); positions.add(itemSize);
        positions.add(-itemSize); positions.add(0f); positions.add(itemSize);
        positions.add( itemSize); positions.add(0f); positions.add(itemSize);
        //front
        for (int i = 0; i < 12; i++){
            light.add(thisLight);
        }
        //front
        indices.add(0+indicesCount); indices.add(1+indicesCount); indices.add(2+indicesCount); indices.add(0+indicesCount); indices.add(2+indicesCount); indices.add(3+indicesCount);
        indicesCount += 4;

        float[] textureFront = getFrontTexturePoints(thisBlock);
        //front
        textureCoord.add(textureFront[1]);textureCoord.add(textureFront[2]);
        textureCoord.add(textureFront[0]);textureCoord.add(textureFront[2]);
        textureCoord.add(textureFront[0]);textureCoord.add(textureFront[3]);
        textureCoord.add(textureFront[1]);textureCoord.add(textureFront[3]);

        //back
        positions.add(-itemSize); positions.add( itemSize*2f); positions.add(-itemSize);
        positions.add( itemSize); positions.add( itemSize*2f); positions.add(-itemSize);
        positions.add( itemSize); positions.add(0f); positions.add(-itemSize);
        positions.add(-itemSize); positions.add(0f); positions.add(-itemSize);
        //back

        //back
        for (int i = 0; i < 12; i++){
            light.add(thisLight);
        }
        //back
        indices.add(0+indicesCount); indices.add(1+indicesCount); indices.add(2+indicesCount); indices.add(0+indicesCount); indices.add(2+indicesCount); indices.add(3+indicesCount);
        indicesCount += 4;

        float[] textureBack = getBackTexturePoints(thisBlock);
        //back
        textureCoord.add(textureBack[1]);textureCoord.add(textureBack[2]);
        textureCoord.add(textureBack[0]);textureCoord.add(textureBack[2]);
        textureCoord.add(textureBack[0]);textureCoord.add(textureBack[3]);
        textureCoord.add(textureBack[1]);textureCoord.add(textureBack[3]);

        //right
        positions.add(itemSize); positions.add(itemSize*2f); positions.add(-itemSize );
        positions.add(itemSize); positions.add(itemSize*2f); positions.add(itemSize );
        positions.add(itemSize); positions.add(0f); positions.add(itemSize );
        positions.add(itemSize); positions.add(0f); positions.add(-itemSize );

        //right
        for (int i = 0; i < 12; i++){
            light.add(thisLight);
        }
        //right
        indices.add(0+indicesCount); indices.add(1+indicesCount); indices.add(2+indicesCount); indices.add(0+indicesCount); indices.add(2+indicesCount); indices.add(3+indicesCount);
        indicesCount += 4;

        float[] textureRight = getRightTexturePoints(thisBlock);
        //right
        textureCoord.add(textureRight[1]);textureCoord.add(textureRight[2]);
        textureCoord.add(textureRight[0]);textureCoord.add(textureRight[2]);
        textureCoord.add(textureRight[0]);textureCoord.add(textureRight[3]);
        textureCoord.add(textureRight[1]);textureCoord.add(textureRight[3]);

        //left
        positions.add(-itemSize ); positions.add(itemSize*2f); positions.add(itemSize);
        positions.add(-itemSize ); positions.add(itemSize*2f); positions.add(-itemSize);
        positions.add(-itemSize ); positions.add(0f); positions.add(-itemSize );
        positions.add(-itemSize ); positions.add(0f); positions.add(itemSize );

        //left
        for (int i = 0; i < 12; i++){
            light.add(thisLight);
        }
        //left
        indices.add(0+indicesCount); indices.add(1+indicesCount); indices.add(2+indicesCount); indices.add(0+indicesCount); indices.add(2+indicesCount); indices.add(3+indicesCount);
        indicesCount += 4;

        float[] textureLeft = getLeftTexturePoints(thisBlock);
        //left
        textureCoord.add(textureLeft[1]);textureCoord.add(textureLeft[2]);
        textureCoord.add(textureLeft[0]);textureCoord.add(textureLeft[2]);
        textureCoord.add(textureLeft[0]);textureCoord.add(textureLeft[3]);
        textureCoord.add(textureLeft[1]);textureCoord.add(textureLeft[3]);

        //top
        positions.add(-itemSize ); positions.add(itemSize*2f ); positions.add(-itemSize);
        positions.add(-itemSize ); positions.add(itemSize*2f ); positions.add(itemSize );
        positions.add(itemSize ); positions.add(itemSize*2f); positions.add(itemSize );
        positions.add(itemSize ); positions.add(itemSize*2f); positions.add(-itemSize );

        //top
        for (int i = 0; i < 12; i++){
            light.add(thisLight);
        }
        //top
        indices.add(0+indicesCount); indices.add(1+indicesCount); indices.add(2+indicesCount); indices.add(0+indicesCount); indices.add(2+indicesCount); indices.add(3+indicesCount);
        indicesCount += 4;

        float[] textureTop = getTopTexturePoints(thisBlock);
        //top
        textureCoord.add(textureTop[1]);textureCoord.add(textureTop[2]);
        textureCoord.add(textureTop[0]);textureCoord.add(textureTop[2]);
        textureCoord.add(textureTop[0]);textureCoord.add(textureTop[3]);
        textureCoord.add(textureTop[1]);textureCoord.add(textureTop[3]);


        //bottom
        positions.add(-itemSize ); positions.add(0f);positions.add(itemSize );
        positions.add(-itemSize); positions.add(0f);positions.add(-itemSize );
        positions.add(itemSize ); positions.add(0f);positions.add(-itemSize);
        positions.add(itemSize ); positions.add(0f);positions.add(itemSize );

        //bottom
        for (int i = 0; i < 12; i++){
            light.add(thisLight);
        }
        //bottom
        indices.add(0+indicesCount); indices.add(1+indicesCount); indices.add(2+indicesCount); indices.add(0+indicesCount); indices.add(2+indicesCount); indices.add(3+indicesCount);

        float[] textureBottom = getBottomTexturePoints(thisBlock);
        //bottom
        textureCoord.add(textureBottom[1]);textureCoord.add(textureBottom[2]);
        textureCoord.add(textureBottom[0]);textureCoord.add(textureBottom[2]);
        textureCoord.add(textureBottom[0]);textureCoord.add(textureBottom[3]);
        textureCoord.add(textureBottom[1]);textureCoord.add(textureBottom[3]);


        //convert the position objects into usable array
        float[] positionsArray = new float[positions.size()];
        for (int i = 0; i < positions.size(); i++) {
            positionsArray[i] = (float)positions.get(i);
        }

        //convert the light objects into usable array
        float[] lightArray = new float[light.size()];
        for (int i = 0; i < light.size(); i++) {
            lightArray[i] = (float)light.get(i);
        }

        //convert the indices objects into usable array
        int[] indicesArray = new int[indices.size()];
        for (int i = 0; i < indices.size(); i++) {
            indicesArray[i] = (int)indices.get(i);
        }

        //convert the textureCoord objects into usable array
        float[] textureCoordArray = new float[textureCoord.size()];
        for (int i = 0; i < textureCoord.size(); i++) {
            textureCoordArray[i] = (float)textureCoord.get(i);
        }

        Texture texture = new Texture("textures/textureAtlas.png");

        Mesh mesh = new Mesh(positionsArray, lightArray, indicesArray, textureCoordArray, texture);

        meshStorage[thisBlock] = mesh;
    }

    public static void cleanUp(){
        for (Mesh thisMesh : meshStorage){
            if (thisMesh != null){
                thisMesh.cleanUp();
            }
        }
    }
}