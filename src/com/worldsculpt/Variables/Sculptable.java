package com.worldsculpt.Variables;

import PluginReference.MC_Block;
import PluginReference.MC_Location;
import PluginReference.MC_Player;
import PluginReference.MC_World;

public interface Sculptable {
	public void getCommandHelper(MC_Player player, String[] args, CommandStarter run);

	public void run(MC_World world, MC_Location location, MC_Player player, Object[] requested);

	public String getName();

	public String getHelpLine();

	public Sculptable newInstance();

	public void leftClick(MC_Block block);
}