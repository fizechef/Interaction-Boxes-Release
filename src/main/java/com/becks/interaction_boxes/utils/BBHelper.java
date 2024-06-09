 package com.becks.interaction_boxes.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.phys.AABB;
import org.joml.Vector3d;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class BBHelper {

    public static boolean intersectAxisAA(int x1low, int x1high, int x2low, int x2high){
        if (x1low <= x2low){
            return x2low <= x1high;
        }
        else {
            return x1low <= x2high;
        }
    }

    public static boolean intersectAABB(BoundingBox bb, BoundingBox bb2){
        return intersectAxisAA(bb.minX(), bb.maxX(), bb2.minX(), bb2.maxX()) &&
                intersectAxisAA(bb.minY(), bb.maxY(), bb2.minY(), bb2.maxY()) &&
                intersectAxisAA(bb.minZ(), bb.maxZ(), bb2.minZ(), bb2.maxZ());
    }

    public static boolean pointInAABB(BlockPos pos, BoundingBox bb){
        return (bb.minX() <= pos.getX() && pos.getX() <= bb.maxX()) && (bb.minY() <= pos.getY() && pos.getY() <= bb.maxY()) && (bb.minZ() <= pos.getZ() && pos.getZ() <= bb.maxZ());
    }

    public static boolean intersectOneOfOrGround(ChunkGenerator chunkGenerator,
                                                 LevelHeightAccessor levelHeightAccessor,
                                                 RandomState randomState,
                                                 Collection<BoundingBox> of,
                                                 BoundingBox one){

        AtomicBoolean groundFlag = new AtomicBoolean(false);
        one.forAllCorners(p -> {
            if (p.getY() >= chunkGenerator.getFirstFreeHeight(p.getX(), p.getZ(), Heightmap.Types.WORLD_SURFACE, levelHeightAccessor, randomState)){
                //System.out.println(p + " " + chunkGenerator.getFirstFreeHeight(p.getX(), p.getZ(), Heightmap.Types.WORLD_SURFACE, levelHeightAccessor, randomState));
                groundFlag.set(true);
            }
        });
        if (groundFlag.get()){
            //System.out.println("Ground abortion");
            return true;
        }
        for(BoundingBox b : of) {
            if (b.minY() < -54){
                return true;
            }
            /*AtomicBoolean groundFlag = new AtomicBoolean(false);
            b.forAllCorners(p -> {
                if (p.getY() >= chunkGenerator.getFirstFreeHeight(p.getX(), p.getZ(), Heightmap.Types.WORLD_SURFACE, levelHeightAccessor, randomState)){
                    //System.out.println(p + " " + chunkGenerator.getFirstFreeHeight(p.getX(), p.getZ(), Heightmap.Types.WORLD_SURFACE, levelHeightAccessor, randomState));
                    groundFlag.set(true);
                }
            });
            if (groundFlag.get()){
                //System.out.println("Ground abortion");
                return true;
            }*/
            if (BBHelper.intersectAABB(b, one)){
                return true;
            }
        }
        return false;
    }

    public static boolean intersectOneOf(Collection<BoundingBox> of,
                                         BoundingBox one){
        for(BoundingBox b : of) {
            if (BBHelper.intersectAABB(b, one)){
                //System.out.println(b);
                return true;
            }
        }
        return false;
    }

    public static boolean pointInOneOf(Collection<BoundingBox> of,
                                         BlockPos pos){
        for(BoundingBox b : of) {
            if (BBHelper.pointInAABB(pos, b)){
                //System.out.println(b);
                return true;
            }
        }
        return false;
    }

    public static Collection<BoundingBox> getToCheck(Collection<Tuple<StructurePiece, Boolean>> pieces){
        List<BoundingBox> l = new ArrayList<>();
        for (Tuple<StructurePiece, Boolean> t : pieces){
            if (t.getB()){
                l.add(t.getA().getBoundingBox());
            }
        }
        return l;
    }

    public static Collection<Vector3d> getRandomPosInAABB(AABB aabb, int count) {
        Collection<Vector3d> pos = new ArrayList<Vector3d>();
        Random rand = new Random();

        for (int i = 0; i<count; i++){
            double rX = rand.nextDouble();
            double rZ = rand.nextDouble();
            double rY = rand.nextDouble();
            rX = aabb.minX + aabb.getXsize()*rX;
            rZ = aabb.minZ + aabb.getXsize()*rZ;
            rY = aabb.minY + aabb.getXsize()*rY;
            pos.add(new Vector3d(rX, rY, rZ));
        }
        return pos;
    }

    public static Collection<Vector3d> getRandomFloorPosInAABB(AABB aabb, int count) {
        Collection<Vector3d> pos = new ArrayList<Vector3d>();
        Random rand = new Random();

        for (int i = 0; i<count; i++){
            double rX = rand.nextDouble();
            double rZ = rand.nextDouble();
            rX = aabb.minX + aabb.getXsize()*rX;
            rZ = aabb.minZ + aabb.getXsize()*rZ;
            pos.add(new Vector3d(rX, aabb.minY, rZ));
        }
        return pos;
    }
}
