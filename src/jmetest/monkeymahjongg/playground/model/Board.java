package jmetest.monkeymahjongg.playground.model;

import java.io.File;
import java.net.URL;
import java.util.Iterator;
import java.util.Vector;


public class Board implements Iterable<Tile> {
	Tile[][][] tiles;
	private int width;
	private int height;
	private int depth;

	Vector<TileGroup> availableGroups = new Vector<TileGroup>();
	Vector<TileGroup> satisfiedGroups = new Vector<TileGroup>();
	private int groupCount;
	private Tile selectedTile = null;
	private int score;
	private int originalTileCount;

	public Board(XMLLevel level) {
		loadTileGroups();

		this.width = level.getWidth();
		this.height = level.getHeight();
		this.depth = level.getDepth();
		tiles = new Tile[width][height][depth];

		for (int line = 0; line < level.getLines().size(); ++line) {
			String data = level.getLines().elementAt(line);
			for (int x = 0; x < data.length(); x++) {
				char c = data.charAt(x);
				if ('1' <= c && c <= '9') {
					int n = c - '1';
					for (int z = 0; z <= n; z++) {
						if (getTile(x - 1, line - 1, z) == null
								&& getTile(x, line - 1, z) == null
								&& getTile(x + 1, line - 1, z) == null
								&& getTile(x - 1, line, z) == null) {
							addTile(x, line, z);
							originalTileCount++;
						}
					}
				}
			}
		}

		setGroupCount(originalTileCount / 4);

		assignGroups();
	}

	protected void assignGroups() {
		for (int x = 0; x < getWidth(); ++x)
			for (int y = 0; y < getHeight(); ++y)
				for (int z = 0; z < getDepth(); ++z) {
					Tile tile = getTile(x, y, z);
					if (tile != null)
						assignGroup(tile);
				}
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
						availableGroups.add(tg);
						extended.add(prefix);
					}
				} else {
					tg = new SimpleTileGroup(imageDirectory + file);
					availableGroups.add(tg);
				}
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
	}

	private void assignGroup(Tile tile) {
		if (availableGroups.size() == 0)
			throw new RuntimeException("no more available groups");
		int groupPos = (int) (Math.random() * availableGroups.size());
		TileGroup tg = availableGroups.elementAt(groupPos);
		if (!tg.assignTo(tile)) {
			availableGroups.remove(availableGroups.indexOf(tg));
			satisfiedGroups.add(tg);
		}
	}

	protected void remove(Tile tile) {
		setTile(tile.getX(), tile.getY(), tile.getZ(), null);
		tile.removed();
	}

	private void addTile(int x, int y, int z) {
		Tile tile = new Tile(this, x, y, z);
		setTile(x, y, z, tile);
	}

	private void setGroupCount(int count) {
		groupCount = count;
		for (int i = availableGroups.size() - 1; i >= groupCount; --i) {
			satisfiedGroups.add(availableGroups.elementAt(i));
			availableGroups.remove(i);
		}
	}

	public int getMissingTileCount() {
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

	protected boolean selectTile(Tile tile) {
		if (tile.isBlocked())
			return false;
		if (selectedTile != null) {
			if (selectedTile == tile) {
				// unselect tile
				setSelectedTile(null);
			} else {
				if (selectedTile.matches(tile)) {
					tile.selected(TileSelection.match);

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
			selectedTile.selected(TileSelection.unselect);
		selectedTile = tile;
		if (selectedTile != null)
			selectedTile.selected(TileSelection.first);
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

	public int getOriginalTileCount() {
		return originalTileCount;
	}

	public int getTileCount() {
		int count = 0;
		for (int x = 0; x < tiles.length; ++x)
			for (int y = 0; y < tiles[x].length; ++y)
				for (int z = 0; z < tiles[x][y].length; ++z) {
					if (tiles[x][y][z] != null)
						count++;
				}
		return count;
	}

	@Override
	public Iterator<Tile> iterator() {
		return new Iterator<Tile>() {

			int x = 0;
			int y = 0;
			int z = 0;

			@Override
			public boolean hasNext() {
				if( x == width - 1 && y == height - 1 && z == depth - 1 )
					return false;
				return true;
			}

			@Override
			public Tile next() {
				Tile tile = tiles[x][y][z];
				if (z < depth - 1 )
					z++;
				else if (y < height - 1 ) {
					y++;
					z = 0;
				} else if (x < width - 1) {
					x++;
					y = 0;
				}
				return tile;
			}

			@Override
			public void remove() {
			}
		};
	}
}
