package com.worldsculpt.Commands;

import java.util.ArrayList;
import java.util.List;

import com.worldsculpt.Variables.CommandStarter;
import com.worldsculpt.Variables.Sculptable;

import PluginReference.ChatColor;
import PluginReference.MC_Command;
import PluginReference.MC_Player;

public class QuickSculptCommand implements MC_Command {
	private Sculptable s;

	public void handleCommand(final MC_Player player, String[] args) {
		String[] a = new String[args.length + 1];
		for (int i = 0; i < args.length; i++)
			a[i + 1] = args[i];
		s.getCommandHelper(player, a, new CommandStarter() {
			public void run(Object[] requested) {
				s.newInstance().run(player.getWorld(), player.getLocation(), player, requested);
			}
		});
	}

	public QuickSculptCommand(Sculptable s) {
		this.s = s;
	}

	public boolean hasPermissionToUse(MC_Player player) {
		return (player != null ? player.isOp() : false);
	}

	public List<String> getTabCompletionList(MC_Player player, String[] args) {
		return new ArrayList<>();
	}

	public List<String> getAliases() {
		return new ArrayList<>();
	}

	public String getCommandName() {
		return s.getName();
	}

	public String getHelpLine(MC_Player player) {
		return ChatColor.GOLD + s.getHelpLine();
	}
}