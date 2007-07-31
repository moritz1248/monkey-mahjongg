package jmetest.monkeymahjongg.playground;

public class Test {

	public static void main(String[] args) {
		int layer[][][] = new int[][][] {
				{ 
					{ 0, 0, 0, 0, 1, 1, 0, 0, 0, 0 },
					{ 0, 0, 0, 1, 1, 1, 1, 0, 0, 0 },
					{ 0, 0, 0, 1, 1, 1, 1, 0, 0, 0 },
					{ 0, 0, 1, 1, 1, 1, 1, 1, 0, 0 },
					{ 0, 0, 1, 1, 1, 1, 1, 1, 0, 0 },
					{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 0 },
					{ 0, 0, 1, 1, 1, 1, 1, 1, 0, 0 },
					{ 0, 0, 0, 1, 1, 1, 1, 0, 0, 0 },
					{ 0, 0, 0, 0, 1, 1, 0, 0, 0, 0 },
					{ 0, 0, 0, 1, 1, 1, 1, 0, 0, 0 } },
				{ 
					{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					{ 0, 0, 0, 0, 1, 1, 0, 0, 0, 0 },
					{ 0, 0, 0, 0, 1, 1, 0, 0, 0, 0 },
					{ 0, 0, 0, 1, 1, 1, 1, 0, 0, 0 },
					{ 0, 0, 0, 1, 1, 1, 1, 0, 0, 0 },
					{ 0, 1, 1, 1, 1, 1, 1, 1, 0, 0 },
					{ 0, 0, 0, 1, 1, 1, 1, 0, 0, 0 },
					{ 0, 0, 0, 0, 1, 1, 0, 0, 0, 0 },
					{ 0, 0, 0, 0, 1, 1, 0, 0, 0, 0 },
					{ 0, 0, 0, 0, 1, 1, 0, 0, 0, 0 } },
				{
					{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					{ 0, 0, 0, 0, 1, 1, 0, 0, 0, 0 },
					{ 0, 0, 0, 1, 1, 1, 1, 0, 0, 0 },
					{ 0, 0, 0, 1, 1, 1, 1, 0, 0, 0 },
					{ 0, 0, 0, 0, 1, 1, 0, 0, 0, 0 },
					{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } }
				};

		int count = Board.countTiles(layer);
		if( count % 4 == 0 )
		{
			Board b = new Board(layer[0][0].length, layer[0].length, layer.length);
			b.setGroupCount( count / 4 );
			b.addTiles(layer);
			
			int missingTileCount = b.missingTileCount();
			if (missingTileCount == 0)
				System.out.println("board is valid");
			else
			{
				System.err.println(String.format(
						"board is not valid, %d tiles missing", missingTileCount));
			}
		}
	}
}
