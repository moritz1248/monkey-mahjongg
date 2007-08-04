package jmetest.monkeymahjongg.playground;

import java.io.File;
import java.net.URL;
import java.util.Vector;

public class Board {
	Tile[][][] tiles;
	private int width;
	private int height;
	private int depth;

	Vector<TileGroup> availableGroups = new Vector<TileGroup>();
	Vector<TileGroup> satisfiedGroups = new Vector<TileGroup>();
	private int groupCount;
	private Tile selectedTile = null;
	private int score;

	public Board(int width, int height, int depth) {
		tiles = new Tile[width][height][depth];
		this.width = width;
		this.height = height;
		this.depth = depth;

		loadTileGroups();
	}

	private void loadTileGroups() {
		Vector<String> extended = new Vector<String>();

		String imageDirectory = "jmetest/monkeymahjongg/images/";
		URL dirURL = getClass().getClassLoader().getResource(imageDirectory);
		File dir = new File(dirURL.getFile());
		if (dir.exists()) {
			String files[] = dir.list();
			for (String file : files) {
				TileGroup tg = null;
				String prefix = getExtendedPrefix(file);
				if (prefix != null) {
					if (!extended.contains(prefix)) {
						tg = new ExtendedTileGroup(imageDirectory, prefix);
						extended.add(prefix);
					}
				} else
					tg = new SimpleTileGroup(imageDirectory + file);

				availableGroups.add(tg);
			}
		}
	}

	private String getExtendedPrefix(String file) {
		if (file.startsWith("flower"))
			return "flower";
		else if (file.startsWith("season"))
			return "season";
		return null;
	}

	public Tile getTile(int x, int y, int z) {
		if (!isValid(x, y, z))
			return null;
		return tiles[x][y][z];
	}

	private boolean isValid(int x, int y, int z) {
		return (x >= 0 && x < width) && (y >= 0 && y < height)
				&& (z >= 0 && z < depth);
	}

	private void setTile(int x, int y, int z, Tile tile) {
		if (isValid(x, y, z))
			tiles[x][y][z] = tile;
		if (tile != null && tile.getGroup() == null)
			assignGroup(tile);
	}

	private void assignGroup(Tile tile) {
		if (availableGroups.size() == 0)
			throw new RuntimeException("no more available groups");
		int groupPos = (int) (Math.random() * availableGroups.size());
		TileGroup tg = availableGroups.elementAt(groupPos);
		if (!tg.assignTo(tile)) {
			availableGroups.remove(tg);
			satisfiedGroups.add(tg);
		}
	}

	protected void remove(Tile tile) {
		setTile(tile.getX(), tile.getY(), tile.getZ(), null);
		tile.removed();
	}

	public void addTile(int x, int y, int z) {
		Tile tile = new Tile(this, x, y, z);
		setTile(x, y, z, tile);
	}

	public void setGroupCount(int count) {
		groupCount = count;
		for (int i = availableGroups.size() - 1; i >= groupCount; --i) {
			satisfiedGroups.add(availableGroups.elementAt(i));
			availableGroups.remove(i);
		}
	}

	public int missingTileCount() {
		int rc = 0;
		if (availableGroups.size() == 0)
			return 0;
		else {
			if (satisfiedGroups.size() == groupCount)
				return 0;

			for (TileGroup tg : availableGroups) {
				if (tg.refCount != 0)
					rc += 4 - tg.refCount;
			}
		}
		return rc;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getDepth() {
		return depth;
	}

	public static int countTiles(int[][][] layers) {
		int count = 0;
		for (int z = 0; z < layers.length; ++z) {
			for (int y = 0; y < layers[z].length; ++y) {
				for (int x = 0; x < layers[z][y].length; ++x) {
					if (layers[z][y][x] == 1)
						count++;
				}
			}
		}
		return count;
	}

	public void addTiles(int[][][] layers) {
		for (int z = 0; z < layers.length; ++z) {
			for (int y = 0; y < layers[z].length; ++y) {
				for (int x = 0; x < layers[z][y].length; ++x) {
					if (layers[z][y][x] == 1)
						addTile(x, y, z);
				}
			}
		}
	}

	protected boolean selectTile(Tile tile) {
		if (tile.isBlocked())
			return false;
		if (selectedTile != null) {
			if (selectedTile == tile) {
				// unselect tile
				setSelectedTile(null);
			} else {
				if (selectedTile.matches(tile)) {
					tile.selected(true);
					
					// remove tiles
					selectedTile.remove();
					tile.remove();
					
					selectedTile = null;
					score++;
				} else {
					setSelectedTile(null);
					return false;
				}
			}
		} else
			setSelectedTile(tile);
		return true;
	}

	private void setSelectedTile(Tile tile) {
		if (tile == null && selectedTile != null)
			selectedTile.selected(false);
		selectedTile = tile;
		if (selectedTile != null)
			selectedTile.selected(true);
	}

	public int getScore() {
		return score;
	}

	public Hint getHint() {
		for (TileGroup group : satisfiedGroups) {
			if (group != null) {
				Hint hint = group.getHint();
				if (hint != null)
					return hint;
			}
		}
		return null;
	}
}
