package com.worldsculpt.Edits.Complex;

import java.io.File;
import java.util.ArrayList;

import com.worldsculpt.Variables.BinaryFileUtil;

import PluginReference.MC_Block;

public class UndoOutputStream {
	private ArrayList<byte[]> toFeed = new ArrayList<>();
	byte[] t = new byte[4];
	private int id;
	private File file;

	private UndoOutputStream(File file, int id) {
		this.file = file;
		this.id = id;
	}

	public void addBlockChange(MC_Block o, MC_Block n, int x, int y, int z) {
		byte[] temp = new byte[21];
		temp[0] = -127;
		temp[1] = (byte) ((o.getId() >> 8) & 0xFF);
		temp[2] = (byte) (o.getId() & 0xFF);
		temp[3] = (byte) o.getSubtype();
		temp[4] = (byte) -128;
		temp[5] = (byte) ((n.getId() >> 8) & 0xFF);
		temp[6] = (byte) (n.getId() & 0xFF);
		temp[7] = (byte) n.getSubtype();
		temp[8] = (byte) -127;
		BinaryFileUtil.integerToByteArray(x, t);
		temp[9] = t[0];
		temp[10] = t[1];
		temp[11] = t[2];
		temp[12] = t[3];
		BinaryFileUtil.integerToByteArray(y, t);
		temp[13] = t[0];
		temp[14] = t[1];
		temp[15] = t[2];
		temp[16] = t[3];
		BinaryFileUtil.integerToByteArray(z, t);
		temp[17] = t[0];
		temp[18] = t[1];
		temp[19] = t[2];
		temp[20] = t[3];
		toFeed.add(temp);
	}

	public void save() {
		BinaryFileUtil.writeFile(file, toFeed.toArray(new byte[21][toFeed.size()]));
	}

	public int getId() {
		return id;
	}

	public static UndoOutputStream createUndoPoint() {
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
				return new UndoOutputStream(file, i);
			}
		}
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