package net.rpl;

public class RPLVersion {
	public static final int MAJOR = 0;
	public static final int MINOR = 1;
	public static final int REVISION = 0;
	public static final int BUILD = -1;

	public static final String getVersion() {
		return String.format("%d.%d.%d.%d", MAJOR, MINOR, REVISION, BUILD);
	}
}
