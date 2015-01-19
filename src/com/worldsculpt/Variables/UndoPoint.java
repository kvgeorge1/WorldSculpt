package com.worldsculpt.Variables;

import java.io.File;

import com.worldsculpt.Edits.Complex.MultiBlockSchematic;

import PluginReference.MC_Block;

public class UndoPoint {
	private int id;
	private File file;
	private MultiBlockSchematic schematic;

	private UndoPoint(File file, int id) {
		this.file = file;
		this.id = id;
		schematic = new MultiBlockSchematic();
	}

	public void addBlockChange(MC_Block o, MC_Block n, int x, int y, int z) {
		schematic.addBlock(o.getId(), o.getSubtype(), 1, x, y, z);
		schematic.addBlock(n.getId(), n.getSubtype(), 2, x, y, z);
	}

	public int getId() {
		return id;
	}

	public void save() {
		schematic.save(file);
	}

	public void load() {
		schematic.load(file);
	}

	public static UndoPoint createUndoPoint() {
		File undoPoints = new File("plugins_mod" + File.separatorChar + "WorldSculpt" + File.separatorChar + "Undo Points");
		undoPoints.mkdirs();
		File file;
		for (int i = 1; true; i++) {
			file = new File(undoPoints, i + ".dat");
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (Exception exception) {
					exception.printStackTrace();
				}
				return new UndoPoint(file, i);
			}
		}
	}

	public static void clearAllUndoPoints() {
		File undoPoints = new File("plugins_mod" + File.separatorChar + "WorldSculpt" + File.separatorChar + "Undo Points");
		undoPoints.mkdirs();
		for (File file : undoPoints.listFiles())
			file.delete();
	}

	public static int getLastUndoPoint() {
		File undoPoints = new File("plugins_mod" + File.separatorChar + "WorldSculpt" + File.separatorChar + "Undo Points");
		undoPoints.mkdirs();
		int i = 0;
		File file;
		while (true) {
			file = new File(undoPoints, (i + 1) + ".dat");
			if (file.exists())
				i++;
			else
				break;
		}
		return i;
	}
}