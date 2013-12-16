package unyuho.common.gui;

public interface IScrollable
{
	/**
	 * スライダー移動時に呼び出される
	 * @param scrollID
	 * @param value
	 */
    public abstract void scrollPerformed(int scrollID, int value);
}
