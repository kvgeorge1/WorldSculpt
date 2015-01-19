package com.worldsculpt.Commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.worldsculpt.Variables.CommandHelper;
import com.worldsculpt.Variables.UndoPoint;

import PluginReference.ChatColor;
import PluginReference.MC_Command;
import PluginReference.MC_Location;
import PluginReference.MC_Player;
import PluginReference.MC_World;

public class HistoryCommand implements MC_Command {
	public void handleCommand(final MC_Player player, String[] args) {
		if (args.length >= 1) {
			if (args[0].equalsIgnoreCase("clear")) {
				new CommandHelper(player, args) {
					protected void run(MC_World world, MC_Location location, Object[] requested) {
						UndoPoint.clearAllUndoPoints();
						player.sendMessage("History cleared.");
					}

					protected int getArgumentType(int argumentNumber) {
						return -1;
					}

					protected int getArgumentLength() {
						return 0;
					}

					protected Object getDefault(int argumentNumber) {
						return null;
					}
				};
			} else
				player.sendMessage("Unknown history action! Use \"/history\" for help.");
		} else {
			player.sendMessage(ChatColor.DARK_AQUA + "------{ WorldSculpt History Help }------");
			player.sendMessage(ChatColor.WHITE + "/history clear");
			player.sendMessage(ChatColor.DARK_AQUA + "---------------");
		}
	}

	public boolean hasPermissionToUse(MC_Player player) {
		return (player != null ? player.isOp() : false);
	}

	public List<String> getAliases() {
		return Arrays.asList("h");
	}

	public String getCommandName() {
		return "history";
	}

	public String getHelpLine(MC_Player arg0) {
		return ChatColor.GOLD + "Use \"/history\" for help.";
	}

	public List<String> getTabCompletionList(MC_Player arg0, String[] arg1) {
		return new ArrayList<>();
	}
}