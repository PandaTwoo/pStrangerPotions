package br.com.pandatwo.pStrangePotions.entities;

import br.com.infernalia.flat.Flat;
import br.com.infernalia.flat.Flats;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


@Getter
@Setter
@NonNull
public class StrangePotion {

    private Flat<PotionEffect> effects;
    private ItemStack item;


    public StrangePotion(ItemStack item) {
        this.item = item;
        this.effects = Flats.emptyList();
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
        if (tag.hasKey("sPotion")) {
            String[] effectsData = tag.getString("sPotion").split(", ");
            for (String effectRawData : effectsData) {
                String[] effectData = effectRawData.split(":");
                PotionEffectType effectType = PotionEffectType.getByName(effectData[0]);
                effects.add(effectType.createEffect(Integer.parseInt(effectData[1]), Integer.parseInt(effectData[2])));
            }
        }
    }

    public void addPotionEffect(PotionEffect effect) {
        effects.add(effect);
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
        StringBuilder data = new StringBuilder();
        for (PotionEffect effecti : effects) {
            if (!data.toString().equals(""))
                data.append(";");
            data.append(effecti.getType().getName()).append(":").append(effecti.getDuration()).append(":").append(effecti.getAmplifier());
        }
        tag.setString("sPotion", data.toString());
        nmsItem.setTag(tag);
        item = CraftItemStack.asBukkitCopy(nmsItem);
    }

    public void removePotionEffect(PotionEffect effect) {
        effects.remove(effect);
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
        StringBuilder data = new StringBuilder();
        for (PotionEffect effecti : effects) {
            if (!data.toString().equals(""))
                data.append(";");
            data.append(effecti.getType().getName()).append(":").append(effecti.getDuration()).append(":").append(effecti.getAmplifier());
        }
        tag.setString("sPotion", data.toString());
        nmsItem.setTag(tag);
        item = CraftItemStack.asBukkitCopy(nmsItem);
    }

    public Flat<PotionEffect> getEffects() {
        return effects;
    }

    public ItemStack getItem() {
        return item;
    }

    public static boolean isStrangePotion(@NonNull ItemStack item) {
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
        return item.getType() != Material.POTION || !tag.hasKey("sPotion");
    }

}
