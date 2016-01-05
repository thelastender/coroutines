package ender.coroutines;


import com.offbynull.coroutines.user.Continuation;
import com.offbynull.coroutines.user.Coroutine;
import com.offbynull.coroutines.user.CoroutineRunner;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author yanfengbing
 * @version 1.0
 * @since 1/5/16 4:05 PM
 */
public class Test
{
	public static void main(String[] args) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException
	{
		CoroutineClassLoader classLoader = new CoroutineClassLoader(CoroutineClassLoader.class.getClassLoader());
		classLoader.addLoaderPackageRoot("ender");
		Class<?> c = classLoader.loadClass("ender.coroutines.Test$MyLoader");

		System.out.println(c.getName() + ", " + c.getClassLoader() + "; " + MyLoader.class.getClassLoader());

		Method m = c.getMethod("load");
		CoroutineRunner r = (CoroutineRunner)m.invoke(c.newInstance());
		r.execute();

		CoroutineRunner r2 = Coroutines.create("ender.coroutines.Test$MyCoroutine");
		r2.execute();

		CoroutineRunner r3 = Coroutines.create("ender.coroutines.Test$MyCoroutine");
		r3.execute();
		r3.execute();
		r3.execute();

		CoroutineRunner r4 = Coroutines.create(MyCoroutine.class);
		r4.execute();
		r4.execute();
	}

	public static final class MyLoader
	{
		public CoroutineRunner load()
		{
			MyCoroutine myCoroutine = new MyCoroutine(1);
			System.out.println(myCoroutine.getClass().getName() + ", " + myCoroutine.getClass().getClassLoader() + "; " + MyCoroutine.class.getClassLoader());
			CoroutineRunner r = new CoroutineRunner(myCoroutine);
			System.out.println(r.getClass().getName() + ", " + r.getClass().getClassLoader());

			r.execute();
			r.execute();
			r.execute();
			r.execute();
			return r;
		}
	}

	public static final class MyCoroutine implements Coroutine
	{
		static
		{
			System.out.println("do some static things.");
		}

		public MyCoroutine()
		{
		}

		public MyCoroutine(int i)
		{
		}

		@Override
		public void run(Continuation c)
		{
			System.out.println("started");
			for(int i = 0; i < 10; i++)
			{
				echo(c, i);
			}
		}

		private void echo(Continuation c, int x)
		{
			System.out.println("" + Thread.currentThread().getName() + " " + x);
			Object o = new Object();
			c.suspend();
		}
	}
}
