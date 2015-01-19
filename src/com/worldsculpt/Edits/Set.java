package com.worldsculpt.Edits;

import com.worldsculpt.Variables.BlockChange;
import com.worldsculpt.Variables.ChiselLocations;
import com.worldsculpt.Variables.ImprovedMapEdit;

import PluginReference.MC_Block;
import PluginReference.MC_World;

public class Set extends ImprovedMapEdit {
	private boolean filled;
	private MC_Block block;
	private int lx, ly, lz, hx, hy, hz;

	public void run(MC_World world, ChiselLocations chisel, MC_Block block, boolean filled) {
		this.filled = filled;
		this.block = block;
		setupBoxIterator(lx = chisel.lx(), ly = chisel.ly(), lz = chisel.lz(), hx = chisel.hx(), hy = chisel.hy(), hz = chisel.hz());
		setup(world, true);
	}

	public boolean calculateNextBlock(BlockChange cal) {
		int[] i = getNextNextBoxIteration();
		if (i == null) {
			block = null;
			return true;
		}
		cal.x = i[0];
		cal.y = i[1];
		cal.z = i[2];
		if (!filled) {
			if (lx == i[0] || i[0] == hx || ly == i[1] || i[1] == hy || lz == i[2] || i[2] == hz)
				cal.block = block;
		} else
			cal.block = block;
		return false;
	}
}