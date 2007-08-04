package jmetest.monkeymahjongg.playground;

import java.util.Vector;

public class Test {

	private static final class GUITile implements ITileListener {
		private Tile tile;
		public static Vector<GUITile> guiTiles = new Vector<GUITile>();

		public GUITile(Tile tile) {
			this.tile = tile;
			guiTiles.add(this);
		}

		@Override
		public void removed(Tile tile) {
			System.out.println("Tile " + tile + " removed "
					+ tile.getGroup().getTextureResource(tile));
			guiTiles.remove(this);
		}

		@Override
		public void selected(Tile tile, boolean selection) {
			if (selection) {
				System.out.println("Tile " + tile + " selected "
						+ tile.getGroup().getTextureResource(tile));
			} else {
				System.out.println("Tile " + tile + " unselected "
						+ tile.getGroup().getTextureResource(tile));
			}
		}

		public Tile getTile() {
			return tile;
		}
	}

	public static void main(String[] args) {
		int maxRemovedTiles = 0;
		maxRemovedTiles = Math.max(maxRemovedTiles, testBoard("level/butterfly.xml"));
		maxRemovedTiles = Math.max(maxRemovedTiles, testBoard("level/standard.xml"));
		maxRemovedTiles = Math.max(maxRemovedTiles, testBoard("level/block.xml"));
		maxRemovedTiles = Math.max(maxRemovedTiles, testBoard("level/castle.xml"));
		maxRemovedTiles = Math.max(maxRemovedTiles, testBoard("level/stairs.xml"));
		maxRemovedTiles = Math.max(maxRemovedTiles, testBoard("level/towers.xml"));

		System.out.println( "best result: " + maxRemovedTiles );
	}

	private static int testBoard(String level) {
		int removedTiles = 0;
		System.out.println("Testing board " + level);
		Board b = new Board(new XMLLevel(level));
		int missingTileCount = b.missingTileCount();
		if (missingTileCount == 0) {
			System.out.println("board is valid");
			removedTiles = enumerating(b);
		} else {
			System.err.println(String.format(
					"board is not valid, %d tiles missing", missingTileCount));
		}
		System.out.println("=====================");
		return removedTiles;
	}

	private static int enumerating(Board board) {
		for (int x = 0; x < board.getWidth(); ++x)
			for (int y = 0; y < board.getHeight(); ++y)
				for (int z = 0; z < board.getDepth(); ++z) {
					Tile tile = board.getTile(x, y, z);
					if (tile != null) {
						GUITile guiTile = new GUITile(tile);
						tile.setTileListener(guiTile);
					}
				}

		while (true) {
			Hint hint = board.getHint();
			if (hint == null) {
				System.out.println("no more hints");
				break;
			}

			hint.getFirst().select();
			hint.getSecond().select();
		}

		int tileCount = board.getTileCount();
		int originalTileCount = board.getOriginalTileCount();
		int removedTiles = originalTileCount - tileCount;
		System.out.println(String.format(
				"done. %d tiles removed. %d/%d tiles remaining",
				removedTiles, tileCount, originalTileCount));
		return removedTiles;
	}
}
