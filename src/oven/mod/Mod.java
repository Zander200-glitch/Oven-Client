package oven.mod;

import oven.Event.Event;
import oven.Event.EventType;
import oven.Oven;

public class Mod {
    private String name;
    public Mod Mod = this;

    public String getName(){return name;}

    public Mod(String name){
        Mod.name = name;
    }
    public void onEvent(Event e){

    }
    public void enable(){

    }
    public void disable() {

    }
    public void setFocused(){
        Oven.setFocusedMod((Mod) this);
    }
    public Mod getMod(){return this;}
}