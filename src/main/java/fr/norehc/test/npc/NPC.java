package fr.norehc.test.npc;

import jline.internal.Nullable;

public class NPC {
    private double posX, posY, posZ;
    private String name, world, skin, signature, oldName, function, skinName;
    private Boolean isNew, exist;

    public NPC(double posX, double posY, double posZ, String name, String world, String skin, String signature, Boolean isNew, String function, String skinName) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.name = name;
        this.world = world;
        this.skin = skin;
        this.signature = signature;
        this.isNew = isNew;
        this.exist = true;
        this.oldName = name;
        this.function = function;
        this.skinName = skinName;
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public double getPosZ() {
        return posZ;
    }

    public void setPosZ(double posZ) {
        this.posZ = posZ;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public String getSkin() {
        return skin;
    }

    public void setSkin(String skin) {
        this.skin = skin;
    }

    public String getSignatures() {
        return signature;
    }

    public void setSignatures(String signature) {
        this.signature = signature;
    }

    public boolean isNew() {
        return isNew;
    }

    public boolean exist() {
        return exist;
    }

    public void delete() {
        exist = false;
    }

    public String getOldName() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getFunction() {
        return function;
    }

    public String getSkinName() {
        return skinName;
    }
}
