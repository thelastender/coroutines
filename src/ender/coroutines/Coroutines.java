package ender.coroutines;

import com.offbynull.coroutines.user.Coroutine;
import com.offbynull.coroutines.user.CoroutineRunner;

import java.lang.reflect.Method;

/**
 * @author yanfengbing
 * @version 1.0
 * @since 1/5/16 9:14 PM
 */
public class Coroutines
{
	private final static CoroutineClassLoader coroutineClassLoader = new CoroutineClassLoader(Coroutines.class.getClassLoader());
	private static Class<?> loaderClass;

	static
	{
		try
		{
			loaderClass = coroutineClassLoader.forceLoadClass(CoroutinesLoader.class.getName());
		}
		catch(Exception e)
		{
			loaderClass = null;
		}
	}

	public static CoroutineRunner create(Class<? extends Coroutine> tClass)
	{
		return create(tClass.getName());
	}
	public static CoroutineRunner create(String className)
	{
		try
		{
			coroutineClassLoader.addLoaderPackageRoot(className);
			Method m = loaderClass.getMethod("load", String.class);
			return (CoroutineRunner)m.invoke(loaderClass.newInstance(), className);
		}
		catch(Exception e)
		{
			throw new IllegalStateException("Load error", e);
		}
	}

	public static class CoroutinesLoader
	{
		public CoroutineRunner load(String className) throws ClassNotFoundException, IllegalAccessException, InstantiationException
		{
			Class<?> c = this.getClass().getClassLoader().loadClass(className);
			Coroutine coroutine = (Coroutine)c.newInstance();
			return new CoroutineRunner(coroutine);
		}
	}
}
