package me.SuperRonanCraft.BetterRTP.references.rtpinfo;

import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.worlds.RTPWorld;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.worlds.WORLD_TYPE;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomLocation {

    public static Location generateLocation(RTPWorld rtpWorld) {
        Location loc;
        switch (rtpWorld.getShape()) {
            case CIRCLE: loc = generateRound(rtpWorld); break;
            case SQUARE:
            default: loc = generateSquare(rtpWorld); break;
        }
        return loc;
    }

    private static Location generateSquare(RTPWorld rtpWorld) {
        // Return a Location where a random X and Z are within the bounds defined by MinRadius and MaxRadius
        int radius_min = rtpWorld.getMinRadius();
        int radius_max = rtpWorld.getMaxRadius();
        try {
            if (radius_min < 0 || radius_max < 0 || radius_min >= radius_max) {
                throw new IllegalArgumentException(); // If MinRadius or MaxRadius is negative, throw an exception
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            BetterRTP.getInstance().getLogger().warning("Incorrect configuration! Check your config and confirm that MinRadius is smaller than MaxRadius and that they are both positive numbers!");
            BetterRTP.getInstance().getLogger().warning("Max: " + rtpWorld.getMaxRadius() + " Min: " + rtpWorld.getMinRadius());
            return null;
        }
        // Generate a random X and Z based off the radius. No quadrants voodoo.
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int x = random.nextInt(-radius_max, radius_max + 1);
        int z = (Math.abs(x) >= radius_min)
            ? random.nextInt(-radius_max, radius_max + 1)
            : (random.nextBoolean() ? 1 : -1) * random.nextInt(radius_min, radius_max + 1);
        x += rtpWorld.getCenterX();
        z += rtpWorld.getCenterZ();
        return new Location(rtpWorld.getWorld(), x, 69, z);
    }

    private static Location generateRound(RTPWorld rtpWorld) {
        // Return a random X and Z based off location on a spiral curve
        int min = rtpWorld.getMinRadius();
        int max = rtpWorld.getMaxRadius();
        int x, z;

        ThreadLocalRandom random = ThreadLocalRandom.current();
        double r = Math.sqrt(random.nextDouble(min * (double) min, max * (double) max));
        double theta = random.nextDouble(0, Math.PI * 2);

        // polar to cartesian
        x = (int) (r * Math.cos(theta));
        z = (int) (r * Math.sin(theta));
        x += rtpWorld.getCenterX();
        z += rtpWorld.getCenterZ();
        return new Location(rtpWorld.getWorld(), x, 69, z);
    }

    public static Location getSafeLocation(WORLD_TYPE type, World world, Location loc, int minY, int maxY, List<String> biomes) {
        switch (type) { //Get a Y position and check for bad blocks
            case NETHER: return getLocAtNether(loc.getBlockX(), loc.getBlockZ(), minY, maxY, world, biomes);
            case NORMAL:
            default: return getLocAtNormal(loc.getBlockX(), loc.getBlockZ(), minY, maxY, world, biomes);
        }
    }
    private static Location getLocAtNormal(int x, int z, int minY, int maxY, World world, List<String> biomes) {
        Block b = getHighestBlock(x, z, world);
        if (!b.getType().isSolid()) { //Water, lava, shrubs...
            if (!badBlock(b.getType(), x, b.getY(), z, world, null)) { //Make sure it's not an invalid block (ex: water, lava...)
                //int y = world.getHighestBlockYAt(x, z);
                b = world.getBlockAt(x, b.getY() - 1, z);
            }
        }
        //Between max and min y
        if (    b.getY() >= minY
                && b.getY() <= maxY
                && !badBlock(b.getType(), x, b.getY(), z, world, biomes)) {
            return new Location(world, x, b.getY() + 1, z);
        }
        return null;
    }

    public static Block getHighestBlock(int x, int z, World world) {
        return world.getHighestBlockAt(x, z);
    }

    private static Location getLocAtNether(int x, int z, int minY, int maxY, World world, List<String> biomes) {
        //Max and Min Y
        for (int y = minY + 1; y < maxY/*world.getMaxHeight()*/; y++) {
            Block block_current = world.getBlockAt(x, y, z);
            if (block_current.getType().isAir() || !block_current.getType().isSolid()) {
                if (!block_current.getType().isAir() &&
                        !block_current.getType().isSolid()) { //Block is not a solid (ex: lava, water...)
                    if (badBlock(block_current.getType(), x, y, z, world, null))
                        continue;
                }
                Material block = world.getBlockAt(x, y - 1, z).getType();
                if (block.isAir()) //Block below is air, skip
                    continue;
                if (world.getBlockAt(x, y + 1, z).getType().isAir() //Head space
                        && !badBlock(block, x, y, z, world, biomes)) //Valid block
                    return new Location(world, x, y, z);
            }
        }
        return null;
    }

    // Bad blocks, or bad biome
    public static boolean badBlock(Material block, int x, int y, int z, World world, List<String> biomes) {
        if (BetterRTP.getInstance().getRTP().getBlockList().contains(block.name()))
            return true;
        //Check Biomes
        if (biomes == null || biomes.isEmpty())
            return false;
        String biomeCurrent = world.getBiome(x, y, z).getKey().getKey();
        for (String biome : biomes)
            if (biomeCurrent.toUpperCase().contains(biome.toUpperCase()))
                return false;
        return true;
        //FALSE MEANS NO BAD BLOCKS/BIOME WHERE FOUND!
    }

}
