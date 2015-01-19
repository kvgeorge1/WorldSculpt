package WorldSculpt;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import PluginReference.ChatColor;
import PluginReference.MC_EventInfo;
import PluginReference.MC_ItemStack;
import PluginReference.MC_Location;
import PluginReference.MC_Player;
import PluginReference.MC_Server;
import PluginReference.PluginBase;
import PluginReference.PluginInfo;

import com.worldsculpt.Commands.BrushCommand;
import com.worldsculpt.Commands.CopyCommand;
import com.worldsculpt.Commands.FlipCommand;
import com.worldsculpt.Commands.HistoryCommand;
import com.worldsculpt.Commands.LineCommand;
import com.worldsculpt.Commands.QuickSculptCommand;
import com.worldsculpt.Commands.RedoCommand;
import com.worldsculpt.Commands.SculptCommand;
import com.worldsculpt.Commands.SetCommand;
import com.worldsculpt.Commands.UndoCommand;
import com.worldsculpt.Commands.WandCommand;
import com.worldsculpt.Edits.Box;
import com.worldsculpt.Edits.Cylinder;
import com.worldsculpt.Edits.Naturalize;
import com.worldsculpt.Edits.Paste;
import com.worldsculpt.Edits.Replace;
import com.worldsculpt.Edits.Smooth;
import com.worldsculpt.Edits.Sphere;
import com.worldsculpt.Edits.Torus;
import com.worldsculpt.Variables.ChiselLocations;
import com.worldsculpt.Variables.CommandStarter;
import com.worldsculpt.Variables.ImprovedMapEdit;
import com.worldsculpt.Variables.PlayerTargetBlock;
import com.worldsculpt.Variables.Sculptable;

public class MyPlugin extends PluginBase {
	public static MC_Server server;
	private static long lastBrush = 0;
	private static final ConcurrentHashMap<String, ChiselLocations> chiselLocations = new ConcurrentHashMap<>();

	@Override
	public void onStartup(MC_Server server) {
		MyPlugin.server = server;
		server.registerCommand(new SculptCommand());
		server.registerCommand(new SetCommand());
		server.registerCommand(new HistoryCommand());
		server.registerCommand(new BrushCommand());
		server.registerCommand(new CopyCommand());
		server.registerCommand(new UndoCommand());
		server.registerCommand(new RedoCommand());
		server.registerCommand(new LineCommand());
		server.registerCommand(new FlipCommand());
		server.registerCommand(new WandCommand());
		addSculptCommand(new Sphere());
		addSculptCommand(new Cylinder());
		addSculptCommand(new Torus());
		addSculptCommand(new Box());
		addSculptCommand(new Paste());
		addSculptCommand(new Smooth());
		addSculptCommand(new Replace());
		addSculptCommand(new Naturalize());
		System.out.println("[WorldSculpt] activated!");
	}

	@Override
	public PluginInfo getPluginInfo() {
		PluginInfo info = new PluginInfo();
		info.name = "WorldSculpt";
		info.description = "A block sculpting plugin to aid in large structure builds (formerly Wraithaven Sculpt)";
		info.version = "v1.2";
		info.eventSortOrder = -10E100D;
		return info;
	}

	public void addSculptCommand(Sculptable s) {
		BrushCommand.s.add(s);
		server.registerCommand(new QuickSculptCommand(s));
	}

	@Override
	public void onInteracted(MC_Player player, MC_Location loc, MC_ItemStack isHandItem) {
		// Only execute if user is OPPED
		//
		if (player.isOp()) {
			// Is player holding minecraft:wooden_axe
			//
			if (isHandItem.getId() == WandCommand.WAND) {
				// Get location to set points for
				//
				ChiselLocations chisel;
				if (chiselLocations.containsKey(player.getName()))
					chisel = chiselLocations.get(player.getName());
				else
					chiselLocations.put(player.getName(), chisel = new ChiselLocations());

				chisel.setPoint2 = true;
				chisel.x2 = loc.getBlockX();
				chisel.y2 = loc.getBlockY();
				chisel.z2 = loc.getBlockZ();

				// Notify user of point set
				//
				player.sendMessage("Set chisel point 2. Location: (" + chisel.x2 + ", " + chisel.y2 + ", " + chisel.z2 + ")");
				if (chisel.setPoint1)
					player.sendMessage("Point 1: (" + chisel.x1 + ", " + chisel.y1 + ", " + chisel.z1 + ")");
				else
					player.sendMessage("Point 1: Not yet set.");
			}
		}
	}

	@Override
	public void onAttemptItemUse(final MC_Player player, MC_ItemStack is, MC_EventInfo ei) {
		if (player.isOp() && System.currentTimeMillis() - lastBrush >= 300 && is != null && is.getId() != 0) {
			String name = is.getCustomizedName();
			if (name != null) {
				if (name.startsWith(ChatColor.AQUA + "Brush: ")) {
					lastBrush = System.currentTimeMillis();
					name = ChatColor.StripColor(name);
					name = name.substring(7);
					for (Sculptable s : BrushCommand.s) {
						if (name.startsWith(s.getName().substring(0, 1).toUpperCase() + s.getName().substring(1).toLowerCase())) {
							name = name.substring(s.getName().length());
							if (name.startsWith(" "))
								name = name.substring(1);
							final int[] i = PlayerTargetBlock.getTargetBlock(player, 150, null);
							final Sculptable sc = s;
							String[] parts = name.isEmpty() ? new String[0] : name.split(" ");
							String[] cmd = new String[parts.length + 1];
							for (int j = 1; j < cmd.length; j++)
								cmd[j] = parts[j - 1];
							s.getCommandHelper(player, cmd, new CommandStarter() {
								public void run(Object[] requested) {
									sc.newInstance()
											.run(player.getWorld(), new MC_Location(i[0], i[1], i[2], 0), player, requested);
								}
							});
							return;
						}
					}
				}
			}
		}
	}

	@Override
	public void onAttemptBlockBreak(MC_Player player, MC_Location loc, MC_EventInfo ei) {
		if (player.isOp()) {
			if (player.getItemInHand() != null && player.getItemInHand().getId() == WandCommand.WAND) {
				ChiselLocations chisel;
				if (chiselLocations.containsKey(player.getName()))
					chisel = chiselLocations.get(player.getName());
				else
					chiselLocations.put(player.getName(), chisel = new ChiselLocations());
				chisel.setPoint1 = true;
				chisel.x1 = loc.getBlockX();
				chisel.y1 = loc.getBlockY();
				chisel.z1 = loc.getBlockZ();
				player.sendMessage("Set chisel point 1. Location: (" + chisel.x1 + ", " + chisel.y1 + ", " + chisel.z1 + ")");
				if (chisel.setPoint2)
					player.sendMessage("Point 2: (" + chisel.x2 + ", " + chisel.y2 + ", " + chisel.z2 + ")");
				else
					player.sendMessage("Point 2: Not yet set.");
				ei.isCancelled = true;
			}
		}
	}

	@Override
	public void onTick(int tickNumber) {
		ImprovedMapEdit.updateAllEdits();
		// PrintMeshWandEdit.MeshStats.updateMeshVoxelizers();
		// ImprovedPrintMesh.MeshStats.updateMeshVoxelizers();
	}

	@Override
	public void onPlayerLogout(String playerName, UUID uuid) {
		chiselLocations.remove(playerName);
	}

	@Override
	public void onShutdown() {
		System.out.println("[WorldSculpt] deactivated!");
	}

	public static ChiselLocations getChiselLocations(String player) {
		if (chiselLocations.containsKey(player))
			return chiselLocations.get(player);
		else {
			ChiselLocations chisel = new ChiselLocations();
			chiselLocations.put(player, chisel);
			return chisel;
		}
	}
}