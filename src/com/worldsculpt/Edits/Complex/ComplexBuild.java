package com.worldsculpt.Edits.Complex;

import java.util.ArrayList;

import com.worldsculpt.Variables.BlockChange;
import com.worldsculpt.Variables.ImprovedMapEdit;

import PluginReference.BlockHelper;
import PluginReference.MC_World;

public class ComplexBuild extends ImprovedMapEdit {
	private ArrayList<int[]> changes;
	private int pos;
	private int x, y, z;

	public ComplexBuild(MultiBlockSchematic schematic, int level, MC_World world, boolean createUndo, int offX, int offY, int offZ) {
		changes = schematic.getAllBlocks(level);
		x = offX;
		y = offY;
		z = offZ;
		setup(world, createUndo);
	}

	public boolean calculateNextBlock(BlockChange cal) {
		if (pos >= changes.size()) {
			changes = null;
			pos = 0;
			return true;
		}
		int[] i = changes.get(pos);
		pos++;
		cal.block = world.getBlockFromName(BlockHelper.getBlockName(i[0]));
		//cal.block.setSubtype(i[1]);
		cal.block.setSubtype(BlockHelper.getBlockSubtype_FromKey(cal.block.getId()));
		cal.x = i[2] + x;
		cal.y = i[3] + y;
		cal.z = i[4] + z;
		return false;
	}
}