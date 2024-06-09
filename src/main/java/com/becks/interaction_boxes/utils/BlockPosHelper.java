package com.becks.interaction_boxes.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;

import java.util.ArrayList;
import java.util.List;

public abstract class BlockPosHelper {
    public static String posToStringForNBTSerialize(BlockPos pos){
        if (pos != null){
            return pos.getX() + ";" + pos.getY() + ";" + pos.getZ();
        }
        return null;
    }

    public static BlockPos stringToPosForNBTDeserialize(String s){
        String[] dims = s.split(";");
        //This is shit, redo sometime pls :))
        for (String dim : dims) {
            //System.out.println(dim);
        }
        if(dims[0].equals("")){
            return null;
        }
        BlockPos pos = new BlockPos(Integer.parseInt(dims[0]), Integer.parseInt(dims[1]), Integer.parseInt(dims[2]));
        //System.out.println("Read pos: " + pos);
        return pos;
    }

    public static Vec3 blockPosToVec3(BlockPos pos){
        return new Vec3(pos.getX(), pos.getY(), pos.getZ());
    }

    public static double distBetweenInXZSpace(BlockPos pos1, BlockPos pos2){
        Vector3d vec = new Vector3d(pos1.getX() - pos2.getX(), 0, pos1.getZ() - pos2.getZ());
        return DirectionHelper.vector3dLengthXZPlaneSpace(vec);
    }

    public static List<BlockPos> getNeighborPositions(BlockPos pos){
        List<BlockPos> positions = new ArrayList<>();
        positions.add(pos.above());
        positions.add(pos.below());
        positions.add(pos.north());
        positions.add(pos.east());
        positions.add(pos.south());
        positions.add(pos.west());
        return positions;
    }

    public static boolean bordersAir(Level level, BlockPos pos){
        for (BlockPos p : getNeighborPositions(pos)){
            if (level.getBlockState(p).isAir()){
                return true;
            }
        }
        return false;
    }

    public static boolean hasAirInDirection(Level level, BlockPos pos, Vec3 vec){
        Vec3 posVec = pos.getCenter();
        if (level.getBlockState(BlockPos.containing(posVec.add(vec.normalize()))).isAir()){
            return true;
        }
        return false;
    }

    public static List<BlockPos> getAllBlockPosBetween(final BlockPos pos1, final BlockPos pos2){
        List<BlockPos> allPos = new ArrayList<>();
        BlockPos pos1Copy = new BlockPos(pos1.getX(), pos1.getY(), pos1.getZ());
        BlockPos pos2Copy = new BlockPos(pos2.getX(), pos2.getY(), pos2.getZ());
        Vec3 vec12Unit = pos2Copy.getCenter().add(pos1Copy.getCenter().scale(-1));
        double vecLength = vec12Unit.length();
        vec12Unit = vec12Unit.normalize();
        Vec3 moveablePos = pos1Copy.getCenter();
        //System.out.println(vecLength + " " + moveablePos.distanceTo(pos2Copy.getCenter()));
        if (vecLength > 0){
            while (moveablePos.distanceTo(pos1Copy.getCenter()) < vecLength){
                //System.out.println(moveablePos);
                moveablePos = moveablePos.add(vec12Unit);
                BlockPos newPos = BlockPos.containing(moveablePos);
                allPos.add(newPos);
            }
        }
        //System.out.println("\n" + pos1 + " " + pos2 + "\n" + allPos);
        return allPos;
    }
}
