package dev.iwilkey.voxar.asset.exception;

/**
 * Exception thrown during the invocation of the Voxar engine as it verifies all referenced assets in "dev.iwilkey.Voxar.VoxarAsset". 
 * If the path referenced does not exist in the ./assets/ directory, the engine will be terminated.
 * @author iwilkey
 */
@SuppressWarnings("serial")
public final class VoxarAssetNotFound extends Exception {
	public VoxarAssetNotFound(String resourcePath) {
        super("The VoxarAsset defined by path \"" + resourcePath + "\" could not be found!");
    }
}
