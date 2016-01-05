package ender.coroutines;

import com.offbynull.coroutines.instrumenter.Instrumenter;
import com.offbynull.coroutines.instrumenter.asm.ClassLoaderClassInformationRepository;

/**
 * @author yanfengbing
 * @version 1.0
 * @since 1/5/16 3:04 PM
 */
class CoroutineClassTransformer
{
	private Instrumenter instrumenter;

	public CoroutineClassTransformer(ClassLoader classLoader)
	{
		instrumenter = new Instrumenter(new ClassLoaderClassInformationRepository(classLoader));
	}

	public byte[] transform(byte[] input)
	{
		return instrumenter.instrument(input);
	}
}
