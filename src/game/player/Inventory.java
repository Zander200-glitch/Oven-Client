package game.player;

import game.blocks.BlockDefinition;
import game.item.Item;

import static game.blocks.BlockDefinition.getBlockDefinition;
import static game.item.ItemDefinition.getRandomItemDefinition;
import static game.item.ItemEntity.createItem;
import static engine.graph.Camera.getCameraRotationVector;
import static game.player.Player.*;

public class Inventory {

    private static Item[][] inventory = new Item[4][9];

    private static Item mouseInventory;

    public static void generateRandomInventory(){
        for (int x = 0; x < 9; x++){
            for (int y = 0; y < 4; y++){
                inventory[y][x] = new Item(getRandomItemDefinition().name, (int)Math.floor(Math.random() * 65));
            }
        }
    }
    public static void resetInventory(){
        for (int x = 0; x < 9; x++){
            for (int y = 0; y < 4; y++){
                inventory[y][x] = null;
            }
        }
    }

    public static void tntFillErUp(){
        for (int x = 0; x < 9; x++){
            for (int y = 0; y < 4; y++){
                inventory[y][x] = new Item("tnt", 64);
            }
        }
    }

    public static boolean addItemToInventory(String name){
        //check whole inventory
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 9; x++) {
                if (inventory[y][x] != null && inventory[y][x].name.equals(name)){
                    inventory[y][x].stack++;
                    return true;
                }
            }
        }
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 9; x++) {
                if (inventory[y][x] == null){
                    inventory[y][x] = new Item(name, 1);
                    return true;
                }
            }
        }

        return false;
    }

    public static void throwItem(){
        Item test = getItemInInventorySlot(getPlayerInventorySelection(), 0);
        if (test != null) {
            BlockDefinition layer2 = getBlockDefinition(test.name);
            if (layer2 != null) {
                String name = layer2.name;
                if (name != null) {
                    createItem(name, getPlayerPosWithEyeHeight(), getCameraRotationVector().mul(10f), 1);
                    removeItemFromInventory(getPlayerInventorySelection(), 0);
                }
            }
        }
    }
    public static void setItemInInventory(int x, int y, String name, int stack){
        inventory[y][x] = new Item(name, stack);
    }

    public static void removeItemFromInventory(int x, int y){
        inventory[y][x] = null;
    }

    public static Item getItemInInventorySlot(int x, int y){
        return inventory[y][x];
    }

    public static String getItemInInventorySlotName(int x, int y){
        if (inventory[y][x] == null){
            return "null";
        } else {
            return inventory[y][x].name;
        }
    }


    public static Item getMouseInventory(){
        return mouseInventory;
    }

    public static void setMouseInventory(Item newItem){
        mouseInventory = newItem;
    }
}
