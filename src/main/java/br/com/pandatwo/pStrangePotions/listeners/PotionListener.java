package br.com.pandatwo.pStrangePotions.listeners;

import br.com.pandatwo.pStrangePotions.entities.StrangePotion;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.Optional;

public class PotionListener implements Listener {

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event) {
        ItemStack item = event.getItem();
        Player player = event.getPlayer();
        if (item == null || item.getType() != Material.POTION)
            return;
        if (StrangePotion.isStrangePotion(item))
            return;
        StrangePotion potion = new StrangePotion(item);
        Optional<PotionEffect> effectOptional = potion.getEffects().stream().findAny();
        effectOptional.ifPresent(potionEffect -> potionEffect.apply(player));
    }

}
