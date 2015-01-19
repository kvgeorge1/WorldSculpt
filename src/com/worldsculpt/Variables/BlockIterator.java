package com.worldsculpt.Variables;

import java.util.ArrayList;

public class BlockIterator {
	private int pos = 0;
	private ArrayList<int[]> blocks = new ArrayList<>();

	private void raytrace(double x0, double y0, double z0, double x1, double y1, double z1) {
		double dx = Math.abs(x1 - x0);
		double dy = Math.abs(y1 - y0);
		double dz = Math.abs(z1 - z0);
		int x = (int) Math.floor(x0);
		int y = (int) Math.floor(y0);
		int z = (int) Math.floor(z0);
		double dt_dx = 1.0 / dx;
		double dt_dy = 1.0 / dy;
		double dt_dz = 1.0 / dz;
		int n = 1;
		int x_inc, y_inc, z_inc;
		double t_next_vertical, t_next_horizontal, t_next_outward;
		if (dx == 0) {
			x_inc = 0;
			t_next_horizontal = dt_dx;
		} else if (x1 > x0) {
			x_inc = 1;
			n += (int) Math.floor(x1) - x;
			t_next_horizontal = (Math.floor(x0) + 1 - x0) * dt_dx;
		} else {
			x_inc = -1;
			n += x - (int) Math.floor(x1);
			t_next_horizontal = (x0 - Math.floor(x0)) * dt_dx;
		}
		if (dy == 0) {
			y_inc = 0;
			t_next_vertical = dt_dy;
		} else if (y1 > y0) {
			y_inc = 1;
			n += (int) Math.floor(y1) - y;
			t_next_vertical = (Math.floor(y0) + 1 - y0) * dt_dy;
		} else {
			y_inc = -1;
			n += y - (int) Math.floor(y1);
			t_next_vertical = (y0 - Math.floor(y0)) * dt_dy;
		}
		if (dz == 0) {
			z_inc = 0;
			t_next_outward = dt_dz;
		} else if (z1 > z0) {
			z_inc = 1;
			n += (int) Math.floor(z1) - z;
			t_next_outward = (Math.floor(z0) + 1 - z0) * dt_dz;
		} else {
			z_inc = -1;
			n += z - (int) Math.floor(z1);
			t_next_outward = (z0 - Math.floor(z0)) * dt_dz;
		}
		for (; n > 0; --n) {
			blocks.add(new int[] { x, y, z });
			if (t_next_vertical <= t_next_horizontal && t_next_vertical <= t_next_outward) {
				y += y_inc;
				t_next_vertical += dt_dy;
			} else if (t_next_horizontal <= t_next_vertical && t_next_horizontal <= t_next_outward) {
				x += x_inc;
				t_next_horizontal += dt_dx;
			} else {
				z += z_inc;
				t_next_outward += dt_dz;
			}
		}
	}

	public int[] getNext() {
		if (pos >= blocks.size())
			return null;
		int[] i = blocks.get(pos);
		pos++;
		return i;
	}

	public BlockIterator(double x0, double y0, double z0, double x1, double y1, double z1) {
		raytrace(x0, y0, z0, x1, y1, z1);
	}

	public int getPos() {
		return pos;
	}

	public int getSize() {
		return blocks.size();
	}

	public void reset() {
		pos = 0;
	}
}