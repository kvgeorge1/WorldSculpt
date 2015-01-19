package com.worldsculpt.Edits;

import java.io.File;

import com.worldsculpt.Edits.Complex.ComplexBuild;
import com.worldsculpt.Edits.Complex.MultiBlockSchematic;
import com.worldsculpt.Variables.CommandHelper;
import com.worldsculpt.Variables.CommandStarter;
import com.worldsculpt.Variables.CopyPoint;
import com.worldsculpt.Variables.Sculptable;

import PluginReference.MC_Block;
import PluginReference.MC_Location;
import PluginReference.MC_Player;
import PluginReference.MC_World;

public class Paste implements Sculptable {
	public void getCommandHelper(MC_Player player, String[] args, final CommandStarter run) {
		new CommandHelper(player, args) {
			protected int getArgumentType(int argumentNumber) {
				return CommandHelper.INTEGER;
			}

			protected int getArgumentLength() {
				return 1;
			}

			protected void run(MC_World world, MC_Location location, Object[] requested) {
				run.run(requested);
			}

			protected Object getDefault(int argumentNumber) {
				return CopyPoint.getLastCopyPoint();
			}
		};
	}

	public void run(MC_World world, MC_Location location, MC_Player player, Object[] requested) {
		File file = new File("plugins_mod" + File.separatorChar + "WraithavenSculpt" + File.separatorChar + "Copy Points"
				+ File.separatorChar + requested[0] + ".dat");
		if (!file.exists()) {
			player.sendMessage("Copy point not found!");
			player.sendMessage(file.getName());
			return;
		}
		MultiBlockSchematic schematic = new MultiBlockSchematic();
		schematic.load(file);
		new ComplexBuild(schematic, 1, world, true, (int) location.x, (int) location.y, (int) location.z);
	}

	public String getName() {
		return "paste";
	}

	public String getHelpLine() {
		return "/paste [edit #]";
	}

	public Sculptable newInstance() {
		return new Paste();
	}

	public void leftClick(MC_Block block) {
	}
}