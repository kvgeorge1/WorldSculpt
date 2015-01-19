package com.worldsculpt.Variables;

import java.util.ArrayList;

import com.worldsculpt.Edits.Complex.UndoOutputStream;

import PluginReference.ChatColor;
import PluginReference.MC_Block;
import PluginReference.MC_Player;
import PluginReference.MC_World;
import WorldSculpt.MyPlugin;

public abstract class ImprovedMapEdit {
	private boolean calculating = true;
	protected MC_World world;
	private UndoOutputStream undoPoint;
	private int ly, lz, hx, hy, hz, ix, iy, iz;
	private int[] boxIterator;
	private int changed = 0;
	private long lastTickTime = -1;
	private long startTime;
	private int blocksPassed = 0;
	private static ArrayList<ImprovedMapEdit> workingEdits = new ArrayList<>();

	public void setup(MC_World world, boolean createUndoPoint) {
		this.world = world;
		if (createUndoPoint)
			undoPoint = UndoOutputStream.createUndoPoint();
		else
			undoPoint = null;
		workingEdits.add(this);
		calculating = true;
		changed = 0;
		lastTickTime = startTime = System.currentTimeMillis();
		blocksPassed = 0;
	}

	private int getAllocationTime() {
		int time = 50 - (int) (System.currentTimeMillis() - lastTickTime);
		lastTickTime = System.currentTimeMillis();
		return time;
	}

	private boolean update() {
		long time = System.currentTimeMillis();
		float over;
		if ((over = (time - startTime)) >= 5000) {
			over /= 1000;
			broadcastMessage(ChatColor.GRAY + "~" + (int) (blocksPassed / over) + " blocks per second.");
			startTime = System.currentTimeMillis();
			blocksPassed = 0;
		}
		BlockChange cal;
		MC_Block block;
		int allocatedTime = getAllocationTime();
		while (System.currentTimeMillis() - time < allocatedTime) {
			if (calculating) {
				calculating = !calculateNextBlock(cal = new BlockChange());
				if (cal.block != null) {
					blocksPassed++;
					block = world.getBlockAt(cal.x, cal.y, cal.z);
					if (block.getId() != cal.block.getId() || block.getSubtype() != cal.block.getSubtype()) {
						world.setBlockAt(cal.x, cal.y, cal.z, cal.block, cal.block.getSubtype());
						changed++;
					}
					if (undoPoint != null)
						undoPoint.addBlockChange(block, cal.block, cal.x, cal.y, cal.z);
				}
			} else {
				if (undoPoint != null)
					undoPoint.save();
				broadcastMessage("Edit complete. Changed " + changed + " block" + (changed == 1 ? "" : "s") + ".");
				if (undoPoint != null && changed > 0)
					broadcastMessage("Created undo point " + undoPoint.getId() + ".");
				return true;
			}
		}
		return false;
	}

	protected void broadcastMessage(String message) {
		for (MC_Player player : MyPlugin.server.getPlayers())
			if (player.isOp())
				player.sendMessage(message);
		System.out.println("[Wraithaven Sculpt] " + message);
	}

	public void setupBoxIterator(int lx, int ly, int lz, int hx, int hy, int hz) {
		ix = lx;
		this.ly = iy = ly;
		this.lz = iz = lz;
		this.hx = hx;
		this.hy = hy;
		this.hz = hz;
		boxIterator = new int[3];
	}

	public int[] getNextNextBoxIteration() {
		if (ix > hx)
			return null;
		boxIterator[0] = ix;
		boxIterator[1] = iy;
		boxIterator[2] = iz;
		iz++;
		if (iz > hz) {
			iz = lz;
			iy++;
			if (iy > hy) {
				iy = ly;
				ix++;
			}
		}
		return boxIterator;
	}

	public abstract boolean calculateNextBlock(BlockChange cal);

	public static void updateAllEdits() {
		if (workingEdits.size() == 0)
			return;
		if (workingEdits.get(0).update())
			workingEdits.remove(0);
	}
}