# coroutines
Coroutines forked from [offbynull's Coroutines](https://github.com/offbynull/coroutines) and implements ClassLoader.

## 说明

依赖于offbynull的Coroutines, 参考apache commons javaflow 实现了自己的ClassLoader,所以可以不用Instrumenter来做编译后处理.

这样做的原因是因为一般来说我们用Coroutine的类都非常少,所以直接ClassLoader就可以,避免了后期重复处理不必要的类.

如果你还是坚持使用Instrumenter,那么可以参考offbynull童鞋的AntTask或者Maven配置.

如果你要做非常复杂的高性能并发,不妨使用Fork/Join.或者使用ExecutorService.

lib里面是依赖的类库,同时我保留了javaflow的代码,但是实际使用的时候,javaflow是不需要的.

lib里面的commons-jci和commons-logging在去掉javaflow的情况下也是不需要的.

## 用法举例:

```
public class Test
{
	public static void main(String[] args)
	{
		CoroutineRunner r = Coroutines.create(MyCoroutine.class);
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

大家可以试试看,实现自己的Coroutine.


## 注意:
> 1. 不支持使用反射调用
> 1. 不支持Lambda表达式
> 1. 本地对象在resume之后,可能地址和之前并不一致.
