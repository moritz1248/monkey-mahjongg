package jmetest.monkeymahjongg.playground.view;

import jmetest.monkeymahjongg.playground.model.Board;
import jmetest.monkeymahjongg.playground.model.ITileListener;
import jmetest.monkeymahjongg.playground.model.Tile;
import jmetest.monkeymahjongg.playground.model.TileSelection;

import com.jme.bounding.BoundingBox;
import com.jme.image.Image;
import com.jme.image.Texture;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.SharedMesh;
import com.jme.scene.TriMesh;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

public class UITile extends SharedMesh implements ITileListener {
	private static final long serialVersionUID = 1L;
	public static String TILE_USER_DATA = "tile";
	private float dx = 3.5f;
	private float dy = 5f;
	private float dz = 1.5f;
	private Tile tile;

	public UITile(Tile tile, TriMesh sharedData) {
		super("tile", sharedData);
		this.tile = tile;
		
		setUserData(TILE_USER_DATA, tile);
		tile.setTileListener(this);

		setupMaterialState();
		setupTextureState(tile.getTextureResource());
		
		setupTranslation();
		setModelBound(new BoundingBox());
		updateModelBound();
	}


	private void setupTranslation() {
		Board board = tile.getBoard();
		int x = tile.getX();
		int y = tile.getY();
		int z = tile.getZ();
		Vector3f translation = new Vector3f(dx
				* (x - board.getWidth() / 2f) + dx / 2, dy
				* (board.getHeight() / 2f - y) - 0.5f * dy, 2 * dz
				* z);
		setLocalTranslation(translation);
	}

	private void setupTextureState(String texture) {
		TextureState ts = DisplaySystem.getDisplaySystem().getRenderer()
				.createTextureState();
		Texture t = TextureManager.loadTexture(MahjonggGameState.class
				.getClassLoader().getResource(texture),
				Texture.MM_LINEAR_LINEAR, Texture.FM_LINEAR,
				Image.GUESS_FORMAT_NO_S3TC, ts.getMaxAnisotropic(), true);
		ts.setTexture(t);
		setRenderState(ts);
		updateRenderState();
	}

	private void setupMaterialState() {
		MaterialState ms = DisplaySystem.getDisplaySystem().getRenderer()
				.createMaterialState();
		ms.setEmissive(ColorRGBA.white);
		setRenderState(ms);
	}

	@Override
	public void removed(Tile tile) {
		// TODO Auto-generated method stub

	}

	@Override
	public void selected(Tile tile, TileSelection selection) {
		// TODO Auto-generated method stub

	}

}
