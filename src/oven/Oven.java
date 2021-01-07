package oven;

import oven.mod.Mod;
import testmod.TMod;

public class Oven {
    private static Mod focusedMod = new Mod(null);

    //mods
    public static Mod modone = new Mod("modone");

    public static void onStart(){
        modone.setFocused();

    }
    public static void onTickPre(){

    }
    public static void onTickPost(){

    }
    public static void onClose(){

    }

    public static void setFocusedMod(Mod fm){
        focusedMod = fm;
    }
    public static Mod getFocusedMod(){return focusedMod;}
}
