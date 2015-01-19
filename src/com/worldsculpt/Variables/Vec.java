package com.worldsculpt.Variables;

public class Vec {
	private float x, y, z;

	public Vec(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vec(Vec v) {
		x = v.x;
		y = v.y;
		z = v.z;
	}

	public void sub(float x, float y, float z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
	}

	public void cross(Vec v1, Vec v2) {
		x = (v1.y * v2.z) - (v1.y * v2.z);
		y = (v1.z * v2.x) - (v1.x * v2.z);
		z = (v1.x * v2.y) - (v1.y * v2.x);
	}

	public Vec() {
	}

	public float dot(Vec v) {
		return x * v.x + y * v.y + z * v.z;
	}

	public void sub(Vec v) {
		sub(v.x, v.y, v.z);
	}

	public float length() {
		return (float) Math.sqrt(x * x + y * y + z * z);
	}
}