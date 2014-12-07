using clojure.lang;
using System;

public class LoopyBenchmark3 : AFunction
{
	//
	// Methods
	//
	public override bool HasArity (int num)
	{
		return num == 0;
	}

	public override object invoke ()
	{
		long num;
		for (num = 1000000L; num > 0L; num = (long)(RT.intCast (num) - 1))
		{
		}
		return num;
	}
}
