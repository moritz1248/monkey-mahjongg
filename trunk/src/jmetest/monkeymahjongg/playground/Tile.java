package jmetest.monkeymahjongg.playground;

public class Tile extends Coord {
	private Board owner;
	private TileGroup group;
	private ITileListener tileListener = null;

	public Tile(Board owner, int x, int y, int z) {
		super(x,y,z);
		this.owner = owner;
	}

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

	public void remove() {
		owner.remove(this);
		group.remove(this);
	}


	public boolean matches(Tile tile) {
		return tile.getGroup().equals(group);
	}

	public TileGroup getGroup() {
		return group;
	}

	public void setGroup(TileGroup group) {
		this.group = group;
	}

	public boolean select() {
		return owner.selectTile(this);
	}

	public void removed() {
		if( tileListener != null )
			tileListener.removed(this);
	}

	public void selected(boolean selection) {
		if( tileListener != null )
			tileListener.selected(this, selection);
	}

	public void setTileListener(ITileListener tileListener) {
		this.tileListener = tileListener;
	}

	@Override
	public String toString() {
		return String.format( "Tile(%d,%d,%d)", x, y, z );
	}
}
