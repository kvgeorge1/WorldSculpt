package com.worldsculpt.Edits;

import com.worldsculpt.Variables.BlockChange;
import com.worldsculpt.Variables.ChiselLocations;
import com.worldsculpt.Variables.ImprovedMapEdit;

import PluginReference.MC_Block;
import PluginReference.MC_World;

public class Flip extends ImprovedMapEdit {
	private int axis;
	private MC_Block other;
	private boolean flip;
	private int otherX, otherY, otherZ;
	private int baseX, baseY, baseZ;
	private float mirror;

	public void run(MC_World world, ChiselLocations c, int axis) {
		this.axis = axis;
		flip = false;
		if (axis == 0)
			mirror = c.lengthX() / 2f;
		else if (axis == 1)
			mirror = c.lengthY() / 2f;
		else
			mirror = c.lengthZ() / 2f;
		clipBounds(c);
		setupBoxIterator(baseX = c.lx(), baseY = c.ly(), baseZ = c.lz(), c.hx(), c.hy(), c.hz());
		setup(world, true);
	}

	public boolean calculateNextBlock(BlockChange cal) {
		if (flip) {
			cal.x = otherX;
			cal.y = otherY;
			cal.z = otherZ;
			cal.block = other;
		} else {
			int[] i = getNextNextBoxIteration();
			if (i == null) {
				other = null;
				return true;
			}
			if (axis == 0 && i[0] == baseX + mirror)
				return false;
			if (axis == 1 && i[1] == baseY + mirror)
				return false;
			if (axis == 2 && i[2] == baseZ + mirror)
				return false;
			cal.x = i[0];
			cal.y = i[1];
			cal.z = i[2];
			other = world.getBlockAt(i[0], i[1], i[2]);
			cal.block = getOther(i[0], i[1], i[2]);
		}
		flip = !flip;
		return false;
	}

	private MC_Block getOther(int x, int y, int z) {
		otherX = x;
		otherY = y;
		otherZ = z;
		if (axis == 0)
			otherX += distanceFromMirror(otherX, baseX);
		else if (axis == 1)
			otherY += distanceFromMirror(otherY, baseY);
		else
			otherZ += distanceFromMirror(otherZ, baseZ);
		return world.getBlockAt(otherX, otherY, otherZ);
	}

	private int distanceFromMirror(int a, int base) {
		return (int) ((mirror - (a - base)) * 2) - 1;
	}

	private void clipBounds(ChiselLocations c) {
		int lx = c.lx();
		int ly = c.ly();
		int lz = c.lz();
		int hx = c.hx();
		int hy = c.hy();
		int hz = c.hz();
		if (axis == 0) {
			c.x2 = lx + (c.lengthX() / 2);
			c.y2 = hy;
			c.z2 = hz;
		} else if (axis == 1) {
			c.y2 = ly + (c.lengthY() / 2);
			c.z2 = hz;
			c.x2 = hx;
		} else {
			c.z2 = lz + (c.lengthZ() / 2);
			c.x2 = hx;
			c.y2 = hy;
		}
		c.x1 = lx;
		c.y1 = ly;
		c.z1 = lz;
	}
}