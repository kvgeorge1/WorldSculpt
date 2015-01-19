package com.worldsculpt.Edits.Complex;

//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.util.ArrayList;
//import PluginReference.MC_Block;
//import PluginReference.MC_World;
//import WraithavenSculpt.MapEdit;
//import WraithavenSculpt.Triangle;
//import WraithavenSculpt.Vec;
//
public class ImprovedPrintMesh {
}
// extends MapEdit{
// private MeshStats mesh;
// private MC_Block block;
// private int bx, by, bz;
// int blockIndex = -1;
// public ImprovedPrintMesh(MC_World world, MeshStats mesh, MC_Block block, int
// x, int y, int z){
// super(world, mesh.blocks.size(), 300, 20000);
// this.block=block;
// this.mesh=mesh;
// this.bx=x;
// this.by=y;
// this.bz=z;
// begin(true, true);
// }
// protected void getNextLocation(int[] coords){
// blockIndex++;
// int[] a = mesh.blocks.get(blockIndex);
// coords[0]=a[0]+bx;
// coords[1]=a[1]+by;
// coords[2]=a[2]+bz;
// }
// protected MC_Block shouldPlace(int x, int y, int z){ return block; }
// public static MeshStats findMeshBounds(File file, float scale, int x, int y,
// int z, MC_World world, MC_Block block){
// MeshStats mesh = new MeshStats();
// mesh.scale=scale;
// mesh.file=file;
// mesh.x=x;
// mesh.y=y;
// mesh.z=z;
// mesh.world=world;
// mesh.block=block;
// mesh.load();
// mesh.calculateBounds();
// mesh.findBlocks();
// return mesh;
// }
// public static class MeshStats{
// private float scale;
// private File file;
// private MC_World world;
// private MC_Block block;
// private int x, y, z;
// private int indexX, indexY, indexZ;
// private int lx, ly, lz, hx, hy, hz;
// private int blocksVoxelized = 0;
// private int blocksToVoxelize;
// private long lastInfoMessage;
// private int recentBlocksVoxelized = 0;
// private ArrayList<Triangle> tris = new ArrayList<>();
// private ArrayList<int[]> blocks = new ArrayList<>();
// private static ArrayList<MeshStats> meshes = new ArrayList<>();
// private void load(){
// try{
// ArrayList<float[]> vertices = new ArrayList<>();
// BufferedReader in = new BufferedReader(new FileReader(file));
// String s;
// while((s=in.readLine())!=null){
// if(s.startsWith("v ")){
// String[] a = s.split(" ");
// vertices.add(new float[]{Float.valueOf(a[1])*scale,
// Float.valueOf(a[2])*scale, Float.valueOf(a[3])*scale});
// }else if(s.startsWith("f ")){
// Triangle t;
// tris.add(t=new Triangle());
// String[] a = s.split(" ");
// float[] v1 = vertices.get(Integer.valueOf(a[1])-1);
// float[] v2 = vertices.get(Integer.valueOf(a[2])-1);
// float[] v3 = vertices.get(Integer.valueOf(a[3])-1);
// t.x1=v1[0];
// t.y1=v1[1];
// t.z1=v1[2];
// t.x2=v2[0];
// t.y2=v2[1];
// t.z2=v2[2];
// t.x3=v3[0];
// t.y3=v3[1];
// t.z3=v3[2];
// }
// }
// in.close();
// }catch(final Exception exception){ exception.printStackTrace(); }
// }
// private void calculateBounds(){
// boolean first = false;
// for(Triangle t : tris){
// if(first){
// first=false;
// lx=Math.round(Math.min(Math.min(t.x1, t.x2), t.x3));
// ly=Math.round(Math.min(Math.min(t.y1, t.y2), t.y3));
// lz=Math.round(Math.min(Math.min(t.z1, t.z2), t.z3));
// hx=Math.round(Math.max(Math.max(t.x1, t.x2), t.x3));
// hy=Math.round(Math.max(Math.max(t.y1, t.y2), t.y3));
// hz=Math.round(Math.max(Math.max(t.z1, t.z2), t.z3));
// }else{
// lx=Math.min(Math.round(Math.min(Math.min(t.x1, t.x2), t.x3)), lx);
// ly=Math.min(Math.round(Math.min(Math.min(t.y1, t.y2), t.y3)), ly);
// lz=Math.min(Math.round(Math.min(Math.min(t.z1, t.z2), t.z3)), lz);
// hx=Math.max(Math.round(Math.max(Math.max(t.x1, t.x2), t.x3)), hx);
// hy=Math.max(Math.round(Math.max(Math.max(t.y1, t.y2), t.y3)), hy);
// hz=Math.max(Math.round(Math.max(Math.max(t.z1, t.z2), t.z3)), hz);
// }
// }
// }
// private long voxelStartTime;
// private void findBlocks(){
// voxelStartTime=System.currentTimeMillis();
// System.out.println("[WraithavenSculpt] Voxelizing triangles...");
// meshes.add(this);
// indexX=lx;
// indexY=ly;
// indexZ=lz;
// blocksToVoxelize=(hx-lx+1)*(hy-ly+1)*(hz-lz+1);
// lastInfoMessage=voxelStartTime;
// }
// public void updateTriangleVoxelizer(){
// long time = System.currentTimeMillis();
// while(System.currentTimeMillis()-time<30)if(runBlock())break;
// if(System.currentTimeMillis()-lastInfoMessage>=5000){
// System.out.println("[WraithavenSculpt] Voxelizing mesh. "+(blocksVoxelized/(double)blocksToVoxelize*100)+"% complete. ~"+(recentBlocksVoxelized/((System.currentTimeMillis()-lastInfoMessage)/1000f))+" blocks per second.");
// lastInfoMessage=System.currentTimeMillis();
// recentBlocksVoxelized=0;
// }
// }
// private boolean runBlock(){
// if(isInsideMesh())blocks.add(new int[]{indexX, indexY, indexZ});
// recentBlocksVoxelized++;
// blocksVoxelized++;
// indexZ++;
// if(indexZ>hz){
// indexZ=lz;
// indexY++;
// if(indexY>hy){
// indexY=ly;
// indexX++;
// if(indexX>hx){
// System.out.println("[WraithavenSculpt] Finished voxelization. (Took "+((System.currentTimeMillis()-voxelStartTime)/1000f)+" seconds.)");
// meshes.remove(this);
// new ImprovedPrintMesh(world, this, block, x, y, z);
// return true;
// }
// }
// }
// return false;
// }
// private boolean isInsideMesh(){
// ArrayList<Triangle> possibleIntersect = getPossibleIntersects();
// int passes = 0;
// for(Triangle t : possibleIntersect)if(intersectsTriangle(t))passes++;
// return passes%2==1;
// // return possibleIntersect.size()>0;
// }
// private ArrayList<Triangle> getPossibleIntersects(){
// ArrayList<Triangle> i = new ArrayList<>();
// for(Triangle t : tris)if(t.isPointInAABB(indexX, indexY, indexZ))i.add(t);
// return i;
// }
// private boolean intersectsTriangle(Triangle t){
// Vec u, v, n;
// Vec dir, w0;
// float a, b;
// u=new Vec(t.x2, t.y2, t.z2);
// u.sub(new Vec(t.x1, t.y1, t.z1));
// v=new Vec(t.x3, t.y3, t.z3);
// v.sub(new Vec(t.x1, t.y1, t.z1));
// n=new Vec();
// n.cross(u, v);
// if(n.length()==0)return false;
// dir=new Vec(0, 0, 1);
// w0=new Vec(indexX, indexY, indexZ);
// w0.sub(new Vec(t.x1, t.y1, t.z1));
// a=-(new Vec(n).dot(w0));
// b=new Vec(n).dot(dir);
// if((float)Math.abs(b)<0.00000001f)return false;
// if(a/b<0)return false;
// return true;
//
//
//
// // return true;
// }
// private MeshStats(){}
// public static void updateMeshVoxelizers(){ for(MeshStats mesh : new
// ArrayList<>(meshes))mesh.updateTriangleVoxelizer(); }
// }
// }