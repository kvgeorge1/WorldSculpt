package com.worldsculpt.Edits.Complex;

//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.util.ArrayList;
//import PluginReference.MC_Block;
//import PluginReference.MC_World;
//import WraithavenSculpt.ChiselLocations;
//import WraithavenSculpt.MapEdit;
//import WraithavenSculpt.Triangle;
//
public class PrintMeshWandEdit {
}
// extends MapEdit{
// private MeshStats mesh;
// private MC_Block block;
// private int bx, by, bz;
// int blockIndex = -1;
// public PrintMeshWandEdit(MC_World world, MeshStats mesh, MC_Block block, int
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
// mesh.c = new ChiselLocations();
// mesh.c.setPoint1=mesh.c.setPoint2=true;
// mesh.file=file;
// mesh.x=x;
// mesh.y=y;
// mesh.z=z;
// mesh.world=world;
// mesh.block=block;
// mesh.load();
// mesh.calculateBounds(x, y, z);
// return mesh;
// }
// public static class MeshStats{
// private ChiselLocations c;
// private float scale;
// private File file;
// private MC_World world;
// private MC_Block block;
// private int x, y, z;
// private int triangleIndex = 0;
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
// findBlocks();
// }catch(final Exception exception){ exception.printStackTrace(); }
// }
// private void calculateBounds(int x, int y, int z){
// boolean first = false;
// int lx = 0;
// int ly = 0;
// int lz = 0;
// int hx = 0;
// int hy = 0;
// int hz = 0;
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
// c.x1=lx+x;
// c.x2=hx+x;
// c.y1=ly+y;
// c.y2=hy+y;
// c.z1=lz+z;
// c.z2=hz+z;
// }
// private long voxelStartTime;
// private void findBlocks(){
// voxelStartTime=System.currentTimeMillis();
// System.out.println("[WraithavenSculpt] Voxelizing triangles...");
// meshes.add(this);
// }
// private void runUpdate(){
// float t1, t2, t3;
// int nx, ny, nz;
// int v3;
// Triangle tri = tris.get(triangleIndex);
// triangleIndex++;
// for(int v1 = 0; v1<=tri.getChecks(); v1++){
// for(int v2 = 0; v2<=tri.getChecks()-v1; v2++){
// v3=tri.getChecks()-(v1+v2);
// t1=v1/(float)tri.getChecks();
// t2=v2/(float)tri.getChecks();
// t3=v3/(float)tri.getChecks();
// nx=Math.round(t1*tri.x1+t2*tri.x2+t3*tri.x3);
// ny=Math.round(t1*tri.y1+t2*tri.y2+t3*tri.y3);
// nz=Math.round(t1*tri.z1+t2*tri.z2+t3*tri.z3);
// if(!contains(nx, ny, nz))blocks.add(new int[]{nx, ny, nz});
// }
// }
// System.out.println("[WraithavenSculpt]    "+triangleIndex+"/"+tris.size()+" triangles complete.");
// if(triangleIndex==tris.size()){
// System.out.println("[WraithavenSculpt] Finsihed voxelization. (Took "+((System.currentTimeMillis()-voxelStartTime)/1000f)+" seconds.)");
// meshes.remove(this);
// new PrintMeshWandEdit(world, this, block, x, y, z);
// }
// }
// public void updateTriangleVoxelizer(){
// long time = System.currentTimeMillis();
// while(System.currentTimeMillis()-time<30)runUpdate();
// }
// private boolean contains(int nx, int ny, int nz){
// for(int[] a : blocks)if(a[0]==nx&&a[1]==ny&&a[2]==nz)return true;
// return false;
// }
// private MeshStats(){}
// public static void updateMeshVoxelizers(){ for(MeshStats mesh : new
// ArrayList<>(meshes))mesh.updateTriangleVoxelizer(); }
// }
// }