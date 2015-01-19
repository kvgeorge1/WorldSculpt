package com.worldsculpt.Commands;

import java.util.ArrayList;
import java.util.List;

import PluginReference.BlockHelper;
import PluginReference.ChatColor;
import PluginReference.MC_Command;
import PluginReference.MC_ItemStack;
import PluginReference.MC_ItemType;
import PluginReference.MC_Player;
import WorldSculpt.MyPlugin;

public class WandCommand implements MC_Command {

	public static final int WAND = MC_ItemType.WOODEN_AXE;

	@Override
	public List<String> getAliases() {
		return new ArrayList<String>();
	}

	@Override
	public String getCommandName() {
		return "wand";
	}

	@Override
	public String getHelpLine(MC_Player arg0) {
		return ChatColor.GOLD + "/wand" + ChatColor.WHITE + " (gives you the 'wand' for making selections).";
	}

	@Override
	public List<String> getTabCompletionList(MC_Player arg0, String[] arg1) {
		return new ArrayList<String>();
	}

	@Override
	public void handleCommand(final MC_Player player, String[] args) {
		// Check to see if player is already holding it in their hand
		//
		if (player.getItemInHand().getId() == WAND) {
			player.sendMessage(ChatColor.GOLD + " You already have the select tool in your inventory! ["
					+ BlockHelper.getBlockFriendlyName(WAND, BlockHelper.getBlockSubtype_FromKey(WAND)) + "]");
			return;
		}

		// Check to see if user already has wand item in their inventory
		// somewhere
		//
		for (MC_ItemStack is : player.getInventory()) {
			if (is.getId() == WAND) {
				player.sendMessage(ChatColor.GOLD + " You already have the wand in your inventory! ["
						+ BlockHelper.getBlockFriendlyName(WandCommand.WAND, BlockHelper.getBlockSubtype_FromKey(WandCommand.WAND)) + "]");
				return;
			}
		}

		// Give User Wand
		//
		//MyPlugin.server.executeCommand("/give " + player.getName() + " " + WAND);
		MC_ItemStack is = MyPlugin.server.createItemStack(WAND, 1, BlockHelper.getBlockSubtype_FromKey(WandCommand.WAND));
		MyPlugin.server.executeCommand("/give " + player.getName() + " " + is.getOfficialName());
	}

	@Override
	public boolean hasPermissionToUse(MC_Player player) {
		return (player != null ? player.isOp() : false);
	}

}
