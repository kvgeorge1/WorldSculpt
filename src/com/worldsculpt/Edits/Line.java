package com.worldsculpt.Edits;

import com.worldsculpt.Variables.BlockChange;
import com.worldsculpt.Variables.BlockIterator;
import com.worldsculpt.Variables.ChiselLocations;
import com.worldsculpt.Variables.ImprovedMapEdit;

import PluginReference.MC_Block;
import PluginReference.MC_World;

public class Line extends ImprovedMapEdit {
	private BlockIterator itr;
	private MC_Block block;

	public void run(MC_World world, ChiselLocations chisel, MC_Block mc_Block) {
		setup(world, true);
		itr = new BlockIterator(chisel.x1, chisel.y1, chisel.z1, chisel.x2, chisel.y2, chisel.z2);
		block = mc_Block;
	}

	public boolean calculateNextBlock(BlockChange cal) {
		int[] i = itr.getNext();
		if (i == null) {
			itr = null;
			block = null;
			return true;
		}
		cal.x = i[0];
		cal.y = i[1];
		cal.z = i[2];
		cal.block = block;
		return false;
	}
}
