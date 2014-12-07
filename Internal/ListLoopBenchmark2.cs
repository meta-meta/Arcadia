using clojure.lang;
using System;
using UnityEngine;
[Serializable]
public class ListLoopBenchmark2 : AFunction
{
	//
	// Static Fields
	//
	protected  static Var const__3;

	protected  static Var const__4;

	protected  static Var const__5;

	protected  static object const__0;

	protected  static Var const__1;

	protected  static object const__2;

	//
	// Constructors
	//
	static ListLoopBenchmark2 ()
	{
		const__0 = (object)1000000L;
		const__1 = RT.var ("clojure.core", ">");
		const__2 = (object)0L;
		const__3 = RT.var ("clojure.core", "conj");
		const__4 = RT.var ("clojure.core", "dec");
		const__5 = RT.var ("clojure.core", "count");
	}

	//
	// Methods
	//
	public override bool HasArity (int num)
	{
		return num == 0;
	}

	public override object invoke ()
	{
		Profiler.BeginSample ((string) "c sharp list loop benchmark 2");
		object obj = PersistentList.EMPTY;
		long num = 1000000L;
		while (num > 0L)
		{
			object arg_41_0 = ((IFn) const__3.getRawRoot ()).invoke (obj, num);
			num = Numbers.dec (num);
			obj = arg_41_0;
		}
		long c = RT.count ((object)obj);
		Profiler.EndSample ();
		return c;
	}
	
}
