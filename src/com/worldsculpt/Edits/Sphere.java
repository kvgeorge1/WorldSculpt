package com.worldsculpt.Edits;

import com.worldsculpt.Variables.BlockChange;
import com.worldsculpt.Variables.CommandHelper;
import com.worldsculpt.Variables.CommandStarter;
import com.worldsculpt.Variables.ImprovedMapEdit;
import com.worldsculpt.Variables.Sculptable;

import PluginReference.MC_Block;
import PluginReference.MC_Location;
import PluginReference.MC_Player;
import PluginReference.MC_World;
import WorldSculpt.MyPlugin;

public class Sphere extends ImprovedMapEdit implements Sculptable {
	private boolean filled;
	private int radius;
	private int x, y, z;
	private MC_Block block;

	private void start(MC_World world, int x, int y, int z, int radius, boolean filled, MC_Block block) {
		setup(world, true);
		this.filled = filled;
		this.radius = radius;
		this.block = block;
		this.x = x;
		this.y = y;
		this.z = z;
		setupBoxIterator(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius);
	}

	public boolean calculateNextBlock(BlockChange cal) {
		int[] i = getNextNextBoxIteration();
		if (i == null) {
			block = null;
			return true;
		}
		int cx = i[0] - x;
		int cy = i[1] - y;
		int cz = i[2] - z;
		cal.x = i[0];
		cal.y = i[1];
		cal.z = i[2];
		if ((filled && Math.round(Math.sqrt(cx * cx + cy * cy + cz * cz)) <= radius)
				|| Math.round(Math.sqrt(cx * cx + cy * cy + cz * cz)) == radius)
			cal.block = block;
		return false;
	}

	public void getCommandHelper(MC_Player player, String[] args, final CommandStarter run) {
		new CommandHelper(player, args) {
			protected int getArgumentType(int argumentNumber) {
				if (argumentNumber == 0)
					return CommandHelper.BLOCK_NAME;
				if (argumentNumber == 1)
					return CommandHelper.INTEGER;
				if (argumentNumber == 2)
					return CommandHelper.BOOLEAN;
				return -1;
			}

			protected Object getDefault(int argumentNumber) {
				if (argumentNumber == 0)
					return MyPlugin.server.getBlockFromName("stone");
				if (argumentNumber == 1)
					return 3;
				if (argumentNumber == 2)
					return true;
				return null;
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
		start(world, location.getBlockX(), location.getBlockY(), location.getBlockZ(), (int) requested[1], (boolean) requested[2],
				(MC_Block) requested[0]);
	}

	public String getName() {
		return "sphere";
	}

	public String getHelpLine() {
		return "/sphere [block(name)] [radius(#)] [filled(true/false)]";
	}

	public Sculptable newInstance() {
		return new Sphere();
	}

	public void leftClick(MC_Block block) {
	}
}