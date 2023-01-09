package dev.iwilkey.voxar.asset.exception;

/**
 * A Voxar engine exception that informs a developer that they are trying to use a valid asset that is not referenced within the current State.
 * @author iwilkey
 */
@SuppressWarnings("serial")
public class VoxarAssetNotReferenced extends Exception {
	public VoxarAssetNotReferenced(String resourcePath) {
        super("The VoxarAsset defined by path \"" + resourcePath + "\" "
        		+ "has not been referenced within the current State. "
        		+ "Please add it to the constructor of the State.");
    }
}
