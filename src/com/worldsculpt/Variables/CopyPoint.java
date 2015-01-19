package com.worldsculpt.Variables;

import java.io.File;

import com.worldsculpt.Edits.Complex.MultiBlockSchematic;

import PluginReference.MC_Block;

public class CopyPoint {
	private int id;
	private File file;
	private int baseX, baseY, baseZ;
	private MultiBlockSchematic schematic;

	private CopyPoint(File file, int id, int baseX, int baseY, int baseZ) {
		this.file = file;
		this.id = id;
		this.baseX = baseX;
		this.baseY = baseY;
		this.baseZ = baseZ;
		schematic = new MultiBlockSchematic();
	}

	public int getId() {
		return id;
	}

	public void addBlockChange(MC_Block block, int x, int y, int z) {
		schematic.addBlock(block.getId(), block.getSubtype(), 1, x - baseX, y - baseY, z - baseZ);
	}

	public void save() {
		schematic.save(file);
	}

	public void load() {
		schematic.load(file);
	}

	public static CopyPoint createCopyPoint(int baseX, int baseY, int baseZ) {
		File copyPoints = new File("plugins_mod" + File.separatorChar + "WorldSculpt" + File.separatorChar + "Copy Points");
		copyPoints.mkdirs();
		File file;
		for (int i = 1; true; i++) {
			file = new File(copyPoints, i + ".dat");
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (Exception exception) {
					exception.printStackTrace();
				}
				return new CopyPoint(file, i, baseX, baseY, baseZ);
			}
		}
	}

	public static void clearAllCopyPoints() {
		File copyPoints = new File("plugins_mod" + File.separatorChar + "WorldSculpt" + File.separatorChar + "Copy Points");
		copyPoints.mkdirs();
		for (File file : copyPoints.listFiles())
			file.delete();
	}

	public static void removeCopyPoint(int editNumber) {
		File copyPoints = new File("plugins_mod" + File.separatorChar + "WorldSculpt" + File.separatorChar + "Copy Points");
		copyPoints.mkdirs();
		File file = new File(copyPoints, editNumber + ".dat");
		if (file.exists())
			file.delete();
	}

	public static int getLastCopyPoint() {
		File copyPoints = new File("plugins_mod" + File.separatorChar + "WorldSculpt" + File.separatorChar + "Copy Points");
		copyPoints.mkdirs();
		int i = 0;
		File file;
		while (true) {
			file = new File(copyPoints, (i + 1) + ".dat");
			if (file.exists())
				i++;
			else
				break;
		}
		return i;
	}
}