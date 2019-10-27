package br.com.pandatwo.pStrangePotions.commands;

import br.com.infernalia.flat.Flat;
import br.com.pandatwo.pStrangePotions.entities.StrangePotion;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Optional;

public class SPotionCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage("§c§l» §fOoops, você não pode fazer isso!");
            return true;
        }

        if (!sender.hasPermission("spotion.admin")) {
            sender.sendMessage("§c§l» §fSem permissão.");
            return true;
        }


        Player player = (Player) sender;
        ItemStack item = player.getItemInHand();

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("list")) {
                if (item.getType() != Material.POTION) {
                    sender.sendMessage("§c§l» §fVocê precisa ter uma poção na mão!");
                    return true;
                }
                StrangePotion potion = new StrangePotion(item);
                Flat<PotionEffect> effects = potion.getEffects();
                if (StrangePotion.isStrangePotion(item) || effects.isEmpty()) {
                    sender.sendMessage("§c§l» §fEssa poção não contém efeitos!");
                    return true;
                }
                sender.sendMessage("§a§l» §fEfeito(s) da poção:");
                effects.forEach(effect -> sender.sendMessage(" §e§l• §b" + effect.getType().getName() + " " + StringUtils.repeat("I", effect.getAmplifier() + 1) + " §e(" + effect.getDuration() + "§e)"));
                return true;
            }
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("remove")) {
                if (item.getType() != Material.POTION) {
                    sender.sendMessage("§c§l» §fVocê precisa ter uma poção na mão!");
                    return true;
                }
                PotionEffectType effectToRemove;
                try {
                    effectToRemove = PotionEffectType.getByName(args[1].toUpperCase());
                } catch (Exception e) {
                    sender.sendMessage("§c§l» §fTipo de poção não encontrado!");
                    return true;
                }
                StrangePotion potion = new StrangePotion(item);
                Optional<PotionEffect> findedEffectOptional = potion.getEffects().find(effect -> effect.getType().getName().equals(effectToRemove.getName()));
                if (!findedEffectOptional.isPresent()) {
                    sender.sendMessage("§c§l» §fEssa poção não possui esse efeito!");
                    return true;
                }
                potion.removePotionEffect(findedEffectOptional.get());
                player.setItemInHand(potion.getItem());
                sender.sendMessage("§a§l» §fEfeito removido com sucesso!");
                return true;
            }
        }

        if (args.length == 4) {
            if (args[0].equalsIgnoreCase("add")) {
                if (item.getType() != Material.POTION) {
                    sender.sendMessage("§c§l» §fVocê precisa ter uma poção na mão!");
                    return true;
                }
                PotionEffectType effectToAdd;
                try {
                    effectToAdd = PotionEffectType.getByName(args[1].toUpperCase());
                } catch (Exception e) {
                    sender.sendMessage("§c§l» §fTipo de poção não encontrado!");
                    return true;
                }
                if (!isInt(args[2]) && Integer.parseInt(args[2]) > 0) {
                    sender.sendMessage("§c§l» §fO tempo da poção deve ser um número inteiro e em segundos!");
                    return true;
                }
                if (!isInt(args[3]) && Integer.parseInt(args[3]) > 0) {
                    sender.sendMessage("§c§l» §fO amplificador da poção deve ser um número inteiro e maior que 0!");
                    return true;
                }
                StrangePotion potion = new StrangePotion(item);
                potion.addPotionEffect(effectToAdd.createEffect(Integer.parseInt(args[2]), Integer.parseInt(args[3]) - 1));
                player.setItemInHand(potion.getItem());
                sender.sendMessage("§a§l» §fEfeito adicionado com sucesso!");
                return true;
            }
        }

        sender.sendMessage("§eÍndice de ajuda do sPotions!");
        sender.sendMessage("");
        sender.sendMessage("§a§l» §f/spotion add (EFEITO) (TEMPO) (AMPLIFICADOR)");
        sender.sendMessage("§a§l» §f/spotion list");

        return true;
    }

    private Boolean isInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
