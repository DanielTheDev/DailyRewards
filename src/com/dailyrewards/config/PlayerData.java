package com.dailyrewards.config;

import com.dailyrewards.PluginClass;
import com.dailyrewards.extentions.Chat;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@SerializableAs("PlayerData")
public class PlayerData implements ConfigurationSerializable {

    private final SimpleDateFormat timeformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private UUID uuid;
    private int claimStreak;
    private int totalClaimed;
    private Date lastClaimed;
    private Date firstClaimed;

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
    }

    public PlayerData(Map<String, Object> data) {
       this.uuid = UUID.fromString((String) data.get("uuid"));
       this.claimStreak = (int) data.get("claim-streak");
       this.totalClaimed = (int) data.get("total-claimed");
        try {
            if(!data.get("last-claimed").equals("none")) this.lastClaimed = this.timeformat.parse((String) data.get("last-claimed"));
            if(!data.get("first-claimed").equals("none")) this.firstClaimed = this.timeformat.parse((String) data.get("first-claimed"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public SimpleDateFormat getTimeformat() {
        return timeformat;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public int getClaimStreak() {
        return claimStreak;
    }

    public void setClaimStreak(int claimStreak) {
        this.claimStreak = claimStreak;
    }

    public int getTotalClaimed() {
        return totalClaimed;
    }

    public void addTotalClaimed(int add) {
        this.totalClaimed+=add;
    }

    public void addClaimStreak(int add) {
        this.totalClaimed+=add;
    }

    public boolean canClaim() {
        if(this.hasLastClaimed()) {
            return this.getLastClaimed().getTime()+(PluginClass.getPluginConfig().getDelay()*1000) < System.currentTimeMillis();
        } else return true;
    }

    public void claim() {
        this.lastClaimed = new Date();
        this.totalClaimed++;
        this.claimStreak++;
        if(this.firstClaimed == null) this.firstClaimed = new Date();

        PluginClass.getPlugin().getPluginLib().getPlayerDataManager().update(this);
    }

    public int timeRemaining() {
        if(!this.canClaim()) {
            return (int)(((this.getLastClaimed().getTime()-System.currentTimeMillis())/1000)+(PluginClass.getPluginConfig().getDelay()));
        } else return 0;
    }

    public int streakTimeRemaining() {
        if(this.lastClaimed != null) {
            int remaining =  PluginClass.getPlugin().getPluginLib().getPluginConfig().getResetStreak()-((int)(System.currentTimeMillis()-this.lastClaimed.getTime())/1000);
            if(remaining < 0) return 0;
            else return remaining;
        } else {
            return 0;
        }
    }

    public void setTotalClaimed(int totalClaimed) {
        this.totalClaimed = totalClaimed;
    }

    public Date getLastClaimed() {
        return lastClaimed;
    }

    public void setLastClaimed(Date lastClaimed) {
        this.lastClaimed = lastClaimed;
    }

    public Date getFirstClaimed() {
        return firstClaimed;
    }

    public String getFirstClaimedFormat() {
        if(this.firstClaimed == null) return "none";
        else return this.timeformat.format(this.firstClaimed);
    }

    public String getFirstClaimedRelative() {
        return (lastClaimed != null ? Chat.timeTranslate((System.currentTimeMillis()-lastClaimed.getTime())/1000) + " ago" : "none");
    }

    public String getLastClaimedRelative() {
        return (lastClaimed != null ? Chat.timeTranslate((System.currentTimeMillis()-lastClaimed.getTime())/1000) + " ago" : "none");
    }

    public String getLastClaimedFormat() {
        if(this.lastClaimed == null) return "none";
        else return this.timeformat.format(this.lastClaimed);
    }

    public void update() {
       if(this.streakTimeRemaining() == 0) this.claimStreak = 0;
    }

    public void setFirstClaimed(Date firstClaimed) {
        this.firstClaimed = firstClaimed;
    }

    public boolean hasFirstClaimed(){
        return this.firstClaimed != null;
    }

    public boolean hasLastClaimed(){
        return this.lastClaimed != null;
    }

    public static PlayerData deserialize(Map<String, Object> data){
        return new PlayerData(data);
    }

    public static PlayerData valueOf(Map<String, Object> data){
        return new PlayerData(data);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("uuid", uuid.toString());
        map.put("claim-streak", claimStreak);
        map.put("total-claimed", totalClaimed);
        map.put("last-claimed", lastClaimed == null ? "none" : this.timeformat.format(lastClaimed));
        map.put("first-claimed", firstClaimed == null ? "none" : this.timeformat.format(firstClaimed));
        return map;
    }

    @Override
    public String toString() {
        return "PlayerData{" +
                "timeformat=" + timeformat +
                ", uuid=" + uuid +
                ", claimStreak=" + claimStreak +
                ", totalClaimed=" + totalClaimed +
                ", lastClaimed=" + lastClaimed +
                ", firstClaimed=" + firstClaimed +
                '}';
    }

    public String getRemainingTimeFormat() {
        int remainingStreakTime = streakTimeRemaining();
        return (remainingStreakTime > 0 ? Chat.timeTranslate(remainingStreakTime) : "none");
    }
}
