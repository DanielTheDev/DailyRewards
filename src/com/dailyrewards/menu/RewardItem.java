package com.dailyrewards.menu;

import com.dailyrewards.config.RewardConfig;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class RewardItem {

    private final RewardConfig.Present present;
    private int x;
    private int y;
    private final int destination_x;
    private final int destination_y;
    private List<int[]> paths;
    private List<Integer> trails;
    private boolean isOpened;

    public RewardItem(RewardConfig.Present present, int x, int y, int destination_x, int destination_y) {
        this.present = present;
        this.x = x;
        this.y = y;
        this.destination_x = destination_x;
        this.destination_y = destination_y;
        this.paths = new ArrayList<>();
        this.trails = new ArrayList<>();
        this.isOpened = false;
    }


    public void addTrail(Inventory inventory, int slot, ItemStack trail) {
        this.trails.add(slot);
        inventory.setItem(slot, trail);
    }

    public void updateTrails(Inventory inventory) {
        if(this.trails.size() > 1) {
            inventory.setItem(this.trails.get(0), null);
            this.trails.remove(0);
        }
    }

    public boolean isOpened() {
        return isOpened;
    }

    public void setOpened(boolean opened) {
        isOpened = opened;
    }

    public List<int[]> getPaths(){
        return this.paths;
    }

    public boolean hasArrived() {
        return hasArrived_X() && hasArrived_Y();
    }

    public boolean hasArrived_X() {
        return x==destination_x;
    }

    public boolean hasArrived_Y() {
        return y==destination_y;
    }

    public int getRemainingX() {
        return Math.abs(destination_x-x);
    }

    public int getRemainingY() {
        return Math.abs(destination_y-y);
    }

    public int getRemainingTypeX() {
        return (destination_x-x) < 0 ? -1 : 1;
    }

    public int getRemainingTypeY() {
        return (destination_y-y) < 0 ? -1 : 1;
    }

    public void moveX(int add) {
        this.x+=add;
    }

    public void moveY(int add) {
        this.y+=add;
    }

    public void teleport(int x, int y) {
        this.x=x;
        this.y=y;
    }

    public RewardConfig.Present getPresent() {
        return present;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDestination_x() {
        return destination_x;
    }

    public int getDestination_y() {
        return destination_y;
    }

    @Override
    public String toString() {
        return "Item{" +
                "x=" + x +
                ", y=" + y +
                ", destination_x=" + destination_x +
                ", destination_y=" + destination_y +
                '}';
    }

    public void destroy() {
        this.paths.clear();
        this.paths = null;
        this.trails.clear();
        this.trails = null;
    }
}
