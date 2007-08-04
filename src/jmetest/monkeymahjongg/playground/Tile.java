package jmetest.monkeymahjongg.playground;

import java.util.Vector;

public class Tile extends Coord {
	private Board owner;
	private TileGroup group;
	private ITileListener tileListener = null;

	public Tile(Board owner, int x, int y, int z) {
		super(x, y, z);
		this.owner = owner;
	}

	/**
	 * This function checks, if a tile is blocked by others
	 * @return true if it is blocked
	 */
	public boolean isBlocked() {
		int tx = x;
		int ty = y;
		int tz = z;

		// check for tiles on top
		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				if (owner.getTile(tx + x, ty + y, tz + 1) != null) {
					return true;
				}
			}
		}

		// check if left is free
		if (owner.getTile(tx - 2, ty - 1, tz) == null
				&& owner.getTile(tx - 2, ty, tz) == null
				&& owner.getTile(tx - 2, ty + 1, tz) == null) {
			return false;
		}

		// check if right is free
		if (owner.getTile(tx + 2, ty - 1, tz) == null
				&& owner.getTile(tx + 2, ty, tz) == null
				&& owner.getTile(tx + 2, ty + 1, tz) == null) {
			return false;
		}
		return true;
	}

	/**
	 * Removes tile from the board
	 */
	public void remove() {
		owner.remove(this);
		group.remove(this);
	}

	protected boolean matches(Tile tile) {
		return tile.getGroup().equals(group);
	}

	/**
	 * 
	 * @return TileGroup for this tile
	 */
	public TileGroup getGroup() {
		return group;
	}

	protected void setGroup(TileGroup group) {
		this.group = group;
	}

	/**
	 * Selects this tile. 
	 * @return true if tile is either selected, unselected or removed 
	 */
	public boolean select() {
		return owner.selectTile(this);
	}

	/**
	 * Gets called, when tile is removed from the board. This function calls the
	 * tile listenener
	 */
	protected void removed() {
		if (tileListener != null)
			tileListener.removed(this);
	}

	/**
	 * Gets called, when tile gets selected (first), to show match (match) and
	 * when it is unselected (unselect)
	 * 
	 * @param selection
	 */
	protected void selected(TileSelection selection) {
		if (tileListener != null)
			tileListener.selected(this, selection);
	}

	/**
	 * Sets the tile listener. The tile-listener gets informed of removal and
	 * selection of this tile
	 * 
	 * @param tileListener an ITileListener implementation
	 */
	public void setTileListener(ITileListener tileListener) {
		this.tileListener = tileListener;
	}

	@Override
	public String toString() {
		return String.format("Tile(%d,%d,%d)", x, y, z);
	}

	/**
	 * Returns all tiles, that match this tile 8can be used to highlight those
	 * tiles)
	 * 
	 * @return Vector<Tile> of all matching tiles
	 */
	public Vector<Tile> getMatchingTiles() {
		Vector<Tile> rc = new Vector<Tile>();
		for (Tile tile : group.getTiles())
			if (tile != null && tile != this)
				rc.add(tile);
		return rc;
	}
}
