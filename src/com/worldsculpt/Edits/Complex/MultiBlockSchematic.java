package com.worldsculpt.Edits.Complex;

import java.io.File;
import java.util.ArrayList;

import com.worldsculpt.Variables.BinaryFileUtil;

public class MultiBlockSchematic {
	private ArrayList<MultiBlockLocation> locs = new ArrayList<>();

	public void load(File file) {
		locs.clear();
		byte[] data = BinaryFileUtil.readFile(file);
		int dataLeft = 0;
		byte[] buf = new byte[12];
		MultiBlockLocation loc = null;
		for (int i = 0; i < data.length; i++) {
			if (dataLeft == 0) {
				dataLeft = 4 * (data[i] + 129) + 12;
				loc = new MultiBlockLocation();
				locs.add(loc);
			} else {
				if (dataLeft > 12) {
					if ((dataLeft - 12) % 4 == 0)
						buf[0] = data[i];
					else if ((dataLeft - 12) % 4 == 3)
						buf[1] = data[i];
					else if ((dataLeft - 12) % 4 == 2)
						buf[2] = data[i];
					else {
						buf[3] = data[i];
						decodeBlock(loc, buf);
					}
				} else {
					buf[12 - dataLeft] = data[i];
					if (dataLeft == 1)
						decodeCoords(loc, buf);
				}
				dataLeft--;
			}
		}
	}

	public void save(File file) {
		byte[] data = null;
		byte[] temp;
		for (MultiBlockLocation loc : locs) {
			temp = new byte[1];
			temp[0] = (byte) (loc.blocks.size() - 129);
			for (int i = 0; i < loc.blocks.size(); i++)
				temp = BinaryFileUtil.combineArrays(temp, encodeBlock(loc.blocks.get(i)));
			temp = BinaryFileUtil.combineArrays(temp, encodeCoord(loc.cords));
			if (data == null)
				data = temp;
			else
				data = BinaryFileUtil.combineArrays(data, temp);
		}
		BinaryFileUtil.writeFile(file, data);
	}

	private void decodeBlock(MultiBlockLocation loc, byte[] block) {
		int[] a = new int[3];
		a[0] = block[1] | block[0] << 8;
		a[1] = block[2];
		a[2] = block[3] + 129;
		loc.blocks.add(a);
	}

	private byte[] encodeBlock(int[] a) {
		byte[] b = new byte[4];
		b[0] = (byte) ((a[0] >> 8) & 0xFF);
		b[1] = (byte) (a[0] & 0xFF);
		b[2] = (byte) a[1];
		b[3] = (byte) (a[2] - 129);
		return b;
	}

	private void decodeCoords(MultiBlockLocation loc, byte[] buf) {
		int x = BinaryFileUtil.byteArrayToInteger(new byte[] { buf[0], buf[1], buf[2], buf[3] });
		int y = BinaryFileUtil.byteArrayToInteger(new byte[] { buf[4], buf[5], buf[6], buf[7] });
		int z = BinaryFileUtil.byteArrayToInteger(new byte[] { buf[8], buf[9], buf[10], buf[11] });
		loc.cords = new int[] { x, y, z };
	}

	private byte[] encodeCoord(int[] a) {
		byte[] c = new byte[12];
		byte[] t = new byte[4];
		BinaryFileUtil.integerToByteArray(a[0], t);
		for (int i = 0; i < t.length; i++)
			c[i] = t[i];
		BinaryFileUtil.integerToByteArray(a[1], t);
		for (int i = 0; i < t.length; i++)
			c[i + 4] = t[i];
		BinaryFileUtil.integerToByteArray(a[2], t);
		for (int i = 0; i < t.length; i++)
			c[i + 8] = t[i];
		return c;
	}

	public void addBlock(int id, int data, int level, int x, int y, int z) {
		for (MultiBlockLocation loc : locs) {
			if (loc.cords[0] == x && loc.cords[1] == y && loc.cords[2] == z) {
				for (int[] c : loc.blocks) {
					if (c[2] == level) {
						c[0] = id;
						c[1] = data;
						return;
					}
				}
				loc.blocks.add(new int[] { id, data, level });
				return;
			}
		}
		MultiBlockLocation loc = new MultiBlockLocation();
		locs.add(loc);
		loc.cords[0] = x;
		loc.cords[1] = y;
		loc.cords[2] = z;
		loc.blocks.add(new int[] { id, data, level });
	}

	public int[] getBlock(int level, int x, int y, int z) {
		for (MultiBlockLocation loc : locs) {
			if (loc.cords[0] == x && loc.cords[1] == y && loc.cords[2] == z) {
				for (int[] b : loc.blocks)
					if (b[2] == level)
						return new int[] { b[0], b[1] };
				return null;
			}
		}
		return null;
	}

	public ArrayList<int[]> getAllBlocks(int level) {
		ArrayList<int[]> b = new ArrayList<>();
		for (MultiBlockLocation loc : locs) {
			for (int[] a : loc.blocks) {
				if (a[2] == level) {
					b.add(new int[] { a[0], a[1], loc.cords[0], loc.cords[1], loc.cords[2] });
					break;
				}
			}
		}
		return b;
	}

	private class MultiBlockLocation {
		private int[] cords = new int[3];
		private ArrayList<int[]> blocks = new ArrayList<>();
	}
}