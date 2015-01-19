package com.worldsculpt.Commands;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.worldsculpt.Edits.Complex.ComplexBuild;
import com.worldsculpt.Edits.Complex.MultiBlockSchematic;
import com.worldsculpt.Variables.CommandHelper;
import com.worldsculpt.Variables.UndoPoint;

import PluginReference.ChatColor;
import PluginReference.MC_Command;
import PluginReference.MC_Location;
import PluginReference.MC_Player;
import PluginReference.MC_World;

public class UndoCommand implements MC_Command {
	public void handleCommand(final MC_Player player, String[] args) {
		String[] a = new String[args.length + 1];
		for (int i = 0; i < args.length; i++)
			a[i + 1] = args[i];
		new CommandHelper(player, a) {
			protected void run(MC_World world, MC_Location location, Object[] requested) {
				File file = new File("plugins_mod" + File.separatorChar + "WorldSculpt" + File.separatorChar + "Undo Points"
						+ File.separatorChar + requested[0] + ".dat");
				if (!file.exists()) {
					player.sendMessage(ChatColor.RED + "Undo point not found!");
					player.sendMessage(file.getName());
					return;
				}
				MultiBlockSchematic schematic = new MultiBlockSchematic();
				schematic.load(file);
				new ComplexBuild(schematic, 1, world, false, 0, 0, 0);
			}

			protected int getArgumentType(int argumentNumber) {
				return CommandHelper.INTEGER;
			}

			protected int getArgumentLength() {
				return 1;
			}

			protected Object getDefault(int argumentNumber) {
				return UndoPoint.getLastUndoPoint();
			}
		};
	}

	public String getCommandName() {
		return "undo";
	}

	public String getHelpLine(MC_Player plr) {
		return ChatColor.GOLD + "/undo [edit #]";
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