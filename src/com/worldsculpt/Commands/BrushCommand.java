package com.worldsculpt.Commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.worldsculpt.Variables.Sculptable;

import PluginReference.ChatColor;
import PluginReference.MC_Command;
import PluginReference.MC_ItemStack;
import PluginReference.MC_Player;

public class BrushCommand implements MC_Command {
	public static ArrayList<Sculptable> s = new ArrayList<>();

	public void handleCommand(MC_Player player, String[] args) {
		for (Sculptable a : s) {
			if (a.getName().equalsIgnoreCase(args[0])) {
				MC_ItemStack item = player.getItemInHand();
				if (item != null && item.getId() != 0) {
					String cmd = "";
					for (int i = 1; i < args.length; i++) {
						if (i == 1)
							cmd = args[i];
						else
							cmd += " " + args[i];
					}
					item.setCustomName(ChatColor.AQUA + "Brush: " + toTitleCase(args[0], cmd));
					player.sendMessage("Sculpt bound.");
				} else
					player.sendMessage("You are not holding an item!");
				return;
			}
		}
		player.sendMessage("Sculpt command not found!");
	}

	public List<String> getAliases() {
		return Arrays.asList("br", "b");
	}

	public String getCommandName() {
		return "brush";
	}

	public String getHelpLine(MC_Player player) {
		return ChatColor.GOLD + "/brush [sculpt command]";
	}

	public List<String> getTabCompletionList(MC_Player player, String[] args) {
		return new ArrayList<>();
	}

	public boolean hasPermissionToUse(MC_Player player) {
		return (player != null ? player.isOp() : false);
	}

	private static String toTitleCase(String a, String b) {
		a = a.toLowerCase();
		a = ChatColor.DARK_AQUA + a.substring(0, 1).toUpperCase() + a.substring(1) + ChatColor.GRAY + " " + b;
		return a;
	}
}