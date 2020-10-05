package game.light;

import java.util.*;

import static game.chunk.Chunk.*;

public class Light {

    private static final byte maxLightLevel = 15;
    private static final byte blockIndicator = 127;
    private static final byte lightDistance = 15;
    private static final byte max = (lightDistance * 2) + 1;

    private static final Deque<LightUpdate> lightSources = new ArrayDeque<>();

    public static void floodFill(int posX, int posY, int posZ) {

        byte[][][] memoryMap = new byte[(lightDistance * 2) + 1][(lightDistance * 2) + 1][(lightDistance * 2) + 1];

        for (int x = posX - lightDistance; x <= posX + lightDistance; x++){
            for (int y = posY - lightDistance; y <= posY + lightDistance; y++){
                for (int z = posZ - lightDistance; z <= posZ + lightDistance; z++){
                    int theBlock = getBlock(x,y,z);
                    if (theBlock == 0 && underSunLight(x,y,z)){
                        memoryMap[x - posX + lightDistance][y - posY + lightDistance][z - posZ + lightDistance] = maxLightLevel;
                    }
                    else if (theBlock == 0){
                        memoryMap[x - posX + lightDistance][y - posY + lightDistance][z - posZ + lightDistance] = 0;
                    }
                    else {
                        memoryMap[x - posX + lightDistance][y - posY + lightDistance][z - posZ + lightDistance] = blockIndicator;
                    }
                }
            }
        }
        for (int x = posX - lightDistance; x <= posX + lightDistance; x++) {
            for (int y = posY - lightDistance; y <= posY + lightDistance; y++) {
                for (int z = posZ - lightDistance; z <= posZ + lightDistance; z++) {
                    if (memoryMap[x - posX + lightDistance][y - posY + lightDistance][z - posZ + lightDistance] == maxLightLevel) {
                        int skipCheck = 0;
                        if (x - posX + lightDistance - 1 >= 0 && memoryMap[x - posX + lightDistance - 1][y - posY + lightDistance][z - posZ + lightDistance] != 0){
                            skipCheck++;
                        }
                        if (x - posX + lightDistance + 1 < max && memoryMap[x - posX + lightDistance + 1][y - posY + lightDistance][z - posZ + lightDistance] != 0){
                            skipCheck++;
                        }
                        if (z - posZ + lightDistance - 1 >= 0 && memoryMap[x - posX + lightDistance][y - posY + lightDistance][z - posZ + lightDistance - 1] != 0){
                            skipCheck++;
                        }
                        if (z - posZ + lightDistance + 1 < max && memoryMap[x - posX + lightDistance][y - posY + lightDistance][z - posZ + lightDistance+ 1] != 0){
                            skipCheck++;
                        }
                        if (y - posY + lightDistance - 1 >= 0 && memoryMap[x - posX + lightDistance][y - posY + lightDistance - 1][z - posZ + lightDistance] != 0){
                            skipCheck++;
                        }
                        if (y - posY + lightDistance + 1 < max && memoryMap[x - posX + lightDistance][y - posY + lightDistance + 1][z - posZ + lightDistance] != 0){
                            skipCheck++;
                        }
                        if (skipCheck < 6){
                            lightSources.add(new LightUpdate(x - posX + lightDistance,y - posY + lightDistance,z - posZ + lightDistance));
                        }
                    }
                }
            }
        }

        while (!lightSources.isEmpty()){
            LightUpdate thisUpdate = lightSources.pop();

            int skipCheck = 0;
            if (thisUpdate.x - 1 >= 0 && memoryMap[thisUpdate.x - 1][thisUpdate.y][thisUpdate.z ] == maxLightLevel){
                skipCheck++;
            }
            if (thisUpdate.x + 1 < max && memoryMap[thisUpdate.x + 1][thisUpdate.y][thisUpdate.z] == maxLightLevel){
                skipCheck++;
            }
            if (thisUpdate.z - 1 >= 0 && memoryMap[thisUpdate.x][thisUpdate.y][thisUpdate.z - 1]  == maxLightLevel){
                skipCheck++;
            }
            if (thisUpdate.z  + 1 < max && memoryMap[thisUpdate.x][thisUpdate.y][thisUpdate.z + 1]  == maxLightLevel){
                skipCheck++;
            }
            if (thisUpdate.y - 1 >= 0 && memoryMap[thisUpdate.x][thisUpdate.y - 1][thisUpdate.z] == maxLightLevel){
                skipCheck++;
            }
            if (thisUpdate.y  + 1 < max && memoryMap[thisUpdate.x][thisUpdate.y + 1][thisUpdate.z]  == maxLightLevel){
                skipCheck++;
            }
            if (skipCheck == 6){
                continue;
            }

            Deque<LightUpdate> lightSteps = new ArrayDeque<>();

            int[] crawlerPos;

            lightSteps.push(new LightUpdate(thisUpdate.x, thisUpdate.y, thisUpdate.z, maxLightLevel));

            while (!lightSteps.isEmpty()) {
                LightUpdate newUpdate = lightSteps.pop();

                if (newUpdate.level <= 1){
                    continue;
                }
                if (newUpdate.x < 0 || newUpdate.x > max || newUpdate.y < 0 || newUpdate.y > max || newUpdate.z < 0 || newUpdate.z > max) {
                    continue;
                }

                crawlerPos = new int[]{newUpdate.x, newUpdate.y, newUpdate.z};

                //+x
                {
                    if (crawlerPos[0] + 1 < max && memoryMap[crawlerPos[0] + 1][crawlerPos[1]][crawlerPos[2]] < newUpdate.level) {
                        memoryMap[crawlerPos[0] + 1][crawlerPos[1]][crawlerPos[2]] = (byte) (newUpdate.level-1);
                        lightSteps.add(new LightUpdate(crawlerPos[0] + 1, crawlerPos[1], crawlerPos[2], (byte) (newUpdate.level-1)));
                    }
                }

                //-x
                {
                    if (crawlerPos[0] - 1 >= 0 && memoryMap[crawlerPos[0] - 1][crawlerPos[1]][crawlerPos[2]] < newUpdate.level) {
                        memoryMap[crawlerPos[0] - 1][crawlerPos[1]][crawlerPos[2]] = (byte) (newUpdate.level-1);
                        lightSteps.add(new LightUpdate(crawlerPos[0] - 1, crawlerPos[1], crawlerPos[2], (byte) (newUpdate.level-1)));
                    }
                }

                //+z
                {
                    if (crawlerPos[2] + 1 < max && memoryMap[crawlerPos[0]][crawlerPos[1]][crawlerPos[2] + 1] < newUpdate.level) {
                        memoryMap[crawlerPos[0]][crawlerPos[1]][crawlerPos[2] + 1] = (byte) (newUpdate.level-1);
                        lightSteps.add(new LightUpdate(crawlerPos[0], crawlerPos[1], crawlerPos[2] + 1, (byte) (newUpdate.level-1)));
                    }
                }

                //-z
                {
                    if (crawlerPos[2] - 1 >= 0 && memoryMap[crawlerPos[0]][crawlerPos[1]][crawlerPos[2] - 1] < newUpdate.level) {
                        memoryMap[crawlerPos[0]][crawlerPos[1]][crawlerPos[2] - 1] = (byte) (newUpdate.level-1);
                        lightSteps.add(new LightUpdate(crawlerPos[0], crawlerPos[1], crawlerPos[2] - 1, (byte) (newUpdate.level-1)));
                    }
                }

                //+y
                {
                    if (crawlerPos[1] + 1 < max && memoryMap[crawlerPos[0]][crawlerPos[1] + 1][crawlerPos[2]] < newUpdate.level) {
                        memoryMap[crawlerPos[0]][crawlerPos[1] + 1][crawlerPos[2]] = (byte) (newUpdate.level-1);
                        lightSteps.add(new LightUpdate(crawlerPos[0], crawlerPos[1] + 1, crawlerPos[2], (byte) (newUpdate.level-1)));
                    }
                }

                //-y
                {
                    if (crawlerPos[1] - 1 >= 0 && memoryMap[crawlerPos[0]][crawlerPos[1] - 1][crawlerPos[2]] < newUpdate.level) {
                        memoryMap[crawlerPos[0]][crawlerPos[1] - 1][crawlerPos[2]] = (byte) (newUpdate.level-1);
                        lightSteps.add(new LightUpdate(crawlerPos[0], crawlerPos[1] - 1, crawlerPos[2], (byte) (newUpdate.level-1)));
                    }
                }
            }
        }

        for (int x = posX - lightDistance; x <= posX + lightDistance; x++){
            for (int y = posY - lightDistance; y <= posY + lightDistance; y++){
                for (int z = posZ - lightDistance; z <= posZ + lightDistance; z++){
                    if (memoryMap[x - posX + lightDistance][y - posY + lightDistance][z - posZ + lightDistance] != blockIndicator) {
                        setLight(x, y, z, memoryMap[x - posX + lightDistance][y - posY + lightDistance][z - posZ + lightDistance]);
                    }
                }
            }
        }
    }
}