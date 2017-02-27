package com.wetDude.warp;

import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.UUID;

import com.google.gson.annotations.Expose;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import static java.util.Collections.emptyList;

/**
 * Stores and manages warps.
 */
public class WarpStorage {
	/**
	 * Contains a single warp information
	 */
    public class WarpInfo {
        public WarpInfo(String name, BlockPos pos, int dim, UUID own) {
            this.x = pos.getX();
            this.y = pos.getY();
            this.z = pos.getZ();
            this.dimension = dim;
            this.name = name;
			this.owner = own;
        }

        @Expose // warp name
        public String name;
        @Expose // world which warp belongs to
        public int dimension;
        @Expose // position
        public double x;
        @Expose
        public double y;
        @Expose
        public double z;
		@Expose
		public UUID owner;
    }



    @Expose // a list of all warps
    private HashSet<WarpInfo> m_warps;

    public WarpStorage() {
        m_warps = new HashSet<WarpInfo>();
    }
	/**
	 * Check whether there's not warps.
	 */
    public boolean isEmpty() {
        return(m_warps.size() == 0);
    }

	/**
	 * Add new warp.
	 * @param name warp name
	 * @param position warp position
	 * @param dimension warp dimension
	 * return returns false if warp exists and belongs to another player
	 */
    public boolean addWarp(String name, BlockPos position, int dimension, UUID owner) {
        // check if warp already exists
		for(WarpInfo info : m_warps) {
			if(info.name.equals(name)) {
				if(!info.owner.equals(owner)) {
					return false;
				}
					
				info.x = position.getX();
				info.y = position.getY();
				info.z = position.getZ();
				info.dimension = dimension;
				return true;
			}
		}

        // create new if not found
        m_warps.add(new WarpInfo(name, position, dimension, owner));
		return true;
    }

    /**
	 * Remove warp from the list.
	 * It checks for null so it is safe to use with getWarp().
	 * @param w warp to remove
	 * @return true if successully removed; false if not found.
	 */
    public boolean removeWarp(WarpInfo w) {
        if(w == null) {
            return false;
        }

		return m_warps.remove(w);
    }
	
	/**
	 * Remove warp from the list.
	 * @param name warp name
	 * @return true if found and removed; false otherwise
	 */
	public boolean removeWarp(String name, UUID owner) {
		WarpInfo w = this.getWarp(name);
		if(w == null) {
			return false;
		}
		
		if(!w.owner.equals(owner)) {
			return false;
		}
		
		return m_warps.remove(w);
	}

	/**
	 * Get warp by its name.
	 * @param name warp name
	 * @return warp info or null if doesn't exist
	 */
    @Nullable
    public WarpInfo getWarp(String name) {		
		for(WarpInfo info : m_warps) {
			if(info.name.equals(name)) {
				return info;
			}
		}

        return null;
    }

	/**
	 * Get all warps as list.
	 */
    public HashSet<WarpInfo> getWarpList() {
        return m_warps;
    }
}
