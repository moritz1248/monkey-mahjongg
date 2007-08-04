package jmetest.monkeymahjongg.playground;

public class ExtendedTileGroup extends TileGroup {

	String imageDirectory;
	String prefix; 
	
	public ExtendedTileGroup( String imageDirectory, String prefix )
	{
		this.imageDirectory = imageDirectory;
		this.prefix = prefix;
	}
	
	@Override
	public String getTextureResource(Tile tile) {
		for( int i=0; i<tiles.length; ++i)
			if( tiles[i] == tile )
				return String.format( "%s%s%d", imageDirectory, prefix, i+1 );
		return null;
	}

}
