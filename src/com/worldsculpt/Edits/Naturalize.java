package com.worldsculpt.Edits;

import java.util.ArrayList;

import com.worldsculpt.Variables.BlockChange;
import com.worldsculpt.Variables.CommandHelper;
import com.worldsculpt.Variables.CommandStarter;
import com.worldsculpt.Variables.ImprovedMapEdit;
import com.worldsculpt.Variables.Sculptable;

import PluginReference.MC_Block;
import PluginReference.MC_Location;
import PluginReference.MC_Player;
import PluginReference.MC_World;

public class Naturalize extends ImprovedMapEdit implements Sculptable {
	private boolean calculating;
	private ArrayList<int[]> stacks;
	private int stackPos, stackNum;
	private MC_Block grass, stone, dirt;
	private int baseY;
	private int radius;

	public void getCommandHelper(MC_Player player, String[] args, final CommandStarter run) {
		new CommandHelper(player, args) {
			protected void run(MC_World world, MC_Location location, Object[] requested) {
				run.run(requested);
			}

			protected Object getDefault(int argumentNumber) {
				return 5;
			}

			protected int getArgumentType(int argumentNumber) {
				return CommandHelper.INTEGER;
			}

			protected int getArgumentLength() {
				return 1;
			}
		};
	}

	public void run(MC_World world, MC_Location location, MC_Player player, Object[] requested) {
		radius = (int) requested[0];
		if (radius < 0) {
			player.sendMessage("Radius cannot be negative!");
			return;
		}
		calculating = true;
		stacks = new ArrayList<>();
		grass = world.getBlockFromName("grass");
		stone = world.getBlockFromName("stone");
		dirt = world.getBlockFromName("dirt");
		baseY = location.getBlockY() - radius;
		setupBoxIterator(location.getBlockX() - radius, -radius, location.getBlockZ() - radius, location.getBlockX() + radius,
				radius, location.getBlockZ() + radius);
		setup(world, true);
	}

	public boolean calculateNextBlock(BlockChange cal) {
		if (calculating) {
			int[] i = getNextNextBoxIteration();
			if (i == null) {
				calculating = false;
				return false;
			}
			MC_Block block = world.getBlockAt(i[0], (radius - i[1]) + baseY, i[2]);
			if (block.getId() == 1 || block.getId() == 2 || block.getId() == 3)
				addToStack(i[0], (radius - i[1]) + baseY, i[2]);
			return false;
		} else {
			if (stackNum >= stacks.size()) {
				stacks = null;
				return true;
			}
			int[] s = stacks.get(stackNum);
			if (stackPos > s[3]) {
				stackNum++;
				stackPos = 0;
				return false;
			}
			int x = s[0];
			int y = s[1] - stackPos;
			int z = s[2];
			int type = stackPos < s[4] ? 0 : 1;
			if (type == 1 && Math.random() < 0.02)
				type = 0;
			if (type == 0 && world.getBlockAt(x, y + 1, z).getId() == 0)
				type = 2;
			stackPos++;
			cal.x = x;
			cal.y = y;
			cal.z = z;
			if (type == 2)
				cal.block = grass;
			else if (type == 0)
				cal.block = dirt;
			else
				cal.block = stone;
			return false;
		}
	}

	private void addToStack(int x, int y, int z) {
		for (int[] s : stacks) {
			if (x == s[0] && z == s[2]) {
				if (s[1] - y == s[3] + 1) {
					s[3]++;
					return;
				}
			}
		}
		stacks.add(new int[] { x, y, z, 0, (int) (Math.random() * 4 + 3) });
	}

	public String getName() {
		return "naturalize";
	}

	public String getHelpLine() {
		return "/naturalize";
	}

	public Sculptable newInstance() {
		return new Naturalize();
	}

	public void leftClick(MC_Block block) {
	}
}