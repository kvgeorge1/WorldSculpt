package com.worldsculpt.Edits;

import com.worldsculpt.Commands.WandCommand;
import com.worldsculpt.Variables.BlockChange;
import com.worldsculpt.Variables.ChiselLocations;
import com.worldsculpt.Variables.CopyPoint;
import com.worldsculpt.Variables.ImprovedMapEdit;

import PluginReference.BlockHelper;
import PluginReference.MC_Location;
import PluginReference.MC_Player;
import PluginReference.MC_World;
import WorldSculpt.MyPlugin;

public class Copy extends ImprovedMapEdit {
	private CopyPoint copyPoint;

	public void run(MC_World world, MC_Location location, MC_Player player, Object[] requested) {
		final ChiselLocations chisel = MyPlugin.getChiselLocations(player.getName());
		if (!(chisel.setPoint1 && chisel.setPoint2)) {
			player.sendMessage("You must set both chisel locations in order to use chisel commands! To set chisel locations, left click a block with a "
					+ BlockHelper.getBlockFriendlyName(WandCommand.WAND, BlockHelper.getBlockSubtype_FromKey(WandCommand.WAND)) + " to set point one. Right click a block with a "
					+ BlockHelper.getBlockFriendlyName(WandCommand.WAND, BlockHelper.getBlockSubtype_FromKey(WandCommand.WAND)) + " to set point 2.");
			return;
		}
		copyPoint = CopyPoint.createCopyPoint((int) location.x, (int) location.y, (int) location.z);
		broadcastMessage("Created copy point: " + copyPoint.getId());
		broadcastMessage("   Point of origin: (" + (int) location.x + ", " + (int) location.y + ", " + (int) location.z + ")");
		setup(world, false);
		setupBoxIterator(chisel.lx(), chisel.ly(), chisel.lz(), chisel.hx(), chisel.hy(), chisel.hz());
	}

	public boolean calculateNextBlock(BlockChange cal) {
		int[] i = getNextNextBoxIteration();
		if (i == null) {
			copyPoint.save();
			copyPoint = null;
			return true;
		}
		copyPoint.addBlockChange(world.getBlockAt(i[0], i[1], i[2]), i[0], i[1], i[2]);
		return false;
	}
}