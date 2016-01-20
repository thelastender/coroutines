package ender.coroutines;

/**
 * @author yanfengbing
 * @version 1.0
 * @since 1/7/16 10:57 AM
 */
public abstract class Bootstrap
{
	public static void init(String... packages)
	{
		for(String p : packages)
		{
			CoroutinesHelper.addLoaderPackageRoot(p);
		}
		Thread.currentThread().setContextClassLoader(CoroutinesHelper.getCoroutineClassLoader());
	}

	public static Bootstrap create(Class<? extends Bootstrap> tClass)
	{
		return (Bootstrap)CoroutinesHelper.createObjectUseCoroutineClassLoader(tClass);
	}

	public abstract void start(String[] args);
}
