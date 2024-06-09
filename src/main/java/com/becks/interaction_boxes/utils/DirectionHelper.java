package com.becks.interaction_boxes.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public abstract class DirectionHelper {
    public static Rotation getRotation(Direction from, Direction toMatch){
        if (from.equals(Direction.UP) || from.equals(Direction.DOWN) || toMatch.equals(Direction.UP) || toMatch.equals(Direction.DOWN)){
            return Rotation.NONE;
        }
        Rotation r = Rotation.NONE;
        while (!r.rotate(toMatch).equals(from.getOpposite())){
            //System.out.println(r);
            r = r.getRotated(Rotation.CLOCKWISE_90);
        }
        return r;
    }

    public static Vec3i getOffsets(Rotation or, Rotation r, Direction d){
        Vec3i newX = or.rotate(Direction.EAST).getNormal();
        Vec3i newZ = or.rotate(Direction.SOUTH).getNormal();
       //System.out.println(or);
       //System.out.println(newX + " " + newZ);
       //System.out.println(r);
       //System.out.println(d);
       //System.out.println(d.getNormal());
        switch(r){
            case NONE:
                return d.getNormal().multiply(25);
            case CLOCKWISE_90:
                return newX.multiply(24).offset(d.getNormal().multiply(25));
            case CLOCKWISE_180:
                return newX.multiply(24).offset(newZ.multiply(24)).offset(d.getNormal().multiply(25));
            case COUNTERCLOCKWISE_90:
                return newZ.multiply(24).offset(d.getNormal().multiply(25));
        }
        return null;
    }

    public static Vec3i getRotatedVec(Vec3i toRotate, Rotation rotation){
        Vec3i newX = rotation.rotate(Direction.EAST).getNormal();
        Vec3i newZ = rotation.rotate(Direction.SOUTH).getNormal();
        Vec3i newY = Direction.UP.getNormal();
        newY = newY.multiply(toRotate.getY());
        newX = newX.multiply(toRotate.getX());
        newZ = newZ.multiply(toRotate.getZ());
        return newX.offset(newY).offset(newZ);
    }

    public static Vector3d getRotatedVec(Vector3d toRotate, Rotation rotation){
        Vec3i newX = rotation.rotate(Direction.EAST).getNormal();
        Vec3i newZ = rotation.rotate(Direction.SOUTH).getNormal();
        Vec3i newY = Direction.UP.getNormal();
        Vector3d newXD = new Vector3d(newX.getX(), newX.getY(), newX.getZ());
        Vector3d newYD = new Vector3d(newY.getX(), newY.getY(), newY.getZ());
        Vector3d newZD = new Vector3d(newZ.getX(), newZ.getY(), newZ.getZ());
        newYD.mul(toRotate.y);
        newXD.mul(toRotate.x);
        newZD.mul(toRotate.z);
        Vector3d result = new Vector3d(0, 0, 0);
        result.add(newXD);
        result.add(newYD);
        result.add(newZD);
        return result;
    }

    public static AABB rotateAABBblockCenterRelated(AABB toRotate, Rotation r){
        //System.out.println(toRotate);
        Vector3d firstCorner = new Vector3d(((double)toRotate.minX), ((double)toRotate.minY), ((double)toRotate.minZ));
        Vector3d secondCorner = new Vector3d(((double)toRotate.maxX), ((double)toRotate.maxY), ((double)toRotate.maxZ));
        firstCorner.mul(16);
        secondCorner.mul(16);
        //System.out.println("original: " + firstCorner.x + " " + firstCorner.y + " " + firstCorner.z + " " + " to " + secondCorner.x + " " + secondCorner.y + " " + secondCorner.z);
        Vector3d inversMiddleShift = new Vector3d(-8,-8,-8);
        firstCorner.add(inversMiddleShift);
        secondCorner.add(inversMiddleShift);
        //System.out.println("offset: " + firstCorner.x + " " + firstCorner.y + " " + firstCorner.z + " " + " to " + secondCorner.x + " " + secondCorner.y + " " + secondCorner.z);
        firstCorner = getRotatedVec(firstCorner, r);
        Vector3d middleShift = new Vector3d(8,8,8);
        firstCorner.add(middleShift);
        secondCorner = getRotatedVec(secondCorner, r);
        secondCorner.add(middleShift);
        firstCorner.mul(1.0/16.0);
        secondCorner.mul(1.0/16.0);
        //System.out.println("rotated: " + firstCorner.x + " " + firstCorner.y + " " + firstCorner.z + " " + " to " + secondCorner.x + " " + secondCorner.y + " " + secondCorner.z);
        AABB newAABB = new AABB(
                Math.min(firstCorner.x, secondCorner.x),
                Math.min(firstCorner.y, secondCorner.y),
                Math.min(firstCorner.z, secondCorner.z),
                Math.max(firstCorner.x, secondCorner.x),
                Math.max(firstCorner.y, secondCorner.y),
                Math.max(firstCorner.z, secondCorner.z));
        //System.out.println(newAABB);
        return newAABB;
    }

    public static int getRotationDegrees(Rotation r){
        switch(r){
            case NONE -> {
                return 0;
            }
            case CLOCKWISE_90 -> {
                return 90;
            }
            case CLOCKWISE_180 -> {
                return 180;
            }
            case COUNTERCLOCKWISE_90 -> {
                return 270;
            }
        }
        return 0;
    }

    public static float getRotationRadians(Rotation r){
        int rotDegrees = getRotationDegrees(r);

        return (float) ((rotDegrees * Math.PI)/180.0);
    }

    public static Rotation getRotationNoOpp(Direction from, Direction toMatch) {
        if (from.equals(Direction.UP) || from.equals(Direction.DOWN) || toMatch.equals(Direction.UP) || toMatch.equals(Direction.DOWN)){
            return Rotation.NONE;
        }
        Rotation r = Rotation.NONE;
        while (!r.rotate(from).equals(toMatch)){
            //System.out.println(r);
            r = r.getRotated(Rotation.CLOCKWISE_90);
        }
        return r;
    }

    public static boolean equalVec(Vector3d vec1, Vector3d vec2){
        return vec1.x == vec2.x && vec1.y == vec2.y && vec1.z == vec2.z;
    }

    public static double getAngleBetweenVec3dInXZPlainSpace(Vector3d vec1, Vector3d vec2){
        double XX = vec1.x * vec2.x;
        double ZZ = vec1.z * vec2.z;
        double vec1XZlength = Math.sqrt((vec1.x * vec1.x) + (vec1.z * vec1.z));
        double vec2XZlength = Math.sqrt((vec2.x * vec2.x) + (vec2.z * vec2.z));
        double cosA = (XX + ZZ)/(vec1XZlength * vec2XZlength);
        double aInRad = Math.acos(cosA);
        return aInRad * (180D/Math.PI);
    }
    public static Vector3d rotateVec3dInXZPlainSpace(Vector3d vec, double degrees){
        degrees = degrees / (180D/Math.PI);
        double newX = vec.x * Math.cos(degrees) - vec.z * Math.sin(degrees);
        double newZ = vec.x * Math.sin(degrees) + vec.z * Math.cos(degrees);
        return new Vector3d(newX, vec.y, newZ);
    }

    /**
     * Rotates a vec around a specified axis
     * @param vec the vector to rotate
     * @param degrees rotation degrees
     * @param normal the axis vector to rotate around, or the normal vector of the plain space to rotate in
     * @return
     */
    public static Vec3 rotateVec3dInPlainSpace(Vec3 vec, double degrees, Vec3 normal){
        double d = degrees / (180D/Math.PI);
        Vec3 unitNormal = normal.normalize();
        double ux = unitNormal.x;
        double uy = unitNormal.y;
        double uz = unitNormal.z;
        double x = vec.x;
        double y = vec.y;
        double z = vec.z;
        double newX = (Math.cos(d) + ux*ux*(1-Math.cos(d))) * x + (ux*uy*(1-Math.cos(d)) - uz * Math.sin(d)) * y + (ux*uz*(1-Math.cos(d)) + uy*Math.sin(d)) * z;
        double newY = (uy*ux*(1-Math.cos(d)) + uz*Math.sin(d)) * x + (Math.cos(d) + uy*uy*(1-Math.cos(d))) * y + (uy*uz*(1-Math.cos(d))-ux*Math.sin(d)) * z;
        double newZ = (uz*ux*(1-Math.cos(d)) - uy*Math.sin(d)) * x + (uz*uy*(1-Math.cos(d)) + ux*Math.sin(d)) * y + (Math.cos(d) + uz*uz*(1-Math.cos(d))) * z;
        return new Vec3(newX, newY, newZ);
    }

    public static String vector3dToString(Vector3d vec){
        return vec.x + " " + vec.y + " " + vec.z;
    }

    public static Vec3i vector3dToVec3i(Vector3d vec){
        return new Vec3i((int)vec.x, (int)vec.y, (int)vec.z);
    }

    public static Vec3i vec3ToVec3i(Vec3 vec){
        return new Vec3i((int)vec.x, (int)vec.y, (int)vec.z);
    }

    public static Vector3d vec3iToVector3d(Vec3i vec){
        return new Vector3d(vec.getX(), vec.getY(), vec.getZ());
    }

    public static double vector3dLengthXZPlaneSpace(Vector3d vec){
        return Math.sqrt((vec.x * vec.x) + (vec.z * vec.z));
    }

    public static double getDegreesForRadialDistance(double radius, double distance){
        return distance/(Math.PI * radius / 180);
    }

    public static List<Direction> getAllOrthogonalDirections(Direction normalDirection){
        List<Direction> orthDirec = new ArrayList<>();
        for (Direction d : Direction.values()){
            if (!(d.getOpposite().equals(normalDirection) || d.equals(normalDirection))){
                orthDirec.add(d);
            }
        }
        return orthDirec;
    }

    public static Vec3 getSomeOrthogonalVec(Vec3 vec){
        //TODO check if this actually works
        List<Tuple<Integer,Double>> magicList = new ArrayList<>();
        magicList.add(new Tuple<>(0, vec.x));
        magicList.add(new Tuple<>(1, vec.y));
        magicList.add(new Tuple<>(2, vec.z));
        magicList.sort(Comparator.comparingDouble(Tuple::getB));
        magicList.get(0).setB(0d);
        magicList.get(2).setB(-magicList.get(2).getB());
        int i1 = magicList.get(2).getA();
        int i2 = magicList.get(1).getA();
        magicList.get(2).setA(i2);
        magicList.get(1).setA(i1);
        magicList.sort(Comparator.comparingInt(Tuple::getA));
        return new Vec3(magicList.get(0).getB(), magicList.get(1).getB(), magicList.get(2).getB()).normalize();
    }

    /**
     * creates a sphere cap shaped vector field
     * @param normalizedR radius Vector of full sphere
     * @param normalizedROrthogonal some vector orthogonal to normalizedR
     * @param sphereCapAngle cutoff angle of spherecap
     * @param density density of the vector field
     * @return
     */
    public static List<Vec3> fillSphereCapVectorField(Vec3 normalizedR, Vec3 normalizedROrthogonal, double sphereCapAngle, double density){
        Random random = new Random();
        double d = sphereCapAngle / (180D/Math.PI);
        double sphereCapArea = 2 * Math.PI * (1 - Math.cos(d));
        List<Vec3> vectorField = new ArrayList<>();
        while (sphereCapArea > 0){
            Vec3 vec = new Vec3(normalizedR.x, normalizedR.y, normalizedR.z);
            vec = rotateVec3dInPlainSpace(vec, random.nextDouble() * sphereCapAngle, normalizedROrthogonal);
            vec = rotateVec3dInPlainSpace(vec, random.nextDouble() * 360, normalizedR);
            vec.normalize();
            vectorField.add(vec);
            sphereCapArea = sphereCapArea - density;
        }
        return vectorField;
    }

    public static List<Vec3> fillSingleLayerConeVectorField(Vec3 normalizedR, Vec3 normalizedROrthogonal, double coneAngle, double density) {
        Random random = new Random();
        List<Vec3> vectorField = new ArrayList<>();
        for (int i = 0; i < density; i++) {
            Vec3 vec = new Vec3(normalizedR.x, normalizedR.y, normalizedR.z);
            vec = rotateVec3dInPlainSpace(vec, coneAngle + 90, normalizedROrthogonal);
            vec = rotateVec3dInPlainSpace(vec, random.nextDouble() * 360, normalizedR);
            vec.normalize();
            vectorField.add(vec);
        }
        return vectorField;
    }

    public static Vec3 getBlockTypeSurfaceNormalQuickAndDirty(Level level, BlockPos pos, Vec3 direction){
        Tuple<Vec3, Vec3> surfactRepresentatives = new Tuple<>(null, null);
        Vec3 posVec = pos.getCenter();
        double type = level.getBlockState(pos).getBlock().getExplosionResistance();
        Vec3 inverseDirection = direction.scale(-1);
        Vec3 inverseDirectionNormalXY = new Vec3(inverseDirection.x, inverseDirection.y, 0).normalize();
        Vec3 inverseDirectionNormalZY = new Vec3(0, inverseDirection.y, inverseDirection.z).normalize();
        int steps = 0;
        Vec3 positiveRotatedX = inverseDirectionNormalXY.scale(1);
        Vec3 negativeRotatedX = inverseDirectionNormalXY.scale(1);
        Vec3 positiveRotatedZ = inverseDirectionNormalZY.scale(1);
        Vec3 negativeRotatedZ = inverseDirectionNormalZY.scale(1);
        while ((level.getBlockState(BlockPos.containing(posVec.add(positiveRotatedX))).getBlock().getExplosionResistance() < type || level.getBlockState(BlockPos.containing(posVec.add(positiveRotatedX))).isAir()) && steps < 8){
            positiveRotatedX = DirectionHelper.rotateVec3dInPlainSpace(positiveRotatedX, 45, new Vec3(0,0,1));
            steps++;
        }
        steps = 0;
        while ((level.getBlockState(BlockPos.containing(posVec.add(negativeRotatedX))).getBlock().getExplosionResistance() < type || level.getBlockState(BlockPos.containing(posVec.add(negativeRotatedX))).isAir()) && steps < 8){
            negativeRotatedX = DirectionHelper.rotateVec3dInPlainSpace(negativeRotatedX, -45, new Vec3(0,0,1));
            steps++;
        }
        steps = 0;
        while ((level.getBlockState(BlockPos.containing(posVec.add(positiveRotatedZ))).getBlock().getExplosionResistance() < type || level.getBlockState(BlockPos.containing(posVec.add(positiveRotatedZ))).isAir()) && steps < 8){
            positiveRotatedZ = DirectionHelper.rotateVec3dInPlainSpace(positiveRotatedZ, 45, new Vec3(1,0,0));
            steps++;
        }
        steps = 0;
        while ((level.getBlockState(BlockPos.containing(posVec.add(negativeRotatedZ))).getBlock().getExplosionResistance() < type || level.getBlockState(BlockPos.containing(posVec.add(negativeRotatedZ))).isAir()) && steps < 8){
            negativeRotatedZ = DirectionHelper.rotateVec3dInPlainSpace(negativeRotatedZ, -45, new Vec3(1,0,0));
            steps++;
        }
        surfactRepresentatives.setA(positiveRotatedX.add(negativeRotatedX.scale(-1)).normalize());
        surfactRepresentatives.setB(positiveRotatedZ.add(negativeRotatedZ.scale(-1)).normalize());
        Vec3 planeNormal = surfactRepresentatives.getA().cross(surfactRepresentatives.getB());
        System.out.println("planevecs: " + surfactRepresentatives.getA() + " " + surfactRepresentatives.getB() + " planeNormal: " + planeNormal);
        return planeNormal;
    }

    public static double getAngleBetweenVecAndPlane(Vec3 vec, Vec3 planeNormal){
        double cosA = (vec.dot(planeNormal))/(vec.length()*planeNormal.length());
        System.out.println((Math.acos(cosA) * (180/Math.PI)));
        return 90 - (Math.acos(cosA) * (180/Math.PI));
    }

    public static Vec3 getReflectionVecOnPlane(Vec3 vec, Vec3 planeNormal){
        Vec3 planeUnitNormal = planeNormal.normalize();
        Vec3 reflection = vec.add(planeUnitNormal.scale(2 * vec.dot(planeUnitNormal)).scale(-1));
        return reflection;
    }
}
