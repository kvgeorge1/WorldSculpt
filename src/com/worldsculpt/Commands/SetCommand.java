package com.worldsculpt.Commands;

import java.util.ArrayList;
import java.util.List;

import com.worldsculpt.Edits.Set;
import com.worldsculpt.Variables.ChiselLocations;
import com.worldsculpt.Variables.CommandHelper;

import PluginReference.BlockHelper;
import PluginReference.ChatColor;
import PluginReference.MC_Block;
import PluginReference.MC_BlockType;
import PluginReference.MC_Command;
import PluginReference.MC_Location;
import PluginReference.MC_Player;
import PluginReference.MC_World;
import WorldSculpt.MyPlugin;

public class SetCommand implements MC_Command {
	public void handleCommand(MC_Player player, String[] args) {
		final ChiselLocations chisel = MyPlugin.getChiselLocations(player.getName());
		if (!(chisel.setPoint1 && chisel.setPoint2)) {
			player.sendMessage(ChatColor.RED
					+ "You must set both chisel locations in order to use chisel commands! To set chisel locations, left click a block with a "
					+ BlockHelper.getBlockFriendlyName(WandCommand.WAND, BlockHelper.getBlockSubtype_FromKey(WandCommand.WAND)) + " to set point one. Right click a block with a "
					+ BlockHelper.getBlockFriendlyName(WandCommand.WAND, BlockHelper.getBlockSubtype_FromKey(WandCommand.WAND)) + " to set point 2.");
			return;
		}
		String[] a = new String[args.length + 1];
		for (int i = 0; i < args.length; i++)
			a[i + 1] = args[i];
		new CommandHelper(player, a) {
			protected void run(MC_World world, MC_Location location, Object[] requested) {
				new Set().run(world, chisel, (MC_Block) requested[0], (boolean) requested[1]);
			}

			protected Object getDefault(int argumentNumber) {
				if (argumentNumber == 0)
					return MyPlugin.server.getBlockFromName(BlockHelper.getBlockName(MC_BlockType.STONE));
				if (argumentNumber == 1)
					return true;
				return null;
			}

			protected int getArgumentType(int argumentNumber) {
				if (argumentNumber == 0)
					return CommandHelper.BLOCK_NAME;
				if (argumentNumber == 1)
					return CommandHelper.BOOLEAN;
				return -1;
			}

			protected int getArgumentLength() {
				return 2;
			}
		};
	}

	public List<String> getAliases() {
		return new ArrayList<String>();
	}

	public String getCommandName() {
		return "set";
	}

	public String getHelpLine(MC_Player plr) {
		return ChatColor.GOLD + "/set [block(name)] [filled(true/false)]";
	}

	public List<String> getTabCompletionList(MC_Player plr, String[] args) {
		return new ArrayList<>();
	}

	public boolean hasPermissionToUse(MC_Player player) {
		return (player != null ? player.isOp() : false);
	}
}