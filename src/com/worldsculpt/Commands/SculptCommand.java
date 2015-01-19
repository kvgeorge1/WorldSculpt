package com.worldsculpt.Commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.worldsculpt.Variables.CommandHelper;
import com.worldsculpt.Variables.CopyPoint;

import PluginReference.ChatColor;
import PluginReference.MC_Command;
import PluginReference.MC_Location;
import PluginReference.MC_Player;
import PluginReference.MC_World;

public class SculptCommand implements MC_Command {
	public void handleCommand(final MC_Player player, String[] args) {
		if (args.length >= 1) {
			if (args[0].equalsIgnoreCase("copyclear")) {
				new CommandHelper(player, args) {
					protected void run(MC_World world, MC_Location location, Object[] requested) {
						CopyPoint.clearAllCopyPoints();
						player.sendMessage(ChatColor.DARK_PURPLE + "Copy points cleared.");
					}

					protected int getArgumentType(int argumentNumber) {
						return -1;
					}

					protected int getArgumentLength() {
						return 0;
					}

					protected Object getDefault(int argumentNumber) {
						return null;
					}
				};
			} else if (args[0].equalsIgnoreCase("copyremove")) {
				new CommandHelper(player, args) {
					protected void run(MC_World world, MC_Location location, Object[] requested) {
						CopyPoint.removeCopyPoint((int) requested[0]);
						player.sendMessage(ChatColor.DARK_PURPLE + "Copy points removed.");
					}

					protected int getArgumentType(int argumentNumber) {
						return CommandHelper.INTEGER;
					}

					protected int getArgumentLength() {
						return 1;
					}

					protected Object getDefault(int argumentNumber) {
						return CopyPoint.getLastCopyPoint();
					}
				};
			} else
				player.sendMessage(ChatColor.RED + "Unknown sculpt shape! Use \"/scuplt\" for help.");
			// if(args[0].equalsIgnoreCase("mesh")){
			// new CommandHelper(player, args){
			// protected int getArgumentType(int argumentNumber){
			// if(argumentNumber==0)return CommandHelper.BLOCK_NAME;
			// if(argumentNumber==1)return CommandHelper.MESH_FILE;
			// if(argumentNumber==2)return CommandHelper.FLOAT;
			// return -1;
			// }
			// protected Object getDefault(int argumentNumber){
			// if(argumentNumber==0)return
			// MyPlugin.server.getBlockFromName("stone");
			// if(argumentNumber==1)return "null";
			// if(argumentNumber==2)return 1f;
			// return null;
			// }
			// protected void run(MC_World world, MC_Location location, Object[]
			// requested){ PrintMeshWandEdit.findMeshBounds((File)requested[1],
			// (float)requested[2], location.getBlockX(), location.getBlockY(),
			// location.getBlockZ(), world, (MC_Block)requested[0]); }
			// protected int getArgumentLength(){ return 3; }
			// };
			// }else if(args[0].equalsIgnoreCase("mesh2")){
			// new CommandHelper(player, args){
			// protected int getArgumentType(int argumentNumber){
			// if(argumentNumber==0)return CommandHelper.BLOCK_NAME;
			// if(argumentNumber==1)return CommandHelper.MESH_FILE;
			// if(argumentNumber==2)return CommandHelper.FLOAT;
			// return -1;
			// }
			// protected Object getDefault(int argumentNumber){
			// if(argumentNumber==0)return
			// MyPlugin.server.getBlockFromName("stone");
			// if(argumentNumber==1)return "null";
			// if(argumentNumber==2)return 1f;
			// return null;
			// }
			// protected void run(MC_World world, MC_Location location, Object[]
			// requested){ ImprovedPrintMesh.findMeshBounds((File)requested[1],
			// (float)requested[2], location.getBlockX(), location.getBlockY(),
			// location.getBlockZ(), world, (MC_Block)requested[0]); }
			// protected int getArgumentLength(){ return 3; }
			// };
			// }
		} else {
			player.sendMessage(ChatColor.DARK_AQUA + "------{ WorldSculpt Help }------");
			player.sendMessage(ChatColor.WHITE + "/scuplt copyclear");
			player.sendMessage(ChatColor.GRAY + "/scuplt copyremove [edit #]");
			player.sendMessage(ChatColor.DARK_AQUA + "---------------");
		}
	}

	public boolean hasPermissionToUse(MC_Player player) {
		return (player != null ? player.isOp() : false);
	}

	public List<String> getAliases() {
		return Arrays.asList("s");
	}

	public String getCommandName() {
		return "sculpt";
	}

	public String getHelpLine(MC_Player arg0) {
		return ChatColor.GOLD + "Use \"/scuplt\" for help.";
	}

	public List<String> getTabCompletionList(MC_Player arg0, String[] arg1) {
		return new ArrayList<>();
	}
}
