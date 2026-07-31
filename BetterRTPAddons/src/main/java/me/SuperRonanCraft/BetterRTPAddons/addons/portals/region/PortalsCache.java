package me.SuperRonanCraft.BetterRTPAddons.addons.portals.region;

import me.SuperRonanCraft.BetterRTPAddons.addons.portals.AddonPortals;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public class PortalsCache {

    AddonPortals addonPortals;
    private List<PortalsRegionInfo> registeredPortals;
    private final HashMap<Player, PortalsRegionInfo> portalsBeingCreated = new HashMap<>();

    public PortalsCache(AddonPortals addonPortals) {
        this.addonPortals = addonPortals;
    }

    public void load() {
        registeredPortals = addonPortals.getDatabase().getPortals();
    }

    public void unload() {
        portalsBeingCreated.clear();
    }

    void uncache(Player p) {
        portalsBeingCreated.remove(p);
    }

    public List<PortalsRegionInfo> getRegisteredPortals() {
        return registeredPortals;
    }

    public boolean removeRegisteredPortal(PortalsRegionInfo portal) {
        registeredPortals.remove(portal);
        return addonPortals.getDatabase().removePortal(portal);
    }

    public boolean addRegisteredPortal(Player p, String name) {
        if (!portalsBeingCreated.containsKey(p))
            return false;
        PortalsRegionInfo portal = portalsBeingCreated.get(p);
        portal.name = name;
        registeredPortals.add(portal);
        return addonPortals.getDatabase().setPortal(portal);
    }

    public void cachePortal(Player p, Location loc, boolean loc2) {
        PortalsRegionInfo portal;
        if (portalsBeingCreated.containsKey(p)) {
            portal = portalsBeingCreated.get(p);
        } else {
            portal = new PortalsRegionInfo();
            portalsBeingCreated.put(p, portal);
        }
        Location old_loc1 = portal.loc_1;
        Location old_loc2 = portal.loc_2;
        if (loc2)
            portal.loc_2 = loc;
        else
            portal.loc_1 = loc;

        if (portal.loc_1 != null && portal.loc_2 != null)
            if (old_loc1 == null || old_loc2 == null)
                addonPortals.msgs.getLocation_Ready(p);

        p.sendBlockChange(loc, Material.GLOWSTONE.createBlockData());
    }

    public PortalsRegionInfo getPortal(Player p) {
        return portalsBeingCreated.getOrDefault(p, null);
    }
}
