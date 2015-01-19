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

public class Smooth extends ImprovedMapEdit implements Sculptable {
	private boolean creatingHeightMap;
	private int[][] bounds;
	private int[][] lowMap;
	private boolean checkLowMap;
	private MC_Block[][] blocks;
	private int iterations;
	private int radius;
	private int heightMapFinderX, heightMapFinderY, heightMapFinderZ;
	private int baseX, baseY, baseZ;
	private int passes = 0;
	private boolean iterating;
	private boolean placing;
	private MC_Block air;

	public void getCommandHelper(MC_Player player, String[] args, final CommandStarter run) {
		new CommandHelper(player, args) {
			protected Object getDefault(int argumentNumber) {
				if (argumentNumber == 2)
					return true;
				return 4;
			}

			protected int getArgumentType(int argumentNumber) {
				if (argumentNumber == 2)
					return CommandHelper.BOOLEAN;
				return CommandHelper.INTEGER;
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
		creatingHeightMap = true;
		radius = (int) requested[0];
		bounds = new int[radius * 2 + 1][radius * 2 + 1];
		lowMap = new int[radius * 2 + 1][radius * 2 + 1];
		blocks = new MC_Block[radius * 2 + 1][radius * 2 + 1];
		for (int a = 0; a < radius * 2 + 1; a++)
			for (int b = 0; b < radius * 2 + 1; b++)
				bounds[a][b] = -radius;
		for (int a = 0; a < radius * 2 + 1; a++)
			for (int b = 0; b < radius * 2 + 1; b++)
				lowMap[a][b] = radius + 1;
		iterations = (int) requested[1];
		checkLowMap = (boolean) requested[2];
		heightMapFinderX = heightMapFinderY = heightMapFinderZ = -radius;
		baseX = location.getBlockX();
		baseY = location.getBlockY();
		baseZ = location.getBlockZ();
		setup(world, true);
	}

	public boolean calculateNextBlock(BlockChange cal) {
		if (creatingHeightMap) {
			MC_Block block;
			if ((block = world.getBlockAt(heightMapFinderX + baseX, heightMapFinderY + baseY, heightMapFinderZ + baseZ)).getId() != 0) {
				bounds[heightMapFinderX + radius][heightMapFinderZ + radius] = heightMapFinderY;
				if (block.isSolid()) {
					blocks[heightMapFinderX + radius][heightMapFinderZ + radius] = block;
					if (checkLowMap && lowMap[heightMapFinderX + radius][heightMapFinderZ + radius] > heightMapFinderY)
						lowMap[heightMapFinderX + radius][heightMapFinderZ + radius] = heightMapFinderY;
				}
			} else if (blocks[heightMapFinderX + radius][heightMapFinderZ + radius] == null)
				blocks[heightMapFinderX + radius][heightMapFinderZ + radius] = block;
			heightMapFinderZ++;
			if (heightMapFinderZ > radius) {
				heightMapFinderZ = -radius;
				heightMapFinderY++;
				if (heightMapFinderY > radius) {
					heightMapFinderY = -radius;
					heightMapFinderX++;
					if (heightMapFinderX > radius)
						creatingHeightMap = false;
				}
			}
			return false;
		} else {
			if (iterating || passes < iterations) {
				if (iterating) {
					int[] i = getNextNextBoxIteration();
					if (i == null) {
						iterating = false;
						return false;
					}
					boolean small = i[0] == -radius + 1 || i[0] == radius - 1 || i[2] == -radius + 1 || i[2] == radius - 1;
					int[] s = new int[small ? 9 : 25];
					int pos = 0;
					for (int x = small ? -1 : -2; x <= (small ? 1 : 2); x++) {
						for (int z = small ? -1 : -2; z <= (small ? 1 : 2); z++) {
							s[pos] = bounds[i[0] + x + radius][i[2] + z + radius];
							pos++;
						}
					}
					smoothGround(s, small ? 4 : 12);
					pos = 0;
					for (int x = small ? -1 : -2; x <= (small ? 1 : 2); x++) {
						for (int z = small ? -1 : -2; z <= (small ? 1 : 2); z++) {
							bounds[i[0] + x + radius][i[2] + z + radius] = s[pos];
							pos++;
						}
					}
				} else {
					passes++;
					iterating = true;
					setupBoxIterator(-radius + 1, -radius + 1, -radius + 1, radius - 1, radius - 1, radius - 1);
				}
				return false;
			} else {
				if (!placing) {
					air = world.getBlockFromName("air");
					placing = true;
					setupBoxIterator(-radius + baseX, -radius + baseY, -radius + baseZ, radius + baseX, radius + baseY, radius
							+ baseZ);
					return false;
				}
				int[] i = getNextNextBoxIteration();
				if (i == null) {
					creatingHeightMap = false;
					bounds = null;
					blocks = null;
					lowMap = null;
					iterations = 0;
					radius = 0;
					heightMapFinderX = heightMapFinderY = heightMapFinderZ = 0;
					baseX = baseY = baseZ = 0;
					passes = 0;
					iterating = false;
					placing = false;
					return true;
				}
				cal.x = i[0];
				cal.y = i[1];
				cal.z = i[2];
				boolean passesLowMap;
				if (checkLowMap) {
					if (lowMap[i[0] - baseX + radius][i[2] - baseZ + radius] <= i[1] - baseY)
						passesLowMap = true;
					else
						passesLowMap = false;
				} else
					passesLowMap = true;
				if (passesLowMap) {
					if (i[1] - baseY <= bounds[i[0] - baseX + radius][i[2] - baseZ + radius]) {
						if (!world.getBlockAt(i[0], i[1], i[2]).isSolid())
							cal.block = blocks[i[0] - baseX + radius][i[2] - baseZ + radius];
					} else
						cal.block = air;
				}
				return false;
			}
		}
	}

	private void smoothGround(int[] pass, int c) {
		double total = 0;
		for (int i = 0; i < pass.length; i++)
			total += pass[i];
		total /= pass.length;
		if (Math.abs(total - pass[c]) >= 0.5) {
			if (total > pass[c])
				pass[c]++;
			else
				pass[c]--;
		}
	}

	public String getName() {
		return "smooth";
	}

	public String getHelpLine() {
		return "/smooth [radius] [iterations]";
	}

	public Sculptable newInstance() {
		return new Smooth();
	}

	public void leftClick(MC_Block block) {
	}
}