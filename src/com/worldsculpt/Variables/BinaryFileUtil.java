package com.worldsculpt.Variables;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class BinaryFileUtil {
	public static byte[] readFile(File file) {
		BufferedInputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(file));
			byte[] d = new byte[in.available()];
			in.read(d);
			return d;
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
		}
		return null;
	}

	public static void writeFile(File file, byte[]... data) {
		if (data == null)
			return;
		BufferedOutputStream out = null;
		try {
			out = new BufferedOutputStream(new FileOutputStream(file));
			for (byte[] d : data) {
				if(d != null)
					out.write(d);
				else
					break;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
		}
	}

	public static void integerToByteArray(int a, byte[] buf) {
		buf[0] = (byte) ((a >> 24) & 0xFF);
		buf[1] = (byte) ((a >> 16) & 0xFF);
		buf[2] = (byte) ((a >> 8) & 0xFF);
		buf[3] = (byte) (a & 0xFF);
	}

	public static void toBooleanArray(byte a, boolean[] buf) {
		buf[0] = (a & 128) == 128;
		buf[1] = (a & 64) == 64;
		buf[2] = (a & 32) == 32;
		buf[3] = (a & 16) == 16;
		buf[4] = (a & 8) == 8;
		buf[5] = (a & 4) == 4;
		buf[6] = (a & 2) == 2;
		buf[7] = (a & 1) == 1;
	}

	public static byte fromBooleanArray(boolean[] a) {
		byte b = 0;
		if (a[7])
			b += 1;
		if (a[6])
			b += 2;
		if (a[5])
			b += 4;
		if (a[4])
			b += 8;
		if (a[3])
			b += 16;
		if (a[2])
			b += 32;
		if (a[1])
			b += 64;
		if (a[0])
			b += 128;
		return b;
	}

	public static byte[] combineArrays(byte[] a, byte[] b) {
		byte[] c = new byte[a.length + b.length];
		for (int i = 0; i < a.length; i++)
			c[i] = a[i];
		for (int i = a.length; i < c.length; i++)
			c[i] = b[i - a.length];
		return c;
	}

	public static int byteArrayToInteger(byte[] b) {
		return b[3] & 0xFF | (b[2] & 0xFF) << 8 | (b[1] & 0xFF) << 16 | (b[0] & 0xFF) << 24;
	}
}