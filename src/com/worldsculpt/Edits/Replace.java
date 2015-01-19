package com.worldsculpt.Edits;

import com.worldsculpt.Variables.BlockChange;
import com.worldsculpt.Variables.CommandHelper;
import com.worldsculpt.Variables.CommandStarter;
import com.worldsculpt.Variables.ImprovedMapEdit;
import com.worldsculpt.Variables.Sculptable;

import PluginReference.ChatColor;
import PluginReference.MC_Block;
import PluginReference.MC_Location;
import PluginReference.MC_Player;
import PluginReference.MC_World;
import WorldSculpt.MyPlugin;

public class Replace extends ImprovedMapEdit implements Sculptable {
	private int radius;
	private MC_Block to;
	private MC_Block from;

	public void getCommandHelper(MC_Player player, String[] args, final CommandStarter run) {
		new CommandHelper(player, args) {
			protected Object getDefault(int argumentNumber) {
				if (argumentNumber == 0)
					return MyPlugin.server.getBlockFromName("stone");
				if (argumentNumber == 1)
					return 3;
				return null;
			}

			protected int getArgumentType(int argumentNumber) {
				if (argumentNumber == 1)
					return CommandHelper.INTEGER;
				return CommandHelper.BLOCK_NAME;
			}

			protected int getArgumentLength() {
				return 3;
			}

			protected void run(MC_World world, MC_Location location, Object[] requested) {
				run.run(requested);
			}
		};
	}

	public void run(MC_World world, MC_Location location, MC_Player player, Object[] requested) {
		radius = (int) requested[1];
		if (radius < 0) {
			player.sendMessage(ChatColor.RED + "The radius cannot be negative!");
			return;
		}
		to = (MC_Block) requested[0];
		if (requested[2] != null)
			from = (MC_Block) requested[2];
		setupBoxIterator(-radius + location.getBlockX(), -radius + location.getBlockY(), -radius + location.getBlockZ(), radius
				+ location.getBlockX(), radius + location.getBlockY(), radius + location.getBlockZ());
		setup(world, true);
	}

	public boolean calculateNextBlock(BlockChange cal) {
		int[] i = getNextNextBoxIteration();
		if (i == null) {
			to = null;
			from = null;
			return true;
		}
		cal.x = i[0];
		cal.y = i[1];
		cal.z = i[2];
		MC_Block old = world.getBlockAt(i[0], i[1], i[2]);
		if (from == null) {
			if (old.getId() != 0)
				cal.block = to;
		} else if (old.getId() == from.getId() && old.getSubtype() == from.getSubtype())
			cal.block = to;
		return false;
	}

	public String getName() {
		return "replace";
	}

	public String getHelpLine() {
		return "/replace [to (block)] [radius (#)] [from (block)]";
	}

	public Sculptable newInstance() {
		return new Replace();
	}

	public void leftClick(MC_Block block) {
	}
}