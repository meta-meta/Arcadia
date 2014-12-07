using clojure.lang;
using System;

public class LoopyBenchmark2 : AFunction
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
		for (num = 1000000L; num > 0L; num = num - 1)
		{
		}
		return num;
	}
}
