package ender.coroutines.test;


import com.offbynull.coroutines.user.Continuation;
import com.offbynull.coroutines.user.Coroutine;
import com.offbynull.coroutines.user.CoroutineRunner;
import ender.coroutines.Bootstrap;

import java.io.Serializable;

/**
 * @author yanfengbing
 * @version 1.0
 * @since 1/5/16 4:05 PM
 */
public class TestCoroutine extends Bootstrap
{
	public static void main(String[] args)
	{
		Bootstrap.init("ender.coroutines.test");
		Bootstrap bootstrap = Bootstrap.create(TestCoroutine.class);
		bootstrap.start(args);
	}

	@Override
	public void start(String[] args)
	{
		CoroutineRunner runner = new CoroutineRunner(new MyCoroutine());
		runner.execute();
		runner.execute();
		runner.execute();
		runner.execute();
	}

	private static class MyCoroutine implements Coroutine, Serializable
	{
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
