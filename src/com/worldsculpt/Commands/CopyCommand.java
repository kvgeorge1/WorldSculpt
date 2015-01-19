package com.worldsculpt.Commands;

import java.util.ArrayList;
import java.util.List;

import com.worldsculpt.Edits.Copy;
import com.worldsculpt.Variables.CommandHelper;

import PluginReference.ChatColor;
import PluginReference.MC_Command;
import PluginReference.MC_Location;
import PluginReference.MC_Player;
import PluginReference.MC_World;

public class CopyCommand implements MC_Command {
	public void handleCommand(final MC_Player player, String[] args) {
		String[] a = new String[args.length + 1];
		for (int i = 0; i < args.length; i++)
			a[i + 1] = args[i];
		new CommandHelper(player, a) {
			protected void run(MC_World world, MC_Location location, Object[] requested) {
				new Copy().run(player.getWorld(), player.getLocation(), player, requested);
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
	}

	public String getCommandName() {
		return "copy";
	}

	public String getHelpLine(MC_Player plr) {
		return ChatColor.GOLD + "/copy";
	}

	public List<String> getTabCompletionList(MC_Player plr, String[] args) {
		return new ArrayList<String>();
	}

	public boolean hasPermissionToUse(MC_Player player) {
		return (player != null ? player.isOp() : false);
	}

	public List<String> getAliases() {
		return new ArrayList<String>();
	}
}