# coroutines
Coroutines forked from [offbynull's Coroutines](https://github.com/offbynull/coroutines) and implements ClassLoader.

## 说明

依赖于offbynull的Coroutines，参考apache commons javaflow 实现了自己的ClassLoader，
所以可以不用Instrumenter来做编译后处理。

这样做的原因是因为一般来说我们用Coroutine的类都非常少，所以直接ClassLoader就可以，
避免了后期重复处理不必要的类。

如果你还是坚持使用Instrumenter,那么可以参考offbynull童鞋的AntTask或者Maven配置。
但是这样有很多限制，比如说运行和编译的JDK版本必须一致这种奇葩的规定。

我实现了一个Bootstrap，这样子main方法所在的类直接继承Bootstrap，
就可以完美的实现大型项目的自动Coroutine支持。

如果你要做非常复杂的高性能并发,不妨使用Fork/Join.或者使用ExecutorService.

lib里面是依赖的类库,同时我保留了javaflow的代码,但是实际使用的时候,javaflow是不需要的.

lib里面的commons-jci和commons-logging在去掉javaflow的情况下也是不需要的.

## 注意
1. 可能你在offbynull同学的github上看到了很多说明，例如反射，lambda表达式等，
其实说白了，就一点限制，所有的Continuation变量不能作为类的成员变量，只能作为方法的参数传来传去。
	> 如果你看了offbynull的源代码，你就会发现他instrument的时候使用方法的参数是否是Continuation变量来判断是否需要instrument的。
	> 而反射和lambda表达式前者一定不会用Continuation作为方法参数，后者你如果写的方法中有Continuation作为函数参数，那么并不会出现问题。
1. 最好不要Serialize/Deserialize你的Coroutine类。如果你明白Java对象序列化和反序列化的真正含义的话，你应该是清楚为什么不能这样做的。
或者说你一定要这样做，那么你自己需要弄明白你究竟在做什么，不要乱写。

## 用法举例:

### 简单使用

```
public class Test
{
	public static void main(String[] args)
	{
		CoroutineRunner r = CoroutinesHelper.create(MyCoroutine.class);
		r.execute();
		r.execute();
	}

	public static final class MyCoroutine implements Coroutine
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
```

### 大型项目使用

大型项目可以直接使用Bootstrap，main方法所在的类只要继承Bootstrap然后按照下面的方式调用即可。
注意，一定要先调用`Bootstrap.init()`方法来将自己项目的所有包加入。

```
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
```

大家可以试试看，实现自己的Coroutine。

有任何问题可以联系我：[thelastender@gmail.com](mailto:thelastender@gmail.com)

