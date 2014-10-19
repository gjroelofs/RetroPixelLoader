package net.rpl;

import java.net.URL;
import java.net.URLClassLoader;

public class ModClassLoader extends URLClassLoader {
	public ModClassLoader() {
		super(new URL[0]);
	}

	@Override
	public void addURL(URL url) {
		super.addURL(url);
	}

	@Override
	public URL[] getURLs() {
		URL[] urls = ((URLClassLoader) ClassLoader.getSystemClassLoader()).getURLs();
		System.arraycopy(super.getURLs(), 0, urls, urls.length - 1, super.getURLs().length);
		return urls;
	}
}
