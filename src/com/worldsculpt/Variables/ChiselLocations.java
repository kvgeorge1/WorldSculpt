package com.worldsculpt.Variables;

public class ChiselLocations {
	public int x1, y1, z1, x2, y2, z2;
	public boolean setPoint1, setPoint2;

	public int cubicSize() {
		int lx = Math.min(x1, x2);
		int ly = Math.min(y1, y2);
		int lz = Math.min(z1, z2);
		int hx = Math.max(x1, x2);
		int hy = Math.max(y1, y2);
		int hz = Math.max(z1, z2);
		return (hx - lx + 1) * (hy - ly + 1) * (hz - lz + 1);
	}

	public int lx() {
		return Math.min(x1, x2);
	}

	public int ly() {
		return Math.min(y1, y2);
	}

	public int lz() {
		return Math.min(z1, z2);
	}

	public int hx() {
		return Math.max(x1, x2);
	}

	public int hy() {
		return Math.max(y1, y2);
	}

	public int hz() {
		return Math.max(z1, z2);
	}

	public int lengthX() {
		return hx() - lx() + 1;
	}

	public int lengthY() {
		return hy() - ly() + 1;
	}

	public int lengthZ() {
		return hz() - lz() + 1;
	}
}