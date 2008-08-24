package jmetest.monkeymahjongg.playground.model;

import java.io.IOException;
import java.util.Vector;

import com.jme.util.export.JMEExporter;
import com.jme.util.export.JMEImporter;
import com.jme.util.export.Savable;


public class Tile implements Savable {
	private Board board;
	private TileGroup group;
	private ITileListener tileListener = null;
	private int x;
	private int y;
	private int z;

	public Tile(Board owner, int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.board = owner;
	}

	/**
	 * This function checks, if a tile is blocked by others
	 * 
	 * @return true if it is blocked
	 */
	public boolean isBlocked() {
		int tx = x;
		int ty = y;
		int tz = z;

		// check for tiles on top
		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				if (board.getTile(tx + x, ty + y, tz + 1) != null) {
					return true;
				}
			}
		}

		// check if left is free
		if (board.getTile(tx - 2, ty - 1, tz) == null
				&& board.getTile(tx - 2, ty, tz) == null
				&& board.getTile(tx - 2, ty + 1, tz) == null) {
			return false;
		}

		// check if right is free
		if (board.getTile(tx + 2, ty - 1, tz) == null
				&& board.getTile(tx + 2, ty, tz) == null
				&& board.getTile(tx + 2, ty + 1, tz) == null) {
			return false;
		}
		return true;
	}

	/**
	 * Removes tile from the board
	 */
	public void remove() {
		board.remove(this);
		group.remove(this);
	}

	protected boolean matches(Tile tile) {
		return tile.getGroup().equals(group);
	}

	/**
	 * 
	 * @return TileGroup for this tile
	 */
	protected TileGroup getGroup() {
		return group;
	}

	protected void setGroup(TileGroup group) {
		this.group = group;
	}

	/**
	 * Selects this tile.
	 * 
	 * @return true if tile is either selected, unselected or removed
	 */
	public boolean select() {
		return board.selectTile(this);
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
	 * @param tileListener
	 *            an ITileListener implementation
	 */
	public void setTileListener(ITileListener tileListener) {
		this.tileListener = tileListener;
	}

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

	public String getTextureResource() {
		return group.getTextureResource(this);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public Board getBoard() {
		return board;
	}

	public Class<?> getClassTag() {
		return getClass();
	}


	public void read(JMEImporter im) throws IOException {
	}

	public void write(JMEExporter ex) throws IOException {
	}

	public void showHint() {
		if (tileListener != null)
			tileListener.selected(this, TileSelection.hint);
	}
}
