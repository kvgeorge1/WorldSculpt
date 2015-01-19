package com.worldsculpt.Variables;

import java.io.File;

import PluginReference.MC_Block;
import PluginReference.MC_Location;
import PluginReference.MC_Player;
import PluginReference.MC_World;
import WorldSculpt.MyPlugin;

public abstract class CommandHelper {
	public static final int BLOCK_NAME = 0;
	public static final int INTEGER = 1;
	public static final int AXIS = 2;
	public static final int BOOLEAN = 3;
	public static final int FLOAT = 4;
	public static final int AXIS_DIRECTION = 5;
	public static final int MESH_FILE = 6;

	private void process(MC_Player player, String[] args) {
		if (args.length - 1 > getArgumentLength()) {
			player.sendMessage("Unknown number of arguments!");
			return;
		}
		Object[] requested = new Object[getArgumentLength()];
		for (int i = 1; i < args.length; i++) {
			int type = getArgumentType(i - 1);
			if (type == 0) {
				MC_Block block = MyPlugin.server.getBlockFromName(args[i]);
				if (block == null) {
					player.sendMessage("Block not found! (" + args[i] + ")");
					return;
				}
				requested[i - 1] = block;
			}
			if (type == 1) {
				try {
					requested[i - 1] = Integer.valueOf(args[i]);
				} catch (Exception exception) {
					try {
						Float.valueOf(args[i]);
						player.sendMessage("That's not an integer! (" + args[i] + ")");
					} catch (Exception exception1) {
						player.sendMessage("That's not a number! (" + args[i] + ")");
					}
					return;
				}
			}
			if (type == 2) {
				if (args[i].equalsIgnoreCase("x"))
					requested[i - 1] = 0;
				else if (args[i].equalsIgnoreCase("y"))
					requested[i - 1] = 1;
				else if (args[i].equalsIgnoreCase("z"))
					requested[i - 1] = 2;
				else {
					player.sendMessage("That's not an \"x\", \"y\", or \"z\" value! (" + args[i] + ")");
					return;
				}
			}
			if (type == 3) {
				if (args[i].equalsIgnoreCase("true"))
					requested[i - 1] = true;
				else if (args[i].equalsIgnoreCase("false"))
					requested[i - 1] = false;
				else {
					player.sendMessage("That's not a \"true\" or \"false\" value! (" + args[i] + ")");
					return;
				}
			}
			if (type == 4) {
				try {
					requested[i - 1] = Float.valueOf(args[i]);
				} catch (Exception exception) {
					player.sendMessage("That's not a number! (" + args[i] + ")");
					return;
				}
			}
			if (type == 5) {
				if (args[i].equalsIgnoreCase("x+"))
					requested[i - 1] = 0;
				else if (args[i].equalsIgnoreCase("y+"))
					requested[i - 1] = 1;
				else if (args[i].equalsIgnoreCase("z+"))
					requested[i - 1] = 2;
				else if (args[i].equalsIgnoreCase("x-"))
					requested[i - 1] = 3;
				else if (args[i].equalsIgnoreCase("y-"))
					requested[i - 1] = 4;
				else if (args[i].equalsIgnoreCase("z-"))
					requested[i - 1] = 5;
				else {
					player.sendMessage("That's not an \"x+\", \"y+\", \"z+\", \"x-\", \"y-\", or \"z-\" value! (" + args[i] + ")");
					return;
				}
			}
			if (type == 6) {
				File file = new File("plugins_mod" + File.separatorChar + "WorldSculpt" + File.separatorChar + "Meshes"
						+ File.separatorChar + args[i] + ".obj");
				if (file.exists())
					requested[i - 1] = file;
				else {
					player.sendMessage("File not found! (" + args[i] + ".obj)");
					return;
				}
			}
		}
		for (int i = args.length; i <= getArgumentLength(); i++)
			requested[i - 1] = getDefault(i - 1);

		run(player.getWorld(), player.getLocation(), requested);
	}

	public CommandHelper(MC_Player player, String[] args) {
		process(player, args);
	}

	protected abstract void run(MC_World world, MC_Location location, Object[] requested);

	protected abstract int getArgumentLength();

	protected abstract int getArgumentType(int argumentNumber);

	protected abstract Object getDefault(int argumentNumber);

	public static int axisDirectionToAxis(int a) {
		if (a == 0 || a == 3)
			return 0;
		if (a == 1 || a == 4)
			return 1;
		return 2;
	}
}