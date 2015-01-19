package com.worldsculpt.Variables;

public class Triangle {
	private int checks = -1;
	public float x1, x2, x3, y1, y2, y3, z1, z2, z3;

	public int getChecks() {
		if (checks == -1)
			checks = (int) (Math.round(Math.max(
					Math.max(Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2) + Math.pow(z1 - z2, 2)),
							Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2) + Math.pow(z1 - z2, 2))),
					Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2) + Math.pow(z1 - z2, 2)))) * 3);
		return checks;
	}

	public boolean isPointInAABB(int x, int y, int z) {
		return lowX() <= x && x <= highX() && lowY() <= y && y <= highY() && lowZ() <= z && z <= highZ();
	}

	public int lowX() {
		return (int) Math.floor(Math.min(Math.min(x1, x2), x3));
	}

	public int lowY() {
		return (int) Math.floor(Math.min(Math.min(y1, y2), y3));
	}

	public int lowZ() {
		return (int) Math.floor(Math.min(Math.min(z1, z2), z3));
	}

	public int highX() {
		return (int) Math.ceil(Math.max(Math.max(x1, x2), x3));
	}

	public int highY() {
		return (int) Math.ceil(Math.max(Math.max(y1, y2), y3));
	}

	public int highZ() {
		return (int) Math.ceil(Math.max(Math.max(z1, z2), z3));
	}
}